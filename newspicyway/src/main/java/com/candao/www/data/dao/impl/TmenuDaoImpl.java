package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TmenuDao;
import com.candao.www.data.model.Tmenu;
@Repository
public class TmenuDaoImpl implements TmenuDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public int insert(Tmenu tmenu) {
		// TODO Auto-generated method stub
		return daoSupport.insert(PREFIX+".insert", tmenu);
	}

	@Override
	public int update(Tmenu tmenu) {
		// TODO Auto-generated method stub
		return daoSupport.update(PREFIX+".update", tmenu);
	}

	@Override
	public Tmenu get(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", id);
		return daoSupport.get(PREFIX+".get", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".find", params);
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", id);
		return daoSupport.delete(PREFIX+".delete", params);
	}

	@Override
	public <T, K, V> List<T> findMenuByBrachid(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".findMenuByBrachid", params);
	}

	@Override
	public List<Tmenu> findEffectMenu(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".findEffectMenu", params);
	}

	@Override
	public List<Map<String, Object>> getHeatDishList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".getHeatDishList", params);
	}

	@Override
	public <T, K, V> List<T> findByBranchid(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".findByBranchid", params);
	}

	@Override
	public List<Map<String, Object>> getBranchMenuColumn(
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".getBranchMenuColumn", params);
	}

	@Override
	public List<Map<String, Object>> getBranchMenuDishByType(
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".getBranchMenuDishByType", params);
	}

	@Override
	public List<Map<String,Object>> getMenuDishDetailById(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".getMenuDishDetailById", params);
	}


}
