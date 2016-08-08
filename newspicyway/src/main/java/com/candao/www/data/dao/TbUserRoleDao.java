package com.candao.www.data.dao;


import java.util.Map;
import java.util.List;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbUserRole;

/**
 * 数据访问接口
 *
 */
public interface TbUserRoleDao {    
    public final static String PREFIX = TbUserRoleDao.class.getName();
    
	public TbUserRole get(java.lang.String roleid, java.lang.String userid);
	
	public <K, V> Map<K, V> findOne(java.lang.String roleid, java.lang.String userid);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbUserRole tbUserRole);
	
	public int update(TbUserRole tbUserRole);
	
	public int delete(java.lang.String userid );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	/**
	 * 增加用户多种角色
	 * @param userid
	 * @param roleids
	 * @return
	 */
	public int inserts(String userid,String[] roleids);
	
	public List<String> getUserRole(String userid);

}


