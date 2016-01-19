package com.candao.www.data.dao;
import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
public interface TbSalesSummaryDao {
	public final static String PREFIX = TbSalesSummaryDao.class.getName();

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

	
	public <K, V> Map<K, V> findOne(java.lang.String id);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	

	public int delete(java.lang.String id );
	
	
	public <T, K, V> List<T> findFD(Map<K, V> params) ;
	
	public <T, K, V> List<T> dates(Map<K, V> params);
	
}