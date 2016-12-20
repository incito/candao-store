package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.candao.www.preferential.model.PreDealInfoBean;
import com.candao.www.preferential.precache.CacheManager;
import com.candao.www.utils.ReturnMes;

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
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao) {
		String preferentialid = (String) paraMap.get("preferentialid"); // 优惠活动id
		String orderid = (String) paraMap.get("orderid"); // 账单号
		BigDecimal bd = new BigDecimal((String) paraMap.get("preferentialAmt"));
		List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid,
				PropertiesUtils.getValue("current_branch_id"), tbPreferentialActivityDao);
		Map tempMap = tempMapList.get(0);

		// discount大于0为折扣优免
		BigDecimal discount = (BigDecimal) tempMap.get("discount");
		// amount大于0为现金优免
		BigDecimal caseAmount = (BigDecimal) tempMap.get("amount");

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();

		// 获取当前账单的 菜品列表
		
		List<TorderDetail> orderDetailList =this.loadCache(orderid, paraMap.containsKey("updateId") ? "info" : "");
		
		// 当前订单总价amountCount
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
		// 使用优惠卷列表
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		TorderDetailPreferential addPreferential = null;
		// 最终优惠金额
		BigDecimal amount = new BigDecimal(0);
		// 0不能挂账，1挂账
		String can_credit = String.valueOf(tempMap.get("can_credit"));
		// 设置金额
		if (discount != null && discount.doubleValue() > 0) {
			PreDealInfoBean deInfo = this.calDiscount(amountCount, bd, discount);
			if (deInfo.getPreAmount().doubleValue() > 0) {
				amount = deInfo.getPreAmount();
				addPreferential = this.createPreferentialBean(paraMap, amount, orderDetailList.size(), discount, 1,
						tempMapList);
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
			}
			this.disMes(result, amountCount, amountCount, bd, deInfo.getDistodis());
		} else if (caseAmount != null && caseAmount.doubleValue() > 0) {
			// 获取除开优惠的支付金额
			BigDecimal orderTempPrice = amountCount.subtract(new BigDecimal((String) (paraMap.get("preferentialAmt"))));
			// 获取优惠卷个数(如果POS选择多张优惠卷，后台会在数据库写入多个数据，所以要拆分优惠卷)
			int preferentialNum = Integer.valueOf((String) paraMap.get("preferentialNum"));
			amount = caseAmount.multiply(new BigDecimal(preferentialNum));
			for (int i = 0; i < preferentialNum; i++) {
				String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
				Date insertime = (paraMap.containsKey("insertime") ? (Date) paraMap.get("insertime") : new Date());
				addPreferential = new TorderDetailPreferential(updateId, orderid, "", preferentialid, caseAmount,
						String.valueOf(orderDetailList.size()), 1, 1, discount, 0, insertime);
				// 设置优惠名称
				TbPreferentialActivity activity = new TbPreferentialActivity();
				activity.setName((String) tempMap.get("name"));
				addPreferential.setActivity(activity);
				// 是否挂账，优免
				if (can_credit.equals("0")) {
					if (orderTempPrice.compareTo(new BigDecimal("0")) == -1) {
						addPreferential.setToalDebitAmountMany(caseAmount.multiply(new BigDecimal("-1")));
					} else if (orderTempPrice.compareTo(caseAmount) == -1) {
						addPreferential.setToalDebitAmountMany(orderTempPrice.subtract(caseAmount));
					}
					addPreferential.setToalDebitAmount(caseAmount);
					orderTempPrice = orderTempPrice.subtract(caseAmount);
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
