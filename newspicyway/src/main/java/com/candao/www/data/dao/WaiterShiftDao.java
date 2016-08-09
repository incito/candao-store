package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

public interface WaiterShiftDao {
	
	public final static String PREFIX = WaiterShiftDao.class.getName();

	/**
	 * 
	 * 查询服务员信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getWaiterShiftInfo(Map<String,Object> paramMap); 
	
	/**
	 * 
	 * 查询订单实收
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getOrderPayMount(Map<String,Object> paramMap); 
	
	/**
	 * 
	 * 查询订单虚增
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getOrderInflated(Map<String,Object> paramMap); 
	
}
