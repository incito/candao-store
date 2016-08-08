package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.utils.RoundingEnum;
import com.candao.www.webroom.service.DataDictionaryService;

/**
 * 
 * @author 梁东 计算账单策略
 */
public class CalMenuOrderAmount implements CalMenuOrderAmountInterface {

	@Override
	public BigDecimal calPayAmount(DataDictionaryService dataDictionaryService, BigDecimal menuAmout,
			BigDecimal amount) {
		BigDecimal payAmount = new BigDecimal("0");
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
			payAmount = calPayAmount(RoundingMode.HALF_UP, menuAmout, amount, listFind);
			break;
		case REMOVETAIL:
			/** 抹零处理 **/
			payAmount = calPayAmount(RoundingMode.UP, menuAmout, amount, listFind);
			break;
		default:
			payAmount = menuAmout;
			break;
		}
		return payAmount;
	}

	private BigDecimal calPayAmount(RoundingMode branchid, BigDecimal menuAmout, BigDecimal amount,
			List<Map<String, Object>> listFind) {
		BigDecimal payAmount = new BigDecimal("0");
		if (listFind == null || listFind.size() <= 0) {
			// 支付价格菜单价-优惠价
			payAmount = menuAmout.subtract(amount);
		} else {
			// 0：分 1角 2元
			int itemId = Integer.valueOf((String) listFind.get(1).get("itemid"));
			int scale = itemId == 0 ? 2 : itemId == 1 ? 1 : 0;
			// 当前菜单多少钱
			// 当前优惠有多少钱
			payAmount = (menuAmout.subtract(amount)).divide(new BigDecimal("1"), scale, branchid);
		}
		/** 如果小于0表示使用的优惠总金额，大于总价，所以支付价应该为0 **/
		if (payAmount.doubleValue() <= 0) {
			payAmount = new BigDecimal("0");
		}
		return payAmount;
	}

}
