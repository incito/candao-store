package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 排班报表分析
 * @author zhouyao
 * @serialData 2015-12-29
 */
public interface SchedulingDao {

	public final static String PREFIX = SchedulingDao.class.getName();
	public <T, K, V> List<T> schedulingReport(Map<K, V> params);
}
