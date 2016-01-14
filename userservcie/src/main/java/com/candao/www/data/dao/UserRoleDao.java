package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.UserRole;

/**
 * 
 * 用户角色关联-Dao 接口
 * @author lishoukun
 * @date 2015/04/21
 */
public interface UserRoleDao {
	public final static String PREFIX = UserRoleDao.class.getName();
	/*
	 * 用户角色关联
	 */
	//获取一条用户角色关联，根据主键
	UserRole getUserRoleById(String id);
	//获取一条用户角色关联，随机获取
	UserRole getUserRoleByRand();
	//查询用户角色关联列表，根据条件
	List<UserRole> queryUserRoleList(Map<String,Object> userRoleMap);
	//获取用户角色关联总数量，根据条件
	int getUserRoleTotal(Map<String,Object> userRoleMap);
	//查询用户角色关联分页信息，根据条件
	<E, K, V> Page<E> queryUserRolePage(Map<String,Object> userRoleMap, int currentPage, int pageSize);
	//添加一条用户角色关联记录
	int addUserRole(UserRole userRole);
	//修改一条用户角色关联记录
	int updateUserRole(UserRole userRole);
	//删除一条用户角色关联记录 ，根据主键
	int deleteUserRoleById(String id);
	//删除用户角色关联记录 ，根据条件
	int deleteUserRole(Map<String,Object> userRoleMap);
}
