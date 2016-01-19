package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 优惠分析图表
 * @author Administrator
 *
 */
public interface TPreferentialAnalysisChartsDao {

	public final static String PREFIX = TPreferentialAnalysisChartsDao.class.getName();
	public <T, K, V> List<T> findPreferential(Map<K, V> params);
	public <T, K, V> List<T> findPreferentialDetail(Map<K, V> params);
	public <T, K, V> List<T> findPreferentialView(Map<K, V> params);
	public <T, K, V> List<T> findBranchPreferential(Map<K, V> params);
}
