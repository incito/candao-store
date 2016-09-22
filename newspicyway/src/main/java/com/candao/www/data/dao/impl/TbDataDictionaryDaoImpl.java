package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.data.model.TbDataDictionary;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TdishUnit;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbDataDictionaryDaoImpl implements TbDataDictionaryDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbDataDictionary get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}
	@Override
    public <K, V> Map<K, V>  getDish(java.lang.String id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".getDish", params);
	}
	@Override
	public  <K, V> Map<K, V>  getDishUnit(java.lang.String id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".getDishUnit", params);
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
	public int insert(TbDataDictionary tbDataDictionary) {
		return dao.insert(PREFIX + ".insert", tbDataDictionary);
	}
	
	@Override
	public int insertPadimg(TbDataDictionary tbDataDictionary) {
		return dao.insert(PREFIX +".insertPadimg", tbDataDictionary);
	}

	@Override
	public int update(TbDataDictionary tbDataDictionary) {
		return dao.update(PREFIX + ".update", tbDataDictionary);
	}
	@Override
	public void updatetDish(Tdish dish) {
		 dao.update(PREFIX + ".updatetDish", dish);
		
	}

	@Override
	public void updatetDishUnit(TdishUnit dishUnit) {
		 dao.update(PREFIX + ".updatetDishUnit", dishUnit);
		
	}
	@Override
    public void insertDish(Tdish dish){
		dao.insert(PREFIX + ".insertDish", dish);
    }
	@Override
	public void insertDishUnit(TdishUnit dishUnit){
		dao.insert(PREFIX + ".insertDishUnit", dishUnit);
	}

	@Override
	public int delete(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dishTasteId", id);
		return dao.delete(PREFIX + ".deleteTasteId", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}
	
	@Override
	public List<Map<String, Object>> getDataDictionaryTag() {
		return dao.find(PREFIX+".getDataDictionaryTag");
	}

	@Override
	public List<Map<String, Object>> getTypeandTypename() {
		// TODO Auto-generated method stub
		return dao.find(PREFIX+".getTypeandTypename");
	}

	@Override
	public List<Map<String, Object>> getDatasByType(String type) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		return dao.find(PREFIX+".getDatasByType",params);
	}

	@Override
	public int delDishTasteDao(String dishTasteId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dishTasteId", dishTasteId);
		return dao.delete(PREFIX + ".deleteTasteId", params);
	}

	@Override
	public <T, K, V> List<T> getSystemList() {
		Map<String, Object> params = new HashMap<String, Object>();
		return dao.find(PREFIX + ".getAllSystem", params);
	}

	
	@Override
	public List<Map<String, Object>> getDicListByType(String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		return dao.find(PREFIX+".getDicListByType",params);
	}
	
	@Override
	public int updataDicItemDesc(Map<String,Object> paramMap) {
		return dao.update(PREFIX+".updataDicItemDesc",paramMap);
	}
	
	@Override
	public int updataCallTimeSet(Map<String,Object> paramMap) {
		return dao.update(PREFIX+".updataCallTimeSet",paramMap);
	}
	
	
	
}


