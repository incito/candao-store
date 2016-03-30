package com.candao.www.data.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TPreferentialAnalysisChartsDao;

/**
 * 优惠分析图表
 * @author Administrator
 *
 */
@Repository
public class TPreferentialAnalysisChartsDaoImpl implements TPreferentialAnalysisChartsDao {

	private static final Logger logger = LoggerFactory.getLogger(TPreferentialAnalysisChartsDaoImpl.class);
	
	@Autowired
	private DaoSupport daoSupport;

    @Override
	public <T, K, V> List<T> findPreferential(Map<K, V> params) {
	     return  daoSupport.find(PREFIX + ".findPreferential", params);
	}
    public <T, K, V> List<T> findPreferentialDetail(Map<K, V> params){
    	return daoSupport.find(PREFIX + ".findPreferentialDetail", params);
    }
    public <T, K, V> List<T> findPreferentialView(Map<K, V> params){
    	try{
    		return daoSupport.find(PREFIX + ".findPreferentialView", params);
    	}catch(Exception ex){
    		logger.error(ex.getMessage());
    		ex.printStackTrace();
    	}
    	return new ArrayList<T>();
    }
    public <T, K, V> List<T> findBranchPreferential(Map<K, V> params){
    	return daoSupport.find(PREFIX + ".findBranchPreferential", params);
    }
}
