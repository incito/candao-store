package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * @author Candao 新辣到特殊优惠
 */
public class AutoCalPreFerntialStrategy extends CalPreferentialStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {
		String branchid = PropertiesUtils.getValue("current_branch_id");
		Map<String, Object> params = new HashMap<>();
		params.put("type", 3);
		params.put("branchid", branchid);
		params.put("status", 2);
		String orderid = (String) paraMap.get("orderid"); // 账单号
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<Map<String, Object>> orderDetailList = torderDetailDao.findorderByDish(orderid);
		// 1:双拼锅 0：单品
		List<TorderDetail> doublePots = new ArrayList<>();
		List<TorderDetail> singleDishs = new ArrayList<>();
		// 记录菜品个数
		for (Map<String, Object> detailMap : orderDetailList) {
			String dishLevel = (String) detailMap.get("level");
			String unitName = (String) detailMap.get("dishunit");
			String dishId = (String) detailMap.get("dishid");
			TorderDetail torderDetail = null;
			if (dishLevel != null && dishLevel.equals("1")) {
				torderDetail = new TorderDetail();
				torderDetail.setDishid(dishId);
				torderDetail.setOrderprice((BigDecimal) detailMap.get("orderprice"));
				torderDetail.setDishnum((String) detailMap.get("dishnum"));
				doublePots.add(torderDetail);
			} else if (unitName != null && unitName.equals("扎")) {
				torderDetail = new TorderDetail();
				torderDetail.setDishid(dishId);
				torderDetail.setOrderprice((BigDecimal) detailMap.get("orderprice"));
				torderDetail.setDishnum((String) detailMap.get("dishnum"));
				singleDishs.add(torderDetail);
			}
		}
		// 双拼锅立减
		Map<String, Object> doublePotAmountMap = calDoublePot(doublePots, tbPreferentialActivityDao, params, orderid,
				paraMap);
		// 第二杯半价
		Map<String, Object> singleDishMap = singleDishs(singleDishs, tbPreferentialActivityDao, params, orderid,
				paraMap, tdishDao, orderDetailPreferentialDao);
		// 计算和值
		// 计算自动共优惠
		BigDecimal doublePotAmount = (BigDecimal) doublePotAmountMap.get("amount");
		BigDecimal singleDisAmount = (BigDecimal) singleDishMap.get("amount");
		doublePotAmountMap.put("amount", doublePotAmount.add(singleDisAmount));
		// 计算优惠卷
		List<TorderDetailPreferential> doublePotPreferentials = (List<TorderDetailPreferential>) doublePotAmountMap
				.get("detailPreferentials");
		List<TorderDetailPreferential> singleDisPreferentials = (List<TorderDetailPreferential>) singleDishMap
				.get("detailPreferentials");
		doublePotPreferentials.addAll(singleDisPreferentials);
		doublePotAmountMap.put("detailPreferentials", singleDisPreferentials);
		return doublePotAmountMap;
	}

	private Map<String, Object> singleDishs(List<TorderDetail> singleDishs,
			TbPreferentialActivityDao tbPreferentialActivityDao, Map<String, Object> params, String orderid,
			Map<String, Object> paraMap, TdishDao tdishDao, TorderDetailPreferentialDao orderDetailPreferentialDao) {
		params.put("name", "第二扎半价");
		Map<String, Object> result = new HashMap<>();
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();

		BigDecimal amount = new BigDecimal("0");

		// 获取菜单菜品价格()
		Map<String, BigDecimal> orderDetailMap = new HashMap<>();
		for (TorderDetail detail : singleDishs) {
			String dishId = detail.getDishid();
			if (orderDetailMap.containsKey(detail.getDishid())) {

				BigDecimal currentAmount = orderDetailMap.get(dishId);
				BigDecimal tempAmount = detail.getOrderprice();
				String num = detail.getDishnum();
				BigDecimal setAmout = tempAmount.divide(new BigDecimal(2)).multiply(new BigDecimal(num));
				orderDetailMap.put(dishId, currentAmount.add(setAmout));
			} else {
				BigDecimal tempAmount = detail.getOrderprice();
				Double num = Double.valueOf(detail.getDishnum());
				if (num > 1) {
					BigDecimal setAmout = tempAmount.divide(new BigDecimal(2)).multiply(new BigDecimal(num - 1));
					orderDetailMap.put(dishId, setAmout);
				} else {
					orderDetailMap.put(dishId, new BigDecimal("0"));
				}

			}

		}

		// 判断是否是 重新计算，重新计算规则updateId不为空，resultCal 为false
		// 是重新计算还是，默认计算
		String dataDishID = (String) paraMap.get("dishId");
		if (paraMap.containsKey("updateId") && !orderDetailMap.containsKey(dataDishID)) {
			// 如果重新计算没有找到数据库记录数据及删除原来数据
			Map<String, Object> deletMap = new HashMap<>();
			deletMap.put("orderid", orderid);
			deletMap.put("DetalPreferentiald", paraMap.get("updateId"));
			orderDetailPreferentialDao.deleteDetilPreFerInfo(deletMap);
		} else {
			// 硬编码方式，档期类型采用硬编码a26e1025-9a02-433b-bcfd-a3384aaf6a75
			Map<String, Object> columnIdMap = new HashMap<>();
			columnIdMap.put("columnid", "a26e1025-9a02-433b-bcfd-a3384aaf6a75");
			List<Map<String, Object>> tdish_dish_typeMap = tdishDao.findDishes(columnIdMap);
			// 将数据装还成 dishID commoid对应关系
			Map<String, String> dishIDColumindMap = new HashMap<>();
			for (Map<String, Object> dishTypeMap : tdish_dish_typeMap) {
				dishIDColumindMap.put((String) dishTypeMap.get("dishid"), (String) dishTypeMap.get("columnid"));
			}
			List<Map<String, Object>> pres = tbPreferentialActivityDao.findPreferentialDetail(params);
			if (!orderDetailMap.isEmpty() && !dishIDColumindMap.isEmpty() && pres != null && !pres.isEmpty()) {
				Map<String, Object> res = pres.get(0);
				String preferentialid = (String) res.get("preferential");
				for (String keyset : orderDetailMap.keySet()) {
					String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId")
							: IDUtil.getID();
					if (dishIDColumindMap.containsKey(keyset)) {
						// 获取菜单价格
						amount = amount.add(orderDetailMap.get(keyset));
						// 是否大于0
						if (orderDetailMap.get(keyset).doubleValue() > 0) {
							TorderDetailPreferential torder = new TorderDetailPreferential(updateId, orderid, keyset,
									preferentialid, orderDetailMap.get(keyset), String.valueOf("1"), 0, 1,
									new BigDecimal(1), 2);
							TbPreferentialActivity activity = new TbPreferentialActivity();
							activity.setName((String) res.get("name"));
							torder.setActivity(activity);
							// 设置优免金额
							torder.setToalFreeAmount(amount);
							detailPreferentials.add(torder);
						}

					}
				}
			}
		}

		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

	private Map<String, Object> calDoublePot(List<TorderDetail> doublePots,
			TbPreferentialActivityDao tbPreferentialActivityDao, Map<String, Object> params, String orderid,
			Map<String, Object> paraMap) {
		params.put("name", "双拼");
		Map<String, Object> result = new HashMap<>();
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
		BigDecimal amount = new BigDecimal("0");
		if (!doublePots.isEmpty()) {
			List<Map<String, Object>> pres = tbPreferentialActivityDao.findPreferentialDetail(params);
			if(!pres.isEmpty()){
				Map<String, Object> res = pres.get(0);
				for (TorderDetail detail : doublePots) {
					if (pres != null && !pres.isEmpty()) {
						BigDecimal tempAmount = new BigDecimal((String) res.get("amount"));
						String preferentialid = (String) res.get("preferential");

						TorderDetailPreferential torder = new TorderDetailPreferential(updateId, orderid,
								detail.getDishid(), preferentialid, tempAmount, String.valueOf("1"), 0, 1,
								new BigDecimal(1), 2);
						TbPreferentialActivity activity = new TbPreferentialActivity();
						activity.setName((String) res.get("name"));
						torder.setActivity(activity);
						// 设置优免金额
						torder.setToalFreeAmount(amount);
						detailPreferentials.add(torder);
						amount = amount.add(tempAmount);
					}
				}
			}
		
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
