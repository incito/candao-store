package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbTableAreaDao;
import com.candao.www.data.model.TbTableArea;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbTableAreaDaoImpl implements TbTableAreaDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbTableArea get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public TbTableArea tableAvaliableStatus(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".tableAvaliableStatus", params);
	}
	
	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		
		return dao.find(PREFIX + ".find", params);
	}
	public <T, K, V> List<T> getCount(Map<K, V> params) {
		return dao.find(PREFIX+".count",params);
	}
	/*public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}*/
	@Override
	public int insert(TbTableArea tbTableArea) {
		return dao.insert(PREFIX + ".insert", tbTableArea);
	}

	@Override
	public int update(TbTableArea tbTableArea) {
		return dao.update(PREFIX + ".update", tbTableArea);
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
	
	@Override
	
	public List<Map<String, Object>> getTableTag() {
		return dao.find(PREFIX+".getTableTag");
	}

	@Override
	public int updateListOrder(List<TbTableArea> tbtableArea) {
		return dao.update(PREFIX + ".updateListOrder", tbtableArea);
	}

	@Override
	public List<Map<String, Object>> findTableCountAndAreaname() {
		// TODO Auto-generated method stub
		return dao.find(PREFIX+".findTableCountAndAreaname");
	}
}


