package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

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
		detail_params.put("preferential", activityID);
		detail_params.put("branchid", branchid);
		List<Map<String, Object>> detailList = tbPreferentialActivityDao.findPreferentialDetail(detail_params);
		return detailList;
	}
	/***
	 * 计算菜品总价
	 */
	protected BigDecimal getAmountCount(List<TorderDetail> orderDetailList){
		BigDecimal amountCount=new BigDecimal("0");
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
	 * 现金优免 手动输入
	 * 
	 * @return
	 */
	protected Map<String, Object> cashGratis(Map<String, Object> params, TorderDetailMapper torderDetailDao,TbPreferentialActivityDao tbPreferentialActivityDao) {
		Map<String, Object> resultMap = new HashMap<>();
		String preferentialid = (String) params.get("preferentialid"); //优惠活动id
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String orderid = (String) params.get("orderid"); // 账单号
		String isCustom = String.valueOf( params.get("isCustom"));
		String preferentialAmout=params.containsKey("preferentialAmout")?String.valueOf( params.get("preferentialAmout")):"0";
		BigDecimal amout = new BigDecimal(preferentialAmout);
		if (isCustom.equals("1") && amout.doubleValue() > 0) {
			// 获取当前账单的 菜品列表
			Map<String, String> orderDetail_params = new HashMap<>();
			orderDetail_params.put("orderid", orderid);
			List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
			//菜单价格
			BigDecimal orderPrice=new BigDecimal("0");
			for(TorderDetail torderDetail:orderDetailList){
				  BigDecimal dataOrderPrice=torderDetail.getOrderprice()==null?new BigDecimal("0"):torderDetail.getOrderprice();
				orderPrice=orderPrice.add(dataOrderPrice.multiply(new BigDecimal(torderDetail.getDishnum())));
			}
			
			 String updateId=params.containsKey("updateId")?(String)params.get("updateId"):IDUtil.getID();
				Date insertime = (params.containsKey("insertime") ?  (Date) params.get("insertime")
						: new Date());
			List<TorderDetailPreferential> listRest = new ArrayList<>();
			List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid, branchid, tbPreferentialActivityDao);
			Map tempMap = tempMapList.get(0);
			TorderDetailPreferential detailPreferential = new TorderDetailPreferential(updateId, orderid, "",
					(String) params.get("preferentialid"), amout, String.valueOf(orderDetailList.size()), 1, 1,
					new BigDecimal(0), 1,insertime);
			//设置优惠名称
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) tempMap.get("name"));
			detailPreferential.setActivity(activity);
			detailPreferential.setCoupondetailid((String) (tempMapList.size()>1?tempMap.get("preferential"):tempMap.get("id")));
			//特殊团购卷
			if(String.valueOf(params.get("type")).equals("05")){
				if(orderPrice.compareTo(amout)==-1){
					detailPreferential.setToalDebitAmountMany(orderPrice.subtract(amout));
				}
				detailPreferential.setToalDebitAmount(amout);
			}else{
				detailPreferential.setToalFreeAmount(amout);
			}
			listRest.add(detailPreferential);
			resultMap.put("amount", amout);
			resultMap.put("detailPreferentials", listRest);
			return resultMap;
		} 
		return null;
	}

}
