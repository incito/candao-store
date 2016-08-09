package com.candao.www.permit.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.User;
import com.candao.www.data.model.UserBranch;

/**
 * 
 * 基本用户-Service接口
 * 
 * @author lishoukun
 * @date 2015/04/21
 */
public interface UserService {
	/**
	 * 通过账户（手机、邮箱）和密码登录
	 * @param loginName
	 * @param password
	 * @return
	 */
	public User login(String loginName, String password);
	
	/**
	 * 验证密码
	 * @param user
	 * @return
	 */
	public User validatePassword(String id,String password);
	
	/**
	 * 验证密码,根据帐号密码
	 * @param user
	 * @return
	 */
	public User validatePasswordByAccount(String account,String password);
	/**
	 * 验证密码及登录编号,根据帐号
	 * @param user
	 * @return
	 */
	public Map<String,Object> validatePasswordLoginTypeByAccount(String id,String password,String loginType);

	/**
	 * 获取授权给用户的url
	 * 
	 * @param user
	 * @return
	 */
	public HashSet<String> getAuthedUrls(User user);

	/**
	 * 更新用户密码
	 * 
	 * @param name
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public boolean updatePassword(String id, String oldPwd, String newPwd);
	/**
	   * 更新用户状态
	   * @param name
	   * @param oldPwd
	   * @param newPwd
	   * @return
	   */
	  public boolean updateStatus(String id, String status);

	/**
	 * 获取一条基本用户，根据主键
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(String id);
	
	public User getUserByjobNum(String id);
	/**
	 * 获取一条基本用户，根据条件
	 * 
	 * @param userMap
	 * @return
	 */
	public User getUser(Map<String, Object> userMap);

	/**
	 * 查询基本用户列表，根据条件
	 * 
	 * @param userMap
	 * @return
	 */
	public List<User> queryUserList(Map<String, Object> userMap);

	/**
	 * 查询基本用户 分页信息，根据条件
	 * 
	 * @param userMap
	 * @return
	 */
	public Page<Map<String, Object>> queryUserPage(Map<String, Object> userMap,
			Integer currentPage, Integer pageSize);
	/**
	 * 获取基本用户总数量，根据条件
	 * 
	 * @param userMap
	 * @return
	 */
	public Integer getUserTotal(Map<String, Object> userMap);

	/**
	 * 添加基本用户
	 * 
	 * @param user
	 * @return
	 */
	public User addUser(User user);

	/**
	 * 更新基本用户
	 * 
	 * @param user
	 * @return
	 */
	public User updateUser(User user);

	/**
	 * 删除基本用户,根据主键
	 * 
	 * @param id
	 * @return
	 */
	public Integer deleteUserById(String id);

	/**
	 * 批量删除基本用户,根据主键集合
	 * 
	 * @param ids
	 * @return
	 */
	public Integer deleteUsersByIds(String ids);

	/**
	 * 删除基本用户,根据条件
	 * 
	 * @param userMap
	 * @return
	 */
	public Integer deleteUser(Map<String, Object> userMap);
	/**
	 * 设置邮箱
	 * @param id
	 * @param email
	 * @return
	 */
	public boolean setEmail(String id,String email);
	/**
	 * 设置手机
	 * @param id
	 * @param mobile
	 * @return
	 */
	public boolean setMobile(String id,String mobile);
	/**
	 * 找回密码，根据邮箱
	 * @param email
	 * @param password
	 * @return
	 */
	public boolean retrievePwdByEmail(String email,String password);
	/**
	 * 找回密码，根据手机
	 * @param mobile
	 * @param password
	 * @return
	 */
	public boolean retrievePwdByMobile(String email,String password);
	/**
  	 * 计算用户安全分数,根据用户id
  	 * @param id 用户id
  	 */
	public Integer computeSafeScore(String id);
	
	
	//------------迁移用户管理部分代码----------------//TODO
	
	/**
	 * 更新用户登录时间
	 * @param userid
	 * @return
	 */
	public void updateLoginTime(String userid);
	

	/**
	 * 查询最大用户排序
	 * @author tom_zhao
	 * @return
	 */
	public User findMaxOrderNum();
	
	/**
	 * 更新用户排序
	 * @param account 用户账号
	 * @param ordernum 排序
	 */
	public void updateUserOrderNum(String account,int ordernum);
	
	/**
	 * 根据账号获取对应用户。
	 * @param account
	 * @return
	 */
	public User getUserByAccount(String account);

	/**
	 * 获取分店名称
	 * @author tom_zhao
	 * @return
	 */
	public User getCurrentBranchName();

	/**
	 * 获取租户名称
	 * @author tom_zhao
	 * @return
	 */
	public User getTenantName();
	/**
	   * 根据用户id获取关联门店信息
	   * @param userId
	   * @return
	   */
	  public List<UserBranch> queryUserBranchListByUserId(String userId);
}
