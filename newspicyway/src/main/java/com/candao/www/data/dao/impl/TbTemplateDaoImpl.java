package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbTemplateDao;
import com.candao.www.data.model.Ttemplate;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbTemplateDaoImpl implements TbTemplateDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public Ttemplate get(java.lang.String id) {
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
	public int insert(Ttemplate tbTemplate) {
		return dao.insert(PREFIX + ".insert", tbTemplate);
	}

	@Override
	public int update(Ttemplate tbTemplate) {
		return dao.update(PREFIX + ".update", tbTemplate);
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
	public List<Ttemplate> validateTemplate(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX+".validateTemplate",params);
	}

	@Override
	public int updateTemplates(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		return dao.update(PREFIX + ".updateTemplates", list);
	}

	@Override
	public int addTtemplate(List<Ttemplate> list) {
		// TODO Auto-generated method stub
		return dao.insert(PREFIX+".insertOnce", list);
	}

	@Override
	public int delTtemplate(String menuid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", menuid);
		return dao.delete(PREFIX+".deleteTemplateList", params);
	}

	@Override
	public <E, K, V> List<E> getTtemplateBymenuId(Map<K, V> params) {
		// TODO Auto-generated method stub
		return dao.find(PREFIX+".findTempalteList", params);
	}

	@Override
	public <E, K, V> List<E> getTtemplateBymenuIdPad(Map<K, V> params) {
		// TODO Auto-generated method stub
		 return dao.find(PREFIX+".getTtemplateBymenuIdPad", params);
	}
}


