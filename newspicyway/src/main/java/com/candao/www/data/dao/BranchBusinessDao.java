package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TBranchBusinessInfo;

/**
 * 
 * 呼叫服务员
 * 
 * 
 * @author YANGZHONGLI
 *
 */
public interface BranchBusinessDao {
	
	public final static String PREFIX = BranchBusinessDao.class.getName();
	/**
	 * 
	 * 查询所有指定门店，指定日期的所有的数据信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<TBranchBusinessInfo> getBuinessInfos(Map<String,Object> paramMap);
	
	/**
	 * 
	 * 按天查询门店的数据信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<TBranchBusinessInfo> getBuinessInfosByDay(Map<String,Object> paramMap);
	/**
	 * 
	 * 按天查询门店订单信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,String>> getBranchDayOrders(Map<String,String> paramMap);
		
}
