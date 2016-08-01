package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.ToperationLog;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.webroom.model.Order;
import com.candao.www.webroom.model.Table;
import com.candao.www.webroom.model.UrgeDish;

public interface OrderDetailService {

//	public String saveOrderDetails(Order order );
	public Map<String, Object> setOrderDetailList(Order order );
	
	
	public void discardOrderDetail(TorderDetail orderDetail,String discardUserId,String discardReason);
	
	
//	public int addDishOnOrderDetail(TorderDetail orderDetail);
	
//	public int reduceDishOnOrderDetail(TorderDetail orderDetail);
	
//	public String startOrderDetail(TorderDetail order);

//	public String discardDish( UrgeDish urgeDish);
	public String discardDishList( UrgeDish urgeDish,ToperationLog toperationLog);

 
	public String urgeDishList(UrgeDish urgeDish);
 
	public String cookiedishList(UrgeDish urgeDish);

//	public TorderDetail findByOrderNoAndDishNo(Map<String, String> mapDetail);
	
	public List<TorderDetail> find(Map<String, String> mapDetail);
	public TorderDetail findOne(Map<String, String> mapDetail);

	public List<Map<String,String>> findTemp(Map<String, String> mapDetail);

	public String cleantable(Table table);


	public String updateorderprice(Order order);


	public String getOrderDetailByOrderId(String orderid);
	
	public void printStatement(String orderno);
	
	/**
	 * 获取品项销售明细
	 * @param timeMap
	 * @return
	 */
	public List<Map<String, Object>> getItemSellDetail(Map<String, Object> timeMap) throws Exception;

	public Map<String, Object> placeOrder(Order order);

	public void   afterprint(String orderid);

	/**
	 * 咖啡模式清台
	 * @param table
	 * @return
	 */
	public String cleantableSimply(Table table);
}
