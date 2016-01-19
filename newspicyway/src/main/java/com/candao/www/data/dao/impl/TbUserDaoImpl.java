package com.candao.www.data.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TbUserDao;
import com.candao.www.data.model.TbUser;

/**
 * 数据访问接口
 * @author mew
 *
 */
@Repository
public class TbUserDaoImpl  implements TbUserDao {
	@Autowired
	private DaoSupport dao;
	@Override
	public TbUser get(java.lang.String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public int insert(TbUser tbUser) {
		return dao.insert(PREFIX + ".insert", tbUser);
	}

	@Override
	public int update(TbUser tbUser) {
		return dao.update(PREFIX + ".update", tbUser);
	}

	@Override
	public int delete(java.lang.String userid,int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		params.put("status", status);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}
	
	@Override
	public  TbUser  getCurrentUser(String username, String userPassword) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("userPassword", userPassword);
		params.put("status", TbUser.STATUS_DELETE);
		return dao.get(PREFIX + ".getCurrentUser", params);
	}

	@Override
	public List<Map<String, Object>> getUserTat(int itemid) {
    	return dao.getSqlSessionTemplate().selectList(PREFIX + ".getUserTat", itemid);
	}

	@Override
	public List<String> getAllowAccessUrl(String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		return dao.find(PREFIX + ".getAllowAccessUrl", params);
	}

	@Override
	public List<String> getAllowAccessButton(String userid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		return dao.find(PREFIX + ".getAllowAccessButton", params);
	}
	
	@Override
	public TbUser findByuserName(java.lang.String userName){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", userName);
		return dao.get(PREFIX + ".findByName", params);
	}
	
	@Override
	public List<TbUser> findAllServiceUser(){
		return dao.find(PREFIX + ".findAllServiceUser", null);
	}
	
	@Override
	public int updateLoginTime(String userid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		return dao.update(PREFIX + ".updateLoginTime", params);
	}

	@Override
	public TbUser findMaxOrderNum() {
		return dao.get(PREFIX + ".findMaxOrderNum", null);
	}

	@Override
	public void updateUserOrderNum(String username,int ordernum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", username);
		params.put("ordernum", ordernum);
		dao.update(PREFIX + ".updateUserOrderNum", params);
	}
}


