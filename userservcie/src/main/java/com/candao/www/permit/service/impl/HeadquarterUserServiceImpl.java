package com.candao.www.permit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.www.data.dao.UserBranchDao;
import com.candao.www.data.dao.UserDao;
import com.candao.www.data.dao.UserRoleDao;
import com.candao.www.data.model.HeadquarterUser;
import com.candao.www.data.model.User;
import com.candao.www.data.model.UserBranch;
import com.candao.www.data.model.UserRole;
import com.candao.www.permit.common.Constants;

/**
 * 
 * 总店用户-Service实现类
 * 
 * @author lishoukun
 * @date 2015/04/21
 */
@Service
public class HeadquarterUserServiceImpl extends UserServiceImpl{
  @Autowired
  private UserDao userDao;
  
  @Autowired
  private UserRoleDao userRoleDao;
  
  @Autowired
  private UserBranchDao userBranchDao;
  /**
   * 添加总店用户
   * @param user
   * @return
   */
  public User addUser(User user1) {
	  HeadquarterUser user = (HeadquarterUser)user1;
	  //设置为总店用户
	  user.setUserType(Constants.HEADQUARTER_USER);
	  //调用父类，保存用户
	  User addUser = super.addUser(user);
	  if(addUser!=null){
		  String userId = user.getId();
		  //保存用户门店关联
		  List<UserBranch> branchs = user.getBranchs();
		  for(UserBranch ub : branchs){
			  ub.setId(IdentifierUtils.getId().generate().toString().replaceAll("-", ""));
			  ub.setUserId(userId);
			  userBranchDao.addUserBranch(ub);
		  }
		  return user;
	  }else{
		  return null;
	  }
  }
  /**
   * 更新总店用户
   * @param user
   * @return
   */
  public User updateUser(User user1) {
	  HeadquarterUser user = (HeadquarterUser)user1;
	  User updateUser = super.updateUser(user);
	  if(updateUser!=null){
		  String userId = user.getId();
		  //删除之前的用户门店关联
		  Map<String,Object> delMap = new HashMap<String,Object>();
		  delMap.put("userId", userId);
		  userBranchDao.deleteUserBranch(delMap);
		  //保存新的用户门店关联
		  List<UserBranch> branchs = user.getBranchs();
		  for(UserBranch ub : branchs){
			  ub.setId(IdentifierUtils.getId().generate().toString().replaceAll("-", ""));
			  ub.setUserId(userId);
			  userBranchDao.addUserBranch(ub);
		  }
		  return user;
	  }else{
		  return null;
	  }
  }
  /**
   * 分页查询总店用户
   * @param userMap
   * @param currentPage
   * @param pageSize
   * @return
   */
  public Page<Map<String,Object>> queryUserPage(Map<String, Object> userMap,Integer currentPage, Integer pageSize) {
	  userMap.put("userType",Constants.HEADQUARTER_USER);
	  return super.queryUserPage(userMap, currentPage, pageSize);
  }
  
  /**
   * 不分页查询总店用户
   * @param parameterObject
   * @param skipResults
   * @param maxResults
   * @return
   */
  public List<User> queryUserList(Map<String,Object> userMap) {
	return super.queryUserList(userMap);
  }
  /**
   * 根据id列查询总店用户
   * @param userId
   * @return
   */
  public User getUserById(String id){
	  User user1 = userDao.getUserById(id);
	  HeadquarterUser user = new HeadquarterUser(user1);
	  //获取关联角色信息
	  Map<String,Object> queryMap = new HashMap<String,Object>();
	  queryMap.put("userId", id);
	  List<UserRole> userRoleList = userRoleDao.queryUserRoleList(queryMap);
	  user.setRoles(userRoleList);
	  //获取关联门店信息
	  List<UserBranch> userBranchList = userBranchDao.queryUserBranchList(queryMap);
	  user.setBranchs(userBranchList);
	  return user;
  }
  /**
   * 删除总店用户,根据id
   * @param id
   * @return
   */
  public Integer deleteUserById(String id) {
	  int delCount = super.deleteUserById(id);
	  //删除关联门店信息
	  Map<String,Object> delMap = new HashMap<String,Object>();
	  delMap.put("userId", id);
	  userBranchDao.deleteUserBranch(delMap);
	  return delCount;
  }
}
