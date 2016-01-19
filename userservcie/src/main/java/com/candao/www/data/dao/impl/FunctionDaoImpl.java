package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.FunctionDao;
import com.candao.www.data.model.Function;

/**
 * @author zhao
 *
 */
@Repository
public class FunctionDaoImpl implements FunctionDao {
  @Autowired
  private DaoSupport dao;

  /* (non-Javadoc)
   * @see com.candao.www.permit.persistence.dao.FunctionDao#insertFunction(com.candao.www.permit.entity.Function)
   */
  public int insert(Function function) {
    return dao.insert(PREFIX + ".insert", function);
  }

  /* (non-Javadoc)
   * @see com.candao.www.permit.persistence.dao.FunctionDao#update(com.candao.www.permit.entity.Function)
   */
  public int update(Function function) {
    return dao.update(PREFIX + ".update", function);
  }

  /* (non-Javadoc)
   * @see com.candao.www.permit.persistence.dao.FunctionDao#delete(String)
   */
  public int delete(String id) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    return dao.delete(PREFIX + ".delete", params);
  }
  
  public <T, K, V> List<T> getFunctionUrls(String function) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("function", function);
    return dao.find(PREFIX + ".getFunctionUrls", params);
  }

  public void insertFunctionUrls(String function, List<String> urls) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("function", function);
    params.put("urls", urls);
    dao.insert(PREFIX + ".insertFunctionUrls", params);
  }

  public void deleteFunctionUrl(String function) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("function", function);
    dao.delete(PREFIX + ".deleteFunctionUrl", params);
  }

  public <T, K, V> List<T> getChildFunction(String parentFun) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("parentFun", parentFun);
    return dao.find(PREFIX + ".getChildFunction", params);
  }

  public <T, K, V> List<T> getFunctionList(Map<K, V> params) {
    return dao.find(PREFIX +".getFunctionList", params);
  }
  
  /**
   * 获取功能编号列表
   * @param params
   * @return
   */
  public List<Map<String,Object>> getFunctionCodeList(Map<String, Object> params) {
	  return dao.find(PREFIX +".getFunctionCodeList", params);
  }
  /**
   * 获取角色、功能、URL列表
   * @param params
   * @return
   */
  public List<Map<String,Object>> queryRoleFunctionUrlList(Map<String, Object> params) {
	  return dao.find(PREFIX +".queryRoleFunctionUrlList", params);
  }

  public <E, K, V> Page<E> getFunctionListPage(Map<K, V> params, int current, int pagesize) {
    return dao.page(PREFIX + ".getFunctionListPage", params, current, pagesize);
  }

	@Override
	public Function getFunctionById(String id) {
		Map<String , Object> param=new HashMap();
		param.put("id", id);
		return this.dao.get(PREFIX+".getFunctionById", param);
	}
	
	/**
	 * 获取方法列表，根据条件（可以包括userId,roleId等）
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> queryFunctionList(Map<K, V> params) {
	    return dao.find(PREFIX + ".queryFunctionList", params);
	}
	/**
	 * 获取方法数量，根据条件（可以包括userId,roleId等）
	 * @param params
	 * @return
	 */
	public <T, K, V> Integer getFunctionTotal(Map<K, V> params) {
	    return dao.get(PREFIX + ".getFunctionTotal", params);
	}

}
