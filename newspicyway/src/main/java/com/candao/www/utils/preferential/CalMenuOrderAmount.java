package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.utils.RoundingEnum;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.service.DataDictionaryService;

/**
 * 
 * @author 梁东 计算账单策略
 */
public class CalMenuOrderAmount implements CalMenuOrderAmountInterface {

	@Override
	public void calPayAmount(DataDictionaryService dataDictionaryService, OperPreferentialResult preferentialResul) {
		Map<String, Object> map = new HashMap<>();
		map.put("type", "ROUNDING");
		List<Map<String, Object>> listFind = dataDictionaryService.findByParams(map);
		if ("ROUNDING".equals(map.get("type"))) {
			map.clear();
			map.put("type", "ACCURACY");
			List<Map<String, Object>> listFind2 = dataDictionaryService.findByParams(map);
			listFind.addAll(listFind2);
		}
		RoundingEnum roundingEnum = RoundingEnum.fromString((String) listFind.get(0).get("itemid"));
		switch (roundingEnum) {
		case ROUNDTOINTEGER:
			preferentialResul.setMoneyWipeName("四舍五入");
			preferentialResul.setMoneyDisType("1");
			/** 4舍5入 **/
			calPayAmount(RoundingMode.HALF_UP, preferentialResul, listFind);
			break;
		case REMOVETAIL:
			/** 抹零处理 **/
			preferentialResul.setMoneyWipeName("抹零");
			preferentialResul.setMoneyDisType("2");
			calPayAmount(RoundingMode.DOWN, preferentialResul, listFind);
			break;
		default:
			BigDecimal payAmount = preferentialResul.getMenuAmount().subtract(preferentialResul.getAmount());
			preferentialResul.setPayamount(payAmount);
			preferentialResul.setMoneyDisType("0");
			break;
		}
	}

	private void calPayAmount(RoundingMode branchid, OperPreferentialResult preferentialResul,
			List<Map<String, Object>> listFind) {
		BigDecimal menuAmout = preferentialResul.getMenuAmount();
		BigDecimal amount = preferentialResul.getAmount();
		BigDecimal payAmount = new BigDecimal("0");
		BigDecimal noCalAmount = new BigDecimal("0");
		if (listFind == null || listFind.size() <= 0) {
			// 支付价格菜单价-优惠价
			payAmount = menuAmout.subtract(amount);
		} else {
			// 0：分 1角 2元
			int itemId = Integer.valueOf((String) listFind.get(1).get("itemid"));
			int scale = itemId == 0 ? 2 : itemId == 1 ? 1 : 0;

			// 当前菜单多少钱没有四舍五入处理的
			noCalAmount = menuAmout.subtract(amount);
			// 当前优惠有多少钱(对元进行处理)
			if (scale == 0) {
				payAmount = noCalAmount.divide(new BigDecimal(10)).setScale(0,branchid)
						.multiply(new BigDecimal(10));
			} else {

				payAmount = noCalAmount.divide(new BigDecimal("1"), scale-1, branchid);
			}
		}
		/** 如果小于0表示使用的优惠总金额，大于总价，所以支付价应该为0 **/
		if (payAmount.doubleValue() <= 0) {
			payAmount = new BigDecimal("0");
		}
		preferentialResul.setPayamount(payAmount);
		preferentialResul.setMoneyWipeAmount(noCalAmount.subtract(payAmount));
	}

}
