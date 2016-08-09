package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.UserBranchDao;
import com.candao.www.data.model.UserBranch;

/**
 * 
 * 用户门店关联-Dao 实现类
 * @author lishoukun
 * @date 2015/04/21
 */
@Repository
public class UserBranchDaoImpl implements UserBranchDao{
	@Autowired
	private DaoSupport dao;
	/*
	 * 用户门店关联
	 */
	//获取一条用户门店关联，根据主键
	public UserBranch getUserBranchById(String id){
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return dao.get(PREFIX + ".getUserBranchById", params);
	}
	//获取一条用户门店关联，随机获取
	public UserBranch getUserBranchByRand(){
		return dao.get(PREFIX + ".getUserBranchByRand",new HashMap<String, Object>());
	}
	//查询用户门店关联列表，根据条件
	public List<UserBranch> queryUserBranchList(Map<String,Object> userBranchMap){
		return dao.find(PREFIX + ".queryUserBranchList", userBranchMap);
	}
	//获取用户门店关联总数量，根据条件
	public int getUserBranchTotal(Map<String,Object> userBranchMap){
		return dao.get(PREFIX + ".getUserBranchTotal", userBranchMap);
	}
	//查询用户门店关联分页信息，根据条件
	public <E, K, V> Page<E> queryUserBranchPage(Map<String,Object> userBranchMap, int currentPage, int pageSize){
		return dao.page(PREFIX + ".queryUserBranchList", userBranchMap, currentPage, pageSize);
	}
	//添加一条用户门店关联记录
	public int addUserBranch(UserBranch userBranch){
		return dao.insert(PREFIX + ".addUserBranch", userBranch);
	}
	//修改一条用户门店关联记录
	public int updateUserBranch(UserBranch userBranch){
		return dao.update(PREFIX + ".updateUserBranch", userBranch);
	}
	//删除一条用户门店关联记录 ，根据主键
	public int deleteUserBranchById(String id){
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id", id);
	    return dao.delete(PREFIX + ".deleteUserBranchById", params);
	}
	//删除用户门店关联记录 ，根据条件
	public int deleteUserBranch(Map<String,Object> userBranchMap){
		return dao.delete(PREFIX + ".deleteUserBranch", userBranchMap);
	}
}
