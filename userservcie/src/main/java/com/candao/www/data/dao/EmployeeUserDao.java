package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.EmployeeUser;
import com.candao.www.data.model.Role;

/**
 * 员工
 * @author YHL
 *
 */
public interface EmployeeUserDao {
	public final static String PREFIX = EmployeeUserDao.class.getName();
	
	/**
	 * 获取所有员工列表
	 * @return
	 */
	public List<EmployeeUser> getAll();
	
	/**
	 * 分页
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
	 * 更新员工账号状态
	 * @param user_id
	 * @param status
	 * 				参见 Constants中变量
	 * @return
	 */
	public int updateUserStatus(String user_id,String status);
	
	/**
	 * 根据ID，获取员工对象（包含基本信息）
	 * @param id
	 * @return
	 */
	public EmployeeUser get(String id);
	/**
	 * 获取员工用户对象列表，根据条件
	 * @param param
	 * @return
	 */
	public List<EmployeeUser> get(Map<String,Object> param);
	
	/**
	 * 删除员工信息
	 * @param id
	 * @return
	 */
	public int delete(String id);
	
	/**
	 * 根据门店ID，获取门店信息。
	 * @param branch_id
	 * @return
	 */
	public Map<String ,Object > getBranchById(String branch_id);
	
	/**
	 * 获取 指定门店下的拥有特定角色权限的用户列表。
	 * <br/>注意：在调用过程中，门店id应当为一个有效的值
	 * @param roles  角色数组
	 * @param branchId 门店id
	 * @return
	 */
	public List<EmployeeUser> getEmployeeUserByRoles4Store(List<Role> roles,String branchId);
	
	/**
	 * 根据门店ID，获取门店下地所有得员工列表信息
	 * @param branchId
	 * @return
	 */
	public List<EmployeeUser> getEmployeeUser4Store(String branchId);

	public EmployeeUser getByparams(Map<String, Object> map);
}
