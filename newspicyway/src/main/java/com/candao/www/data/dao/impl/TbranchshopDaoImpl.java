package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbranchshopDao;
import com.candao.www.data.model.Tbranchshop;
@Repository
public class TbranchshopDaoImpl implements TbranchshopDao {
	@Autowired
    private DaoSupport dao;
	@Override
	public Tbranchshop get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public int insert(Tbranchshop tbranchshop) {
		return dao.insert(PREFIX + ".insert", tbranchshop);
	}

	@Override
	public int update(Tbranchshop tbranchshop) {
		return dao.update(PREFIX + ".update", tbranchshop);
	}

	@Override
	public int delete(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}
	
 
	
	public <T, K, V> List<T> findIds(List<String> listIds){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", listIds);
		return dao.find(PREFIX+".findIds",map);
	}
	
	@Override
	public List<Tbranchshop> findAll(){
		 
		return dao.find(PREFIX+".getAll",null);
	}

}
