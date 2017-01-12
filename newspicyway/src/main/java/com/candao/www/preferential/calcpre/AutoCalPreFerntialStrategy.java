package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TbOrderDetailPreInfo;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/**
 * 
 * @author Candao 新辣到特殊优惠
 */
public class AutoCalPreFerntialStrategy extends CalPreferentialStrategy {

	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {

		/** 参数解析 **/
		String orderid = (String) paraMap.get("orderid"); // 账单号
		String memberno = (String) paraMap.get("memberno");
		/** 公用参数查询 **/
		Map<String, Object> params = new HashMap<>();
		params.put("type", 3);
		params.put("branchid", PropertiesUtils.getValue("current_branch_id"));
		params.put("status", 2);
		params.put("memberno", memberno);
		// 卷对应的菜品数据（一个菜多少个卷，但一个菜品优惠）
		Map<String, ComplexTorderDetail> beforderidetailidMap = (Map<String, ComplexTorderDetail>) this
				.getUsePreInfoMap(orderid, orderDetailPreferentialDao).get("orderDetailIdMap");

		// 1:双拼锅 0：单品
		List<ComplexTorderDetail> singleDishs = new ArrayList<>();
		// 获取当前订单的鱼锅个数,鱼的个数(直接根据获取订单下面的鱼的个数就能知道鱼锅中的鱼)
		Map<String, Object> fishComMap = new HashMap<>();
		// 记录菜品个数
		for (ComplexTorderDetail torderDetailInfo : orderDetailList) {
			String dishtype = torderDetailInfo.getDishtype();
			BigDecimal orderprice = torderDetailInfo.getOrderprice();
			// 如果单间为空或者为0不计算优惠
			if (((dishtype.equals("0") || dishtype.equals("2"))
					&& (orderprice == null || orderprice.doubleValue() <= 0))) {
				continue;
			}

			ComplexTorderDetail beforData = beforderidetailidMap.get(torderDetailInfo.getOrderdetailid());
			Double beDishNum = 0d;
			Double dishNum = Double.valueOf(torderDetailInfo.getDishnum());
			if (beforData != null) {
				beDishNum = Double.valueOf(beforData.getDishnum());
				if (dishNum <= beDishNum) {
					continue;
				}
			}
			// setDishNum
			String setDisNum = String.valueOf(dishNum - beDishNum);

			String dishLevel = torderDetailInfo.getLevel();
			String dishUnit = torderDetailInfo.getDishunit();
			String dishId = torderDetailInfo.getDishid();
			ComplexTorderDetail torderDetail = null;
			if (dishLevel != null && dishLevel.equals("1")) {
				fishComMap.put(torderDetailInfo.getPrimarykey(), new ArrayList<ComplexTorderDetail>());
				fishComMap.put(torderDetailInfo.getPrimarykey() + "fishMony", 0D);
			} else if (dishUnit != null && dishUnit.equals("扎")) {
				torderDetail = new ComplexTorderDetail();
				torderDetail.setDishid(dishId);
				torderDetail.setDishunit(dishUnit);
				torderDetail.setOrderprice(torderDetailInfo.getOrderprice());
				torderDetail.setDishnum(setDisNum);
				torderDetail.setOrderdetailid(torderDetailInfo.getOrderdetailid());
				singleDishs.add(torderDetail);
			}
		}
		// 拆分鱼锅中的鱼
		ComplexTorderDetail torderDetail = null;
		for (ComplexTorderDetail torderDetailInfo : orderDetailList) {
			if (fishComMap.containsKey(torderDetailInfo.getParentkey())) {
				int ismaster = torderDetailInfo.getIsmaster();
				if (ismaster == 0) {
					ComplexTorderDetail beforData = beforderidetailidMap.get(torderDetailInfo.getOrderdetailid());

					if (torderDetailInfo.getIspot() == 0) {
						double torderDishNum = Double.valueOf(torderDetailInfo.getDishnum());
						double dishNum=beforData==null?0:Double.valueOf(beforData.getDishnum());
						if (dishNum < torderDishNum) {
							// 鱼
							String setDisNum = String.valueOf(torderDishNum - dishNum);
							torderDetail = new ComplexTorderDetail();
							torderDetail.setDishid(torderDetailInfo.getDishid());
							torderDetail.setDishunit(torderDetailInfo.getDishunit());
							torderDetail.setOrderprice(torderDetailInfo.getOrderprice());
							torderDetail.setDishnum(setDisNum);
							torderDetail.setOrderdetailid(torderDetailInfo.getOrderdetailid());
							torderDetail.setDebitamount(
									torderDetailInfo.getOrderprice().multiply(new BigDecimal(setDisNum)));
							ArrayList<ComplexTorderDetail> fishList = (ArrayList<ComplexTorderDetail>) fishComMap
									.get(torderDetailInfo.getParentkey());
							fishList.add(torderDetail);

							// 双拼鱼锅中两条鱼的价格
							String keyCounter = torderDetailInfo.getParentkey() + "fishMony";
							BigDecimal fishAmount = new BigDecimal(torderDetailInfo.getDishnum())
									.multiply(torderDetailInfo.getOrderprice());
							fishComMap.put(keyCounter, (Double) fishComMap.get(keyCounter) + fishAmount.doubleValue());
						}
					}
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

	private Map<String, Object> singleDishs(List<ComplexTorderDetail> singleDishs,
			TbPreferentialActivityDao tbPreferentialActivityDao, Map<String, Object> params, String orderid,
			Map<String, Object> paraMap, TdishDao tdishDao, TorderDetailPreferentialDao orderDetailPreferentialDao) {

		/** 查询数据库是否有第二扎半价优惠 **/
		Map<String, Object> singleDishsMap = new HashMap<>();
		singleDishsMap.putAll(params);
		singleDishsMap.put("name", "第二扎半价");
		List<Map<String, Object>> pres = tbPreferentialActivityDao.findPreferentialDetail(singleDishsMap);
		Map<String, Object> res = pres.get(0);

		// 硬编码方式，档期类型采用硬编码a26e1025-9a02-433b-bcfd-a3384aaf6a75
		Map<String, Object> columnIdMap = new HashMap<>();
		columnIdMap.put("columnid", "a26e1025-9a02-433b-bcfd-a3384aaf6a75");
		List<Map<String, Object>> tdish_dish_typeMap = tdishDao.findDishes(columnIdMap);

		// 返回数据集
		Map<String, Object> result = new HashMap<>();
		// 计算优惠列表
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		// 技术优惠金额
		BigDecimal amount = new BigDecimal("0");

		// 将数据装还成 dishID commoid对应关系
		Map<String, String> dishIDColumindMap = new HashMap<>();
		for (Map<String, Object> dishTypeMap : tdish_dish_typeMap) {
			dishIDColumindMap.put((String) dishTypeMap.get("dishid"), (String) dishTypeMap.get("columnid"));
		}

		/** 创建优惠信息 **/
		String conId = (String) (pres.size() > 1 ? res.get("preferential") : res.get("id"));
		TorderDetailPreferential torder = this.createPreferentialBean(paraMap, amount, amount, new BigDecimal("0"),
				new BigDecimal("0"), Constant.CALCPRETYPE.NOGROUP, (String) res.get("name"), conId,
				Constant.CALCPRETYPE.SYSTENUSEPRE);
		/** 优惠卷下挂菜品 **/
		List<TbOrderDetailPreInfo> detailPreInfos = new ArrayList<>();
		if (!singleDishs.isEmpty() && !dishIDColumindMap.isEmpty() && pres != null && !pres.isEmpty()) {
			// 计数器饮品格式
			boolean isCalc = false;
			for (ComplexTorderDetail torderDetail : singleDishs) {
				String dishId = torderDetail.getDishid();
				BigDecimal tempAmount = torderDetail.getOrderprice() == null ? new BigDecimal("0")
						: torderDetail.getOrderprice();
				double dishNum = Double.valueOf(torderDetail.getDishnum());
				if (dishIDColumindMap.containsKey(dishId)) {
					if (dishNum > 1 || isCalc) {
						dishNum = !isCalc && dishNum > 1 ? dishNum - 1 : dishNum;
						BigDecimal calAmount = new BigDecimal(dishNum).multiply(tempAmount)
								.multiply(new BigDecimal("0.5"));
						amount = amount.add(calAmount);
						detailPreInfos.add(this.createOrderDetailInfo(IDUtil.getID(), torder, torderDetail, calAmount));
					}
					isCalc = true;
				}
			}

			String preferentialid = (String) res.get("preferential");
			if (amount.doubleValue() > 0) {
				torder.setDeAmount(amount);
				torder.setToalFreeAmount(amount);
				torder.setDetailPreInfos(detailPreInfos);
				torder.setPreferential(preferentialid);
				detailPreferentials.add(torder);
			}

		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> calDoublePot(Map<String, Object> fishComMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, Map<String, Object> params, String orderid,
			Map<String, Object> paraMap, TorderDetailPreferentialDao orderDetailPreferentialDao) {
		// 获取数据库配置的使用优惠ID
		HashMap<String, Object> doublePotPrams = new HashMap<>();
		doublePotPrams.putAll(params);
		doublePotPrams.put("preferential", paraMap.get("doubSpellPreId"));
		List<Map<String, Object>> pres = tbPreferentialActivityDao.findPreferentialDetail(doublePotPrams);
		// 判断是否有会员
		// String memberno = String.valueOf(paraMap.get("memberno"));
		String memberno = "11111";
		// 会员登录减免优惠
		BigDecimal menberAmount = new BigDecimal("0");

		// 返回数据集
		Map<String, Object> result = new HashMap<>();
		// 返回优惠金额
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		// 获取当前优惠信息
		BigDecimal amount = new BigDecimal("0");
		if (!pres.isEmpty()) {
			Map<String, Object> res = pres.get(0);
			// 优惠ID
			String preferentialid = (String) res.get("preferential");
			String id = (String) res.get("id");
			// 优惠名称
			String preName = (String) res.get("name");
			// 优惠金额
			BigDecimal decimal = new BigDecimal(String.valueOf(res.get("amount")));
			// 获取多少鱼锅可以jinx
			int fishNo = 0;
			Set<String> keyList = fishComMap.keySet();
			// 创建优惠
			String conId = pres.size() > 1 ? preferentialid : id;
			TorderDetailPreferential torder = this.createPreferentialBean(paraMap, amount, amount, new BigDecimal("0"),
					new BigDecimal("0"), Constant.CALCPRETYPE.NOGROUP, preName, conId,
					Constant.CALCPRETYPE.SYSTENUSEPRE);
			torder.setPreferential(preferentialid);
			// 创建优惠子元素
			List<TbOrderDetailPreInfo> detailPreInfos = new ArrayList<>();
			for (String key : keyList) {
				if (key.endsWith("fishMony")) {
					continue;
				}
				ArrayList<ComplexTorderDetail> details = (ArrayList<ComplexTorderDetail>) fishComMap.get(key);
				if (details.size() == 2) {
					fishNo = fishNo + 1;
					BigDecimal allDebitamount = new BigDecimal((Double) fishComMap.get(key + "fishMony"));
					// 参入这次优惠的总金额
					for (ComplexTorderDetail torderDetail : details) {
						if (torderDetail.getIspot() == 0) {
							// 因为双拼有鱼菜减免所以只减鱼
							// 计算每一个菜品实收
							BigDecimal preAmount = torderDetail.getDebitamount()
									.divide(allDebitamount, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("20"));
							detailPreInfos
									.add(this.createOrderDetailInfo(IDUtil.getID(), torder, torderDetail, preAmount));
						}
					}
				}

			}
			menberAmount = decimal.multiply(new BigDecimal(fishNo));
			if (!memberno.isEmpty()) {
				amount = amount.add(menberAmount);
				torder.setDeAmount(amount);
				torder.setToalFreeAmount(amount);
				torder.setDetailPreInfos(detailPreInfos);
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
