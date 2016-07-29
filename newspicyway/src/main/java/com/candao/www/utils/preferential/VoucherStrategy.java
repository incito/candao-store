package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/**
 * 
 * @author Candao 优惠卷使用策略
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

		int preferentialNum = (Integer) paraMap.get("preferentialNum");// 使用优惠张数
		BigDecimal amount = new BigDecimal((String) paraMap.get("amount"));

		Map<String, Object> cashGratis = cashGratis(paraMap, torderDetailDao);
		if (cashGratis != null) {
			return cashGratis;
		}
		// 获取当前账单的 菜品列表
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();

		for (int i = 0; i < preferentialNum; i++) {
			 String updateId=paraMap.containsKey("updateId")?(String)paraMap.get("updateId"):IDUtil.getID();
			detailPreferentials.add(
					new TorderDetailPreferential(updateId, orderid, "", (String) paraMap.get("preferentialid"),
							amount, String.valueOf(orderDetailList.size()), 1, 1, new BigDecimal(1), 0));
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount.multiply(new BigDecimal(String.valueOf(preferentialNum))));
		return result;
	}

}
