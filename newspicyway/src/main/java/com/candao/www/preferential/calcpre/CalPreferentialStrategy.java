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
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TbOrderDetailPreInfo;
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
		amount = amountCount.subtract(preferentialAmt)
				.multiply(new BigDecimal("1").subtract(discount.divide(new BigDecimal(10))));
		// if (amountCount.compareTo(BigDecimal.ZERO) > 0 &&
		// disType.equals(Constant.PREDIS.DISCOUNT)
		// && (amountCount.subtract(preferentialAmt)).doubleValue() >= 0) {
		// amount = amountCount.subtract(preferentialAmt)
		// .multiply(new BigDecimal("1").subtract(discount.divide(new
		// BigDecimal(10))));
		// } else if (amountCount.compareTo(BigDecimal.ZERO) > 0 &&
		// disType.equals(Constant.PREDIS.NOTDISCOUNT)) {
		//
		// amount = amountCount.multiply(new
		// BigDecimal("1").subtract(discount.divide(new BigDecimal(10))));
		// }

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
	 *            传入参数
	 * @param amount
	 *            优惠总额
	 * @param freeAmount
	 *            优免金额
	 * @param debitAmout
	 *            挂账金额
	 * @param tempDishNum
	 *            优惠菜品个数
	 * @param discount
	 *            折扣率
	 * @param isGroup
	 *            是否是全局优惠
	 * @param preName
	 *            优惠名称
	 * @param coupondetailid
	 *            记账优惠卷ID
	 * @param isCustom
	 *            优惠采用类型
	 * @return
	 */
	protected TorderDetailPreferential createPreferentialBean(Map<String, Object> paraMap, BigDecimal amount,
			BigDecimal freeAmount, BigDecimal debitAmout, double tempDishNum, BigDecimal discount, int isGroup,
			String preName, String coupondetailid, int isCustom) {
		String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
		Date insertime = (paraMap.containsKey("insertime") ? (Date) paraMap.get("insertime") : new Date());
		String orderid = (String) paraMap.get("orderid");
		String preferentialid = paraMap.containsKey("preferentialid") ? (String) paraMap.get("preferentialid") : "";

		TorderDetailPreferential addPreferential = new TorderDetailPreferential(updateId, orderid, preferentialid,
				amount, isGroup, discount, isCustom, insertime);
		// 设置优惠名称
		TbPreferentialActivity activity = new TbPreferentialActivity();
		activity.setName(preName);

		addPreferential.setActivity(activity);
		addPreferential.setCoupondetailid(coupondetailid);
		// 设置优免金额
		addPreferential.setToalFreeAmount(freeAmount);
		// 设置挂账金额
		addPreferential.setToalDebitAmount(debitAmout);
		// 设置优惠总额
		addPreferential.setDeAmount(amount);

		return addPreferential;
	}

	/**
	 * 
	 * @param orderTopriceMap
	 *            订单的ID 对应菜品实际收入
	 * @param orderDetailList
	 *            当前订单数据
	 */
	protected void setorederMenuDebit(Map<String, ComplexTorderDetail> orderTopriceMap,
			List<ComplexTorderDetail> orderDetailList) {
		for (ComplexTorderDetail torderDetail : orderDetailList) {
			String orderdetailid = torderDetail.getOrderdetailid();
			if (orderTopriceMap.containsKey(orderdetailid)) {
				ComplexTorderDetail detail = orderTopriceMap.get(orderdetailid);
				torderDetail.setDebitamount(detail.getDebitamount());
				torderDetail.setCouponid(detail.getCouponid());
			}
		}

	}

	protected List<TorderDetailPreferential> getPresentAndspecialPriclist(String orderId,
			TorderDetailPreferentialDao detailPreferentialDao) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderid", orderId);
		return detailPreferentialDao.getPresentAndspecialPriclist(params);
	}

	protected List<TorderDetailPreferential> getAllPreInfolist(String orderId,
			TorderDetailPreferentialDao detailPreferentialDao) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderid", orderId);
		return detailPreferentialDao.getAllPreInfolist(params);
	}

	protected TbOrderDetailPreInfo createOrderDetailInfo(String id, TorderDetailPreferential detailPreferential,
			ComplexTorderDetail orderDetail, BigDecimal dePreAmount) {
		TbOrderDetailPreInfo detailPreInfo = new TbOrderDetailPreInfo(id, detailPreferential.getId(),
				detailPreferential.getOrderid(), orderDetail.getOrderdetailid(), new Date());
		detailPreInfo.setDishID(orderDetail.getDishid());
		detailPreInfo.setUnit(orderDetail.getDishunit());
		detailPreInfo.setDePreAmount(dePreAmount);
		return detailPreInfo;
	}

	/**
	 * 
	 * @param orderid
	 * @param detailPreferentialDao
	 * @return 查询单品菜记录
	 */
	protected Map<String, Object> getUsePreInfoMap(String orderid, TorderDetailPreferentialDao detailPreferentialDao) {
		
		 Map<String, Object> reMap =new HashMap<>();
		// 赠送菜品个数
		 Map<String, Double> beforGiftDishMap = new HashMap<>();
		// 已经赠送过得菜品菜品唯一标号对应菜品数据key:oredridlid Value:dishID+unit
		Map<String, ComplexTorderDetail> beforderidetailidMap = new HashMap<>();
		//当前菜品优惠了多少钱key:orederidetailid value:amount
		Map<String,BigDecimal> beforAmountMap=new HashMap<>();
		// 卷对应的菜品数据（一个菜多少个卷）
		List<TorderDetailPreferential> torderDetailPreferentials = this.getPresentAndspecialPriclist(orderid,
				detailPreferentialDao);
		// 主键ID对应数据内容
		Map<String, TorderDetailPreferential> updateMap = new HashMap<>();
		for (TorderDetailPreferential detailPreferential : torderDetailPreferentials) {
			updateMap.put(detailPreferential.getId(), detailPreferential);
			List<TbOrderDetailPreInfo> detailPreinfoList = detailPreferential.getDetailPreInfos();
			for (TbOrderDetailPreInfo preInfo : detailPreinfoList) {
				String key = preInfo.getDishID() + preInfo.getUnit();

				double getDishNum = preInfo.getDishNum();
				  BigDecimal deAmount = preInfo.getDePreAmount();
				if (beforderidetailidMap.containsKey(preInfo.getOrderidetailid())) {
					ComplexTorderDetail detail = beforderidetailidMap.get(preInfo.getOrderidetailid());
					Double comDishNum=Double.valueOf(detail.getDishnum());
					detail.setDishnum(String.valueOf(comDishNum+getDishNum));
					detail.setPreAmount(detail.getPreAmount().add(deAmount));
					beforderidetailidMap.put(preInfo.getOrderidetailid(), detail);
				} else {
					ComplexTorderDetail detail= new ComplexTorderDetail();
					detail.setDishnum(String.valueOf(getDishNum));
					detail.setPreAmount(deAmount);
					beforderidetailidMap.put(preInfo.getOrderidetailid(), detail);
				}

				if(beforAmountMap.containsKey(preInfo.getOrderidetailid())){
					
				}
				
				if (beforGiftDishMap.containsKey(key)) {
					Double dishNum = beforGiftDishMap.get(key);
					beforGiftDishMap.put(key, dishNum + preInfo.getDishNum());
				} else {
					beforGiftDishMap.put(key, preInfo.getDishNum());
				}
			}
		}
		reMap.put("orderDetailIdMap", beforderidetailidMap);
		reMap.put("dishUnitMap", beforGiftDishMap);
		return null;
	}
	/**
	 * 
	 * @param orderDetailList
	 * @param orderid
	 * @param detailPreferentialDao
	 * 每一个菜品实际金额
	 */
	protected void  everyorderDetailAmount(List<ComplexTorderDetail> orderDetailList,String orderid,TorderDetailPreferentialDao detailPreferentialDao){
		
		List<TorderDetailPreferential> allPreInfoList = this.getAllPreInfolist(orderid, detailPreferentialDao);
		//单一菜品优惠
		List<TorderDetailPreferential> singleList=new ArrayList<>();
		//多个菜品优惠
		List<TorderDetailPreferential> manyList=new ArrayList<>();
		//查找特价卷或者赠菜卷靠前
		for( TorderDetailPreferential preferential:allPreInfoList){
			if(preferential.getIsCustom()==3||preferential.getIsCustom()==4){
				singleList.add(preferential);
			}else{
				manyList.add(preferential);
			}
		}
		//计算每一个菜优惠多少钱
//		Map<String, V>
		//合并数据
		singleList.addAll(manyList);
		//计算每一个菜品当前剩余价
		for(TorderDetailPreferential allpre:singleList){
			//优免总金额
			BigDecimal deamount = allpre.getDeAmount();
			List<TbOrderDetailPreInfo> detailpreInfoList = allpre.getDetailPreInfos();
			if(allpre.getIsGroup()==1){
				//全单折扣
			}
		}
	}
}
