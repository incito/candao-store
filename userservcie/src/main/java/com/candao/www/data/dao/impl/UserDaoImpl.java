package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.UserDao;
import com.candao.www.data.model.User;

/**
 * 
 * 基本用户-Dao 实现类
 * @author lishoukun
 * @date 2015/04/21
 */
@Repository
public class UserDaoImpl implements UserDao{
	@Autowired
	private DaoSupport dao;
	//获取一条基本用户，根据主键
	public User getUserById(String id){
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return dao.get(PREFIX + ".getUserById", params);
	}
	//获取一条基本用户，随机获取
	public User getUserByRand(){
		return dao.get(PREFIX + ".getUserByRand",new HashMap<String, Object>());
	}
	//查询基本用户列表，根据条件
	public List<User> queryUserList(Map<String,Object> userMap){
		return dao.find(PREFIX + ".queryUserList", userMap);
	}
	//查询基本用户分页信息，根据条件
	public <E, K, V> Page<E> queryUserPage(Map<String,Object> userMap, int currentPage, int pageSize){
		return dao.page(PREFIX + ".queryUserList", userMap, currentPage, pageSize);
	}
	//获取基本用户总数量，根据条件
	public int getUserTotal(Map<String,Object> userMap){
		return dao.get(PREFIX + ".getUserTotal", userMap);
	}
	//添加一条基本用户记录
	public int addUser(User user){
		return dao.insert(PREFIX + ".addUser", user);
	}
	//修改一条基本用户记录
	public int updateUser(User user){
		return dao.update(PREFIX + ".updateUser", user);
	}
	//删除一条基本用户记录 ，根据主键
	public int deleteUserById(String id){
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return dao.delete(PREFIX + ".deleteUserById", params);
	}
	//删除基本用户记录 ，根据条件
	public int deleteUser(Map<String,Object> userMap){
		return dao.delete(PREFIX + ".deleteUserById", userMap);
	}
  @Override
  public List<User> getDuplicateUser(Map<String, Object> userMap) {
    return dao.find(PREFIX + ".getDuplicateUser", userMap);
  }
  @Override
  public int updatePassword(String account, String password) {
    Map<String, String> userMap = new HashMap<String, String>();
    userMap.put("account", account);
    userMap.put("password", password);
    return dao.update(PREFIX + ".updatePassword", userMap);
  }
  @Override
  public User login(String loginName, String password) {
    Map<String, String> params = new HashMap<String, String>();
    params.put("loginName", loginName);
    params.put("password", password);
    return dao.get(PREFIX + ".login", params);
  }
	@Override
	public int updateLoginTime(String userId) {
		Map<String, String> params = new HashMap<String, String>();
	    params.put("id", userId);
		return this.dao.update(PREFIX+".updateLoginTime",params );
	}
	@Override
	public User findMaxOrderNum() {
		return this.dao.get(PREFIX+".findMaxOrderNum",null);
	}
	@Override
	public void updateUserOrderNum(String account, int ordernum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("order_num", ordernum);
		dao.update(PREFIX + ".updateUserOrderNum", params);
	}
	@Override
	public User getCurrentBranchName() {
	     
	    return dao.get(PREFIX + ".getCurrentBranchName", null);
	}
	@Override
	public User getTenantName() {
		 return dao.get(PREFIX + ".getTenantName", null);
	}
	@Override
	public User getUserByjobNum(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("jobnum", id);
		return dao.get(PREFIX + ".getUserByjobNum", params);
	}
}
