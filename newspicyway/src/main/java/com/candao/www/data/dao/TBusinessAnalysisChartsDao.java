package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 营业分析图表
 * @author Administrator
 *
 */
public interface TBusinessAnalysisChartsDao {

	public final static String PREFIX = TBusinessAnalysisChartsDao.class.getName();


	/**
	 * 营业分析图表查询
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> isfindBusinessReport(Map<String, Object> params);

}
