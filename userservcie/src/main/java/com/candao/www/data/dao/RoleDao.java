package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.candao.www.data.model.Function;
import com.candao.www.data.model.Role;
import com.candao.www.permit.common.ScopeDict;




public interface RoleDao {
	public final static String PREFIX = RoleDao.class.getName();
	  
	int insertRole(Role role);
	int updateRole(Role role);
	void deleteRole(String id);
	//无查询条件不分页查询角色
	List<Role> getRoleList();
	//有查询条件不分页查询角色
	List<Role> getRoleList(@Param(value="role") Object role);
	//判断角色去重
	public List<Role> getRoletbrolebranchList(Object role,String branch_id);
	//无查询条件分页查询角色
	List<Role> getRoleListPage(@Param(value="start") int start, @Param(value="pageSize") int pageSize);
	//有查询条件分页查询角色
	List<Role> getRoleListPage(@Param(value="role") Object role, @Param(value="start") int start, @Param(value="pageSize") int pageSize);
	//查询角色的功能权限
	List<Function> getRoleFunction(@Param(value="role") Object role, @Param(value="application") String application);
	//统计角色数
	int countRoleList(@Param(value="role") Object role);
	//删除授予角色的功能
	void deleteRoleFunction(@Param(value="roleId") String roleId);
	//批量增加角色功能记录
	int batchInsertRoleFunction(@Param(value="roleId") String roleId, @Param(value="functions") List functions);
	//查询角色可以访问的url
	List<String> getPermitUrl(@Param(value="roleId") String roleId);
	
	/**
	 * 根据范围字典，获取对应范围中得权限列表
	 * @param scope
	 * @return
	 */
	public List<Role> getRoleListByScope(ScopeDict scope);
	
	/**
	 * 获取门店的角色权限列表。
	 * 角色 范围默认为：<code>ScopeDict.ALL_OF_STORE</code>
	 * @return
	 */
	public List<Role> getRoleListForCurrentStore(String branch_id);
	
	/**
	 * <pre>
	 * 获取默认门店信息。
	 * 注意：此方法仅仅适用于门店端。需要表 t_branch_info有且只有一条门店的数据。
	 * </pre>
	 * @return 如果没有符合数据，返回null
	 */
	public Map getCurrentStoreInfo();
	
	/**
	 * 插入一条 当前门店与权限角色关联记录。
	 * id 在数据库自动生成
	 * <pre>
	 * param的key-value如下
	 * 		KEY           描述
	 *		role_id        String ,角色ID
	 * 		branch_id      String ,门店ID
	 * 		branch_name    String ,门店名称
	 * </pre>
	 * @param param
	 * @return
	 */
	public int insertBranchRole(Map<String,Object> param);
	
	
	/**
	 * 删除当前门店与角色映射的记录
	 * @param roleId
	 * @return
	 */
	public int deleteRoleForCurrentStore(String roleId,String branch_id);
	
	/**
	 * 获取权限有几个用户使用。
	 * @param roleid
	 * @return
	 */
	public int getUserCountOfRole(String roleid);
	
	/**
	 * 根据角色的名字判断角色名称必须唯一
	 * @param name
	 * @return
	 */
	public  <T, K, V> List<T> getRoleByName(Map<K, V> params);
	/**
	 * 根据ID获取权限角色信息
	 * @param id
	 * @return
	 */
	public Role get(String id);
	
	/**
	 * 根据功能模块，获取拥有此功能模块的角色列表
	 * @param codes
	 * @return
	 */
	public List<Role> getRoleListByFunctionsCodes(List<String> codes);
	
}