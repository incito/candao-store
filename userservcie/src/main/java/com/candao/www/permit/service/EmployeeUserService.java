package com.candao.www.permit.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.EmployeeUser;
import com.candao.www.data.model.User;

/**
 * 提供员工操作相关接口
 * @author YHL
 *
 */
public interface EmployeeUserService {


	/**
	 * 获取所有员工列表
	 * @return
	 */
	public List<EmployeeUser> getAll();
	
	/**
	 * 分页 
	 * <pre>
	 * 	注意：分页获取员工用户列表。参数 params中 必须包含一条
	 * 		key为 branch_id ，VALUE为 当前门店ID的记录。表示当前代表的门店。用作查询当前门店下的员工的关键参数。
	 * </pre>
	 * @param params
	 * @param current
	 * @param pagesize
	 * @return
	 */
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
	/**
	 * 保存员工信息
	 * @param employeeUser
	 * @return
	 */
	public int save(EmployeeUser employeeUser);
	
	/**
	 * 更新员工信息
	 * @param employeeUser
	 * @return
	 */
	public int update(EmployeeUser employeeUser);
	
	/**
	 * 
	 * @param user_id
	 * @param status
	 * @return
	 */
	public int updateUserStatus(String user_id,boolean status);
	/**
	  * 更新用户职务权限密码
	  * @param id
	  * @param oldPwd
	  * @param newPwd
	  * @return
	  */
	public boolean updatePaymentPassword(String id, String oldPwd, String newPwd);
	
	/**
	 * 根据ID，获取员工对象（包含基本信息）
	 * @param id
	 * @return
	 */
	public EmployeeUser get(String id);
	/**
	 * 获取用户,根据用户id
	 * @param id
	 * @return
	 */
	public EmployeeUser getUserById(String userId) ;
	
	/**
	 * 删除员工信息
	 * @param id
	 * @return
	 */
	public int delete(String id);
	
	/**
	 * 根据主键获取 门店信息(t_branch)。
	 * @param branch_id
	 * @return
	 */
	public Map<String ,Object > getBranchById(String branch_id);
	
	
	
	
	/**<pre>
	 * 获取当前门店下所有的服务员信息（此功能属于门店端）
	 * 
	 * 当前门店 取自 t_branch_info。所以必须保证此表有且只有一条数据。
	 * </pre>
	 * @param object
	 * @return
	 */
	public List<EmployeeUser> findAllServiceUserForCurrentStore();
	
	/**<pre>
	 * 检查指定门店下是否存在某个工号。
	 * 1.如果是新增，那么数据库不能存在该工号。
	 * 2.如果是修改，那么数据库不能存在 该账号之外的账号使用该工号。（门店端）
	 * </pre>
	 * @param jobNumber  要检查的工号
	 * @param userId  要排除的账号。
	 * 			如果不排除特定用户，可以传入null
	 * @param branch_id 门店id
	 * @return
	 */
	public boolean isExistJobNumberExcludeUser(String jobNumber,String userId,String branch_id);
	
	/**
	 * <pre>
	 * 检查指定门店下是否存在某个 员工账号。
	 * 1.如果是新增用户，那么数据库（该门店下）不可以存在该账号
	 * 2.如果是修改，那么该数据库不能存在该账号之外的人使用此账号。
	 * </pre>
	 * @param backAccount 后台登陆账号
	 * @param userId
	 * @param branch_id
	 * @return
	 */
	public boolean isExistBackAccountExcludeUser(String backAccount,String userId, String branch_id);

	public EmployeeUser getByParams(Map<String, Object> map);
	
	
}
