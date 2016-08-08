package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/***
 * 还在单位优免
 * 
 * @author Candao
 *
 */
public class InnerfreeStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String orderid = (String) paraMap.get("orderid"); // 账单号
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		Map tempMap = this.discountInfo(preferentialid, branchid, tbPreferentialActivityDao);

		// discount大于0为折扣优免
		BigDecimal discount = (BigDecimal) tempMap.get("discount");
		// amount大于0为现金优免
		BigDecimal caseAmount = (BigDecimal) tempMap.get("amount");

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();

		// 获取当前账单的 菜品列表
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);

		// 当前订单总价amountCount
		BigDecimal amountCount = new BigDecimal(0.0);
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
		// 使用优惠卷列表
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		TorderDetailPreferential addPreferential = null;
		// 最终优惠金额
		BigDecimal amount = new BigDecimal(0);
		//0不能挂账，1挂账
		String can_credit=String.valueOf(tempMap.get("can_credit"));
		// 设置金额
		if (discount != null && discount.doubleValue() > 0) {
			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();

			if (amountCount.compareTo(BigDecimal.ZERO) > 0 && (amountCount.subtract(bd).compareTo(BigDecimal.ZERO)) != -1){
				// 折扣计算方式
				amount = amountCount.subtract(bd)
						.multiply(new BigDecimal("1").subtract(discount.divide(new BigDecimal(10))));
				addPreferential = new TorderDetailPreferential(updateId, orderid, "", preferentialid, amount,
						String.valueOf(orderDetailList.size()), 1, 1, discount, 0);
				//设置优惠名称
				TbPreferentialActivity activity = new TbPreferentialActivity();
				activity.setName((String) tempMap.get("name"));
				addPreferential.setActivity(activity);
				//是否挂账，优免
				if(can_credit.equals("0")){
					addPreferential.setToalDebitAmount(amount);
				}else{
					addPreferential.setToalFreeAmount(amount);
				}
				detailPreferentials.add(addPreferential);
			}
	
		} else if (caseAmount != null && caseAmount.doubleValue() > 0) {
			// 现金减免方式
			//获取优惠卷个数(如果POS选择多张优惠卷，后台会在数据库写入多个数据，所以要拆分优惠卷)
			int preferentialNum=Integer.valueOf((String)paraMap.get("preferentialNum"));
			amount=caseAmount.multiply(new BigDecimal(preferentialNum));
			for(int i=0;i<preferentialNum;i++){
				String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
				addPreferential = new TorderDetailPreferential(updateId, orderid, "", preferentialid, caseAmount,
						String.valueOf(orderDetailList.size()), 1, 1, discount, 0);
				//设置优惠名称
				TbPreferentialActivity activity = new TbPreferentialActivity();
				activity.setName((String) tempMap.get("name"));
				addPreferential.setActivity(activity);
				//是否挂账，优免
				//是否挂账，优免
				if(can_credit.equals("0")){
					addPreferential.setToalDebitAmount(amount);
				}else{
					addPreferential.setToalFreeAmount(amount);
				}
				detailPreferentials.add(addPreferential);
			}
		}

		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
