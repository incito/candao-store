package com.candao.www.data.dao;


import java.util.Map;
import java.util.List;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbResourceRole;

/**
 * 数据访问接口
 *
 */
public interface TbResourceRoleDao {    
    public final static String PREFIX = TbResourceRoleDao.class.getName();
    
	public TbResourceRole get(java.lang.String roleid, java.lang.String resourceid);
	
	public <K, V> Map<K, V> findOne(java.lang.String roleid, java.lang.String resourceid);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbResourceRole tbResourceRole);
	
	public int update(TbResourceRole tbResourceRole);
	
	public int delete(java.lang.String roleid);

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	
	/**
	 * 给角色增加多个资源
	 * @param userid
	 * @param roleids
	 * @return
	 */
	public int inserts(String roleid,String[] resourceids);
	
	public List<String> getRoleResource(String roleid);

}


