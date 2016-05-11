/**
 * 
 */
package com.candao.www.permit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.FunctionDao;
import com.candao.www.data.dao.RoleDao;
import com.candao.www.data.dao.UserRoleDao;
import com.candao.www.data.model.Application;
import com.candao.www.data.model.Function;
import com.candao.www.data.model.Role;
import com.candao.www.data.model.UserRole;
import com.candao.www.permit.common.ScopeDict;
import com.candao.www.permit.service.FunctionService;
import com.candao.www.permit.vo.RoleVO;



/**
 * @author zhao
 *
 */
@Service
public class FunctionServiceImpl implements FunctionService {
  
  @Autowired
  FunctionDao functionDao;
  
  @Autowired
  private RoleDao roleDao;
  
  @Autowired
  private UserRoleDao userRoleDao;

  /**
   * 查询功能列表，同时查询功能对应的url
   * @param parameterObject
   * @return
   */
  @Transactional()
  public List<Function> getFunctionListWithUrl(Map<String, Object> params){
    List<Function> functions = getFunctionList(params);
    Function fn = null;
    for(int i = 0; i < functions.size(); i++){
      fn = functions.get(i);
      List<String> urls = functionDao.getFunctionUrls(fn.getId());
      fn.setUrls(urls);
    }
    return functions;
  }
  
  /**
   * 查询功能列表
   * @param parameterObject
   * @return
   */
  public List<Function> getFunctionList(Map<String, Object> params){
    List<Function> functions = functionDao.getFunctionList(params);
    return functions;
  }
  
  /**
   * 查询指定层级的功能
   * @param level
   * @return
   */
  public List<Function> getFunctionsByLevel(int level){
    Map params = new HashMap();
    params.put("level", level);
    return functionDao.getFunctionList(params);
  }
  
  /**
   * 查询指定层级的功能，同时查询功能对应的url
   * @param level
   * @return
   */
  public List<Function> getFunctionsWithUrlByLevel(int level){
    Map params = new HashMap();
    params.put("level", level);
    return getFunctionListWithUrl(params);
  }
  
  /**
   * 查询子功能，同时查询功能对应的url
   * @param fn
   * @return
   */
  public List<Function> getChildFunction(Function fn){
    return functionDao.getChildFunction(fn.getId());
  }
  
  /**
   * 查询子功能
   * @param fn
   * @return
   */
  @Transactional()
  public List<Function> getChildFunctionWithUrl(Function fn){
    List<Function> functions = functionDao.getChildFunction(fn.getId());
    Function fnTemp = null;
    for(int i = 0; i < functions.size(); i++){
      fnTemp = functions.get(i);
      List<String> urls = functionDao.getFunctionUrls(fnTemp.getId());
      fnTemp.setUrls(urls);
    }
    return functions; 
  }
  
  /**
   * 根据上下级关系获取功能
   * @return
   */
  @Transactional()
  public List<Function> getAllStructuredFunction(){
    List<Function> function = getFunctionsWithUrlByLevel(1);
    for(int i = 0; i < function.size(); i++){
      recursiveChildFunction(function.get(i));
    }
    return function;
  }
  
  /**
   * 递归查询子功能
   * @param fn
   * @return
   */
  private List<Function> recursiveChildFunction(Function fn){
    List<Function> function = getChildFunction(fn);
    fn.setChildFuns(function);
    if(function == null){
      return function;
    }
    for(int i = 0; i < function.size(); i++){
      recursiveChildFunction(function.get(i));
    }
    return function;
  }
  
  /**
   * 根据上下级关系获取功能，同时取url
   * @return
   */
  @Transactional()
  public List<Function> getAllStructuredFunctionWithUrl(){
    List<Function> function = getFunctionsWithUrlByLevel(1);
    Function fn = null;
    for(int i = 0; i < function.size(); i++){
      fn = function.get(i);
      List<String> urls = functionDao.getFunctionUrls(fn.getId());
      fn.setUrls(urls);
      recursiveChildFunction(fn);
    }
    return function;
  }
  
  /**
   * 递归查询子功能，同时取url
   * @param fn
   * @return
   */
  private List<Function> recursiveChildFunctionWithUrl(Function fn){
    List<Function> function = getChildFunctionWithUrl(fn);
    fn.setChildFuns(function);
    if(function == null){
      return function;
    }
    for(int i = 0; i < function.size(); i++){
      recursiveChildFunctionWithUrl(function.get(i));
    }
    return function;
  }

	@Override
	public List<Function> getTopLevelFunctionsByScopeCode(ScopeDict scope) {
		Map params=new HashMap();
		params.put("scopeCode", scope.getValue());
		params.put("level", 1);//第一个级别的。
		List<Function> topFunctions= this.functionDao.getFunctionList(params);
		//遍历，获取功能子列表
		for(int i=0;i<topFunctions.size();i++){
			Function f=topFunctions.get(i);
			f.setChildFuns( this.recursiveChildFunction(f));
			topFunctions.set(i, f);
		}
		
		return topFunctions;
	}
	@Override
	public Function getFunctionById(String id) {
		return this.functionDao.getFunctionById(id);
	}

