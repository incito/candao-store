package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TbNoDiscountDish;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.preferential.model.PreDealInfoBean;

/**
 * 
 * @author Candao 折扣优惠卷
 */
public class DiscountTicketStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao,List<ComplexTorderDetail> orderDetailList) {
		// 错误提示
		String orderid = (String) paraMap.get("orderid"); // 账单号
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String branchid = PropertiesUtils.getValue("current_branch_id");
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid, branchid, tbPreferentialActivityDao);
		Map tempMap = tempMapList.get(0);
		BigDecimal discount = (BigDecimal) tempMap.get("discount");

		// 获取当前账单的 菜品列表
		Map<String, Object> result = new HashMap<>();
		// 将菜单表转换为菜品编号(key):菜品交易详情（Value）
		Set<String> orderDetailSet = new HashSet<>();
		for (TorderDetail detail : orderDetailList) {
			String key = detail.getDishid() + detail.getDishunit();
			orderDetailSet.add(key);
		}
		// 获取不参与折扣的菜品。并放入map
		Map<String, Object> noDisMap = new HashMap<>();
		noDisMap.put("discountId", preferentialid);
		noDisMap.put("orderDetail", orderDetailList);
		List<TbNoDiscountDish> noDiscountDishlist = tbDiscountTicketsDao.getNoDiscountDishsByDish(noDisMap);
		// 折扣方式
		Set<String> noDiscountDishSet = new HashSet<>();
		// 鱼锅折扣方式
		Set<String> fishnoDiscountDishSet = new HashSet<>();
		// 根据不优惠的菜品获取菜品库的菜品信息
		for (TbNoDiscountDish t : noDiscountDishlist) {
			String dishID = t.getDish();
			String dishUnit = t.getUnit();
			// 处理鱼锅
			Tdish tdish = tdishDao.get(dishID);
			if (tdish.getDishtype() == 1 && orderDetailSet.contains(dishID + dishUnit)) {
				List<Tdish> tdishList = tbDiscountTicketsDao.getDishidList(dishID);
				if (tdishList.size() > 0) {
					for (Tdish dish : tdishList) {
						fishnoDiscountDishSet.add(dish.getDishid());
					}
				}
			}
			noDiscountDishSet.add(dishID + dishUnit);
		}
		// 4.遍历账单的菜品，如果输入不进行折扣的菜品，则不处理。否则，认为是需要计算优惠。
		BigDecimal amount = new BigDecimal(0); // 最终优惠金额
		// 菜品原价
		BigDecimal amountCount = new BigDecimal(0.0);
		// 优惠折扣菜单个数
		double tempDishNum = 0;
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		for (TorderDetail d : orderDetailList) {
			String key = d.getDishid() + d.getDishunit();
			// 如果在不优惠的列表中没有找到这个菜品，则认为这个菜品是可以优惠打折的。
			if ((!noDiscountDishSet.contains(key) && !fishnoDiscountDishSet.contains(d.getDishid()))
					|| (fishnoDiscountDishSet.contains(d.getDishid()) && !d.getDishtype().equals("1"))) {
				// 判断价格，如果菜品价格存在null的问题，
				if (null != d.getOrderprice()) {
					// 如果此菜品是多份，则计算多份总的优惠价格
					amountCount = amountCount.add(d.getOrderprice().multiply(new BigDecimal(d.getDishnum())));
					tempDishNum += Double.valueOf(d.getDishnum());
				}
			}
		}
		// 如果需要折扣的菜品的总价不大于0或者小于已经折扣掉的金额，则不计算本次折扣金额
		PreDealInfoBean deInfo = this.calDiscount(amountCount, bd, discount);
		if (deInfo.getPreAmount().doubleValue() > 0) {
			amount = deInfo.getPreAmount();
			int group = tempDishNum == orderDetailList.size() ? 1 : 0;
			TorderDetailPreferential preSub = this.createPreferentialBean(paraMap, amount, tempDishNum, discount, group,
					tempMapList);
			detailPreferentials.add(preSub);
		} else {
			// 如果为空说明当前已经删除了此菜品，那么就应该删除此优惠卷
			if (paraMap.containsKey("updateId")) {
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("DetalPreferentiald", paraMap.get("updateId"));
				delMap.put("orderid", orderid);
				orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
			}
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		this.disMes(result, amountCount, amountCount, bd, deInfo.getDistodis());
		return result;
	}

}
