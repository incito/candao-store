package com.candao.www.utils.preferential;

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
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/**
 * 
 * @author Candao 优惠卷使用策略 代金卷团购卷使用策略
 */
public class VoucherStrategy extends CalPreferentialStrategy {

	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailMapper torderDetailDao,
			TorderDetailPreferentialDao orderDetailPreferentialDao, TbDiscountTicketsDao tbDiscountTicketsDao,
			TdishDao tdishDao) {

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();

		String orderid = (String) paraMap.get("orderid"); // 账单号
		// 使用优惠张数
		int preferentialNum = Integer.valueOf((String) paraMap.get("preferentialNum"));
		String activityID = (String) paraMap.get("preferentialid");
		String disrate = String.valueOf(paraMap.get("disrate"));
		BigDecimal discount = new BigDecimal(disrate.trim().isEmpty() ? "0" : disrate);
		Map<String, Object> cashGratis = cashGratis(paraMap, torderDetailDao, tbPreferentialActivityDao);
		if (cashGratis != null) {
			return cashGratis;
		}
		List<Map<String, Object>> tempMapList = this.discountInfo(activityID,
				PropertiesUtils.getValue("current_branch_id"), tbPreferentialActivityDao);

		Map preMap = tempMapList.get(0);
		//
		// 获取当前账单的 菜品列表
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
		// 菜单总价
		BigDecimal orderPrice = new BigDecimal(0);
		for (TorderDetail torderDetail : orderDetailList) {
			orderPrice = orderPrice
					.add(torderDetail.getOrderprice().multiply(new BigDecimal(torderDetail.getDishnum())));
		}

		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		BigDecimal amount = new BigDecimal(String.valueOf(preMap.get("amount")));
		BigDecimal orderTempPrice = orderPrice.subtract(new BigDecimal((String) (paraMap.get("preferentialAmt"))));
		for (int i = 0; i < preferentialNum; i++) {

			String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
			TorderDetailPreferential torder = new TorderDetailPreferential(updateId, orderid, "",
					(String) paraMap.get("preferentialid"), amount, String.valueOf(orderDetailList.size()), 1, 1,
					discount, 0);
			// 设置优惠名称
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) preMap.get("name"));
			torder.setActivity(activity);
			torder.setCoupondetailid((String) (tempMapList.size() > 1 ? preMap.get("preferential") : preMap.get("id")));

			// 如果为团购卷
			if (String.valueOf(paraMap.get("type")).equals("05")) {

				// 是团购又是手动输入的
				String preferentialAmout = (String) paraMap.get("preferentialAmout");
				BigDecimal cashprelAmout = new BigDecimal(preferentialAmout);
				if (cashprelAmout.doubleValue() > 0) {
					// 设置挂账以及优免（团购有挂账 及优免，代金卷只有优免）
					if (orderTempPrice.compareTo(new BigDecimal("0")) == -1) {
						torder.setToalDebitAmountMany(cashprelAmout.multiply(new BigDecimal("-1")));
					} else if (orderTempPrice.compareTo(cashprelAmout) == -1) {
						torder.setToalDebitAmountMany(orderTempPrice.subtract(cashprelAmout));
					}
					orderTempPrice=	orderTempPrice.subtract(cashprelAmout);

					torder.setToalDebitAmount(cashprelAmout);

					torder.setDeAmount(cashprelAmout);
					amount = cashprelAmout;
				} else {
					// 设置挂账以及优免（团购有挂账 及优免，代金卷只有优免）
					String billAmoutStri = preMap.get("bill_amount").toString();
					// 设置为挂账金额
					String setAmoutStr = preMap.get("amount").toString();
					BigDecimal billAmout = new BigDecimal(billAmoutStri);
					BigDecimal setAmout = new BigDecimal(setAmoutStr);
					// 设置挂账以及优免（团购有挂账 及优免，代金卷只有优免）
					if (orderTempPrice.compareTo(new BigDecimal("0")) == -1) {
						torder.setToalDebitAmountMany(setAmout.multiply(new BigDecimal("-1")));
					} else if (orderTempPrice.compareTo(setAmout) == -1) {
						torder.setToalDebitAmountMany(orderTempPrice.subtract(setAmout));
					}
					orderTempPrice=	orderTempPrice.subtract(billAmout);
					torder.setToalFreeAmount(billAmout.subtract(setAmout));
					torder.setToalDebitAmount(setAmout);
					torder.setDeAmount(billAmout);
					amount = billAmout;
				}
			} else {
				// 代金卷
				torder.setDeAmount(amount);
				torder.setToalFreeAmount(amount);
			}

			detailPreferentials.add(torder);
		}
		result.put("detailPreferentials", detailPreferentials);
		result.put("amount", amount.multiply(new BigDecimal(String.valueOf(preferentialNum))));
		return result;
	}

}
