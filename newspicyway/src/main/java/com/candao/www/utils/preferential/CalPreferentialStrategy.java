package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;

/**
 * 
 * @author Candao 基类做同凑运算
 */
public abstract class CalPreferentialStrategy implements CalPreferentialStrategyInterface {

	/***
	 * 获取优惠券信息
	 * 
	 * @return
	 */
	protected Map discountInfo(String activityID, String branchid,
			TbPreferentialActivityDao tbPreferentialActivityDao) {
		Map<String, String> detail_params = new HashMap<>();
		detail_params.put("preferential", activityID);
		detail_params.put("branchid", branchid);
		List<Map<String, Object>> detailList = tbPreferentialActivityDao.findPreferentialDetail(detail_params);
		Map detailMap = detailList.get(0);
		return detailMap;
	}

	/**
	 * 现金优免 手动输入
	 * 
	 * @return
	 */
	protected Map<String, Object> cashGratis(Map<String, Object> params, TorderDetailMapper torderDetailDao) {
		Map<String, Object> resultMap = new HashMap<>();
		String orderid = (String) params.get("orderid"); // 账单号
		String isCustom = (String) params.get("isCustom");
		String discount = (String) params.get("disrate");
		String preferentialAmoutInfo = (String) params.get("preferentialAmout");
		BigDecimal amout = new BigDecimal(preferentialAmoutInfo);
		if (isCustom.equals("1") && amout.doubleValue() > 0&&Double.valueOf(discount)<=0) {
			// 获取当前账单的 菜品列表
			Map<String, String> orderDetail_params = new HashMap<>();
			orderDetail_params.put("orderid", orderid);
			List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);

			List<TorderDetailPreferential> listRest = new ArrayList<>();
			TorderDetailPreferential detailPreferential = new TorderDetailPreferential(IDUtil.getID(), orderid, "",
					(String) params.get("preferentialid"), amout, String.valueOf(orderDetailList.size()), 1, 1,
					new BigDecimal(0), 0);
			listRest.add(detailPreferential);

			resultMap.put("amount", amout);
			resultMap.put("detailPreferentials", listRest);
			return resultMap;
		}
		return null;
	}

	/**
	 * 折扣优免 手动输入
	 */
	protected void discountGratis() {
		

	}

}
