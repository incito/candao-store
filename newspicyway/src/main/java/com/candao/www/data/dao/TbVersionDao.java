package com.candao.www.data.dao;


import java.util.Map;
import java.util.List;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbVersion;

/**
 * 数据访问接口
 *
 */
public interface TbVersionDao {    
    public final static String PREFIX = TbVersionDao.class.getName();
    
	public TbVersion get(int type);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int update(TbVersion tbVersion);
	

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

}


