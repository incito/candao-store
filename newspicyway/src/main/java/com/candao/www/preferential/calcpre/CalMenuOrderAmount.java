package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.preferential.model.PreDealInfoBean;
import com.candao.www.utils.RoundingEnum;
import com.candao.www.webroom.service.DataDictionaryService;

/**
 * 
 * @author 梁东 计算账单策略
 */
public class CalMenuOrderAmount implements CalMenuOrderAmountInterface {

	@Override
	public PreDealInfoBean calPayAmount(DataDictionaryService dataDictionaryService, String itemid, BigDecimal menuAmount, BigDecimal amount) {
		List<Map<String, Object>> listFind = calculationMethod(dataDictionaryService);
		PreDealInfoBean dealInfoBean = new PreDealInfoBean();
		String calItemid = "";
		if (itemid == null || !itemid.equals("0")) {
			calItemid = (String) listFind.get(0).get("itemid");
		} else {
			calItemid = itemid;
		}
		RoundingEnum roundingEnum = RoundingEnum.fromString(calItemid);
		switch (roundingEnum) {
		case ROUNDTOINTEGER:
			/** 4舍5入 **/
			dealInfoBean = calPayAmount(RoundingMode.HALF_UP, listFind, amount, menuAmount);
			dealInfoBean.setMoneyWipeName("四舍五入");
			dealInfoBean.setMoneyDisType("1");
			break;
		case REMOVETAIL:
			/** 抹零处理 **/

			dealInfoBean = calPayAmount(RoundingMode.HALF_DOWN, listFind, amount, menuAmount);
			dealInfoBean.setMoneyWipeName("抹零");
			dealInfoBean.setMoneyDisType("2");
			break;
		default:
			BigDecimal payAmount = menuAmount.subtract(amount);
			dealInfoBean.setPayAmount(payAmount.compareTo(new BigDecimal("0")) == 1 ? payAmount : new BigDecimal("0"));
			dealInfoBean.setMoneyDisType("0");
			break;
		}
		return dealInfoBean;
	}

	private PreDealInfoBean calPayAmount(RoundingMode branchid, List<Map<String, Object>> listFind,
			BigDecimal statisticPrice, BigDecimal menuAmout) {
		BigDecimal payAmount = new BigDecimal("0");
		BigDecimal noCalAmount = new BigDecimal("0");
		if (listFind == null || listFind.size() <= 0) {
			// 支付价格菜单价-优惠价
			payAmount = menuAmout.subtract(statisticPrice);
		} else {
			// 0：分 1角 2元
			int itemId = Integer.valueOf((String) listFind.get(1).get("itemid"));
			int scale = itemId == 0 ? 2 : itemId == 1 ? 1 : 0;

			// 当前菜单多少钱没有四舍五入处理的
			noCalAmount = menuAmout.subtract(statisticPrice);
			// 当前优惠有多少钱(对元进行处理)
			if (scale == 0) {
				payAmount = noCalAmount.divide(new BigDecimal(10)).setScale(0, branchid).multiply(new BigDecimal(10));
			} else {

				payAmount = noCalAmount.divide(new BigDecimal("1"), scale - 1, branchid);
			}
		}
		/** 如果小于0表示使用的优惠总金额，大于总价，所以支付价应该为0 **/
		if (payAmount.doubleValue() <= 0) {
			payAmount = new BigDecimal("0");
		}
		BigDecimal menuAmount = menuAmout;
		BigDecimal moneyWipeAmount = null;
		if (menuAmount.compareTo(statisticPrice) == -1) {
			moneyWipeAmount = new BigDecimal("0");
		} else {
			moneyWipeAmount = noCalAmount.subtract(payAmount);
		}
		return new PreDealInfoBean(payAmount, moneyWipeAmount);
	}

	private List<Map<String, Object>> calculationMethod(DataDictionaryService dataDictionaryService) {
		Map<String, Object> map = new HashMap<>();
		map.put("type", "ROUNDING");
		List<Map<String, Object>> listFind = dataDictionaryService.findByParams(map);
		if ("ROUNDING".equals(map.get("type"))) {
			map.clear();
			map.put("type", "ACCURACY");
			List<Map<String, Object>> listFind2 = dataDictionaryService.findByParams(map);
			listFind.addAll(listFind2);
		}
		return listFind;
	}

}
