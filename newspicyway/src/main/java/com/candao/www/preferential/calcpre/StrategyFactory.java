package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.dataserver.mapper.CaleTableAmountMapper;
import com.candao.www.dataserver.mapper.OrderMapper;
import com.candao.www.dataserver.mapper.OrderOpMapper;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.TorderDetailPreferentialService;

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
		strategyMap.put(Constant.CouponType.YAZUOFREE, new YazuoStrategy());
		strategyMap.put(Constant.CouponType.YAZUO_DISCOUNT_TICKET, new YazuoStrategy());
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
	 * 计算实收金额 优免金额 挂账金额 小费
	 * @param orderDetailPreferentialDao 
	 * 
	 * @param caleTableAmountMapper
	 * @param orderid
	 * @param dataDictionaryService
	 * @param torderDetailPreferentialService 
	 * @param preferentialResult
	 * @param orderMapper
	 *            (核心计算方式)
	 */
	public void calcAmount(TorderDetailPreferentialDao orderDetailPreferentialDao, CaleTableAmountMapper caleTableAmountMapper, String orderid,
			DataDictionaryService dataDictionaryService, OperPreferentialResult preferentialResult,
			OrderMapper orderMapper, OrderOpMapper orderOpMapper,String itemid) {
		caleTableAmountMapper.pCaleTableAmount(orderid);
		Map<String, Object> amountMap = new HashMap<>();
		amountMap.put("orderid", orderid);
		List<Map<String, Object>> resAmountList = orderMapper.selectTableOrder(orderid);
		if (resAmountList != null && !resAmountList.isEmpty()) {
			Map<String, Object> resAmountMap = resAmountList.get(0);
			BigDecimal tipAmount = new BigDecimal(String.valueOf(resAmountMap.get("tipAmount")));// 小费
			BigDecimal dueamount = new BigDecimal(String.valueOf(resAmountMap.get("dueamount")));// 订单（菜品）总价
			BigDecimal originalOrderAmount=new BigDecimal(String.valueOf(resAmountMap.get("freeamount")));// 原价
			// 赠送金额
			float zaAmount = orderOpMapper.getZdAmountByOrderId(orderid);
			preferentialResult.setZdAmount(new BigDecimal(zaAmount));
			// 全单总价（不包含小费）
			preferentialResult.setMenuAmount(dueamount);
			preferentialResult.setTipAmount(tipAmount);
			//原始价格
			preferentialResult.setOriginalOrderAmount(originalOrderAmount);
			// 计算实际收入金额
			// 优免调整（如果优惠大于订单价做优惠调整）
			if (preferentialResult.getAmount().compareTo(preferentialResult.getMenuAmount()) == 1) {
				// 挂账多收规则 优免调整=((挂账+优免)-订单价)
				BigDecimal orderPrice = preferentialResult.getMenuAmount();
				// 优免
				BigDecimal toalFreeAmount = preferentialResult.getToalFreeAmount();
				// 挂账
				BigDecimal toaldDebitAmount = preferentialResult.getToalDebitAmount();
				BigDecimal toaldDebitAmountMany = preferentialResult.getToalDebitAmountMany();
				preferentialResult
						.setAdjAmout(orderPrice.subtract(toaldDebitAmount.add(toalFreeAmount).add(toaldDebitAmountMany)));
			}
			//查询数据库价格
			BigDecimal statisticPrice = orderDetailPreferentialDao.statisticALLDiscount(orderid);
			new CalMenuOrderAmount().calPayAmount(dataDictionaryService, preferentialResult,itemid,statisticPrice);
			//优惠总消费重新计算（菜单总价-应收金额）
			preferentialResult.setAmount(preferentialResult.getMenuAmount().subtract(preferentialResult.getPayamount()));
			// 应收应该是小费+消费
			preferentialResult.setPayamount(preferentialResult.getPayamount().add(tipAmount));
			//预结单小票
			preferentialResult.setReserveAmout(preferentialResult.getPayamount().add(preferentialResult.getToalDebitAmount()));
			

		}
	}

	/**
	 * 
	 * @param orderPrice
	 *            订单总价
	 * @param orderPrePrice
	 *            订单总优免
	 * @param debitPrice
	 *            挂账金额
	 * @param onePrePrice
	 *            当前优惠金额
	 * @param calType
	 *            5挂账 6优免（对应数据库字典表）
	 * @return
	 */
	public Map<String, BigDecimal> calPreferentialPrice(BigDecimal orderPrice, BigDecimal orderPrePrice,
			BigDecimal debitPrice, BigDecimal onePrePrice, int calType) {
		// 返回
		Map<String, BigDecimal> resultMapPrice = new HashMap<>();
		// 实际收入价格
		BigDecimal orderAct = orderPrice.subtract(orderPrePrice);
		// 优免金额
		BigDecimal toalFreeAmount = new BigDecimal(0);
		// 挂装金额
		BigDecimal toalDebitAmount = new BigDecimal(0);
		switch (calType) {
		case Constant.PAYWAY.DEBITE_ACCOUNT:
			// 挂账
			toalDebitAmount = debitPrice;
			if (orderAct.compareTo(onePrePrice) == 1) {
				// 如果实际支付金额大于总优惠金额
				toalFreeAmount = onePrePrice.subtract(debitPrice);
			} else if (orderAct.compareTo(onePrePrice) == -1 && orderAct.compareTo(debitPrice) == 1) {
				// 如果实际支付金额大于挂账金额，小于抵用金额
				toalFreeAmount = orderAct.subtract(debitPrice);
			}
			break;
		case Constant.PAYWAY.PAYWAY_FREE:
			// 优免
			if (orderAct.compareTo(onePrePrice) == 1) {
				toalFreeAmount = onePrePrice;
			} else if (orderAct.compareTo(onePrePrice) == -1) {
				toalFreeAmount = orderAct;
			} else {
				toalFreeAmount = onePrePrice;
			}
			break;
		}
		resultMapPrice.put("toalFreeAmount", toalFreeAmount);
		resultMapPrice.put("toalDebitAmount", toalDebitAmount);
		return resultMapPrice;
	}
}
