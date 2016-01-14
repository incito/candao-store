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
}
