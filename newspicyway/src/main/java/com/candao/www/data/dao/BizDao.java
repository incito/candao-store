package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * 呼叫服务员
 * 
 * 
 * @author YANGZHONGLI
 *
 */
public interface BizDao {
	
	public final static String PREFIX = BizDao.class.getName();
	
	/**
	 * 
	 * 查询指定门店的所有用户信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getUsers(String branchId);
	
	/**
	 * 
	 * 查询指定门店开业结业信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getBizInfos(String beginTime,String endTime,String branchId);
	
	public List<Map<String, Object>> getBizNodeClassInfos(String beginTime,String endTime,String branchId);
		
}
