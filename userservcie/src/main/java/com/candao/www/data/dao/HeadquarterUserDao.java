package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.HeadquarterUser;

/**
 * 
 * 用户总店关联-Dao 接口
 * @author lishoukun
 * @date 2015/04/21
 */
public interface HeadquarterUserDao {
	public final static String PREFIX = HeadquarterUserDao.class.getName();
	/*
	 * 用户总店关联
	 */
	//获取一条用户总店关联，根据主键
	HeadquarterUser getHeadquarterUserById(String id);
	//获取一条用户总店关联，随机获取
	HeadquarterUser getHeadquarterUserByRand();
	//查询用户总店关联列表，根据条件
	List<HeadquarterUser> queryHeadquarterUserList(Map<String,Object> headquarterUserMap);
	//获取用户总店关联总数量，根据条件
	int getHeadquarterUserTotal(Map<String,Object> headquarterUserMap);
	//查询用户总店关联分页信息，根据条件
	<E, K, V> Page<E> queryHeadquarterUserPage(Map<String,Object> headquarterUserMap, int currentPage, int pageSize);
	//添加一条用户总店关联记录
	int addHeadquarterUser(HeadquarterUser headquarterUser);
	//修改一条用户总店关联记录
	int updateHeadquarterUser(HeadquarterUser headquarterUser);
	//删除一条用户总店关联记录 ，根据主键
	int deleteHeadquarterUserById(String id);
	//删除用户总店关联记录 ，根据条件
	int deleteHeadquarterUser(Map<String,Object> headquarterUserMap);
}
