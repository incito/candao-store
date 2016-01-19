package com.candao.www.permit.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.utils.IdentifierUtils;
import com.candao.www.data.dao.EmployeeUserDao;
import com.candao.www.data.dao.FunctionDao;
import com.candao.www.data.dao.RoleDao;
import com.candao.www.data.dao.UserRoleDao;
import com.candao.www.data.model.Application;
import com.candao.www.data.model.Function;
import com.candao.www.data.model.Role;
import com.candao.www.data.model.UserRole;
import com.candao.www.permit.common.BusinessException;
import com.candao.www.permit.common.ScopeDict;
import com.candao.www.permit.service.RoleService;
import com.candao.www.permit.vo.RoleVO;



@Service("roleServiceImpl1")
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private FunctionDao functionDao;
	
	@Autowired
	private EmployeeUserDao employeeUserDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	  
	@Transactional()
  public boolean insertRole(Role role,String brank_id) {
	  String name = role.getName();
	  if(name == null || name.trim().length() <= 0){
	    throw new BusinessException("新创建角色名称不能为空！");
	  }
	  //此处需要新建一个role，因为传过来的role有可能包含id,这样的话，就不好直接验证了。
	  Role role1 = new Role();
	  role1.setName(role.getName());
	  role1.setScopeCode(role.getScopeCode());
		role1.setBranch_id(brank_id);
	  List<Role> roles = roleDao.getRoletbrolebranchList(role1, brank_id);
	  if(roles != null && roles.size() > 0){
	    throw new BusinessException("系统内已经存在名为（" + name + "）的角色。");
	  }
	  role.setId( IdentifierUtils.getId().generate().toString().replaceAll("-", ""));
    int result = roleDao.insertRole(role);
    return result == 1;
  }


	public boolean updateRole(Role role) {
    int result = roleDao.updateRole(role);
    return result == 1;
  }

  /**
   * 删除角色及其功能权限
   * @param roleId
   */
  @Transactional()
  public void deleteRole(String roleId , String branch_id) {
	Role role= this.roleDao.get(roleId);
    roleDao.deleteRoleFunction(roleId); //同时删除角色功能
    roleDao.deleteRole(roleId);
    
    if(  !StringUtils.isBlank(branch_id) ){ //删除某个门店下的角色
    	this.roleDao.deleteRoleForCurrentStore(roleId, branch_id);
    }
    
  }
  
  /**
   * 分页查询角色
   * @param parameterObject
   * @param skipResults
   * @param maxResults
   * @return
   */
  @Transactional()
  public List<Role> getRoleListPage(Object parameterObject, Map pageInfo, boolean nextPage) {
    int pageSize = (Integer) pageInfo.get("pageSize");
    int start = (Integer) pageInfo.get("currentPage") * pageSize;
    //计算总页数，如果通过分页按钮查询不再重复获取
    if(!nextPage){
      int count = roleDao.countRoleList(parameterObject);
      pageInfo.put("count", count);
      int totalPage = count/pageSize;
      if(count%pageSize != 0){
        totalPage ++;
      }
      pageInfo.put("totalPage", totalPage);
    }
    return roleDao.getRoleListPage(parameterObject, start, pageSize);
  }
  
  /**
   * 不分页查询角色
   * @param parameterObject
   * @param skipResults
   * @param maxResults
   * @return
   */
  @Transactional()
  public List<Role> getRoleList(Object parameterObject) {
    return roleDao.getRoleList(parameterObject);
  }
  
  /**
   * 根据id列查询角色
   * @param userId
   * @return
   */
  public Role getRole(String roleId){
    if(roleId == null || roleId.trim().length() <= 0){
      throw new BusinessException("缺少查询角色id");
    }
    Role role = new Role();
    role.setId(roleId);
    List<Role> roles = roleDao.getRoleList(role);
    if(null!=roles && roles.size() > 0){
      return roles.get(0);
    }
    return null;
  }
  
  /**
   * 查询角色的功能权限
   * @param role
   * @param app
   * @return
   */
  public List<Function> getPermittedFnList(Role role, Application app){
    List<Function> functions = roleDao.getRoleFunction(role, app.getId());
    return functions;
  }
  
  /**
   * 以List形式查询角色的功能权限，带功能url
   * @param role
   * @param app
   * @return
   */
  @Transactional()
  public List<Function> getPermittedFnListWithUrl(Role role, Application app){
    List<Function> functions = roleDao.getRoleFunction(role, app.getId());
    Function fn = null;
    for(int i = 0; i < functions.size(); i++){
      fn = functions.get(i);
      List<String> urls = functionDao.getFunctionUrls(fn.getId());
      fn.setUrls(urls);
    }
    return functions;
  }
  
  /**
   * 以Map形式查询角色的功能权限，带功能url
   * @param role
   * @param app
   * @return
   */
  public Map getPermittedFnMapWithUrl(Role role, Application app){
    Map map = new HashMap();
    List<Function> functions = null;
    if(app != null){
      functions = roleDao.getRoleFunction(role, app.getId());
    } else {
      functions = roleDao.getRoleFunction(role, null);
    }
    
    Function fn = null;
    for(int i = 0; i < functions.size(); i++){
      fn = functions.get(i);
      List<String> urls = functionDao.getFunctionUrls(fn.getId());
      fn.setUrls(urls);
      map.put(fn.getId(), fn);
    }
    
    return map;
  }
  
  /**
   * 保存角色授权信息，先删除已经授予角色的功能，再批量添加
   * @param role
   * @param functions
   */
  @Transactional()
  public void saveRoleFunction(String role, List<Function> functions){
    if(role == null || role.trim().length() <= 0){
      return;
    }

    roleDao.deleteRoleFunction(role);
    //是否有新增的角色功能，否则清空角色功能权限
    if(functions != null && functions.size() > 0){
      roleDao.batchInsertRoleFunction(role, functions);
    }
  }

	@Override
	public boolean saveOrUpdateRoleVO(RoleVO roleVO , String branch_id) {
		boolean result=true;
		
		if( StringUtils.isBlank( roleVO.getRole().getId() ) ){  //增加
			//保存role
			Role role=roleVO.getRole();
			role.setId( UUID.randomUUID().toString());

			//role.setCreatetime( new Timestamp( DateUtils.currentDate().getTime()) );
			this.insertRole( role,branch_id);
			this.saveRoleFunction(role.getId(), roleVO.getFunctions());
			//保存 角色与当前门店的关联信息
			if( !StringUtils.isBlank(branch_id) ){
				Map<String , Object > storeMap= this.employeeUserDao.getBranchById(branch_id);
				int _id = Integer.parseInt( storeMap.get("branchid").toString()) ;//(int) storeMap.get("branchid");
				String _name = (String) storeMap.get("branchname");
				
				Map<String , Object > param=new HashMap();
				param.put("role_id", role.getId() );
				param.put("branch_id", String.valueOf(_id ) );
				param.put("branch_name", _name);
				
				this.roleDao.insertBranchRole(param);
			}
		}else{ //编辑
			Role role=roleVO.getRole();
			//获取数据库数据
			Role baseObj=this.getRole(role.getId() );
			
			//目前修改的只有名称
			baseObj.setName(role.getName());
			
			this.updateRole(baseObj);
			this.saveRoleFunction(role.getId(), roleVO.getFunctions());
		}
		return result;
	}

	@Override
	public RoleVO getRoleVOById(String id) {
		RoleVO vo=new RoleVO();
		Role role=this.getRole(id);
		if(null!=role){
			vo.setRole(role);
			vo.setFunctions( this.getPermittedFnList(role,  new Application()));
		}
		return vo;
	}


	@Override
	public List<Role> getRoleListByScope(ScopeDict scope) {
		List<Role> roleList = roleDao.getRoleListByScope(scope);
		return roleList;
	}
	
	/**
	 * 获取角色对象和对应的功能CODE列表
	 * @param scope
	 * @return
	 */
	public List<Role> getRoleFunctionCodeListByScope(ScopeDict scope) {
		List<Role> roleList = roleDao.getRoleListByScope(scope);
		
		Map<String,Object> queryFunMap = new HashMap<String,Object>();
		queryFunMap.put("scopeCode", scope.getValue());
		List<Map<String,Object>> funList = functionDao.getFunctionCodeList(queryFunMap); 
		for(Role role : roleList){
			List<Function> nFunList = new ArrayList<Function>();
			for(Map<String,Object> funMap : funList){
				if(role.getId().equals(funMap.get("id"))){
					Function f = new Function();
					f.setCode(String.valueOf(funMap.get("code")));
					nFunList.add(f);
				}
			}
			role.setFunctions(nFunList);
		}
		return roleList;
	}

	@Override
	public boolean queryWhether2DeletedRole(String id) {
		boolean result=true;
		int count=this.roleDao.getUserCountOfRole(id);
		if(count>0){
			result=false;
		}
		return result;
	}
	
	@Override
	public String  getRoleByName(Map<String,Object> params){
		 List<Map<String,Object>> RoleList = roleDao.getRoleByName(params);
		 String count=null;
		 for (int i = 0; i < RoleList.size(); i++) {
			 count =  RoleList.get(i).get("count").toString();
		 }
		return count;
	}
	@Override
	public List<Role> getRoleListForCurrentStore(String branch_id) {
		return this.roleDao.getRoleListForCurrentStore(branch_id);
	}

	@Override
	public List<RoleVO> getRoleList4User(String user_id) {
		Map< String ,Object > param = new HashMap();
		param.put("userId", user_id);
		List<UserRole> urs=this.userRoleDao.queryUserRoleList(param);
		
		List<RoleVO> roles=new ArrayList();
		
		for(UserRole ur:urs){
			RoleVO vo=new RoleVO();
			Role role=this.getRole( ur.getRoleId() );
			if(null!=role){
				vo.setRole(role);
				vo.setFunctions( this.getPermittedFnList(role,  new Application()));
			}
			roles.add(vo);
		}
		
		return roles;
		
	}

	@Override
	public boolean checkRoleName4Store(String roleName,String roleId, String branch_id) {
		boolean isExist=false;
		
		List<Role> roleList=this.roleDao.getRoleListForCurrentStore(branch_id);
		for( Role r:roleList){
			//如果存在roleId，则判断是否是编辑的时候，判断角色名称重复
			if( !StringUtils.isBlank(  roleId ) ){
				if( r.getName().trim().equalsIgnoreCase( roleName ) && !r.getId().equalsIgnoreCase(roleId)){
					isExist=true;
					break;
				}
			}else{
				if( r.getName().trim().equalsIgnoreCase( roleName ) ){
					isExist=true;
					break;
				}
			}
		}
		
		return isExist;
	}




	
}
