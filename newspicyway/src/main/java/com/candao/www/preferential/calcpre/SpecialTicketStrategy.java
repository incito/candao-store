package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbDiscountTicketsDao;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.TbOrderDetailPreInfo;
import com.candao.www.data.model.TbPreferenceDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.utils.ReturnMes;

/**
 * 
 * @author Candao 特价优惠策略
 */
public class SpecialTicketStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		/** 解析参数 **/
		// 账单号
		String orderid = (String) paraMap.get("orderid");
		// 优惠活动id
		String preferentialid = (String) paraMap.get("preferentialid");

		// 赠送菜品记录开始------>

		// 已经赠送过得菜品菜品唯一标号对应菜品数据key:oredridlid Value:dishID+unit
		Map<String, Double> beforGiftDishUnitMap = new HashMap<>();
		// Key:orderilid
		Map<String, Double> befOrderilidMap = new HashMap<>();
		// 卷对应的菜品数据（一个菜多少个卷）
		List<TorderDetailPreferential> torderDetailPreferentials = this.getPresentAndspecialPriclist(orderid,
				orderDetailPreferentialDao);
		// 主键ID对应数据内容
		Map<String, TorderDetailPreferential> updateMap = new HashMap<>();
		for (TorderDetailPreferential detailPreferential : torderDetailPreferentials) {
			updateMap.put(detailPreferential.getId(), detailPreferential);
			List<TbOrderDetailPreInfo> detailPreinfoList = detailPreferential.getDetailPreInfos();
			for (TbOrderDetailPreInfo preInfo : detailPreinfoList) {
				String key = preInfo.getDishID() + preInfo.getUnit();

				double getDishNum = preInfo.getDishNum();
				if (befOrderilidMap.containsKey(preInfo.getOrderidetailid())) {
					Double giftNum = befOrderilidMap.get(preInfo.getOrderidetailid());
					befOrderilidMap.put(preInfo.getOrderidetailid(), getDishNum + giftNum);
				} else {
					befOrderilidMap.put(preInfo.getOrderidetailid(), getDishNum);
				}
				if (beforGiftDishUnitMap.containsKey(key)) {
					Double dishNum = beforGiftDishUnitMap.get(key);
					beforGiftDishUnitMap.put(key, dishNum + preInfo.getDishNum());
				} else {
					beforGiftDishUnitMap.put(key, preInfo.getDishNum());
				}
			}
		}
		// 赠送菜品记录结束------>

		// 特价卷包含的菜品---开始
		List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid,
				PropertiesUtils.getValue("current_branch_id"), tbPreferentialActivityDao);
		// 当前特价优惠卷支持1.单一菜品，2.多规格菜品
		Map<String, TbPreferenceDetail> dishCouponAmountMap = new HashMap<>();
		for (Map<String, Object> specialInfoMap : tempMapList) {
			// dishId
			String dishId = (String) specialInfoMap.get("dish");
			// unit
			String unit = (String) specialInfoMap.get("unit");
			// price
			BigDecimal price = (BigDecimal) specialInfoMap.get("price");
			// preferential
			String preferential = (String) specialInfoMap.get("preferential");
			String unitKey = unit == null ? "" : unit;
			String key = dishId + unitKey;
			TbPreferenceDetail preferenceDetail = new TbPreferenceDetail();
			preferenceDetail.setDish(dishId);
			preferenceDetail.setPreferential(preferential);
			preferenceDetail.setPrice(price);
			preferenceDetail.setCoupondetailid((String) specialInfoMap.get("id"));
			dishCouponAmountMap.put(key, preferenceDetail);
		} // ------>查找菜品特价卷包含菜品结束

		// 可进行特价处理的菜品key：dishid+dishUnit value:菜品详情
		Map<String, ComplexTorderDetail> useSpecialPreMap = new HashMap<>();
		/** 订单菜品对应个数 **/
		Map<String, Double> orderDishNumMap = new HashMap<>();
		/** 获取对应的菜品ID编号 **/
		Map<String, List<ComplexTorderDetail>> detailidListMap = new HashMap<>();
		// 订单中还能满足进行特价的菜品
		for (ComplexTorderDetail torderDetail : orderDetailList) {
			if(torderDetail.getPricetype().equals("1")){
				continue;
			}
			String key = torderDetail.getDishid() + torderDetail.getDishunit();
			String giftorderdetailid = torderDetail.getOrderdetailid();
			double dishNum = Double.valueOf(torderDetail.getDishnum());
			if (orderDishNumMap.containsKey(key)) {
				Double resDishNum = orderDishNumMap.get(key);
				orderDishNumMap.put(key, dishNum + resDishNum);
			} else {
				orderDishNumMap.put(key, dishNum);
			}

			// 如果包含数据当前菜品是在打折菜品之内
			if (dishCouponAmountMap.containsKey(key)) {
				/** 查看那些菜品没有使用优惠 **/
				if (detailidListMap.containsKey(key)) {
					detailidListMap.get(key).add(torderDetail);
				} else {
					List<ComplexTorderDetail> detailidlist = new ArrayList<>();
					detailidlist.add(torderDetail);
					detailidListMap.put(key, detailidlist);
				}

				// 新增使用
				TbPreferenceDetail preOrderDetail = dishCouponAmountMap.get(key);
				torderDetail.setCouponid(preOrderDetail.getCoupondetailid());
				if (befOrderilidMap.containsKey(giftorderdetailid)) {
					Double dishnum = Double.valueOf(torderDetail.getDishnum());
					double giveNum = befOrderilidMap.get(giftorderdetailid);
					if (dishnum > giveNum) {
						useSpecialPreMap.put(key, torderDetail);
					}
				} else {
					useSpecialPreMap.put(key, torderDetail);
				}

			}
		}
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		// 是否需要提示标示使用优惠是否成功0成功1失败
		boolean flag = false;
		// 用于后面的 更新优惠价格到账单详情
		BigDecimal amount = new BigDecimal(0);

		if (paraMap.containsKey("updateId")) {
			
			TorderDetailPreferential orderDetialPerInfo = updateMap.get((String) paraMap.get("updateId"));
			TbOrderDetailPreInfo beforderdish = orderDetialPerInfo.getDetailPreInfos().get(0);
			String key = beforderdish.getDishID() + beforderdish.getUnit();
			// 当前菜品个数
			Double orderDishNum = orderDishNumMap.get(key);
			// 获取当前使用菜品优惠个数如果优惠大于菜品者删除优惠
			Double userPreferId = beforGiftDishUnitMap.get(key);
			if (orderDishNum == null || orderDishNum < userPreferId) {
				// 如果为空说明当前已经删除了此菜品，那么就应该删除此优惠卷
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("DetalPreferentiald", paraMap.get("updateId"));
				delMap.put("orderid", orderid);
				orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
			} else {
				boolean falg = false;
				List<ComplexTorderDetail> befOredreList = detailidListMap.get(key);
				ComplexTorderDetail tempOrderDetail = null;
				for (ComplexTorderDetail complexTorderDetail : befOredreList) {
					if (beforderdish.getOrderidetailid().equals(complexTorderDetail.getOrderdetailid())) {
						falg = true;
						tempOrderDetail = complexTorderDetail;
						break;
					}
				}
				if (falg) {
					Map tempMap = tempMapList.get(0);
					String preName = (String) tempMap.get("name") + "(" + tempOrderDetail.getTitle() + "/"
							+ tempOrderDetail.getDishunit() + ")";
					orderDetialPerInfo.getActivity().setName(preName);
					amount = amount.add(orderDetialPerInfo.getDeAmount());
					detailPreferentials.add(orderDetialPerInfo);
				} else {
					ComplexTorderDetail detail = useSpecialPreMap.get(key);
					Map tempMap = tempMapList.get(0);
					String preName = (String) tempMap.get("name") + "(" + detail.getTitle() + "/" + detail.getDishunit()
							+ ")";
					orderDetialPerInfo.getActivity().setName(preName);
					orderDetialPerInfo.getDetailPreInfos().get(0).setOrderidetailid(detail.getOrderdetailid());
					amount = amount.add(orderDetialPerInfo.getDeAmount());
					detailPreferentials.add(orderDetialPerInfo);
				}

			}

		} else {
			for (String key : useSpecialPreMap.keySet()) {
				ComplexTorderDetail teDetail = useSpecialPreMap.get(key);

				// 根据2015-06-02跟唐家荣的沟通。特价券是 一张一个菜 如果客人点了10份，就用10张券 。
				Map tempMap = tempMapList.get(0);
				String preName = (String) tempMap.get("name") + "(" + teDetail.getTitle() + "/" + teDetail.getDishunit()
						+ ")";
				TorderDetailPreferential resultTorderD = crateOrderDeailPre(key, paraMap, teDetail, dishCouponAmountMap,
						preName);
				if (resultTorderD != null) {
					amount = amount.add(resultTorderD.getDeAmount());
					detailPreferentials.add(resultTorderD);
				}
			}
		}
		flag = detailPreferentials.size() > 0 ? true : false;
		result.put("amount", amount.setScale(2, RoundingMode.HALF_UP));
		result.put("detailPreferentials", detailPreferentials);
		result.put("falg", flag);
		result.put("mes", flag ? ReturnMes.SUCCESS.getMsg() : ReturnMes.SPECIAL_FAIL.getMsg());
		return result;
	}

	private TorderDetailPreferential crateOrderDeailPre(String key, Map<String, Object> paraMap,
			ComplexTorderDetail teDetail, Map<String, TbPreferenceDetail> dishCouponAmountMap, String preName) {
		TorderDetailPreferential detailPreferential = null;
		TbPreferenceDetail preferenceDetail = dishCouponAmountMap.get(key);
		BigDecimal preInfo = preferenceDetail.getPrice();
		BigDecimal menuCash = teDetail.getOrderprice() == null ? new BigDecimal("0") : teDetail.getOrderprice();
		if (null != preInfo && null != menuCash && menuCash.compareTo(preInfo) > 0) {
			// 创建根元素
			detailPreferential = this.createPreferentialBean(paraMap, menuCash.subtract(preInfo),
					menuCash.subtract(preInfo), new BigDecimal("0"),  new BigDecimal("0"),
					Constant.CALCPRETYPE.NOGROUP, preName, dishCouponAmountMap.get(key).getCoupondetailid(),
					Constant.CALCPRETYPE.SPECIALUSEPRE);

			// 创建子元素
			List<TbOrderDetailPreInfo> detailPreInfos = new ArrayList<>();
			detailPreInfos.add(createOrderDetailInfo(IDUtil.getID(), detailPreferential, teDetail,
					detailPreferential.getDeAmount()));
			detailPreferential.setDetailPreInfos(detailPreInfos);
		}
		return detailPreferential;
	}

}
