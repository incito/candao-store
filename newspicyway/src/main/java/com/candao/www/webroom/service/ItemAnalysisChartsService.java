package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

/**
 * 品项分析图表
 * @author Administrator
 *
 */
public interface ItemAnalysisChartsService {

	/**
	 * 查询品项分析图表存储过程
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemAnalysisChartsForPro(Map<String,Object> params);
	
	/**
	 * 品类销售top10
	 * @author weizhifang
	 * @since 2015-7-4
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDishNumTop10(List<Map<String,Object>> itemCharsList);
	
	/**
	 * 品类销售top10趋势图
	 * @author weizhifang
	 * @since 2015-7-4
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemDishNumTop10Trend(List<Map<String,Object>> dishNumList);
	
	/**
	 * 品类销售金额top10
	 * @author weizhifang
	 * @since 2015-7-4
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemAmountTop10(List<Map<String,Object>> itemCharsList);
	
	/**
	 * 品类销售金额top10趋势图
	 * @author weizhifang
	 * @since 2015-7-4
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemAmountTop10trend(List<Map<String,Object>> amountList);
	
	/**
	 * 查询品项分析图表存储过程(指定分类统计)
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemAnalysisChartsForColumnPro(Map<String,Object> params);
	
	/**
	 * 查询品项销售千次信息
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,String>> getColumnItemThousandsTimesReportForView(Map<String,Object> params);
}
