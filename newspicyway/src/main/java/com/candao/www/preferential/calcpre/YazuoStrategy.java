package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;

/**
 * 
 * @author Candao 雅座使用优惠
 */
public class YazuoStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {

		// 已经优免金额
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		// 折扣
		String disrate = String.valueOf(paraMap.get("disrate"));
		BigDecimal discount = new BigDecimal(disrate.trim().isEmpty() ? "0" : disrate);
		// 账单号
		String orderid = (String) paraMap.get("orderid");
		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();

		String memberno = String.valueOf(paraMap.get("memberno"));

		BigDecimal amount = null;
		if (memberno.isEmpty() && paraMap.containsKey("updateId")) {
			Map<String, Object> delMap = new HashMap<>();
			delMap.put("DetalPreferentiald", paraMap.get("updateId"));
			delMap.put("orderid", orderid);
			orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
			amount = new BigDecimal(0);
		} else if (!memberno.isEmpty()) {
			// 优惠金额
			amount = new BigDecimal(String.valueOf(paraMap.get("preferentialAmout")));
			// 使用优惠张数
			int preferentialNum = Integer.valueOf((String) paraMap.get("preferentialNum"));
			for (int i = 0; i < preferentialNum; i++) {
				TorderDetailPreferential torder = this.createPreferentialBean(paraMap, amount, amount,new BigDecimal("0"), orderDetailList.size(),
						discount, 1, (String) paraMap.get("preferentialName"), (String) paraMap.get("preferentialid"));
				// 设置优惠类型
				torder.setPreType((String) paraMap.get("type"));
				// 设置优惠名称
				torder.setPreName((String) paraMap.get("preferentialName"));

				if (paraMap.get("type").equals(Constant.CouponType.YAZUOFREE)) {
					// 代金卷
					torder.setDeAmount(amount);
					torder.setToalFreeAmount(amount);
				} else if (paraMap.get("type").equals(Constant.CouponType.YAZUO_DISCOUNT_TICKET)) {
					Map<String, String> orderDetail_params = new HashMap<>();
					orderDetail_params.put("orderid", orderid);

					BigDecimal amountCount = this.getAmountCount(orderDetailList);
					if (amountCount.compareTo(BigDecimal.ZERO) > 0
							&& (amountCount.subtract(bd).compareTo(BigDecimal.ZERO)) != -1) {
						// 折扣计算方式
						amount = amountCount.subtract(bd)
								.multiply(new BigDecimal("1").subtract(discount.divide(new BigDecimal(10))));
						torder.setDeAmount(amount);
						torder.setToalFreeAmount(amount);
					}
				}

				detailPreferentials.add(torder);
				amount = amount.multiply(new BigDecimal(String.valueOf(preferentialNum)));
			}
		}

		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
