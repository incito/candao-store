package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.UserRoleDao;
import com.candao.www.data.model.UserRole;

/**
 * 
 * 用户角色关联-Dao 实现类
 * @author lishoukun
 * @date 2015/04/21
 */
@Repository
public class UserRoleDaoImpl implements UserRoleDao{
	@Autowired
	private DaoSupport dao;
	/*
	 * 用户角色关联
	 */
	//获取一条用户角色关联，根据主键
	public UserRole getUserRoleById(String id){
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return dao.get(PREFIX + ".getUserRoleById", params);
	}
	//获取一条用户角色关联，随机获取
	public UserRole getUserRoleByRand(){
		return dao.get(PREFIX + ".getUserRoleByRand",new HashMap<String, Object>());
	}
	//查询用户角色关联列表，根据条件
	public List<UserRole> queryUserRoleList(Map<String,Object> userRoleMap){
		return dao.find(PREFIX + ".queryUserRoleList", userRoleMap);
	}
	//获取用户角色关联总数量，根据条件
	public int getUserRoleTotal(Map<String,Object> userRoleMap){
		return dao.get(PREFIX + ".getUserRoleTotal", userRoleMap);
	}
	//查询用户角色关联分页信息，根据条件
	public <E, K, V> Page<E> queryUserRolePage(Map<String,Object> userRoleMap, int currentPage, int pageSize){
		return dao.page(PREFIX + ".queryUserRoleList", userRoleMap, currentPage, pageSize);
	}
	//添加一条用户角色关联记录
	public int addUserRole(UserRole userRole){
		return dao.insert(PREFIX + ".addUserRole", userRole);
	}
	//修改一条用户角色关联记录
	public int updateUserRole(UserRole userRole){
		return dao.update(PREFIX + ".updateUserRole", userRole);
	}
	//删除一条用户角色关联记录 ，根据主键
	public int deleteUserRoleById(String id){
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return dao.delete(PREFIX + ".deleteUserRoleById", params);
	}
	//删除用户角色关联记录 ，根据条件
	public int deleteUserRole(Map<String,Object> userRoleMap){
		return dao.delete(PREFIX + ".deleteUserRole", userRoleMap);
	}
}
