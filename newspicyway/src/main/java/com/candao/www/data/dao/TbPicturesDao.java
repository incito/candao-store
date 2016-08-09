package com.candao.www.data.dao;


import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbPictures;

/**
 * 数据访问接口
 *
 */
public interface TbPicturesDao {    
    public final static String PREFIX = TbPicturesDao.class.getName();
    
	public TbPictures get(java.lang.String id);
	
	public <K, V> Map<K, V> findOne(java.lang.String id);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbPictures tbPictures);
	
	public int update(TbPictures tbPictures);
	
	public int delete(java.lang.String id );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

	
	

}


