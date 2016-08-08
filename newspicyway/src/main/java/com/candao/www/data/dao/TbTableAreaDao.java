package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbTableArea;

public interface TbTableAreaDao {
public final static String PREFIX = TbTableAreaDao.class.getName();
    
	public TbTableArea get(java.lang.String id);
	
	public  TbTableArea tableAvaliableStatus(java.lang.String id);
	
	//public <T, K, V> List<T> find(Map<K, V> params);
	public <T, K, V> List<T> find(Map<K, V> params);
	public <T, K, V> List<T> getCount(Map<K, V> params);
	public int insert(TbTableArea tbTableArea);
	
	public int update(TbTableArea tbTableArea);
	
	public int delete(java.lang.String id );

	
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
	/**
	 * 取得数据分类
	 * @return
	 */
	
	
	public List<Map<String,Object>> getTableTag();

	public int updateListOrder(List<TbTableArea> tbtableArea);
	public List<Map<String,Object>> findTableCountAndAreaname();
	
}
