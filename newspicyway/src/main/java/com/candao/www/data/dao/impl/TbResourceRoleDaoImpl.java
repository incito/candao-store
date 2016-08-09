package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbResourceRoleDao;
import com.candao.www.data.model.TbResourceRole;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbResourceRoleDaoImpl implements TbResourceRoleDao {
    @Autowired
    private DaoSupport dao;
	@Override
	public TbResourceRole get(java.lang.String roleid, java.lang.String resourceid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		params.put("zhiyuanid", resourceid);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String roleid, java.lang.String resourceid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		params.put("zhiyuanid", resourceid);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public int insert(TbResourceRole tbResourceRole) {
		return dao.insert(PREFIX + ".insert", tbResourceRole);
	}

	@Override
	public int update(TbResourceRole tbResourceRole) {
		return dao.update(PREFIX + ".update", tbResourceRole);
	}

	@Override
	public int delete(java.lang.String roleid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public int inserts(String roleid, String[] resourceids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		params.put("resourceids", resourceids);
		return dao.insert(PREFIX + ".inserts", params);
	}

	@Override
	public List<String> getRoleResource(String roleid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleid", roleid);
		return dao.find(PREFIX + ".getRoleResource", params);
	}
}


