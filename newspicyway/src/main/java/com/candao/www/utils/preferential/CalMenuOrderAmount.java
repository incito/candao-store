package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.utils.RoundingEnum;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.service.DataDictionaryService;

public class CalMenuOrderAmount implements CalMenuOrderAmountInterface {

	@Override
	public void calAmount(DataDictionaryService dataDictionaryService, OperPreferentialResult preferentialResult) {
		Map<String, Object> map = new HashMap<>();
		map.put("type", "ROUNDING");

		List<Map<String, Object>> listFind = dataDictionaryService.findByParams(map);
		if ("ROUNDING".equals(map.get("type"))) {
			map.clear();
			map.put("type", "ACCURACY");
			List<Map<String, Object>> listFind2 = dataDictionaryService.findByParams(map);
			listFind.addAll(listFind2);
		}
		RoundingEnum roundingEnum = RoundingEnum.fromString("1");
		switch (roundingEnum) {

		case ROUNDTOINTEGER:
			/** 4舍5入 **/
			calAmount(RoundingMode.HALF_UP, preferentialResult, listFind);
			break;
		case REMOVETAIL:
			/** 抹零处理 **/
			calAmount(RoundingMode.UP, preferentialResult, listFind);
			break;
		default:
			preferentialResult.setPayamount(preferentialResult.getMenuAmount());
			break;
		}

	}

	private void calAmount(RoundingMode branchid, OperPreferentialResult preferentialResult,
			List<Map<String, Object>> listFind) {
		BigDecimal payAmount = new BigDecimal("0");
		if (listFind == null || listFind.size() <= 0) {
			// 支付价格菜单价-优惠价
			payAmount = preferentialResult.getMenuAmount().subtract(preferentialResult.getAmount());
		} else {
			// 0：分 1角 2元
			int itemId = Integer.valueOf((String) listFind.get(1).get("itemid"));
			int scale = itemId == 0 ? 2 : itemId == 1 ? 1 : 0;
			// 当前菜单多少钱
			BigDecimal menuItem = preferentialResult.getMenuAmount();
			// 当前优惠有多少钱
			BigDecimal amount = preferentialResult.getAmount();
			payAmount = (menuItem.subtract(amount)).divide(new BigDecimal("1"), scale, branchid);
		}

		if (payAmount.doubleValue() <= 0) {
			preferentialResult.setPayamount(new BigDecimal("0"));
		} else {
			preferentialResult.setPayamount(payAmount);
		}

	}

}
