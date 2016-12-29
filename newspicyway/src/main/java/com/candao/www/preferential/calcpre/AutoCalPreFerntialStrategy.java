package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;

/**
 * 
 * @author Candao 新辣到特殊优惠
 */
public class AutoCalPreFerntialStrategy extends CalPreferentialStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {
		String branchid = PropertiesUtils.getValue("current_branch_id");
		Map<String, Object> params = new HashMap<>();
		params.put("type", 3);
		params.put("branchid", branchid);
		params.put("status", 2);
		params.put("memberno", paraMap.get("memberno"));
		String orderid = (String) paraMap.get("orderid"); // 账单号
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		// 1:双拼锅 0：单品
		List<TorderDetail> singleDishs = new ArrayList<>();
		// 获取当前订单的鱼锅个数,鱼的个数(直接根据获取订单下面的鱼的个数就能知道鱼锅中的鱼)
		Map<String, Integer> fishComMap = new HashMap<>();
		// 记录菜品个数
		for (ComplexTorderDetail torderDetailInfo : orderDetailList) {
			String dishLevel = torderDetailInfo.getLevel();
			String dishUnit = torderDetailInfo.getDishunit();
			String dishId = torderDetailInfo.getDishid();
			TorderDetail torderDetail = null;
			if (dishLevel != null && dishLevel.equals("1")) {
				fishComMap.put(torderDetailInfo.getPrimarykey(), 0);
			} else if (dishUnit != null && dishUnit.equals("扎")) {
				torderDetail = new TorderDetail();
				torderDetail.setDishid(dishId);
				torderDetail.setOrderprice(torderDetailInfo.getOrderprice());
				torderDetail.setDishnum(torderDetailInfo.getDishnum());
				torderDetail.setDishunit(dishUnit);
				singleDishs.add(torderDetail);
			}
		}
		// 拆分鱼锅中的鱼
		for (ComplexTorderDetail torderDetailInfo : orderDetailList) {
			if (fishComMap.containsKey(torderDetailInfo.getParentkey())) {
				int ismaster = torderDetailInfo.getIsmaster();
				int ispot = torderDetailInfo.getIspot();
				if (ismaster == 0 && ispot == 0) {
					fishComMap.put(torderDetailInfo.getParentkey(), fishComMap.get(torderDetailInfo.getParentkey())+1);
				}
			}

		}
		// 优先干掉新新辣道优惠
		Map<String, Object> delMap = new HashMap<>();
		delMap.put("orderid", orderid);
		delMap.put("custom", "2");
		orderDetailPreferentialDao.deleteForXinladao(delMap);
		// 双拼锅立减
		Map<String, Object> doublePotAmountMap = calDoublePot(fishComMap, tbPreferentialActivityDao, params, orderid,
				paraMap, orderDetailPreferentialDao);
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
		doublePotAmountMap.put("detailPreferentials", doublePotPreferentials);
		return doublePotAmountMap;
	}

	private Map<String, Object> singleDishs(List<TorderDetail> singleDishs,
			TbPreferentialActivityDao tbPreferentialActivityDao, Map<String, Object> params, String orderid,
			Map<String, Object> paraMap, TdishDao tdishDao, TorderDetailPreferentialDao orderDetailPreferentialDao) {

		Map<String, Object> singleDishsMap = new HashMap<>();
		singleDishsMap.putAll(params);
		singleDishsMap.put("name", "第二扎半价");

		Map<String, Object> result = new HashMap<>();
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();

		BigDecimal amount = new BigDecimal("0");

		// 是重新计算还是，默认计算
		// 硬编码方式，档期类型采用硬编码a26e1025-9a02-433b-bcfd-a3384aaf6a75
		Map<String, Object> columnIdMap = new HashMap<>();
		columnIdMap.put("columnid", "a26e1025-9a02-433b-bcfd-a3384aaf6a75");
		List<Map<String, Object>> tdish_dish_typeMap = tdishDao.findDishes(columnIdMap);
		// 将数据装还成 dishID commoid对应关系
		Map<String, String> dishIDColumindMap = new HashMap<>();
		for (Map<String, Object> dishTypeMap : tdish_dish_typeMap) {
			dishIDColumindMap.put((String) dishTypeMap.get("dishid"), (String) dishTypeMap.get("columnid"));
		}
		List<Map<String, Object>> pres = tbPreferentialActivityDao.findPreferentialDetail(singleDishsMap);
		if (!singleDishs.isEmpty() && !dishIDColumindMap.isEmpty() && pres != null && !pres.isEmpty()) {
			// 遍历订单饮品为扎的数据
			// 计数器饮品格式
			int i = 0;
			for (TorderDetail torderDetail : singleDishs) {
				String dishId = torderDetail.getDishid();
				BigDecimal tempAmount = torderDetail.getOrderprice() == null ? new BigDecimal("0")
						: torderDetail.getOrderprice();
				double dishNum = Double.valueOf(torderDetail.getDishnum());
				if (dishIDColumindMap.containsKey(dishId)) {
					if (dishNum > 1 && i == 0) {
						amount = amount
								.add(new BigDecimal(dishNum - 1).multiply(tempAmount).multiply(new BigDecimal("0.5")));
					}
					if (i > 0) {
						amount = amount
								.add(tempAmount.multiply(new BigDecimal(dishNum)).multiply(new BigDecimal("0.5")));
					}
					i++;
				}
			}
			Map<String, Object> res = pres.get(0);
			String preferentialid = (String) res.get("preferential");
			if (amount.doubleValue() > 0) {
				String conId = (String) (pres.size() > 1 ? res.get("preferential") : res.get("id"));
				TorderDetailPreferential torder = this.createPreferentialBean(paraMap, amount, amount,
						new BigDecimal("0"), 1, new BigDecimal("0"),Constant.CALCPRETYPE.NOGROUP, (String) res.get("name"),
						conId,Constant.CALCPRETYPE.SYSTENUSEPRE);
				torder.setPreferential(preferentialid);
				detailPreferentials.add(torder);
			}

		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

	private Map<String, Object> calDoublePot(Map<String, Integer> fishComMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, Map<String, Object> params, String orderid,
			Map<String, Object> paraMap, TorderDetailPreferentialDao orderDetailPreferentialDao) {
		// 获取多少鱼锅可以jinx
		int fishNo = 0;
		Iterator<Integer> iter = fishComMap.values().iterator();
		while (iter.hasNext()) {
			int value = iter.next();
			if (value==2) {
				fishNo = fishNo + 1;
			}
		}

		// 获取配置ID
		HashMap<String, Object> doublePotPrams = new HashMap<>();
		doublePotPrams.putAll(params);
		doublePotPrams.put("preferential", paraMap.get("doubSpellPreId"));

		Map<String, Object> result = new HashMap<>();
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		BigDecimal amount = new BigDecimal("0");
		BigDecimal menberAmount = new BigDecimal("0");
		String memberno = String.valueOf(paraMap.get("memberno"));
		List<Map<String, Object>> pres = tbPreferentialActivityDao.findPreferentialDetail(doublePotPrams);
		if (fishNo != 0 && !pres.isEmpty()) {
			Map<String, Object> res = pres.get(0);
			String preferentialid = (String) res.get("preferential");
			menberAmount = new BigDecimal(String.valueOf(res.get("amount"))).multiply(new BigDecimal(fishNo));
			if (!memberno.isEmpty()) {
				amount = amount.add(menberAmount);
				String conId = (String) (pres.size() > 1 ? res.get("preferential") : res.get("id"));
				TorderDetailPreferential torder = this.createPreferentialBean(paraMap, amount, amount,
						new BigDecimal("0"), fishNo, new BigDecimal("0"), Constant.CALCPRETYPE.NOGROUP, (String) res.get("name"),
						conId,Constant.CALCPRETYPE.SYSTENUSEPRE);
				torder.setPreferential(preferentialid);
				detailPreferentials.add(torder);
			}

		}
		paraMap.remove("doubSpellPreId");
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		result.put("menberAmount", menberAmount);
		return result;
	}

}
