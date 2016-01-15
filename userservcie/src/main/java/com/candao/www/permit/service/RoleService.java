package com.candao.www.permit.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.Application;
import com.candao.www.data.model.Function;
import com.candao.www.data.model.Role;
import com.candao.www.permit.common.ScopeDict;
import com.candao.www.permit.vo.RoleVO;



public interface RoleService {
  public boolean insertRole(Role role,String brank_id);

  public boolean updateRole(Role role);

  /**
   * 删除角色及其功能权限
   * @param roleId   角色ID
   * @param branch_id 门店ID
   * 	<p>
   * 		总店删除role的时候，没有对应的门店信息。可以传入NULL
   * 	</p>
   */
  public void deleteRole(String roleId , String branch_id);
  
  /**
   * 分页查询角色
   * @param parameterObject
   * @param skipResults
   * @param maxResults
   * @return
   */
  public List<Role> getRoleListPage(Object parameterObject, Map pageInfo, boolean nextPage);
  
  /**
   * 不分页查询角色
   * @param parameterObject
   * @param skipResults
   * @param maxResults
   * @return
   */
  public List<Role> getRoleList(Object parameterObject);
  
  /**
   * 根据id列查询角色
   * @param userId
   * @return
   */
  public Role getRole(String roleId);
  /**
	 * 根据角色的名字判断角色名称必须唯一
	 * @param name
	 * @return
	 */
  public String  getRoleByName(Map<String,Object> params);
  /**
   * 查询角色的功能权限
   * @param role
   * @param app
   * @return
   */
  public List<Function> getPermittedFnList(Role role, Application app);
  
  /**
   * 以List形式查询角色的功能权限，带功能url
   * @param role
   * @param app
   * @return
   */
  public List<Function> getPermittedFnListWithUrl(Role role, Application app);
  
  /**
   * 以Map形式查询角色的功能权限，带功能url
   * @param role
   * @param app
   * @return
   */
  public Map getPermittedFnMapWithUrl(Role role, Application app);
  
  /**
   * 保存角色授权信息，先删除已经授予角色的功能，再批量添加
   * @param role
   * @param functions
   */
  public void saveRoleFunction(String role, List<Function> functions);
  
  /**
   * 根据权限角色ID，获取权限角色对象以及对应的权限包含的功能模块。
   * @param id
   * @return
   */
  public RoleVO getRoleVOById(String id);
  
  /**
   * 此方法 用于添加/更新权限角色 VO对象。
   * 	<pre>
   * 		当添加的时候，调用 <code>saveRoleFunction</code>,
   * 		当更新的时候，更新role对象，并重新更新 对应的function
   * 	</pre>
   * @param roleVO
   * @param branch_id  要关联的门店ID。
   * 		NULL -- 保存角色不关联 门店。
   * @return
   */
  public boolean saveOrUpdateRoleVO(RoleVO roleVO ,String branch_id) ;
  
  /**
   * 根据范围字典获取 对应范围内的角色权限列表
   * @param scope
   * @return
   */
  public List<Role> getRoleListByScope(ScopeDict scope);
  /**
   * 获取角色对象和对应的功能CODE列表
   * @param scope
   * @return
   */
  public List<Role> getRoleFunctionCodeListByScope(ScopeDict scope);
  
  /**
   * 判断是否可以删除角色信息。
   * （判断角色是否有用户在使用）
   * @param id
   * @return
   */
  public boolean queryWhether2DeletedRole(String id);
  
  /**
   *
   * 获取当前门店权限角色列表
   * @return
   */
  public List<Role> getRoleListForCurrentStore(String branch_id);
  
  /**
   * 根据用户ID，获取用户的权限角色。
   * @param user_id
   * @return
   */
  public List<RoleVO> getRoleList4User(String user_id);
  
  /**
   * 检查 指定门店中是否存在某个角色名称
   * @param roleName  角色名称
   * @param roleId    角色ID（如果存在主键，则检查的时候，排除此角色。如果不存在主键，传入空/NULL等，认为是新增角色的时候检查名称）
   * @param branch_id
   * @return
   * 	boolean -true ：存在；
   * 	boolean -false: 不存在
   */
  public boolean checkRoleName4Store(String roleName ,String roleId, String branch_id);

}
