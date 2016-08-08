package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Function;



public interface FunctionDao {
  public final static String PREFIX = FunctionDao.class.getName();
  
	int insert(Function function);
	int update(Function function);
	int delete(String id);
	//有查询条件不分页查询
	<T, K, V> List<T> getFunctionList(Map<K, V> params);
	/**
	 * 获取功能编号列表
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getFunctionCodeList(Map<String, Object> params);
	/**
	  * 获取角色、功能、URL列表
	  * @param params
	  * @return
	  */
	public List<Map<String,Object>> queryRoleFunctionUrlList(Map<String, Object> params);
	//有查询条件分页查询
	<E, K, V> Page<E> getFunctionListPage(Map<K, V> params, int current, int pagesize);
	//查询功能对应的url
	<T, K, V> List<T> getFunctionUrls(String function);
	//插入功能对应的url，批量插入
	void insertFunctionUrls(String function, List<String> urls);
	//根据功能删除功能url映射
	void deleteFunctionUrl(String function);
  //查询子功能
	<T, K, V> List<T> getChildFunction(String parentFun);
	
	/**
	 * 根据ID，获取function对象
	 * @param id
	 * @return
	 */
	public Function getFunctionById(String id);
	/**
	 * 获取方法列表，根据条件（可以包括userId,roleId等）
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> queryFunctionList(Map<K, V> params);
	/**
	 * 获取方法数量，根据条件（可以包括userId,roleId等）
	 * @param params
	 * @return
	 */
	public <T, K, V> Integer getFunctionTotal(Map<K, V> params);
}