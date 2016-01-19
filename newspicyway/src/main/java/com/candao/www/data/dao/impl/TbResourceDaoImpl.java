package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbResourceDao;
import com.candao.www.data.model.TbResource;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbResourceDaoImpl implements TbResourceDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbResource get(java.lang.String resourcesid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourcesid", resourcesid);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String resourcesid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourcesid", resourcesid);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public int insert(TbResource tbResource) {
		return dao.insert(PREFIX + ".insert", tbResource);
	}

	@Override
	public int update(TbResource tbResource) {
		return dao.update(PREFIX + ".update", tbResource);
	}

	@Override
	public int delete(java.lang.String resourcesid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourcesid", resourcesid);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public List<TbResource> getLeftMenu(String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		return dao.find(PREFIX + ".getLeftMenu", params);
	}

	@Override
	public List<String> findAllPath() {
		return  dao.find(PREFIX + ".findAllPath");
	}

	@Override
	public List<TbResource> findByRoleid(Map<String, Object> params) {
		 
		return dao.find(PREFIX + ".findByRoleid", params);
	}
}


