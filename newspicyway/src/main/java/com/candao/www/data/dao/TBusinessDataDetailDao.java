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

    //实收方式，动态变化，按照itemsort排序
    public List<Map<String,Object>> getSettlementOptions(Map<String, Object> params);
    //实收数据，按照实收方式分组，按照itemsort排序
    public List<Map<String,Object>> getOrderActualSettlements(Map<String, Object> params);
    //虚增
    public Map<String,Object> getOrderInflate(Map<String, Object> params);
    //他行实收
    public Map<String,Object> getthActualAmount(Map<String, Object> params);
}
