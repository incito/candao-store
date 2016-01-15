package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 详细数据统计表
 * @author Administrator
 *
 */
public interface TDataDetailDao {

	public final static String PREFIX = TDataDetailDao.class.getName();
	public <T, K, V> List<T> findDataStatistics(Map<K, V> params);
}
