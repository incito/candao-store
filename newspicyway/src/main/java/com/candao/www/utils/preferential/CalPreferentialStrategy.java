package com.candao.www.utils.preferential;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TbPreferentialActivityDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.model.TbPreferentialActivity;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TorderDetailPreferential;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.utils.RoundingEnum;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.service.DataDictionaryService;

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
	protected Map<String, Object> cashGratis(Map<String, Object> params, TorderDetailMapper torderDetailDao,TbPreferentialActivityDao tbPreferentialActivityDao) {
		Map<String, Object> resultMap = new HashMap<>();
		String preferentialid = (String) params.get("preferentialid"); //优惠活动id
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String orderid = (String) params.get("orderid"); // 账单号
		String isCustom = String.valueOf( params.get("isCustom"));
		String preferentialAmout = String.valueOf( params.get("preferentialAmout"));
		BigDecimal amout = new BigDecimal(preferentialAmout);
		if (isCustom.equals("1") && amout.doubleValue() > 0) {
			// 获取当前账单的 菜品列表
			Map<String, String> orderDetail_params = new HashMap<>();
			orderDetail_params.put("orderid", orderid);
			List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
			 String updateId=params.containsKey("updateId")?(String)params.get("updateId"):IDUtil.getID();
			List<TorderDetailPreferential> listRest = new ArrayList<>();
			Map tempMap = this.discountInfo(preferentialid, branchid, tbPreferentialActivityDao);
			TorderDetailPreferential detailPreferential = new TorderDetailPreferential(updateId, orderid, "",
					(String) params.get("preferentialid"), amout, String.valueOf(orderDetailList.size()), 1, 1,
					new BigDecimal(0), 1);
			TbPreferentialActivity activity = new TbPreferentialActivity();
			activity.setName((String) tempMap.get("name"));
			detailPreferential.setActivity(activity);
			listRest.add(detailPreferential);
			resultMap.put("amount", amout);
			resultMap.put("detailPreferentials", listRest);
			return resultMap;
		} 
		return null;
	}

}
