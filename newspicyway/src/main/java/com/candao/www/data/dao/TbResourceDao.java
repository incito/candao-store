package com.candao.www.data.dao;


import java.util.Map;
import java.util.List;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbResource;

/**
 * 数据访问接口
 *
 */
public interface TbResourceDao {    
    public final static String PREFIX = TbResourceDao.class.getName();
    
	public TbResource get(java.lang.String resourcesid);
	
	public <K, V> Map<K, V> findOne(java.lang.String resourcesid);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbResource tbResource);
	
	public int update(TbResource tbResource);
	
	public int delete(java.lang.String resourcesid );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	/**
	 * 得到左侧树菜单
	 * @param userid
	 * @return
	 */
	public List<TbResource> getLeftMenu(String userid);
	/**
	 * 得到所有资源访问路径
	 * @return
	 */
	public List<String> findAllPath();

	public List<TbResource> findByRoleid(Map<String, Object> params);

}


