package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TtemplateDishUnitlDao;
import com.candao.www.data.model.TtemplateDishUnit;
@Repository
public class TtemplateDishUnitlDaoImpl implements TtemplateDishUnitlDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public int addTtemplateDishUnit(List<TtemplateDishUnit> list) {
		// TODO Auto-generated method stub
		return daoSupport.insert(PREFIX+".insertOnce", list);
	}

	@Override
	public int delTtemplateDishUnit(String menuid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", menuid);
		return daoSupport.delete(PREFIX+".delTtemplateDishUnit", params);
	}

	@Override
	public <E, K, V> List<E> getTtemplateDishUnitByparams(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".getTtemplateDishUnitByparams", params);
	}

	@Override
	public <E, K, V> List<E> getTtemplateDishUnitByparamsPad(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".getTtemplateDishUnitByparamsPad", params);
	}

	@Override
	public boolean updateDishStatus(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return daoSupport.update(PREFIX+".updateDishStatus", params)>0;
	}

	@Override
	public List<Map<String, Object>> findOneTtd(Map<String, Object> paramsTtd) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".findOneTtd", paramsTtd);
	}

	@Override
	public <E, K, V> List<E> getTtemplatefishpotUnitByparams(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".getTtemplatefishpotUnitByparams", params);
	}

	@Override
	public List<TtemplateDishUnit> getTtemplateDishUnitByStatus() {
		Map<String, Object> params = new HashMap<>();
		params.put("status", "1");	//估清
		return daoSupport.find(PREFIX+".getTtemplateDishUnitByStatus", params);
	}

	@Override
	public boolean updateStatus(String dishIds) {
		Map<String, Object> map = new HashMap<>();
		map.put("dishIds", dishIds);
		return daoSupport.update(PREFIX+".updateStatus", map) > 0;
	}

}
