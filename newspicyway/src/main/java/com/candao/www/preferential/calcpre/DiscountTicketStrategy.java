package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import com.candao.www.data.model.TbNoDiscountDish;
import com.candao.www.data.model.TbOrderDetailPreInfo;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.preferential.model.PreDealInfoBean;

/**
 * 
 * @author Candao 折扣优惠卷
 */
public class DiscountTicketStrategy extends CalPreferentialStrategy {

	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {

		/** 定义返回类型 **/
		Map<String, Object> result = new HashMap<>();
		BigDecimal amount = new BigDecimal("0");
		// 打折总金额
		BigDecimal countAmount = new BigDecimal("0");
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		/** 参数解析 **/
		String orderid = (String) paraMap.get("orderid"); // 账单号
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String branchid = PropertiesUtils.getValue("current_branch_id");
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));

		/** 获取优惠卷信息 **/
		List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid, branchid, tbPreferentialActivityDao);
		Map tempMap = tempMapList.get(0);
		String conupId = (String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id"));
		BigDecimal discount = (BigDecimal) tempMap.get("discount");

		// 之前计算的优惠
		Map<String, ComplexTorderDetail> resultDetail = new HashMap<>();
		try {
			resultDetail = this.everyorderDetailAmount(orderDetailList, orderid, orderDetailPreferentialDao,
					(String) paraMap.get("updateId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

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
		// 普通菜品折扣方式
		Set<String> noDiscountDishSet = new HashSet<>();
		// 鱼锅折扣方式
		Set<String> fishnoDiscountDishSet = new HashSet<>();

		// 根据不优惠的菜品获取菜品库的菜品信息
		for (TbNoDiscountDish t : noDiscountDishlist) {
			String key = t.getDish() + t.getUnit();
			int dishType = t.getDishtype();
			if (orderDetailSet.contains(key)) {
				// 处理鱼锅
				if (dishType == 1) {
					List<Tdish> tdishList = tbDiscountTicketsDao.getDishidList(t.getDish());
					if (tdishList.size() > 0) {
						for (Tdish dish : tdishList) {
							fishnoDiscountDishSet.add(dish.getDishid() + dish.getUnit());
						}
					}
				}
				noDiscountDishSet.add(key);
			}
		}
		// 部分折扣是否包含
		boolean isPart = false;
		// 计算
		TorderDetailPreferential prantOrderDetail = null;
		if (noDiscountDishSet.isEmpty()) {
			/** 全单折扣优惠 **/
			for (String key : resultDetail.keySet()) {
				countAmount = countAmount.add(resultDetail.get(key).getDebitamount());
			}
			int group = resultDetail.size() == orderDetailList.size() ? Constant.CALCPRETYPE.GROUP
					: Constant.CALCPRETYPE.NOGROUP;
			prantOrderDetail = this.createPreferentialBean(paraMap, amount, amount, new BigDecimal("0"), discount,
					group, (String) tempMap.get("name"), conupId, Constant.CALCPRETYPE.NORMALUSEPRE);
		} else {
			prantOrderDetail = this.createPreferentialBean(paraMap, amount, amount, new BigDecimal("0"), discount, 0,
					(String) tempMap.get("name"), conupId, Constant.CALCPRETYPE.NORMALUSEPRE);
			// 参与菜品的列表
			for (String orderdetailid : resultDetail.keySet()) {
				ComplexTorderDetail d = resultDetail.get(orderdetailid);
				// KEY：dishid+unit
				String key = d.getDishid() + d.getDishunit();
				if (d.getDebitamount().doubleValue() > 0) {
					if (!noDiscountDishSet.contains(key) && !fishnoDiscountDishSet.contains(key)
							|| (fishnoDiscountDishSet.contains(key) && !d.getDishtype().equals("1"))) {
						BigDecimal debitAmount = d.getDebitamount();
						BigDecimal preAmonut = debitAmount
								.subtract(debitAmount.multiply(discount).divide(new BigDecimal("10")));
						TbOrderDetailPreInfo subOrderDetail = this.createOrderDetailInfo(IDUtil.getID(),
								prantOrderDetail, d, preAmonut);
						prantOrderDetail.getDetailPreInfos().add(subOrderDetail);
						countAmount = countAmount.add(debitAmount);
						isPart = true;
					}
				}
			}
			if ((String) paraMap.get("updateId") != null) {
				Map<String, Object> deleteSubParams = new HashMap<>();
				deleteSubParams.put("ordpreid", paraMap.get("updateId"));
				deleteSubParams.put("orderid", orderid);
				orderDetailPreferentialDao.deleteSubPreInfo(deleteSubParams);
			}

		}
		if (countAmount.doubleValue() > 0) {
			PreDealInfoBean calPreAmount = this.calDiscount(countAmount, new BigDecimal("0"), discount);
			amount = calPreAmount.getPreAmount();
			prantOrderDetail.setDeAmount(amount);
			prantOrderDetail.setToalFreeAmount(amount);
			detailPreferentials.add(prantOrderDetail);
		} else {
			if ((String) paraMap.get("updateId") != null) {
				Map<String, Object> delMap = new HashMap<>();
				delMap.put("DetalPreferentiald", paraMap.get("updateId"));
				delMap.put("orderid", orderid);
				orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
			}
			this.disMes(result, countAmount, Constant.CouponType.DISCOUNT_TICKET);
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
