package com.candao.www.data.dao;


import java.util.Map;
import java.util.List;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbRole;

/**
 * 数据访问接口
 *
 */
public interface TbRoleDao {    
    public final static String PREFIX = TbRoleDao.class.getName();
    
	public TbRole get(java.lang.String roleid);
	
	public <K, V> Map<K, V> findOne(java.lang.String roleid);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbRole tbRole);
	
	public int update(TbRole tbRole);
	
	public int delete(java.lang.String roleid ,int status);

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
	/**
	 * 得到标签
	 * @return
	 */
	public List<Map<String,Object>> getRoleList();

}


