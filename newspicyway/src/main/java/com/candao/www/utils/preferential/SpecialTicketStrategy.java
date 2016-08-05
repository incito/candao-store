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
		/** 当前订单使用优惠记录 **/
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
		List<Map<String, Object>> detailList = tbPreferentialActivityDao.findPreferentialDetailList(detail_params);

		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		// 获取可以优惠的菜品列表
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
		// 获取订单单品菜品的分数

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		// 用于后面的 更新优惠价格到账单详情
		BigDecimal amount = new BigDecimal(0);
		// 优惠的金额信息
		// 菜品目前金额
		BigDecimal menuCash = null;
		for (TorderDetail d : orderDetailList) {
			// 订单菜品不再优惠菜品之中
			if (!dishCouponAmountMap.containsKey(d.getDishid())) {
				continue;
			}
			if (preferInfoMap.containsKey(d.getDishid())) {
				Double disNum = Double.valueOf(d.getDishnum());
				Double preferNum = preferInfoMap.get(d.getDishid());
				if (preferNum - disNum >= 0) {
					preferInfoMap.put(d.getDishid(), preferInfoMap.get(d.getDishid()) - 1);
					continue;
				}
			}
			TbPreferenceDetail preferenceDetail = dishCouponAmountMap.get(d.getDishid());
			BigDecimal preInfo = preferenceDetail.getPrice();
			menuCash = d.getOrderprice();

			if (null != preInfo && null != menuCash && menuCash.compareTo(preInfo) > 0) {
				// // 将此菜品添加到 orderDishMapList，用于后续金额的更新
				amount = amount.add(menuCash.subtract(preInfo));
				String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
				TorderDetailPreferential detailPreferential = new TorderDetailPreferential(updateId, orderid,
						preferenceDetail.getDish(), preferenceDetail.getPreferential(), amount, "1", 0, 1,
						new BigDecimal(0), 0);
				// 设置优惠名称
				detailPreferential.setActivity(activity);
				//设置挡墙优惠券的优免金额(特价属于优免)
				detailPreferential.setToalFreeAmount(amount);
				detailPreferentials.add(detailPreferential);
				// 一个菜品只能使用一张卷 ，如果多个菜品需要使用多张优惠卷
				if (Constant.CouponType.SPECIAL_TICKET.toString().equals(type)) {
					dishCouponAmountMap.remove(d.getDishid());
				}
			}
		}
		result.put("amount", amount.setScale(2, RoundingMode.HALF_UP));
		result.put("detailPreferentials", detailPreferentials);
		return result;
	}

}
