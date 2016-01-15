package com.candao.www.security.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TbUser;


public interface UserService {
/**
 * 分页查询用户
 * @param params
 * @param current
 * @param pagesize
 * @return
 */
 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
 /**
  * 保存用户
  * @param tbuser
  * @return
  */
 public boolean save(TbUser tbUser);
 /**
  * 更改用户
  * @param tbuser
  * @return
  */
 public boolean update(TbUser tbUser);
 /**
  * 查询单个用户
  * @param userid
  * @return
  */
 public TbUser findById(String userid);
 /**
  * 删除单个用户
  * @param userid
  * @return
  */
 public boolean deleteById(String userid,int status);
 
	/**
	 * 得到标签
	 * @return
	 */
	public List<Map<String,Object>> getUserTat();
	/**
	 * 修改用户密码
	 * @param userid
	 * @param password
	 * @return
	 */
	public boolean updatePassword(String userid,String password);
	/**
	 * 检查用户是否存在
	 * @param username
	 * @return
	 */
	public boolean checkUesrExist(String username);
	/**
	 * 检查用户是否可以访问某个URL
	 * @param userid
	 * @param url
	 * @return
	 */
	public boolean checkAllowAccessUrl(String userid,String url);
	
	/**
	 * 得到用户可以访问的button集合
	 * @param userid
	 * @param url
	 * @return
	 */
	public List<String> getAllowAccessButton(String userid);
	
	/**
	 * 根据用户名获取用户信息
	 * @author zhao
	 * @param userName
	 * @return
	 */
	public TbUser findByuserName(String userName);
	 
	
	/**
	 * 获取所有的服务员信息
	 * @author zhao
	 * @param object
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
	 * 查询最大用户排序
	 * @author tom_zhao
	 * @return
	 */
	public TbUser findMaxOrderNum();
	
	/**
	 * update user order num
	 * @author tom_zhao
	 * @param username
	 */
	public void updateUserOrderNum(String username,int ordernum);
}
