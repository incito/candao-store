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
	 * 查询订单虚增,以服务员分组
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getOrderInflateInfo(Map<String,Object> paramMap); 
	/**
	 * 
	 * 查询订单虚增
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getOrderInflateInfoByOrder(Map<String,Object> paramMap); 
	/**
	 * 
	 * 查询订单虚增
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getOrderInflated(Map<String,Object> paramMap); 
	
	/**
	 * 统计服务员考核信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getOrderInfoGroupByWaiter(Map<String,Object> paramMap);
	
	/**
	 * 统计订单的付费详情
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getOrderSettlementInfo(Map<String,Object> paramMap);

	/**
	 * 得到
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getSettlementInfo(Map<String,Object> paramMap);
	
	
	public List<Map<String,Object>> getOrderSettlementInfoDetail(Map<String,Object> paramMap);
	public List<Map<String,Object>> getOrderInfoGroupByOrder(Map<String,Object> paramMap);
}
