package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.Torder;
import com.candao.www.data.model.TorderDetail;

public interface OrderService {

	public int saveOrder(Torder order );
	
	public int discardOrder(Torder order);
	
	public int addDishOnOrder(TorderDetail orderDetail);
	
	public int reduceDishOnOrder(TorderDetail orderDetail);
	
	public String startOrder(Torder order);
	
	public Map<String, Object> findOrderById(String orderId);
	/**
	 * 获取更换pad的信息
	 * @author shen
	 * @date:2015年5月14日下午3:52:29
	 * @Description: TODO
	 */
	public Map<String, Object> switchPadOrderInfo(Map<String,Object> params);
	
	public Torder findOrderByTableId(Torder order); 
	
	public int update(Torder order);
	
	public String updateOrder(Torder  tOrder);

	public String querycoupons(String dishId);

	public String queryallcoupons();
	
	public Map<String,Object> updateDishWeight(Map<String, Object> params);

	public void executeSql(String sql);

	public int updateInvoiceid(Torder order);
	
	public Torder get(java.lang.String id);
	
	public void setOrderMember(Map<String,Object> params);

	public List<Torder> verifyAllOrder();

//	public int verifyAllCLean();
//
//	public int verifyAllTableClear();

	public String callEndWork(String userName, String isSucess);

    
}
