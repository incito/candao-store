package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

public interface ReportExportDao {

	public final static String PREFIX = TbDayIncomeBillDao.class.getName();
	
	/**
	 * 品类销售top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> getItemDishNumTop10(Map<K, V> params);
	
	/**
	 * 品类销售趋势top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> getItemAmountTop10(Map<K, V> params);
	
	/**
	 * 品项售卖份数top10趋势图
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public <T,K,V> List<T> getItemDishNumTop10Trend(Map<String,Object> params);
	
	/**
	 * 品类销售趋势top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemAmountTop10trend(Map<String,Object> params);
	
}