	@Override
	public List<Function> getMenuFunction4User(String user_id) {
		
	      List<Function> menuList=new ArrayList();
	      
	      Map< String ,Object > param = new HashMap();
		  param.put("userId", user_id);
		  List<UserRole> urs=this.userRoleDao.queryUserRoleList(param);
		  List<Function> allFn=new ArrayList(0);
		  for(UserRole ur:urs){ 
				Role role= roleDao.get( ur.getRoleId() );
				List<Function> fs=this.roleDao.getRoleFunction(role, null );
				//遍历模块，并获取模块对应的url
				for( int i=0;i<fs.size();i++){
					Function f= fs.get(i);
					List<String> urls = functionDao.getFunctionUrls(f.getId());
				    f.setUrls(urls);
				    fs.set(i, f);
				}
				
				allFn.addAll(fs);
		  }
		  
		  menuList.addAll(allFn);
	      List<Function> tmpFunctions=null;
	      for(Function f:allFn){
//	    	  if( !StringUtils.isBlank( f.getParentFun() )){
//	    			  Function pf=this.functionDao.getFunctionById(  f.getParentFun() );
//	    			  menuList.add(pf);
//	    	  } 
	    	  
	    	  menuList.addAll( getAllParentsOfFunction(f));
	      }
	      
	      return menuList;
	}
	
	/**
	 * 获取模块节点的所有父节点，并且放在一个list里面返回。
	 * @param f
	 * @return
	 */
	private List<Function> getAllParentsOfFunction(Function f){
		
		List<Function> fns=new ArrayList(0);
		if( null!=f){
			if( !StringUtils.isBlank( f.getParentFun())){
				Function pf=this.functionDao.getFunctionById(  f.getParentFun() );
				List<String> urls = functionDao.getFunctionUrls(pf.getId());
			    pf.setUrls(urls);
				fns.add(pf);
				fns.addAll(  getAllParentsOfFunction(pf) );
			}
		}
		return fns;
	}
	
	/**
	 * 获取所有pos和pad权限
	 * @param f
	 * @return
	 */
	public Map<String,Object> getPosPadAuthByAccount(String account){
		//定义返回对象
		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
		//查询方法
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("account", account);
		List<Function> funList = this.functionDao.queryFunctionList(queryMap);
		//从配置文件中获取相关配置
		Map<String,String> authMap = PropertiesUtils.findValues("logintype.");
		//循环遍历处理
		Iterator<String> it = authMap.keySet().iterator();
		while(it.hasNext()){
			Integer state = 0;
			String key = it.next();
			String value = String.valueOf(authMap.get(key));
			for(Function fun : funList){
				if(fun.getCode().equals(value)){
					state = 1;
					break;
				}
			}
			resultMap.put(key.replaceAll("logintype.", ""),state);
		}
		
		return resultMap;
	}
	/**
	 * 获取角色、功能、URL列表
	 * @param f
	 * @return
	 */
	public Map<String,Object> getRoleFunctionUrlMap(String account){
		//定义返回对象
		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
		//查询方法
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("account", account);
		List<Map<String,Object>> urlList = functionDao.queryRoleFunctionUrlList(queryMap);
		//循环处理
		String currentRoleId = null;
		String currentFunctionId = null;
		String currentRoleMapKey = null;
		String currentFunctionMapKey = null;
		for(Map<String,Object> urlMap : urlList){
			//处理role map
			if(currentRoleId==null||!currentRoleId.equals(urlMap.get("roleId"))){
				String roleId = urlMap.get("roleId")!=null?String.valueOf(urlMap.get("roleId")):"";
				String roleName = urlMap.get("roleName")!=null?String.valueOf(urlMap.get("roleName")):"";
				currentRoleId = roleId;
				currentRoleMapKey = roleId+";"+roleName;
			}
			if(resultMap.get(currentRoleMapKey)==null){
				resultMap.put(currentRoleMapKey,new LinkedHashMap<String,Object>());
			}
			//处理function map
			Map<String,Object> roleMap = (Map<String,Object>)resultMap.get(currentRoleMapKey);
			if(currentFunctionId==null||!currentFunctionId.equals(urlMap.get("functionId"))){
				String functionId = urlMap.get("functionId")!=null?String.valueOf(urlMap.get("functionId")):"";
				String functionName = urlMap.get("functionName")!=null?String.valueOf(urlMap.get("functionName")):"";
				String functionCode = urlMap.get("functionCode")!=null?String.valueOf(urlMap.get("functionCode")):"";
				currentFunctionId = functionId;
				currentFunctionMapKey  = functionId+";"+functionName+";"+functionCode;
			}
			if(roleMap.get(currentFunctionMapKey)==null){
				roleMap.put(currentFunctionMapKey,new LinkedHashMap<String,Object>());
			}
			//处理url map
			Map<String,Object> functionMap = (Map<String,Object>)roleMap.get(currentFunctionMapKey);
			functionMap.put(String.valueOf(urlMap.get("urlId")), urlMap);
		}
		return resultMap;
	}
	
	/**
     * 根据工号查询权限
     * @author weizhifang
     * @since 2016-3-21
     * @param userId
     * @return
     */
    public List<Function> getFunctionForJobNumber(String jobNumber){
    	return functionDao.getFunctionForJobNumber(jobNumber);
    }
	
}
