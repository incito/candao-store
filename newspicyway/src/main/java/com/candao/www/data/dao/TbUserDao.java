package com.candao.www.data.dao;


import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbUser;

/**
 * 数据访问接口
 *
 */
public interface TbUserDao {    
    public final static String PREFIX = TbUserDao.class.getName();
    
	public TbUser get(java.lang.String userid);
	
	public <K, V> Map<K, V> findOne(java.lang.String userid);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TbUser tbUser);
	
	public int update(TbUser tbUser);
	
	public int delete(java.lang.String userid,int status );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

   public TbUser getCurrentUser(String username, String userPassword);
	
	
//	public  Map<String,Object> getCurrentUser(String username, String userPassword);
	/**
	 * 得到标签
	 * @return
	 */
	public List<Map<String,Object>> getUserTat(int itemid);
	/**
	 * 得到用户可以访问的URL
	 * @return
	 */
	public List<String> getAllowAccessUrl(String userid);
	
	/**
	 * 得到用户可以访问的Button
	 * @return
	 */
	public List<String> getAllowAccessButton(String userid);
	
	public  TbUser findByuserName(java.lang.String userName);

	/**
	 * 获取所有服务员信息
	 * @author zhao
	 * @return
	 */
	public List<TbUser> findAllServiceUser();
	
	/**
	 * 更新用户登录时间
	 * @author zhao
	 * @param userid
	 * @return
	 */
	public int updateLoginTime(String userid);

	/**
	 * findMaxOrderNum
	 * @author tom_zhao
	 * @return
	 */
	public TbUser findMaxOrderNum();

	/**
	 * update user order number
	 * @author tom_zhao
	 * @param username
	 */
	public void updateUserOrderNum(String username,int ordernum);
}


