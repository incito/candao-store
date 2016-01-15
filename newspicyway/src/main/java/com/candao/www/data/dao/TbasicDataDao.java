package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbasicData;

public interface TbasicDataDao {
	public final static String PREFIX = TbasicDataDao.class.getName();

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

	public TbasicData get(java.lang.String id);
	
	public <K, V> Map<K, V> findOne(java.lang.String id);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbasicData tbasicData);
	
	public int update(TbasicData tbasicData);
	
	public int delete(java.lang.String id );

	/**
	 * 取得数据分类
	 * @return
	 */
	public List<Map<String,Object>> getDataDictionaryTag(String itemtype);
	
     /**
      * 获取到所有的菜品分类信息
      * @author zhao
      * @param params
      * @return
      */
	public List<Map<String, Object>> findAll(Map<String, Object> params);
	

	/**
	 * 根据分类id 获取模板信息
	 * @author zhao
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findTempByItemId(Map<String, Object> params);
	
	/**
	 * 获取所有菜单分类 
	 * @author zhao
	 * @param params
	 * @return
	 */
	 public <T, K, V> List<T> findCategory(Map<K, V> params) ;
	 /**
	  * 
	  * @param params
	  * @return
	  * @author shen
	  */
	 public <T, K, V> List<T> getListByparams(Map<K, V> params) ;
	 /**
	  * 获取该菜谱下的菜品分类
	  * @author shen
	  * @date:2015年5月6日下午11:43:00
	  * @Description: TODO
	  */
	 public <T, K, V> List<T> getMenuColumn(Map<K, V> params) ;
	 
	 public int updateListOrder(List<TbasicData> tdus);

	public int updateDishTagListOrder(List<Map<String, Object>> dishTag);
}