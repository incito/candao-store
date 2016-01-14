package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.BusinessUserDao;
import com.candao.www.data.model.BusinessUser;
import com.candao.www.permit.common.Constants;

/**
 * 
 * 基本用户-Dao 实现类
 * @author lishoukun
 * @date 2015/04/21
 */
@Repository
public class BusinessUserDaoImpl implements BusinessUserDao{
	@Autowired
	private DaoSupport dao;
	//获取一条用户，根据主键
	public BusinessUser getUserById(String id){
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return dao.get(PREFIX + ".getUserById", params);
	}
	//查询用户列表，根据条件
	public List<BusinessUser> queryUserList(Map<String,Object> userMap){
		return dao.find(PREFIX + ".queryUserList", userMap);
	}
	//查询用户分页信息，根据条件
	public <E, K, V> Page<E> queryUserPage(Map<String,Object> userMap, int currentPage, int pageSize){
		return dao.page(PREFIX + ".queryUserList", userMap, currentPage, pageSize);
	}
	//添加一条用户记录
	public int addUser(BusinessUser user){
		return dao.insert(PREFIX + ".addUser", user);
	}
	//修改一条用户记录
	public int updateUser(BusinessUser user){
		return dao.update(PREFIX + ".updateUser", user);
	}
	//删除一条用户记录 ，根据主键
	public int deleteUserById(String id){
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.delete(PREFIX + ".deleteUserById", params);
	}
	//删除用户记录 ，根据条件
	public int deleteUser(Map<String,Object> userMap){
		return dao.delete(PREFIX + ".deleteUserById", userMap);
	}
  @Override
  public List<BusinessUser> queryBusinessUserbyName(String name) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("businessName", name);
    return dao.find(PREFIX + ".queryUserList", params);
  }
  
  @Override
  public List<String> findLastAccount() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userType", Constants.BUSINESS);
    return dao.find(PREFIX + ".findLastAccount", params);
  }
}
