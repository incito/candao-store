package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.constant.Constant;
import com.candao.www.dataserver.mapper.CaleTableAmountMapper;
import com.candao.www.dataserver.mapper.OrderMapper;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.service.DataDictionaryService;

/**
 * 
 * @author liangDong 使用策略模式 Effective Java 单利模式 1.自由序列化
 *         2.保证只有一个实例（即使使用反射机制也无法多次实例化一个枚举量）； 3.线程安全
 */
public enum StrategyFactory {
	INSTANCE;
	private static Map<String, Object> strategyMap = new HashMap<>();
	static {
		strategyMap.put(Constant.CouponType.SPECIAL_TICKET, new SpecialTicketStrategy());
		strategyMap.put(Constant.CouponType.DISCOUNT_TICKET, new DiscountTicketStrategy());
		strategyMap.put(Constant.CouponType.ONLINEPAY_TICKET, new DiscountTicketStrategy());
		strategyMap.put(Constant.CouponType.VOUCHER, new VoucherStrategy());
		strategyMap.put(Constant.CouponType.GROUPON, new VoucherStrategy());
		strategyMap.put(Constant.CouponType.HANDFREE, new HandfreeStategy());
		strategyMap.put(Constant.CouponType.INNERFREE, new InnerfreeStrategy());
		strategyMap.put("Auto", new AutoCalPreFerntialStrategy());

	}

	private StrategyFactory() {
	};

	public CalPreferentialStrategyInterface buildAnimal(String type) {
		if (!strategyMap.containsKey(type)) {
			return null;
		}
		return (CalPreferentialStrategyInterface) strategyMap.get(type);
	}

	/**
	 * 计算实收金额
	 * 优免金额
	 * 挂账金额
	 * 小费
	 * @param caleTableAmountMapper
	 * @param orderid
	 * @param dataDictionaryService
	 * @param preferentialResult
	 * @param orderMapper
	 * (核心计算方式)
	 */
	public void calcAmount(CaleTableAmountMapper caleTableAmountMapper, String orderid,
			DataDictionaryService dataDictionaryService, OperPreferentialResult preferentialResult,OrderMapper orderMapper) {
		caleTableAmountMapper.pCaleTableAmount(orderid);
		Map<String, Object> amountMap = new HashMap<>();
		amountMap.put("orderid", orderid);
		List<Map<String, Object>> resAmountList = orderMapper.selectTableOrder(orderid);
		if (resAmountList != null && !resAmountList.isEmpty()) {
			Map<String, Object> resAmountMap = resAmountList.get(0);
			BigDecimal tipAmount = new BigDecimal(String.valueOf(resAmountMap.get("tipAmount")));// 小费
			BigDecimal dueamount = new BigDecimal(String.valueOf(resAmountMap.get("dueamount")));//订单（菜品）总价
			//计算实际优免金额与挂账金额
			
			//全单总价（不包含小费）
			preferentialResult.setMenuAmount(dueamount);
			preferentialResult.setTipAmount(tipAmount);
			//计算实际收入金额
			CalMenuOrderAmountInterface amountInterface = new CalMenuOrderAmount();
			preferentialResult.setPayamount(amountInterface.calPayAmount(dataDictionaryService, dueamount, preferentialResult.getAmount()));
		}
	}
}
