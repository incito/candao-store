package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbRoleDao;
import com.candao.www.data.model.TbRole;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbRoleDaoImpl implements TbRoleDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbRole get(java.lang.String roleid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String roleid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public int insert(TbRole tbRole) {
		return dao.insert(PREFIX + ".insert", tbRole);
	}

	@Override
	public int update(TbRole tbRole) {
		return dao.update(PREFIX + ".update", tbRole);
	}

	@Override
	public int delete(java.lang.String roleid,int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		params.put("status", status);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public List<Map<String, Object>> getRoleList() {
		// TODO Auto-generated method stub
		return dao.getSqlSessionTemplate().selectList(PREFIX + ".getRoleList", "");
	}
}


