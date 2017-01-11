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

/**
 * 
 * @author Candao 优惠卷使用策略 代金卷团购卷使用策略
 */
public class VoucherStrategy extends CalPreferentialStrategy {

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> calPreferential(Map<String, Object> paraMap,
			TbPreferentialActivityDao tbPreferentialActivityDao, TorderDetailPreferentialDao orderDetailPreferentialDao,
			TbDiscountTicketsDao tbDiscountTicketsDao, TdishDao tdishDao, List<ComplexTorderDetail> orderDetailList) {

		// 定义 返回值
		Map<String, Object> result = new HashMap<>();

		/**参数解析**/ 
		int preferentialNum = Integer.valueOf((String) paraMap.get("preferentialNum"));
		String activityID = (String) paraMap.get("preferentialid");
		
		/**优惠卷详情**/
		List<Map<String, Object>> tempMapList = this.discountInfo(activityID,
				PropertiesUtils.getValue("current_branch_id"), tbPreferentialActivityDao);
		Map preMap = tempMapList.get(0);

		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		BigDecimal amount = new BigDecimal(String.valueOf(preMap.get("amount")));
		for (int i = 0; i < preferentialNum; i++) {
			String couponID=(String) (tempMapList.size() > 1 ? preMap.get("preferential") : preMap.get("id"));
			String preName=(String) preMap.get("name");
			TorderDetailPreferential torder = this.createPreferentialBean(paraMap, amount, new BigDecimal("0"),  new BigDecimal("0"),
					 new BigDecimal("0"), Constant.CALCPRETYPE.GROUP, preName, couponID, Constant.CALCPRETYPE.NORMALUSEPRE);
			// 如果为团购卷
			if (String.valueOf(paraMap.get("type")).equals("05")) {
				// 是团购又是手动输入的
				String preferentialAmout = (String) paraMap.get("preferentialAmout");
				BigDecimal cashprelAmout = new BigDecimal(preferentialAmout);
				if (cashprelAmout.doubleValue() > 0 && String.valueOf(paraMap.get("isCustom")).equals("1")) {
					amount = cashprelAmout;
					// 设置挂账以及优免（团购有挂账 及优免，代金卷只有优免）
					torder.setIsCustom(1);
					torder.setToalDebitAmount(cashprelAmout);
					torder.setDeAmount(cashprelAmout);
				
				} else {
					// 设置总金额
					BigDecimal billAmout = new BigDecimal(preMap.get("bill_amount").toString());
					BigDecimal debitAmout = new BigDecimal( preMap.get("amount").toString());
					BigDecimal freeAmount=billAmout.subtract(debitAmout) ;
					amount = billAmout;
					// 设置挂账以及优免（团购有挂账 及优免，代金卷只有优免）
					torder.setToalFreeAmount(freeAmount);
					torder.setToalDebitAmount(debitAmout);
					torder.setDeAmount(billAmout);
				
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
