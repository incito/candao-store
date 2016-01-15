package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.model.Tdish;
@Repository
public class TdishDaoImpl implements TdishDao {

	@Autowired
    private DaoSupport dao;
	@Override
	public Tdish get(java.lang.String id) {
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
	public Tdish save(Tdish tdish) {
		int i=0;
		i=dao.insert(PREFIX + ".insert", tdish);
		return i>0?tdish:null;
	}

	@Override
	public Tdish update(Tdish tdish) {
		int i=0;
		i=dao.update(PREFIX + ".updateByPrimaryKey", tdish);
		return i>0?tdish:null;
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
	public int updateArticleStatus(int isselect, String[] articleids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isselect", isselect);
		params.put("articleids", articleids);
		return dao.update(PREFIX + ".updateArticleStatus", params);
	}
	
	@Override
	public int updateDishComsumer(List<String> dishids){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", dishids);
		return dao.update(PREFIX + ".updateDishComsumer", params);
	}
	
	@Override
	public int updateDishSetComsumer(List<String> dishids){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", dishids);
		return dao.update(PREFIX + ".updateDishSetComsumer", params);
	}

	@Override
	public List<Tdish> findAllByIds(List<String> dishids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", dishids);
		return dao.find(PREFIX + ".findAllByIds", params);
	}
	
	public int  updateDishComsumerReduce(List<String> dishids){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", dishids);
		return dao.update(PREFIX + ".updateDishComsumerReduce",params);
	}

	/**
	 * 多个单位退菜 点击数减 1 
	 * @author zhao
	 * @param dishids
	 */
	public int  updateDishSetComsumerReduce(List<String> dishids){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", dishids);
		return dao.update(PREFIX + ".updateDishSetComsumerReduce",params);
	}
	
	public List<Tdish> findAllDish(){
		return dao.find(PREFIX + ".findAllDish");
	}
	
	public List<Tdish> getAllDishSet(){
		return dao.find(PREFIX + ".getAllDishSet");
	}

	public int updateOrderNum(Tdish dish){
		return dao.update(PREFIX + ".updateOrderNum",dish);
	}
	
	@Override
	public <T, K, V> List<T> findD(Map<K, V> params) {
		return dao.find(PREFIX + ".findD", params);
	}

	@Override
	public <T, K, V> List<T> getDishListByType(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getDishListByType", params);
	}

	@Override
	public <T, K, V> List<T> getDishesByDishType(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getDishesByDishType", params);
	}

	@Override
	public <T, K, V> List<T> getdishCol(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getdishCol", params);
	}

	@Override
	public <K, V> int deletepa(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.update(PREFIX + ".deletepa",params);
	}

	@Override
	public <T, K, V> List<T> getDishMapByType(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".getDishMapByType", params);
	}


	@Override
	public List<Map<String, Object>> findDishes(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".findDishes",params);
	}

	@Override
	public Page<Map<String, Object>> pageDao(Map<String, Object> params,
			int page, int rows) {
		// TODO Auto-generated method stub
		return dao.page(PREFIX + ".pageSearch", params, page, rows);
	}

	@Override
	public List<Map<String, Object>> comfirmDelDish(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX + ".comfirmDelDish", map);
	}

	@Override
	public void updateDishNum(String orderId) {
		  dao.update(PREFIX + ".updateDishNum",orderId);
	}
	  
	

}
