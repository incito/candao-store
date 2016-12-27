package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.preferential.model.PreDealInfoBean;
import com.candao.www.utils.ReturnMes;

/**
 * 
 * @author Candao 基类做同凑运算
 */
public abstract class CalPreferentialStrategy implements CalPreferentialStrategyInterface {

	/***
	 * 获取优惠券信息
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> discountInfo(String activityID, String branchid,
			TbPreferentialActivityDao tbPreferentialActivityDao) {
		Map<String, String> detail_params = new HashMap<>();
		detail_params.put("preferentialId", activityID);
		detail_params.put("branchid", branchid);
		List<Map<String, Object>> detailList = tbPreferentialActivityDao.findPreferentialDetailList(detail_params);
		return detailList;
	}

	/***
	 * 计算菜品总价
	 */
	protected BigDecimal getAmountCount(List<ComplexTorderDetail> orderDetailList) {
		BigDecimal amountCount = new BigDecimal("0");
		for (TorderDetail d : orderDetailList) {
			// 判断价格，如果菜品价格存在null的问题，则返回错误信息
			if (null != d.getOrderprice()) {
				// 如果此菜品是多份，则计算多份总的优惠价格
				BigDecimal numOfDish = new BigDecimal("1");
				if (new BigDecimal(d.getDishnum()).compareTo(new BigDecimal("0")) > 0) {
					numOfDish = new BigDecimal(d.getDishnum());
				}
				amountCount = amountCount.add(d.getOrderprice().multiply(numOfDish));
			}
		}
		return amountCount;
	}

	/**
	 * 
	 * @param amountCount
	 *            参与优惠的菜品金额
	 * @param preferentialAmt
	 *            已经优惠的金额
	 * @param discount
	 *            折扣率
	 * @return
	 */
	protected PreDealInfoBean calDiscount(BigDecimal amountCount, BigDecimal preferentialAmt, BigDecimal discount) {
		/** 是否折上折优惠计算 0不进行折上折 1进行折上折 **/
		String disType = PropertiesUtils.getValue("distodis");
		BigDecimal amount = new BigDecimal("0");
		PreDealInfoBean infoBean = new PreDealInfoBean();

		if (amountCount.compareTo(BigDecimal.ZERO) > 0 && disType.equals(Constant.PREDIS.DISCOUNT)
				&& (amountCount.subtract(preferentialAmt)).doubleValue() >= 0) {
			amount = amountCount.subtract(preferentialAmt)
					.multiply(new BigDecimal("1").subtract(discount.divide(new BigDecimal(10))));
		} else if (amountCount.compareTo(BigDecimal.ZERO) > 0 && disType.equals(Constant.PREDIS.NOTDISCOUNT)) {

			amount = amountCount.multiply(new BigDecimal("1").subtract(discount.divide(new BigDecimal(10))));
		}

		infoBean.setPreAmount(amount);
		infoBean.setDistodis(disType);
		return infoBean;
	}

	/**
	 * 
	 * @param result
	 *            返回结果集
	 * @param amountCount
	 *            待优惠金额
	 * @param amount
	 *            支付金额
	 * @param bd
	 * @param disType
	 */
	protected void disMes(Map<String, Object> result, BigDecimal amountCount, BigDecimal amount, BigDecimal bd,
			String disType) {
		boolean flag = true;
		String flagcode = "";
		if (amountCount.doubleValue() <= 0 && amount.doubleValue() <= 0) {
			flag = false;
			flagcode = "2001";
		} else if (amountCount.doubleValue() > 0 && (amountCount.subtract(bd).compareTo(BigDecimal.ZERO)) == -1
				&& disType.equals(Constant.PREDIS.DISCOUNT)) {
			flag = false;
			flagcode = "2002";
		}
		result.put("falg", flag);
		result.put("mes", flag ? ReturnMes.SUCCESS.getMsg() : ReturnMes.mes(flagcode));
	}

