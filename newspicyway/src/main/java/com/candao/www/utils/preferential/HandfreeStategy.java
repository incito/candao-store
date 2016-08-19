package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/**
 * 
 * @author Candao 手工优惠---手工优免 三种优惠方式：赠菜，折扣，优免 此类优惠需要收银员手动输入的
 */
public class HandfreeStategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String orderid = (String) paraMap.get("orderid"); // 账单号
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		String disrate = String.valueOf(paraMap.get("disrate"));
		String giveDish = (String) paraMap.get("dishid");
		BigDecimal discount = new BigDecimal(disrate.trim().isEmpty() ? "0" : disrate);
		Map<String, Object> cashGratis = cashGratis(paraMap, torderDetailDao, tbPreferentialActivityDao);
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
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);

		BigDecimal amount = new BigDecimal(0);
		// 菜品原价
		BigDecimal amountCount = new BigDecimal(0.0);
		for (TorderDetail d : orderDetailList) {
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
			BigDecimal decimalDisrate = new BigDecimal(disrate);
			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
			Date insertime = (paraMap.containsKey("insertime") ?  (Date) paraMap.get("insertime")
					: new Date());
			// 如果需要折扣的菜品的总价不大于0或者小于已经折扣掉的金额，则不计算本次折扣金额
			// 手工输入折扣优免
			if (amountCount.compareTo(BigDecimal.ZERO) > 0
					&& (amountCount.subtract(bd).compareTo(BigDecimal.ZERO)) != -1) {
				amount = amountCount.subtract(bd)
						.multiply(new BigDecimal("1").subtract(decimalDisrate.divide(new BigDecimal(10))));
				TorderDetailPreferential addPreferential = new TorderDetailPreferential(updateId, orderid, "",
						preferentialid, amount, String.valueOf(orderDetailList.size()), 1, 1, discount, 1,insertime);
				// 设置优惠名称
				TbPreferentialActivity activity = new TbPreferentialActivity();
				activity.setName((String) tempMap.get("name"));
				addPreferential.setActivity(activity);
				addPreferential.setCoupondetailid(
						(String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id")));

				// 设置优免
				addPreferential.setToalFreeAmount(amount);
				detailPreferentials.add(addPreferential);
			}

		} else if (!StringUtils.isEmpty(preferentialAmout.trim()) && StringUtils.isEmpty(giveDish)
				&& StringUtils.isEmpty(disrate.trim())) {
			// 手工输入现金优免
			BigDecimal cashprelAmout = new BigDecimal(preferentialAmout);
			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
			Date insertime = (paraMap.containsKey("insertime") ?  (Date) paraMap.get("insertime")
					: new Date());
			amount = cashprelAmout;
			TorderDetailPreferential addPreferential = new TorderDetailPreferential(updateId, orderid, "",
					preferentialid, amount, String.valueOf(orderDetailList.size()), 1, 1, discount, 1,insertime);
			// 设置优惠名称
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) tempMap.get("name"));
			addPreferential.setActivity(activity);
			addPreferential.setCoupondetailid(
					(String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id")));
			// 设置优免
			addPreferential.setToalFreeAmount(amount);
			detailPreferentials.add(addPreferential);
		} else if (!StringUtils.isEmpty(giveDish) && StringUtils.isEmpty(paraMap.get("discount"))
				&& StringUtils.isEmpty(paraMap.get("amount"))) {

			// 赠菜卷，赠卷有dishID，无折扣 ，无现金优免
			// 获取所有ID 以及对应的菜品个数
			String[] preDishNum = ((String) paraMap.get("preferentialNum")).split(",");
			String[] preALLDishId = giveDish.split(",");

			// 将订单数据装换成转换dishID-订单详细
			Map<String, TorderDetail> orderDetailmap = new HashMap<>();
			// 将订单数据转换成dishID-dishNum
			Map<String, Double> orderDishIdToDishNumMap = new HashMap<>();
			for (TorderDetail detail : orderDetailList) {
				orderDetailmap.put(detail.getDishid(), detail);
				if (orderDishIdToDishNumMap.containsKey(detail.getDishid())) {
					orderDishIdToDishNumMap.put(detail.getDishid(),
							Double.valueOf(detail.getDishnum()) + orderDishIdToDishNumMap.get(detail.getDishid()));
				} else {
					orderDishIdToDishNumMap.put(detail.getDishid(), Double.valueOf(detail.getDishnum()));
				}
			}
			// 卷对应的菜品数据（一个菜多少个卷）
			List<TorderDetailPreferential> orderDetailToPreferList = orderDetailPreferentialDao
					.queryDetailPreByGift(orderid);
			Map<String, Double> orderDetailToPreferMap = new HashMap<>();
			for (TorderDetailPreferential detailPreferential : orderDetailToPreferList) {
				if (orderDetailToPreferMap.containsKey(detailPreferential.getDishid())) {
					orderDetailToPreferMap.put(detailPreferential.getDishid(),
							Double.valueOf(detailPreferential.getDishNum())
									+ orderDetailToPreferMap.get(detailPreferential.getDishid()));
				} else {
					orderDetailToPreferMap.put(detailPreferential.getDishid(),
							Double.valueOf(detailPreferential.getDishNum()));
				}

			}

			// 获取数据库使用赠菜卷的数据 不包含
			if (paraMap.containsKey("updateId")) {
				String dataDishId = preALLDishId[0];
				// 获取当前菜品个数
				Double orderDishNum = orderDishIdToDishNumMap.get(dataDishId);
				// 获取当前使用菜品优惠个数如果优惠大于菜品者删除优惠
				Double userPreferId = orderDetailToPreferMap.get(dataDishId);
				if (orderDishNum < userPreferId) {
					// 如果为空说明当前已经删除了此菜品，那么就应该删除此优惠卷
					Map<String, Object> delMap = new HashMap<>();
					delMap.put("DetalPreferentiald", paraMap.get("updateId"));
					delMap.put("orderid", orderid);
					orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
				} else {
					TorderDetailPreferential detailPreferential = createTorderDetail(orderDetailmap, dataDishId, 1,
							paraMap, amount, orderid, preferentialid, tempMap, tempMapList);
					amount=amount.add(detailPreferential.getDeAmount());
					detailPreferentials.add(detailPreferential);
					
				}
			} else {
				// 菜品对应个数
				Map<String, Integer> dishForDishNum = new HashMap<>();
				for (int i = 0; i < preALLDishId.length; i++) {
					dishForDishNum.put(preALLDishId[i], Integer.valueOf(preDishNum[i]));
				}

				for (String dishId : dishForDishNum.keySet()) {
					int inputNum = dishForDishNum.get(dishId);

					if (orderDetailToPreferMap.containsKey(dishId)) {
						// 验证是否可以使用优惠
						// 获取当前菜品个数
						Double orderDishNum = orderDishIdToDishNumMap.get(dishId);
						// 获取当前使用菜品优惠个数如果优惠大于菜品者删除优惠
						Double userPreferIdNum = orderDetailToPreferMap.get(dishId);
						if ((userPreferIdNum + inputNum) > orderDishNum) {
							continue;
						}
					}
					for (int i = 0; i < inputNum; i++) {
						TorderDetailPreferential detailPreferential = createTorderDetail(orderDetailmap, dishId, 1,
								paraMap, amount, orderid, preferentialid, tempMap, tempMapList);
						amount=amount.add(detailPreferential.getDeAmount());
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
				String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
				Date insertime = (paraMap.containsKey("insertime") ?  (Date) paraMap.get("insertime")
						: new Date());
				BigDecimal orderprice = ordetail.getOrderprice()==null?new BigDecimal("0"): ordetail.getOrderprice();
				amount = amount.add(orderprice);
				addPreferential = new TorderDetailPreferential(updateId, orderid, dishId, preferentialid, orderprice,
						"1", 0, 1, new BigDecimal(0), 4,insertime);
				// 设置优惠名称
				TbPreferentialActivity activity = new TbPreferentialActivity();
				activity.setName((String) tempMap.get("name"));
				addPreferential.setActivity(activity);
				addPreferential.setCoupondetailid(
						(String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id")));
				// 设置优免
				addPreferential.setToalFreeAmount(orderprice);
				addPreferential.setDeAmount(orderprice);
			}
		}
		return addPreferential;
	}

}
