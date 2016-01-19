package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbVersionDao;
import com.candao.www.data.model.TbVersion;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbVersionDaoImpl implements TbVersionDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbVersion get(int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		return dao.get(PREFIX + ".get", params);
	}
	

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	 
	@Override
	public int update(TbVersion tbVersion) {
		return dao.update(PREFIX + ".update", tbVersion);
	}

	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}
}


