package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbUserRoleDao;
import com.candao.www.data.model.TbUserRole;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbUserRoleDaoImpl implements TbUserRoleDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbUserRole get(java.lang.String roleid, java.lang.String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		params.put("userid", userid);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String roleid, java.lang.String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		params.put("userid", userid);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public int insert(TbUserRole tbUserRole) {
		return dao.insert(PREFIX + ".insert", tbUserRole);
	}

	@Override
	public int update(TbUserRole tbUserRole) {
		return dao.update(PREFIX + ".update", tbUserRole);
	}

	@Override
	public int delete(java.lang.String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public int inserts(String userid, String[] roleids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		params.put("roleids", roleids);
		return dao.insert(PREFIX + ".inserts", params);
	}

	@Override
	public List<String> getUserRole(String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		return dao.find(PREFIX + ".getUserRole", params);
	}
}


