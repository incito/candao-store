package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.preferential.model.PreDealInfoBean;

/**
 * 
 * @author Candao 手工优惠---手工优免 三种优惠方式：赠菜，折扣，优免 此类优惠需要收银员手动输入的
 */
public class HandfreeStategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String orderid = (String) paraMap.get("orderid"); // 账单号
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		String disrate = String.valueOf(paraMap.get("disrate"));
		String giveDish = (String) paraMap.get("dishid");
		BigDecimal discount = new BigDecimal(disrate.trim().isEmpty() ? "0" : disrate);
		Map<String, Object> cashGratis = cashGratis(paraMap, tbPreferentialActivityDao, orderDetailList);
		if (cashGratis != null) {
			return cashGratis;
		}

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
						new BigDecimal("0"), orderDetailList.size(), discount, 1, (String) tempMap.get("name"), conupId,
						1);

				detailPreferentials.add(preSub);
			}
			this.disMes(result, amountCount, amountCount, bd, deInfo.getDistodis());

		} else if (!StringUtils.isEmpty(preferentialAmout.trim()) && StringUtils.isEmpty(giveDish)
				&& StringUtils.isEmpty(disrate.trim())) {
			// 手工输入现金优免
			BigDecimal cashprelAmout = new BigDecimal(preferentialAmout);
			amount = cashprelAmout;
			String conupId =(String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id"));
			TorderDetailPreferential addPreferential = this.createPreferentialBean(paraMap, amount, amount,
					new BigDecimal("0"), orderDetailList.size(), discount, 1, (String) tempMap.get("name"), conupId,
					1);
			detailPreferentials.add(addPreferential);
		} else if (!StringUtils.isEmpty(giveDish) && StringUtils.isEmpty(paraMap.get("discount"))
				&& StringUtils.isEmpty(paraMap.get("amount"))) {

			// 赠菜卷，赠卷有dishID，无折扣 ，无现金优免
			// 获取所有ID 以及对应的菜品个数
			String[] preDishNum = ((String) paraMap.get("preferentialNum")).split(",");
			String[] preALLDishId = giveDish.split(",");
			String[] units = ((String) paraMap.get("unit")).split(",");

			// 将订单数据装换成转换dishID-订单详细
			Map<String, TorderDetail> orderDetailmap = new HashMap<>();
			// 将订单数据转换成dishID-dishNum
			Map<String, Double> orderDishIdToDishNumMap = new HashMap<>();
			for (TorderDetail detail : orderDetailList) {
				String key = detail.getDishid() + detail.getDishunit();
				orderDetailmap.put(key, detail);
				if (orderDishIdToDishNumMap.containsKey(key)) {
					orderDishIdToDishNumMap.put(key,
							Double.valueOf(detail.getDishnum()) + orderDishIdToDishNumMap.get(key));
				} else {
					orderDishIdToDishNumMap.put(key, Double.valueOf(detail.getDishnum()));
				}
			}
			// 卷对应的菜品数据（一个菜多少个卷）
			List<TorderDetailPreferential> orderDetailToPreferList = orderDetailPreferentialDao
					.queryDetailPreByGift(orderid);
			Map<String, Double> orderDetailToPreferMap = new HashMap<>();
			for (TorderDetailPreferential detailPreferential : orderDetailToPreferList) {
				String key = detailPreferential.getDishid() + detailPreferential.getUnit();
				if (orderDetailToPreferMap.containsKey(key)) {
					orderDetailToPreferMap.put(key,
							Double.valueOf(detailPreferential.getDishNum()) + orderDetailToPreferMap.get(key));
				} else {
					orderDetailToPreferMap.put(key, Double.valueOf(detailPreferential.getDishNum()));
				}

			}

			// 获取数据库使用赠菜卷的数据 不包含
			if (paraMap.containsKey("updateId")) {
				String key = preALLDishId[0] + units[0];
				// 获取当前菜品个数
				Double orderDishNum = orderDishIdToDishNumMap.get(key);
				// 获取当前使用菜品优惠个数如果优惠大于菜品者删除优惠
				Double userPreferId = orderDetailToPreferMap.get(key);
				if (orderDishNum < userPreferId) {
					// 如果为空说明当前已经删除了此菜品，那么就应该删除此优惠卷
					Map<String, Object> delMap = new HashMap<>();
					delMap.put("DetalPreferentiald", paraMap.get("updateId"));
					delMap.put("orderid", orderid);
					orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
				} else {
					tempMap.put("name",
							(String) tempMap.get("name") + "(" + foodNameMap.get(key) + "/" + units[0] + ")");
					TorderDetailPreferential detailPreferential = createTorderDetail(orderDetailmap, key, 1, paraMap,
							amount, orderid, preferentialid, tempMap, tempMapList);
					amount = amount.add(detailPreferential.getDeAmount());
					detailPreferentials.add(detailPreferential);

				}
			} else {
				// 菜品对应个数
				Map<String, Integer> dishForDishNum = new HashMap<>();
				for (int i = 0; i < preALLDishId.length; i++) {
					String key = preALLDishId[i] + units[i];
					String dishNum = preDishNum[i];
					if (dishForDishNum.containsKey(key)) {
						dishForDishNum.put(key, dishForDishNum.get(key) + Integer.valueOf(dishNum));
					} else {
						dishForDishNum.put(key, Integer.valueOf(dishNum));
					}
				}

				for (String dishId : dishForDishNum.keySet()) {
					int inputNum = dishForDishNum.get(dishId);
					if (orderDetailToPreferMap.containsKey(dishId)) {
						// 获取当前菜品个数
						Double orderDishNum = orderDishIdToDishNumMap.get(dishId);
						// 获取当前使用菜品优惠个数如果优惠大于菜品者删除优惠
						Double userPreferIdNum = orderDetailToPreferMap.get(dishId);
						if ((userPreferIdNum + inputNum) > orderDishNum) {
							continue;
						}
					}
					for (int i = 0; i < inputNum; i++) {
						tempMap.put("name", (String) tempMap.get("name") + "(" + foodNameMap.get(dishId) + "/"
								+ orderDetailmap.get(dishId).getDishunit() + ")");
						TorderDetailPreferential detailPreferential = createTorderDetail(orderDetailmap, dishId, 1,
								paraMap, amount, orderid, preferentialid, tempMap, tempMapList);
						amount = amount.add(detailPreferential.getDeAmount());
						detailPreferentials.add(detailPreferential);
					}

				}

			}

		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

	private TorderDetailPreferential createTorderDetail(Map<String, TorderDetail> orderDetailmap, String dishId,
			int inputNum, Map<String, Object> paraMap, BigDecimal amount, String orderid, String preferentialid,
			Map tempMap, List<Map<String, Object>> tempMapList) {
		// 创建优惠
		TorderDetailPreferential addPreferential = null;
		TorderDetail ordetail = orderDetailmap.get(dishId);
		if (ordetail != null) {
			for (int i = 0; i < inputNum; i++) {
				BigDecimal orderprice = ordetail.getOrderprice() == null ? new BigDecimal("0")
						: ordetail.getOrderprice();
				amount = amount.add(orderprice);
				
				String conupId =(String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id"));
			   addPreferential = this.createPreferentialBean(paraMap, amount, amount,
						new BigDecimal("0"), 1, new BigDecimal(0), 0, (String) tempMap.get("name"), conupId,
						4);
			   addPreferential.setDishid(ordetail.getDishid());
			   addPreferential.setUnit(ordetail.getDishunit());
			}
		}
		return addPreferential;
	}

}
