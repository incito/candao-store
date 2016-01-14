package com.candao.www.data.dao;


import java.util.Map;
import java.util.List;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbOperation;

/**
 * 数据访问接口
 *
 */
public interface TbOperationDao {    
    public final static String PREFIX = TbOperationDao.class.getName();
    
	public TbOperation get(java.lang.String id);
	
	public <K, V> Map<K, V> findOne(java.lang.String id);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbOperation tbOperation);
	
	public int update(TbOperation tbOperation);
	
	public int delete(java.lang.String id );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

}


