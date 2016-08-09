package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TDataDetailDao;

/**
 * 详细数据统计表
 * @author Administrator
 *
 */
@Repository
public class TDataDetailDaoImpl implements TDataDetailDao {

	@Autowired
	private DaoSupport daoSupport;
	public <T, K, V> List<T> findDataStatistics(Map<K, V> params){
	    return daoSupport.find(PREFIX + ".findDataStatistics", params);
	}
}
