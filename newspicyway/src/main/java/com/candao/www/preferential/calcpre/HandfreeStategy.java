package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.preferential.calcpre.manual.PresentDishesStrategy;
import com.candao.www.preferential.model.PreDealInfoBean;

/**
 * 
 * @author Canada 手工优惠---手工优免 三种优惠方式：赠菜，折扣，优免 此类优惠需要收银员手动输入的
 */
public class HandfreeStategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		String disrate = String.valueOf(paraMap.get("disrate"));
		String giveDish = (String) paraMap.get("dishid");
		BigDecimal discount = new BigDecimal(disrate.trim().isEmpty() ? "0" : disrate);
		// 返回优惠集合
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		/** 优惠卷信息 **/
		List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid,
				PropertiesUtils.getValue("current_branch_id"), tbPreferentialActivityDao);
		Map tempMap = tempMapList.get(0);
		/** 当前订单信息 **/
		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		BigDecimal amount = new BigDecimal(0);
		// 菜品原价
		BigDecimal amountCount = new BigDecimal(0.0);
		// 获取菜品名称
		Map<String, String> foodNameMap = new HashMap<>();
		for (ComplexTorderDetail d : orderDetailList) {
			foodNameMap.put(d.getDishid() + d.getDishunit(), d.getTitle());
			// 判断价格，如果菜品价格存在null的问题，则返回错误信息
			if (null != d.getOrderprice()) {
				// 如果此菜品是多份，则计算多份总的优惠价格
				BigDecimal numOfDish = new BigDecimal("1");
				if (new BigDecimal(d.getDishnum()).compareTo(new BigDecimal("0")) > 0) {
					numOfDish = new BigDecimal(d.getDishnum());
				}
				amountCount = amountCount.add(d.getOrderprice().multiply(numOfDish));
			}

		}
		/**
		 * disrate不为空为折扣优免 preferentialAmout 不为空为现金优惠 giveDish 不为空为优惠卷信息
		 */
		String preferentialAmout = (String) paraMap.get("preferentialAmout");
		if (!StringUtils.isEmpty(disrate.trim()) && new BigDecimal(preferentialAmout).doubleValue() <= 0
				&& StringUtils.isEmpty(giveDish)) {
			PreDealInfoBean deInfo = this.calDiscount(amountCount, bd, discount);
			if (deInfo.getPreAmount().doubleValue() > 0) {
				amount = deInfo.getPreAmount();
				String conupId = (String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id"));
				TorderDetailPreferential preSub = this.createPreferentialBean(paraMap, amount, amount,
						new BigDecimal("0"), orderDetailList.size(), discount, Constant.CALCPRETYPE.GROUP,
						(String) tempMap.get("name"), conupId, Constant.CALCPRETYPE.WAITERUSEPRE);

				detailPreferentials.add(preSub);
			}
			this.disMes(result, amountCount, amountCount, bd, deInfo.getDistodis());

		} else if (!StringUtils.isEmpty(preferentialAmout.trim()) && StringUtils.isEmpty(giveDish)
				&& new BigDecimal(preferentialAmout).doubleValue() > 0) {
			// 手工输入现金优免
			BigDecimal cashprelAmout = new BigDecimal(preferentialAmout);
			amount = cashprelAmout;
			String conupId = (String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id"));
			TorderDetailPreferential addPreferential = this.createPreferentialBean(paraMap, amount, amount,
					new BigDecimal("0"), orderDetailList.size(), discount, Constant.CALCPRETYPE.GROUP,
					(String) tempMap.get("name"), conupId, Constant.CALCPRETYPE.WAITERUSEPRE);
			detailPreferentials.add(addPreferential);
		} else if (!StringUtils.isEmpty(giveDish) && StringUtils.isEmpty(paraMap.get("discount"))
				&& StringUtils.isEmpty(paraMap.get("amount"))) {

			CalPreferentialStrategy dishesStrategy = new PresentDishesStrategy(tempMapList);
			return dishesStrategy.calPreferential(paraMap, tbPreferentialActivityDao, orderDetailPreferentialDao,
					tbDiscountTicketsDao, tdishDao, orderDetailList);

		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
