package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 营业数据明细表
 * @author Administrator
 *
 */
public interface TBusinessDataDetailDao {

	public final static String PREFIX = TBusinessDataDetailDao.class.getName();
	public List<Map<String, Object>> isgetBusinessDetail(Map<String, Object> params);
	
	
	public List<Map<String, Object>> getOrderInfo(Map<String, Object> params);

}