	/**
	 * 
	 * @param paraMap
	 * 传入参数
	 * @param amount
	 * 优惠总额
	 * @param freeAmount
	 * 优免金额
	 * @param debitAmout
	 * 挂账金额
	 * @param tempDishNum
	 * 优惠菜品个数
	 * @param discount
	 * 折扣率
	 * @param isGroup
	 * 是否是全局优惠
	 * @param preName
	 * 优惠名称
	 * @param coupondetailid
	 * 记账优惠卷ID
	 * @param isCustom
	 * 优惠采用类型
	 * @return
	 */
	protected TorderDetailPreferential createPreferentialBean(Map<String, Object> paraMap, BigDecimal amount,BigDecimal freeAmount,BigDecimal debitAmout,
			double tempDishNum, BigDecimal discount, int isGroup,String  preName,String coupondetailid,int isCustom) {
		String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
		Date insertime = (paraMap.containsKey("insertime") ? (Date) paraMap.get("insertime") : new Date());
		String orderid = (String) paraMap.get("orderid");
		String preferentialid =paraMap.containsKey("preferentialid")? (String) paraMap.get("preferentialid"):""; 
		
		TorderDetailPreferential addPreferential = new TorderDetailPreferential(updateId, orderid, "", preferentialid,
				amount, String.valueOf(tempDishNum), isGroup, 1, discount, isCustom, insertime);
		// 设置优惠名称
		TbPreferentialActivity activity = new TbPreferentialActivity();
		activity.setName(preName);

		addPreferential.setActivity(activity);
		addPreferential
				.setCoupondetailid(coupondetailid);
		// 设置优免金额
		addPreferential.setToalFreeAmount(freeAmount);
		//设置挂账金额
		addPreferential.setToalDebitAmount(debitAmout);
		//设置优惠总额
		addPreferential.setDeAmount(amount);
		
		return addPreferential;
	}

	/**
	 * 现金优免 手动输入
	 * 
	 * @return
	 */
	protected Map<String, Object> cashGratis(Map<String, Object> params,
			TbPreferentialActivityDao tbPreferentialActivityDao,List<ComplexTorderDetail> orderDetailList) {
		Map<String, Object> resultMap = new HashMap<>();
		String preferentialid = (String) params.get("preferentialid"); // 优惠活动id
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String orderid = (String) params.get("orderid"); // 账单号
		String isCustom = String.valueOf(params.get("isCustom"));
		String preferentialAmout = params.containsKey("preferentialAmout")
				? String.valueOf(params.get("preferentialAmout")) : "0";
		BigDecimal amout = new BigDecimal(preferentialAmout);
		if (isCustom.equals("1") && amout.doubleValue() > 0) {
			// 获取当前账单的 菜品列表
			Map<String, String> orderDetail_params = new HashMap<>();
			orderDetail_params.put("orderid", orderid);

			// 菜单价格
			BigDecimal orderPrice = new BigDecimal("0");
			for (TorderDetail torderDetail : orderDetailList) {
				BigDecimal dataOrderPrice = torderDetail.getOrderprice() == null ? new BigDecimal("0")
						: torderDetail.getOrderprice();
				orderPrice = orderPrice.add(dataOrderPrice.multiply(new BigDecimal(torderDetail.getDishnum())));
			}

			String updateId = params.containsKey("updateId") ? (String) params.get("updateId") : IDUtil.getID();
			Date insertime = (params.containsKey("insertime") ? (Date) params.get("insertime") : new Date());
			List<TorderDetailPreferential> listRest = new ArrayList<>();
			List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid, branchid,
					tbPreferentialActivityDao);
			Map tempMap = tempMapList.get(0);
			TorderDetailPreferential detailPreferential = new TorderDetailPreferential(updateId, orderid, "",
					(String) params.get("preferentialid"), amout, String.valueOf(orderDetailList.size()), 1, 1,
					new BigDecimal(0), 1, insertime);
			// 设置优惠名称
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) tempMap.get("name"));
			detailPreferential.setActivity(activity);
			detailPreferential.setCoupondetailid(
					(String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id")));
			// 特殊团购卷
			if (String.valueOf(params.get("type")).equals("05")) {
				if (orderPrice.compareTo(amout) == -1) {
					detailPreferential.setToalDebitAmountMany(orderPrice.subtract(amout));
				}
				detailPreferential.setToalDebitAmount(amout);
			} else {
				detailPreferential.setToalFreeAmount(amout);
			}
			listRest.add(detailPreferential);
			resultMap.put("amount", amout);
			resultMap.put("detailPreferentials", listRest);
			return resultMap;
		}
		return null;
	}
	
//	protected <T>T loadCache(String orderId,String cacheFalg){
//		List<Object> orderDetailList =null;
//		String cacheKey=orderId;
//		if(cacheFalg.equals("info")){
//			 orderDetailList = (List<Object>) CacheManager.getCacheInfo(orderId+cacheFalg).getValue();
//		}else{
//			 orderDetailList = (List<Object>) CacheManager.getCacheInfo(orderId).getValue();
//		}
//		return (T) orderDetailList;
//	}

}
