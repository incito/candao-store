package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.ComplexTorderDetail;
import com.candao.www.data.model.ToperationLog;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.webroom.model.Order;
import com.candao.www.webroom.model.Table;
import com.candao.www.webroom.model.UrgeDish;

public interface OrderDetailService {

	public Map<String, Object> setOrderDetailList(Order order );
	public String discardDishList( UrgeDish urgeDish,ToperationLog toperationLog);

 
	public String urgeDishList(UrgeDish urgeDish);
 
	public String cookiedishList(UrgeDish urgeDish);

	
	public List<TorderDetail> find(Map<String, String> mapDetail);
	public TorderDetail findOne(Map<String, String> mapDetail);


	public void cleantable(Table table);


	public String updateorderprice(Order order);


	public String getOrderDetailByOrderId(String orderid);
	
	public void printStatement(String orderno);
	
	/**
	 * 获取品项销售明细
	 * @param timeMap
	 * @return
	 */
	public List<Map<String, Object>> itemSellDetail(Map<String, Object> timeMap) throws Exception;

	public Map<String, Object> placeOrder(Order order);

	public void   afterprint(String orderid);

	/**
	 * 咖啡模式清台
	 * @param table
	 * @return
	 */
	public String cleantableSimply(Table table);
	
	public int deleteordreDetailByOrderid(String orderid);
	 Map<String, Object> findOrderByInfo(String orderid);
	 
	 /**
	  * 
	  * @param orderId
	  * @return
	  * 查询订单里面包含菜品名称
	  */
	 public List<ComplexTorderDetail> findorderByDish(String orderId);
	
}
