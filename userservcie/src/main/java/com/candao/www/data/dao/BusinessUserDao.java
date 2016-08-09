package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.BusinessUser;

/**
 * 
 * 企业用户-Dao 接口
 * @author zhaozheng
 * @date 2015/04/22
 */
public interface BusinessUserDao {
	public final static String PREFIX = BusinessUserDao.class.getName();
	//获取一条用户，根据主键
	BusinessUser getUserById(String id);
	//查询企业用户列表，根据条件
	List<BusinessUser> queryUserList(Map<String,Object> userMap);
	//查询企业用户分页信息，根据条件
	<E, K, V> Page<E> queryUserPage(Map<String,Object> userMap, int currentPage, int pageSize);
	//添加一条企业用户记录
	int addUser(BusinessUser User);
	//修改一条企业用户记录
	int updateUser(BusinessUser User);
	//删除一条企业用户记录 ，根据主键
	int deleteUserById(String id);
	//删除企业用户记录 ，根据条件
	int deleteUser(Map<String,Object> userMap);
	//根据企业名称查询企业用户列表
  List<BusinessUser> queryBusinessUserbyName(String name);
  //查询最后一条企业用户帐户
  public List<String> findLastAccount();
}