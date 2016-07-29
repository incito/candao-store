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
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

public class InnerfreeStrategy extends CalPreferentialStrategy{

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
		BigDecimal discount = (BigDecimal) tempMap.get("discount");
		
		  Map<String, Object> cashGratis = cashGratis(paraMap, torderDetailDao);
		  if(cashGratis!=null){
			  return cashGratis;
		  }
		// 定义 返回值
		Map<String, Object> result = new HashMap<>();

		// 获取当前账单的 菜品列表
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);

		// 最终优惠金额
		BigDecimal amount = new BigDecimal(0);
		// 菜品原价
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
		// 设置金额
		amount = amountCount.subtract(bd).multiply(new BigDecimal("1").subtract(discount.divide(new BigDecimal(10))));
		 String updateId=paraMap.containsKey("updateId")?(String)paraMap.get("updateId"):IDUtil.getID();
		 TorderDetailPreferential addPreferential =new TorderDetailPreferential(updateId, orderid, "", preferentialid,
					amount, String.valueOf(orderDetailList.size()), 1, 1, discount, 1);
		 List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		 detailPreferentials.add(addPreferential);
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
