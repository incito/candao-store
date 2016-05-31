package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.utils.DateUtils;
import com.candao.www.data.dao.TRethinkSettlementDao;
import com.candao.www.webroom.service.RethinkSettlementService;

@Service
public class RethinkSettlementServiceImpl implements RethinkSettlementService{

	@Autowired
	private TRethinkSettlementDao tRethinkSettlementDao;
	
	/**
	 * 查询反结算数据
	 * @author weizhifang
	 * @since 2015-11-18
	 * @param branchId
	 * @return
	 */
	public List<Map<String,Object>> queryRethinkSettlement(Map<String,Object> params){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		//查询反结算前的数据
		List<Map<String,Object>> rethinkBefore = tRethinkSettlementDao.queryRethinkSettlementBefore(params);
		if(!rethinkBefore.toString().equals("[null]")){
			for(int i=0;i<rethinkBefore.size();i++){
				//订单号
				String orderId = rethinkBefore.get(i).get("orderid").toString();
				//反结算前订单时间
				Date before_clearTime = (Date)rethinkBefore.get(i).get("before_cleartime");
				//反结算前应收
				Double before_shouldamount = (Double)rethinkBefore.get(i).get("before_shouldamount");
				//反结算前实收
				BigDecimal before_paidamount = (BigDecimal)rethinkBefore.get(i).get("before_paidamount");
				//服务员
				String waiterId = (String)rethinkBefore.get(i).get("waiter");
				String waiter = tRethinkSettlementDao.queryUserNameByJobNumber(waiterId,params.get("branchId").toString());
				//授权人
				String authorizerName = (String)rethinkBefore.get(i).get("authorizer_name");
				//根据订单号查询结算后的数据
				Map<String,Object> rethinkAfter = tRethinkSettlementDao.queryRethinkSettlementAfter(orderId);
				String order = (String)rethinkAfter.get("orderid");
				if(order != null){
					//反结算后订单时间
					Date after_clearTime = (Date)rethinkAfter.get("after_cleartime");
					//反结算后应收
					Double after_shouldamount = (Double)rethinkAfter.get("after_shouldamount");
					//反结算后实收
					BigDecimal after_paidamount = (BigDecimal)rethinkAfter.get("after_paidamount");
					//查询会员消费虚增
					BigDecimal inflated = tRethinkSettlementDao.queryMemberInflate(orderId);
					if(inflated != null){
						after_paidamount = after_paidamount.subtract(inflated).setScale(2,BigDecimal.ROUND_HALF_UP);
					}
					//收银员
					String cashierId = (String)rethinkAfter.get("cashier");
					String cashier = tRethinkSettlementDao.queryUserNameByJobNumber(cashierId,params.get("branchId").toString());
					//计算时间差异
					int timedifference = DateUtils.daysOfTwo(before_clearTime, after_clearTime);
					//计算实收差异
					BigDecimal dif = after_paidamount.subtract(before_paidamount).setScale(2,BigDecimal.ROUND_HALF_UP);
					String paidindifference = dif.toString();
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("orderid", orderId);
					map.put("before_cleartime", DateUtils.formatDateToString(before_clearTime));
					map.put("before_shouldamount", before_shouldamount);
					map.put("before_paidamount", before_paidamount);
					map.put("after_cleartime", DateUtils.formatDateToString(after_clearTime));
					map.put("after_shouldamount", after_shouldamount);
					map.put("after_paidamount", after_paidamount);
					map.put("timedifference", timedifference);
					map.put("paidindifference", paidindifference);
					map.put("waiter", waiter != null ? waiter : "");
					map.put("cashier", cashier != null ? cashier : "");
					map.put("authorized", authorizerName);
					list.add(map);
				}
			}
		}
		return list;
	}
	
