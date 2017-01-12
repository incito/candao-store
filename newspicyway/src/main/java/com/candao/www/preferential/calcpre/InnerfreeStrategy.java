package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
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
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.preferential.model.PreDealInfoBean;

/***
 * 合作单位优免
 * 
 * @author Candao
 *
 */
public class InnerfreeStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id

		/** 获取优惠卷信息 **/
		List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid,
				PropertiesUtils.getValue("current_branch_id"), tbPreferentialActivityDao);
		Map tempMap = tempMapList.get(0);

		/** 参数解析 **/
		BigDecimal discount = (BigDecimal) tempMap.get("discount");// discount大于0为折扣优免
		BigDecimal caseAmount = (BigDecimal) tempMap.get("amount");// amount大于0为现金优免
		String orderid = (String) paraMap.get("orderid"); // 账单号
		String can_credit = String.valueOf(tempMap.get("can_credit"));// 0不能挂账，1挂账
		// 定义 返回值
		Map<String, Object> result = new HashMap<>();

		// 当前订单总价amountCount
		BigDecimal amountCount = new BigDecimal(0.0);
		// 使用优惠卷列表
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		TorderDetailPreferential addPreferential = null;
		// 最终优惠金额
		BigDecimal amount = new BigDecimal(0);

		// 设置金额
		if (discount != null && discount.doubleValue() > 0) {

			// 之前计算的优惠
			Map<String, ComplexTorderDetail> resultDetail = new HashMap<>();
			try {
				// 打折总金额
				BigDecimal countAmount = new BigDecimal("0");
				resultDetail = this.everyorderDetailAmount(orderDetailList, orderid, orderDetailPreferentialDao,
						(String) paraMap.get("updateId"));
				/** 全单折扣优惠 **/
				for (String key : resultDetail.keySet()) {
					countAmount = countAmount.add(resultDetail.get(key).getDebitamount());
				}
				PreDealInfoBean deInfo = this.calDiscount(amountCount, new BigDecimal("0"), discount);
				if (deInfo.getPreAmount().doubleValue() > 0) {
					amount = deInfo.getPreAmount();
					String conupId = (String) (tempMapList.size() > 1 ? tempMap.get("preferential")
							: tempMap.get("id"));
					addPreferential = this.createPreferentialBean(paraMap, amount, new BigDecimal("0"),
							new BigDecimal("0"), discount, Constant.CALCPRETYPE.GROUP, (String) tempMap.get("name"),
							conupId, Constant.CALCPRETYPE.NORMALUSEPRE);
					// 是否挂账，优免
					if (can_credit.equals("0")) {
						if (amountCount.compareTo(amount) == -1) {
							addPreferential.setToalDebitAmountMany(amountCount.subtract(amount));
						}
						addPreferential.setToalDebitAmount(amount);
					} else {
						addPreferential.setToalFreeAmount(amount);
					}
					detailPreferentials.add(addPreferential);
				} else {
					if ((String) paraMap.get("updateId") != null) {
						Map<String, Object> deleteSubParams = new HashMap<>();
						deleteSubParams.put("ordpreid", paraMap.get("updateId"));
						deleteSubParams.put("orderid", orderid);
						orderDetailPreferentialDao.deleteDetilPreFerInfo(deleteSubParams);
					}
					this.disMes(result, countAmount, Constant.CouponType.INNERFREE);
				}

			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

		} else if (caseAmount != null && caseAmount.doubleValue() > 0) {
			// 获取优惠卷个数(如果POS选择多张优惠卷，后台会在数据库写入多个数据，所以要拆分优惠卷)
			int preferentialNum = Integer.valueOf((String) paraMap.get("preferentialNum"));
			amount = caseAmount.multiply(new BigDecimal(preferentialNum));
			for (int i = 0; i < preferentialNum; i++) {
				String conupId = (String) (tempMapList.size() > 1 ? tempMap.get("preferential") : tempMap.get("id"));
				addPreferential = this.createPreferentialBean(paraMap, caseAmount, new BigDecimal("0"),
						new BigDecimal("0"), discount, Constant.CALCPRETYPE.GROUP, (String) tempMap.get("name"),
						conupId, Constant.CALCPRETYPE.NORMALUSEPRE);
				// 是否挂账，优免
				if (can_credit.equals("0")) {
					addPreferential.setToalDebitAmount(caseAmount);
				} else {
					addPreferential.setToalFreeAmount(caseAmount);
				}
				detailPreferentials.add(addPreferential);
			}
		}

		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount);
		return result;
	}

}
