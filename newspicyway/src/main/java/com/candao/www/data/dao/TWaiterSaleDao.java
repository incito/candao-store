package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

public interface TWaiterSaleDao {

	public final static String PREFIX = TWaiterSaleDao.class.getName();
	
	/**
	 * 查询服务员销售列表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> waiterSaleListProcedure(Map<String,Object> params);
	
	/**
	 * 按订单查询服务员销售列表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getWaiterSaleDetail(Map<String,Object> params);
	
	/**
	 * 查询服务员菜品信息
	 * @author weizhifang
	 * @sice 2016-3-26
	 * @param params
	 * @return
	 */
	public Map<String,Object> getWaiterDishInfo(Map<String,Object> params);
}