	/**
	 * 查询结账单
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryStatement(Map<String,Object> params){
		Map<String,Object> map = new HashMap<String,Object>();
		//查询订单基本信息
		Map<String,Object> order = tRethinkSettlementDao.queryOrder(params);
		//查询菜品基本信息
		List<Map<String,Object>> orderDetailList = tRethinkSettlementDao.queryOrderDetail(params);
		//查询优惠信息
		List<Map<String,Object>> coupons = tRethinkSettlementDao.queryPreferenceDetail(params);
		//查询结算信息
		Map<String,Object> payways = querySettlementDetail(params.get("orderid").toString());//tRethinkSettlementDao.querySettlementDetail(params);
		List<Map<String,Object>> dishes = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> orderDetail : orderDetailList){
			Map<String,Object> dishMap = new HashMap<String,Object>();
			String itemdesc = (String)orderDetail.get("itemdesc");
			Double count = (Double)orderDetail.get("count");
			BigDecimal price = (BigDecimal)orderDetail.get("price");
			Double amount = (Double)orderDetail.get("amount");
		    dishMap.put("itemdesc", itemdesc);
		    dishMap.put("count", Math.round(count));
		    dishMap.put("price", price.setScale(2,BigDecimal.ROUND_HALF_UP));
		    BigDecimal b = new BigDecimal(amount);
		    dishMap.put("amount", b.setScale(2,BigDecimal.ROUND_HALF_UP));
		    dishes.add(dishMap);
		}
		map.put("orderid", params.get("orderid"));
		map.put("personnum", order.get("personnum") == null ? 0 : order.get("personnum"));
		map.put("begintime", order.get("begintime"));
		map.put("endtime", order.get("endtime"));
		map.put("tableno", order.get("tableno"));
		map.put("area", order.get("area"));
		map.put("waiter", order.get("waiter"));
		map.put("totalconsumption", payways.get("totalconsumption"));
		map.put("payway", payways.get("payway"));
		map.put("payamount", payways.get("payamount"));
		map.put("couponamount", payways.get("couponamount"));
		map.put("giveamount", payways.get("giveamount"));
		map.put("paidamount", payways.get("paidamount"));
		map.put("invoiceamount", payways.get("invoiceamount"));
		map.put("dishes",dishes);
		map.put("coupons", coupons);
		return map;
	}
	
	/**
	 * 查询桌号
	 * @author weizhifang
	 * @since 2015-12-9
	 * @param params
	 * @return
	 */
	public String queryTableNo(Map<String,Object> params){
		return tRethinkSettlementDao.queryTableNo(params);
	}
	
	/**
	 * 结算方式查询
	 * @author weizhifang
	 * @since 2016-5-30
	 * @param params
	 * @return
	 */
	public Map<String,Object> querySettlementDetail(String orderid){
		Map<String,Object> settlement = new HashMap<String,Object>();
		//四舍五入/抹零
		Map<String,Object> payways = tRethinkSettlementDao.queryMoLing(orderid);
		//应收
		BigDecimal yingshou = tRethinkSettlementDao.totalconsumption(orderid);
		//套餐
		BigDecimal taocan = tRethinkSettlementDao.taocanAmount(orderid);
		//实际应收=应收-套餐
		BigDecimal totalconsumption = yingshou.subtract(taocan).setScale(2,BigDecimal.ROUND_HALF_UP);
		//实收
		BigDecimal paidamount = tRethinkSettlementDao.paidamount(orderid);
		//赠菜
		BigDecimal giveamount = tRethinkSettlementDao.giveamount(orderid);
		//总优惠
		BigDecimal couponamount = totalconsumption.subtract(paidamount).setScale(2,BigDecimal.ROUND_HALF_UP);
		settlement.put("totalconsumption", ratioTransform2(totalconsumption));
		settlement.put("paidamount", ratioTransform2(paidamount));
		settlement.put("giveamount", ratioTransform2(giveamount));
		settlement.put("couponamount", ratioTransform2(couponamount));
		settlement.put("invoiceamount", "0.00");
		if(payways != null){
			settlement.put("payway", payways.get("payway"));
			settlement.put("payamount", payways.get("payamount"));
		}else{
			settlement.put("payway", 7);
			settlement.put("payamount", 0.00);
		}
		return settlement;
	}
	
	/** 
     * BigDecimal 保留2位小数
     * @author weizhifang
     * @since 2016-5-31
     * @param amount 金额 
     * @param scale 精度 
     * @return 
     */  
    public static String ratioTransform2(BigDecimal amount) {  
        DecimalFormat df = new DecimalFormat("0.00");  
        df.setRoundingMode(RoundingMode.HALF_UP);  
        return df.format(amount);  
    }  
    
    public static void main(String[] args) {
    	RethinkSettlementServiceImpl r = new RethinkSettlementServiceImpl();
    	System.out.println(r.ratioTransform2(new BigDecimal("3.1415926")));
	}
}
