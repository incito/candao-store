package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.candao.www.utils.ReturnMes;

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
		//获取当前订单使用的优惠卷
		List<TorderDetailPreferential> ordDetailPreList = orderDetailPreferentialDao.queryDetailPreBy(orderid);
		/** 当前订单使用优惠记录 ,以及保存了当前菜品多少张 **/
		Map<String, Double> preferInfoMap = new HashMap<>();
		/**对应关系优惠卷对应，优惠信息**/
		Map<String, TorderDetailPreferential> orderDetialPerenMap=new HashMap<>();
		for (TorderDetailPreferential detailPreferential : ordDetailPreList) {
			String unit=detailPreferential.getUnit()==null?"":detailPreferential.getUnit();
			String key=detailPreferential.getDishid()+unit;
			if (!preferInfoMap.containsKey(key)) {
				preferInfoMap.put(key, (double) 1);
			} else {
				Double num = preferInfoMap.get(key);
				preferInfoMap.put(key, num + 1);
			}
			orderDetialPerenMap.put(detailPreferential.getId(), detailPreferential);
		}
		
		//特价菜品卷
		List<Map<String, Object>> tempMapList = this.discountInfo(preferentialid, PropertiesUtils.getValue("current_branch_id"), tbPreferentialActivityDao);
		//当前特价优惠卷支持1.单一菜品，2.多规格菜品
		Map<String, TbPreferenceDetail> dishCouponAmountMap = new HashMap<>();
		for(Map<String, Object> specialInfoMap:tempMapList){
			//dishId
			String dishId=(String) specialInfoMap.get("dish");
			//unit
			String unit=(String) specialInfoMap.get("unit");
			//price
			BigDecimal price=(BigDecimal)specialInfoMap.get("price");
			//preferential
			String preferential=(String) specialInfoMap.get("preferential");
			String unitKey=unit==null?"":unit;
			String key=dishId+unitKey;
			TbPreferenceDetail preferenceDetail = new TbPreferenceDetail();
			preferenceDetail.setDish(dishId);
			preferenceDetail.setPreferential(preferential);
			preferenceDetail.setPrice(price);
			preferenceDetail.setCoupondetailid( (String) specialInfoMap.get("id"));
			dishCouponAmountMap.put(key, preferenceDetail);
		}
		
		TbPreferentialActivity activity = tbPreferentialActivityDao.get(preferentialid);
		// 当前订单菜品列表
		Map<String, String> orderDetail_params = new HashMap<>();
		orderDetail_params.put("orderid", orderid);
		List<TorderDetail> orderDetailList = torderDetailDao.find(orderDetail_params);
		// 当前订单个数每一个菜品菜品的个数(对应的菜品个数，以及)里层为个数加菜品,
		// 分别为菜品dishID,dishNnum,具体信息
		Map<String, TorderDetail> orderMenuONumMap = new HashMap<>();
		for (TorderDetail torderDetail : orderDetailList) {
			String key = torderDetail.getDishid()+torderDetail.getDishunit();
			double dishNum = Double.valueOf(torderDetail.getDishnum());
			//排除POS怎送赠菜操作
			if(torderDetail.getOrderprice().doubleValue()<=0&&torderDetail.getPricetype().equals("1")){
				continue;
			}
			if (orderMenuONumMap.containsKey(key)) {
				// 如果已经存在数据做叠加菜品处理
				TorderDetail dataOrderDetal = orderMenuONumMap.get(key);
				String tempDishNum = dataOrderDetal.getDishnum();
				// 重新合并(相同菜品的总个数)
				dataOrderDetal.setDishnum(String.valueOf(Double.valueOf(tempDishNum) + dishNum));
				orderMenuONumMap.put(key, dataOrderDetal);
			} else {
				orderMenuONumMap.put(key, torderDetail);
			}
		}
		List<TorderDetailPreferential> detailPreferentials = new ArrayList<>();
		//是否需要提示标示使用优惠是否成功0成功1失败
		boolean flag=false;
		// 分两种方式使用心得优惠卷，重新计算优惠卷(updateIP不为空说明是根据数据库保存优惠重新计算)
		String updateId = paraMap.containsKey("updateId") ? (String) paraMap.get("updateId") : IDUtil.getID();
		Date insertime = (paraMap.containsKey("insertime") ?  (Date) paraMap.get("insertime"): new Date());
		// 用于后面的 更新优惠价格到账单详情
		BigDecimal amount = new BigDecimal(0);
		// 优惠的金额信息
		TorderDetailPreferential orderDetialPerInfo = orderDetialPerenMap.get(updateId);
		if(orderDetialPerInfo!=null){
			String key=orderDetialPerInfo.getDishid()+orderDetialPerInfo.getUnit();
			// 如果有使用的菜品以及返回有ID说明是重新计算优惠
			TorderDetail updateOrderDetail = orderMenuONumMap.get(key);
			
			//订单中的菜品个数 是否能优惠使用个数做判断
			  Double preferNum = preferInfoMap.get(key);
			if (updateOrderDetail == null||preferNum>Double.valueOf(updateOrderDetail.getDishnum())) {
			// 如果为空说明当前已经删除了此菜品，那么就应该删除此优惠卷
			Map<String, Object> delMap = new HashMap<>();
			delMap.put("DetalPreferentiald", paraMap.get("updateId"));
			delMap.put("orderid", orderid);
			orderDetailPreferentialDao.deleteDetilPreFerInfo(delMap);
			} else {
				// 说明当前菜单也有此菜品重新从新计算
				TorderDetailPreferential resultTorderD = crateOrderDeailPre(key, dishCouponAmountMap,
						orderMenuONumMap, updateId, orderid, activity, type,insertime);
				amount=amount.add(resultTorderD.getDeAmount());
				detailPreferentials.add(resultTorderD);
			}
		}else{
			// 1特价卷使用特价计算方式
			for (String key : orderMenuONumMap.keySet()) {
				updateId=IDUtil.getID();
				 insertime=new Date();
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
				flag=true;
				// 根据2015-06-02跟唐家荣的沟通。特价券是 一张一个菜 如果客人点了10份，就用10张券 。

				TorderDetailPreferential resultTorderD = crateOrderDeailPre(key, dishCouponAmountMap, orderMenuONumMap,
						 updateId, orderid, activity, type,insertime);
				
				if (resultTorderD != null) {
					 amount=amount.add(resultTorderD.getDeAmount());
					detailPreferentials.add(resultTorderD);
				}
		}}
		// 定义 返回值
		Map<String, Object> result = new HashMap<>();
		result.put("amount", amount.setScale(2, RoundingMode.HALF_UP));
		result.put("detailPreferentials", detailPreferentials);
		result.put("falg", flag);
		result.put("mes", flag?ReturnMes.SUCCESS.getMsg():ReturnMes.SPECIAL_FAIL.getMsg());
		return result;
	}

	private TorderDetailPreferential crateOrderDeailPre(String key,
			Map<String, TbPreferenceDetail> dishCouponAmountMap, Map<String, TorderDetail> orderMenuONumMap,
			 String updateId, String orderid, TbPreferentialActivity activity, String type,Date insertime) {
		TorderDetailPreferential detailPreferential = null;
		TbPreferenceDetail preferenceDetail = dishCouponAmountMap.get(key);
		BigDecimal preInfo = preferenceDetail.getPrice();
		BigDecimal menuCash  = orderMenuONumMap.get(key).getOrderprice()==null?new BigDecimal("0"):orderMenuONumMap.get(key).getOrderprice();
		String unit=orderMenuONumMap.get(key).getDishunit();
		if (null != preInfo && null != menuCash && menuCash.compareTo(preInfo) > 0) {
			// // 将此菜品添加到 orderDishMapList，用于后续金额的更新
			detailPreferential = new TorderDetailPreferential(updateId, orderid, preferenceDetail.getDish(),
					preferenceDetail.getPreferential(), menuCash.subtract(preInfo), "1", 0, 1, new BigDecimal(0), 0,insertime);
			detailPreferential.setCoupondetailid(dishCouponAmountMap.get(key).getCoupondetailid());
			// 设置优惠名称
			detailPreferential.setActivity(activity);
			// 设置当前优惠券的优免金额(特价属于优免)
			detailPreferential.setToalFreeAmount(menuCash.subtract(preInfo));
			//设置单位
			detailPreferential.setUnit(unit);
			if (Constant.CouponType.SPECIAL_TICKET.toString().equals(type)) {
				dishCouponAmountMap.remove(key);
			}
		}
		return detailPreferential;
	}

}
