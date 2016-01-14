package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.ReportExportDao;

@Repository
public class ReportExportDaoImpl implements ReportExportDao{

	@Autowired
    private DaoSupport dao;
	
	
	@Override
	public <T, K, V> List<T> getItemDishNumTop10(Map<K, V> params) {
		return dao.find(PREFIX + ".getItemDishNumTop10", params);
	}
	
	/**
	 * 品类销售趋势top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> getItemAmountTop10(Map<K, V> params){
		return dao.find(PREFIX + ".getItemAmountTop10", params);
	}
	
	/**
	 * 品项售卖份数top10趋势图
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public <T,K,V> List<T> getItemDishNumTop10Trend(Map<String,Object> params){
		return dao.find(PREFIX + ".getItemDishNumTop10Trend", params);
	}
	
	/**
	 * 品类销售趋势top10
	 * @author weizhifang
	 * @since 2015-6-6
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getItemAmountTop10trend(Map<String,Object> params){
		return dao.find(PREFIX + ".getItemAmountTop10trend", params);
	}
}
