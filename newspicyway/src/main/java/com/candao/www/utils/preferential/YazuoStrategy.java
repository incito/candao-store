package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.constant.Constant;
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
 * @author Candao 雅座使用优惠
 */
public class YazuoStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {

		// 已经优免金额
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		// 优惠金额
		String preAmout = (String) paraMap.get("preferentialAmout");
		// 折扣
		String disrate = String.valueOf(paraMap.get("disrate"));
		BigDecimal discount = new BigDecimal(disrate.trim().isEmpty() ? "0" : disrate);

		// 账单号
		String orderid = (String) paraMap.get("orderid");

		// 当前订单总价amountCount

		// 获取当前账单的 菜品列表
	
		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();

		// 优惠金额
		BigDecimal amount = new BigDecimal(String.valueOf(paraMap.get("preferentialAmout")));
		// 使用优惠张数
		int preferentialNum = Integer.valueOf((String) paraMap.get("preferentialNum"));
		for (int i = 0; i < preferentialNum; i++) {
			// 是否是更新
			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
			Date insertime = (paraMap.containsKey("insertime") ? (Date) paraMap.get("insertime") : new Date());
			TorderDetailPreferential torder = new TorderDetailPreferential(updateId, orderid, "",
					(String) paraMap.get("preferentialid"), amount, "", 1, 1, discount, 5, insertime);

			// 设置优惠名称
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) paraMap.get("preferentialName"));
			torder.setActivity(activity);
			// 设置优惠类型
			torder.setPreType((String) paraMap.get("type"));
			//设置优惠名称
			torder.setPreName((String) paraMap.get("preferentialName"));

			if (paraMap.get("type").equals(Constant.CouponType.YAZUOFREE)) {
				// 代金卷
				torder.setDeAmount(amount);
				torder.setToalFreeAmount(amount);
			} else if (paraMap.get("type").equals(Constant.CouponType.YAZUO_DISCOUNT_TICKET)) {
				Map<String, String> orderDetail_params = new HashMap<>();
				orderDetail_params.put("orderid", orderid);
				List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
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
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount.multiply(new BigDecimal(String.valueOf(preferentialNum))));
		return result;
	}

}