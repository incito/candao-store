package com.candao.www.utils.preferential;

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
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TbPreferenceDetail;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/**
 * 
 * @author Candao 特价优惠策略
 */
public class SpecialTicketStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {
		String orderid = (String) paraMap.get("orderid"); // 账单号
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String type = (String) paraMap.get("type");
		Map tempMap = this.discountInfo(preferentialid, PropertiesUtils.getValue("current_branch_id"),
				tbPreferentialActivityDao);
		TbPreferentialActivity activity = tbPreferentialActivityDao.get(preferentialid);
		/** 当前订单使用优惠记录 ,以及保存了当前菜品多少张 **/
		Map<String, Double> preferInfoMap = new HashMap<>();
		for (TorderDetailPreferential detailPreferential : orderDetailPreferentialDao.queryDetailPreBy(orderid)) {

			if (!preferInfoMap.containsKey(detailPreferential.getDishid())) {
				preferInfoMap.put(detailPreferential.getDishid(), (double) 1);
			} else {
				Double num = preferInfoMap.get(detailPreferential.getDishid());
				preferInfoMap.put(detailPreferential.getDishid(), num + 1);
			}

		}
		Map<String, String> detail_params = new HashMap<>();
		detail_params.put("preferentialId", activity.getId());

		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		// 获取可以优惠的菜品列表
		List<Map<String, Object>> detailList = tbPreferentialActivityDao.findPreferentialDetailList(detail_params);
		Map<String, TbPreferenceDetail> dishCouponAmountMap = new HashMap<>();
		for (Map<String, Object> d : detailList) {

			TbPreferenceDetail preferenceDetail = new TbPreferenceDetail();
			preferenceDetail.setDish((String) d.get("dish"));
			preferenceDetail.setPreferential((String) d.get("id"));
			preferenceDetail.setPrice((BigDecimal) d.get("price"));
			dishCouponAmountMap.put((String) d.get("dish"), preferenceDetail);
		}
		// 更具订单号获取，此次订单下面的菜品
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
		// 当前订单个数每一个菜品菜品的个数(对应的菜品个数，以及)里层为个数加菜品,
		// 分别为菜品dishID,dishNnum,具体信息
		Map<String, TorderDetail> orderMenuONumMap = new HashMap<>();
		for (TorderDetail torderDetail : orderDetailList) {
			String cDishId = torderDetail.getDishid();
			double dishNum = Double.valueOf(torderDetail.getDishnum());
			if (orderMenuONumMap.containsKey(cDishId)) {
				// 如果已经存在数据做叠加菜品处理
				TorderDetail dataOrderDetal = orderMenuONumMap.get(cDishId);
				String tempDishNum = dataOrderDetal.getDishnum();
				// 重新合并(相同菜品的总个数)
				dataOrderDetal.setDishnum(String.valueOf(Double.valueOf(tempDishNum) + dishNum));
				orderMenuONumMap.put(cDishId, dataOrderDetal);
			} else {
				orderMenuONumMap.put(cDishId, torderDetail);
			}
		}

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		// 用于后面的 更新优惠价格到账单详情
		BigDecimal amount = new BigDecimal(0);
		// 优惠的金额信息
		// 菜品目前金额
		BigDecimal menuCash = null;
		// 分两种方式使用心得优惠卷，重新计算优惠卷(updateIP不为空说明是根据数据库保存优惠重新计算)
		String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();

		// 2重新计算特价卷计算方式
		String dishid = (String) paraMap.get("dishId");
		if (dishid != null && paraMap.containsKey("updateId")) {
			// 如果有使用的菜品以及返回有ID说明是重新计算优惠
			TorderDetail updateOrderDetail = orderMenuONumMap.get(dishid);
			if (updateOrderDetail == null) {
				// 如果为空说明当前已经删除了此菜品，那么就应该删除此优惠卷
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("DetalPreferentiald", dishid);
				delMap.put("orderid", orderid);
				orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
			} else {
				// 说明当前菜单也有此菜品重新从新计算
				TorderDetailPreferential resultTorderD = crateOrderDeailPre(dishid, dishCouponAmountMap,
						orderMenuONumMap, menuCash, updateId, orderid, activity, type);
				amount=resultTorderD.getDeAmount();
				detailPreferentials.add(resultTorderD);
			}
		} else {

			// 1特价卷使用特价计算方式
			for (String key : orderMenuONumMap.keySet()) {
				updateId = IDUtil.getID();
				TorderDetail teDetail = orderMenuONumMap.get(key);
				// 合并过后的总的当前菜品的个数
				double dishNum = Double.valueOf(teDetail.getDishnum());
				// 订单菜品不再优惠菜品之中
				if (!dishCouponAmountMap.containsKey(key)) {
					continue;
				}

				if (preferInfoMap.containsKey(key)) {
					// 数据库中保存的优惠个数(如果当前个菜品已经满足了优惠券个数 说明不能使用)
					double preferNum = preferInfoMap.get(key);
					if (preferNum - dishNum >= 0) {
						preferInfoMap.put(key, preferInfoMap.get(key) - 1);
						continue;
					}
				}
				// 根据2015-06-02跟唐家荣的沟通。特价券是 一张一个菜 如果客人点了10份，就用10张券 。
				TorderDetailPreferential resultTorderD = crateOrderDeailPre(key, dishCouponAmountMap, orderMenuONumMap,
						menuCash, updateId, orderid, activity, type);
				if (resultTorderD != null) {
					amount=resultTorderD.getDeAmount();
					detailPreferentials.add(resultTorderD);
					break;
				}
			}
		}
		result.put("amount", amount.setScale(2, RoundingMode.HALF_UP));
		result.put("detailPreferentials", detailPreferentials);
		return result;
	}

	private TorderDetailPreferential crateOrderDeailPre(String dishId,
			Map<String, TbPreferenceDetail> dishCouponAmountMap, Map<String, TorderDetail> orderMenuONumMap,
			BigDecimal menuCash, String updateId, String orderid, TbPreferentialActivity activity, String type) {
		TorderDetailPreferential detailPreferential = null;
		TbPreferenceDetail preferenceDetail = dishCouponAmountMap.get(dishId);
		BigDecimal preInfo = preferenceDetail.getPrice();
		menuCash = orderMenuONumMap.get(dishId).getOrderprice();
		if (null != preInfo && null != menuCash && menuCash.compareTo(preInfo) > 0) {
			// // 将此菜品添加到 orderDishMapList，用于后续金额的更新
			detailPreferential = new TorderDetailPreferential(updateId, orderid, preferenceDetail.getDish(),
					preferenceDetail.getPreferential(), menuCash.subtract(preInfo), "1", 0, 1, new BigDecimal(0), 0);
			// 设置优惠名称
			detailPreferential.setActivity(activity);
			// 设置当前优惠券的优免金额(特价属于优免)
			detailPreferential.setToalFreeAmount(menuCash.subtract(preInfo));
			if (Constant.CouponType.SPECIAL_TICKET.toString().equals(type)) {
				dishCouponAmountMap.remove(dishId);
			}
		}
		return detailPreferential;
	}

}
