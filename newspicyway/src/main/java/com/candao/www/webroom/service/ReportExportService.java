package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

/**
 * 报表导出接口
 * @since 2015-6-5
 *
 */
public interface ReportExportService {

	/**
	 * 品类销售top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDishNumTop10(Map<String,Object> params);
	
	/**
	 * 品类销售top10趋势图
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDishNumTop10Trend(List<Map<String,Object>> dishNumTrendList,Map<String,Object> params);
	
	/**
	 * 品类销售趋势top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemAmountTop10(Map<String,Object> params);
	
}
