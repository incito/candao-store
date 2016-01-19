package com.candao.www.data.dao.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbSalesSummaryDao;
@Repository
public class TbSalesSummaryDaoImpl implements TbSalesSummaryDao {

	@Autowired
    private DaoSupport dao;
	
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".dates", params);
	}

	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.pageAndFooter(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public <T, K, V> List<T> findFD(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.findAndFooter(PREFIX + ".page", params);
	}
	@Override
	public <T, K, V> List<T> dates(Map<K, V> params) {
		
		return dao.find(PREFIX + ".find", params);
	}
}
