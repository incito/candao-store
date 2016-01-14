package com.candao.www.data.dao;


import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbParterner;

/**
 * 数据访问接口
 *
 */
public interface TbparternerDao {    
    public final static String PREFIX = TbparternerDao.class.getName();
    
	public TbParterner get(java.lang.String id);
	
	public <K, V> Map<K, V> findOne(java.lang.String id);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbParterner tbParterner);
	
	public int update(TbParterner tbParterner);
	
	public int delete(java.lang.String id );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
 

}


