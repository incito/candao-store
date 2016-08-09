package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 品项分析图表
 * @author Administrator
 *
 */
public interface TItemAnalysisChartsDao {

	public final static String PREFIX = TItemAnalysisChartsDao.class.getName();
	
	/**
	 * 查询品项分析图表
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemAnalysisChartsForPro(Map<String,Object> params);
	
	
	/**
	 * 查询品项分析图表存储过程(指定分类统计)
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemAnalysisChartsForColumnPro(Map<String,Object> params);
	
	
	/**
	 * 获取所有的订单详细信息
	 * 
	 */
	public List<Map<String,Object>> getAllOrderInfo(Map<String,Object> params);
	
	/**
	 * 获取一整天的来客数
	 * 
	 */
	public List<Map<String, Object>> getAllOrderCustnum(Map<String,Object> params);
	
	/**
	 * 获取整月的来客数
	 * 
	 */
	public List<Map<String, Object>> getAllOrderCustnumOfMonth(Map<String,Object> params);
}
