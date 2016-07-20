package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.User;

/**
 * 
 * 基本用户-Dao 接口
 * @author lishoukun
 * @date 2015/04/21
 */
public interface UserDao {
	public final static String PREFIX = UserDao.class.getName();
	//获取一条基本用户，根据主键
	User getUserById(String id);
	//获取一条基本用户，随机获取
	User getUserByRand();
	//查询基本用户列表，根据条件
	List<User> queryUserList(Map<String,Object> userMap);
	//查询基本用户分页信息，根据条件
	<E, K, V> Page<E> queryUserPage(Map<String,Object> userMap, int currentPage, int pageSize);
	//获取基本用户总数量，根据条件
	int getUserTotal(Map<String,Object> userMap);
	//添加一条基本用户记录
	int addUser(User User);
	//修改一条基本用户记录
	int updateUser(User User);
	//删除一条基本用户记录 ，根据主键
	int deleteUserById(String id);
	//删除基本用户记录 ，根据条件
	int deleteUser(Map<String,Object> userMap);
	/**
	 * 查询重复的用户。查询条件账号、邮箱和手机号
	 * @param userMap
	 * @return
	 */
	List<User> getDuplicateUser(Map<String,Object> userMap);
	
	/**
	 * 更新用户密码
	 * @param account
	 * @param password
	 * @return
	 */
	int updatePassword(String account, String password);
	
	/**
   * 通过账户（手机、邮箱）和密码登录
   * @param loginName
   * @param password
   * @return
   */
	public User login(String loginName, String password);
	
	/**
	 * 根据用户ID，更新用户最后登录时间
	 * @param userId
	 * @return
	 */
	public int updateLoginTime(String userId);
	
	/**
	 * <pre>
	 * 获取用户表中，最大得排序。
	 * <b>注意：这个对象 user 中仅仅有一个 ordernum是有效的。其他字段没有内容。
	 * （因为接口遗留问题，目前只能这么实现。）
	 * </b>
	 * </pre>
	 * @return
	 */
	public User findMaxOrderNum();
	
	/**
	 * 更新用户排序
	 * @param account 用户账号
	 * @param ordernum 排序
	 */
	public void updateUserOrderNum(String account,int ordernum);
	
	User getCurrentBranchName();
	
	User getTenantName();
	
	public User getUserByjobNum(String id);
	
	public String getNameByUserNumber(String jobNumber,String branchId);
	
	
}