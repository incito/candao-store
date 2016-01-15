package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.UserBranch;

/**
 * 
 * 用户门店关联-Dao 接口
 * @author lishoukun
 * @date 2015/04/21
 */
public interface UserBranchDao {
	public final static String PREFIX = UserBranchDao.class.getName();
	/*
	 * 用户门店关联
	 */
	//获取一条用户门店关联，根据主键
	UserBranch getUserBranchById(String id);
	//获取一条用户门店关联，随机获取
	UserBranch getUserBranchByRand();
	//查询用户门店关联列表，根据条件
	List<UserBranch> queryUserBranchList(Map<String,Object> userBranchMap);
	//获取用户门店关联总数量，根据条件
	int getUserBranchTotal(Map<String,Object> userBranchMap);
	//查询用户门店关联分页信息，根据条件
	<E, K, V> Page<E> queryUserBranchPage(Map<String,Object> userBranchMap, int currentPage, int pageSize);
	//添加一条用户门店关联记录
	int addUserBranch(UserBranch userBranch);
	//修改一条用户门店关联记录
	int updateUserBranch(UserBranch userBranch);
	//删除一条用户门店关联记录 ，根据主键
	int deleteUserBranchById(String id);
	//删除用户门店关联记录 ，根据条件
	int deleteUserBranch(Map<String,Object> userBranchMap);
}
