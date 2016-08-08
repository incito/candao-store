package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbasicDataDao;
import com.candao.www.data.model.TbasicData;
@Repository
public class TbasicDataDaoImpl implements TbasicDataDao {
	 @Autowired
	    private DaoSupport dao;
		@Override
		public TbasicData get(java.lang.String id) {
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
		public <T, K, V> List<T> findCategory(Map<K, V> params) {
			return dao.find(PREFIX + ".findCategory", params);
		}

		@Override
		public int insert(TbasicData tbasicData) {
			return dao.insert(PREFIX + ".insert", tbasicData);
		}

		@Override
		public int update(TbasicData tbasicData) {
			return dao.update(PREFIX + ".updateByPrimaryKeySelective", tbasicData);
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
		public List<Map<String, Object>> getDataDictionaryTag(String itemtype) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("itemtype", itemtype);
			return dao.find(PREFIX+".getDataDictionaryTag",params);
		}

		@Override
		public List<Map<String, Object>> findAll(Map<String, Object> params) {
			return dao.find(PREFIX + ".findAll", params);
		}
		
		@Override
		public List<Map<String, Object>> findTempByItemId(Map<String, Object> params) {
			return dao.find(PREFIX + ".findTempByItemId", params);
		}

		@Override
		public <T, K, V> List<T> getListByparams(Map<K, V> params) {
			// TODO Auto-generated method stub
			return dao.find(PREFIX + ".getListByparams", params);
		}

		@Override
		public int updateListOrder(List<TbasicData> tdus) {
			// TODO Auto-generated method stub
			return dao.update(PREFIX + ".updateListOrder", tdus);
		}

		@Override
		public <T, K, V> List<T> getMenuColumn(Map<K, V> params) {
			// TODO Auto-generated method stub
			return dao.find(PREFIX + ".getMenuColumn", params);
		}

		@Override
		public int updateDishTagListOrder(List<Map<String, Object>> dishTag) {
			// TODO Auto-generated method stub
			return dao.update(PREFIX + ".updateDishTagListOrder", dishTag);
		}


		

}
