package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * bossapp 数据库操作类
 * 
 * @author YANGZHONGLI
 *
 */
public interface BossappUtilDao {
	
	public final static String PREFIX = BossappUtilDao.class.getName();

	/**
	 * 
	 * 查询所有桌子信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getAllTablesInfo(String branchid); 
	
	/**
	 * 
	 * 查询订单信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getOrderInfo(Map<String,Object> paramMap); 
	
	/**
	 * 
	 * 查询订单信息(临时)
	 * 
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getOrderInfoTemp(Map<String,Object> paramMap); 
	
	/**
	 * 
	 * 查询订单信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getDayFlow(Map<String,Object> paramMap); 
	
	
}
