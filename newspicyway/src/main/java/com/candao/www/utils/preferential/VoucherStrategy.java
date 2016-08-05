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

/**
 * 
 * @author Candao 优惠卷使用策略
 * 代金卷团购卷使用策略
 */
public class VoucherStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();

		String orderid = (String) paraMap.get("orderid"); // 账单号
		// 使用优惠张数
		int preferentialNum = Integer.valueOf((String) paraMap.get("preferentialNum"));
		String activityID = (String) paraMap.get("preferentialid");
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String disrate = String.valueOf(paraMap.get("disrate"));
		BigDecimal discount = new BigDecimal(disrate.trim().isEmpty() ? "0" : disrate);
		Map<String, Object> cashGratis = cashGratis(paraMap, torderDetailDao,tbPreferentialActivityDao);
		if (cashGratis != null) {
			return cashGratis;
		}
		Map preMap = discountInfo(activityID, branchid, tbPreferentialActivityDao);
		BigDecimal amount = new BigDecimal(String.valueOf(preMap.get("amount")));
		// 获取当前账单的 菜品列表
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();

		for (int i = 0; i < preferentialNum; i++) {
			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
			TorderDetailPreferential torder = new TorderDetailPreferential(updateId, orderid, "",
					(String) paraMap.get("preferentialid"), amount, String.valueOf(orderDetailList.size()), 1, 1,
					discount, 0);
			//设置优惠名称
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) preMap.get("name"));
			torder.setActivity(activity);
			//设置挂账以及优免（团购有挂账 及优免，代金卷只有优免）
			torder.setToalFreeAmount(amount);
		 String billAmout=	(String) preMap.get("bill_amount");
		 if(billAmout!=null){
			 BigDecimal bigDecimal= new BigDecimal(billAmout);
			 //出去硬编码方式999999
			 if(bigDecimal.doubleValue()<90000){
				 torder.setToalDebitAmount(amount);
				 torder.setToalFreeAmount(bigDecimal.subtract(amount));
			 }else{
				 torder.setToalDebitAmount(amount);
			 }
		 }else{
			 torder.setToalFreeAmount(amount);
		 }

			detailPreferentials.add(torder);
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount.multiply(new BigDecimal(String.valueOf(preferentialNum))));
		return result;
	}

}
