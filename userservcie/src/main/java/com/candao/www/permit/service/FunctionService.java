/**
 * 
 */
package com.candao.www.permit.service;

import java.util.List;
import java.util.Map;

import com.candao.www.data.model.Function;
import com.candao.www.permit.common.ScopeDict;



/**
 * @author zhao
 *
 */
public interface FunctionService {
  /**
   * 查询功能列表，同时查询功能对应的url
   * @param params
   * @return
   */
  public List<Function> getFunctionListWithUrl(Map<String, Object> params);
  
  /**
   * 查询功能列表
   * @param params
   * @return
   */
  public List<Function> getFunctionList(Map<String, Object> params);
  
  /**
   * 查询指定层级的功能
   * @param level
   * @return
   */
  public List<Function> getFunctionsByLevel(int level);
  
  /**
   * 查询指定层级的功能，同时查询功能对应的url
   * @param level
   * @return
   */
  public List<Function> getFunctionsWithUrlByLevel(int level);
  
  /**
   * 查询子功能，同时查询功能对应的url
   * @param fn
   * @return
   */
  public List<Function> getChildFunction(Function fn);
  
  /**
   * 查询子功能
   * @param fn
   * @return
   */
  public List<Function> getChildFunctionWithUrl(Function fn);
  
  /**
   * 根据上下级关系获取功能
   * @return
   */
  public List<Function> getAllStructuredFunction();
  
  /**
   * 根据上下级关系获取功能，同时取url
   * @return
   */
  public List<Function> getAllStructuredFunctionWithUrl();
  
  public List<Function> getTopLevelFunctionsByScopeCode(ScopeDict scope);
  
  /**
   * 根据id获取 function
   * @param id
   * @return
   */
  public Function getFunctionById(String id);
  
  /**
   * 根据用户id 获取，用户菜单的模块列表
   * @param user_id
   * @return
   */
  public List<Function>  getMenuFunction4User(String user_id);
  /**
	* 获取所有pos和pad权限
	* @param f
	* @return
	*/
  public Map<String,Object> getPosPadAuthByAccount(String userId);
  /**
   * 获取角色、功能、URL列表
   * @param f
   * @return
   */
  public Map<String,Object> getRoleFunctionUrlMap(String account);
  
}
