package com.candao.www.permit.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.page.Page;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.FunctionDao;
import com.candao.www.data.dao.RoleDao;
import com.candao.www.data.dao.UserBranchDao;
import com.candao.www.data.dao.UserDao;
import com.candao.www.data.dao.UserRoleDao;
import com.candao.www.data.model.User;
import com.candao.www.data.model.UserBranch;
import com.candao.www.data.model.UserRole;
import com.candao.www.permit.common.AccountUtils;
import com.candao.www.permit.common.BusinessException;
import com.candao.www.permit.common.Constants;
import com.candao.www.permit.service.UserService;

/**
 * 
 * 基本用户-Service实现类
 * 
 * @author lishoukun
 * @date 2015/04/21
 */
@Service("t_userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private UserBranchDao userBranchDao;

	@Autowired
	private FunctionDao functionDao;

	private Log log = LogFactory.getLog(UserServiceImpl.class.getName());

	@Override
	public User login(String loginName, String password) {
		List<User> loginUsers = userDao.getUser(loginName, password);
		if (loginUsers == null||loginUsers.isEmpty()) {
			throw new BusinessException("账号/邮箱/手机号或者密码错误");
		}
		User loginUser=null;
		for(User user:loginUsers) {
			if (Constants.ENABLE.equals(user.getStatus())) {
				loginUser=user;
				break;
			}
		}
		if(null==loginUser){
			throw new BusinessException("用户已经失效，请联系管理员");
		}
		return loginUser;
	}

	/**
	 * 验证密码
	 * 
	 * @param user
	 * @return
	 */
	public User validatePassword(String id, String password) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("id", id);
		queryMap.put("password", password);
		List<User> userList = userDao.queryUserList(queryMap);
		if (userList.size() == 1) {
			return userList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 验证密码及登录编号,根据员工号、密码
	 * 
	 * @param user
	 * @return
	 */
	public Map<String, Object> validatePasswordLoginTypeByAccount(String username, String password, String loginType) {
		/*
		 * 验证密码
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("jobNumber", username);
		queryMap.put("paymentPassword", password);
		List<User> userList = userDao.queryUserList(queryMap);
		if (userList.size() != 1) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("success", false);
			resultMap.put("msg", "账号或密码不正确");
			return resultMap;
		}
		User user = userList.get(0);
		/*
		 * 验证权限
		 */
		Map<String, Object> queryMap2 = new HashMap<String, Object>();
		queryMap2.put("account", user.getAccount());
		// 获取登录方式对应的菜单编码
		String code = PropertiesUtils.getValue("logintype." + loginType);
		String loginTypeText = PropertiesUtils.getValue("logintypetext." + loginType);
		queryMap2.put("code", code);
		Integer funTotal = functionDao.getFunctionTotal(queryMap2);
		if (funTotal != 1) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("success", false);
			resultMap.put("msg", "没有" + loginTypeText + "权限");
			return resultMap;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", true);
		resultMap.put("msg", "验证成功");
		resultMap.put("name", userList.get(0).getName());
		return resultMap;
	}

	public Map<String, Object> validateLoginTypeByAccount(String username, String loginType) {
		/*
		 * 验证密码
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("jobNumber", username);
		List<User> userList = userDao.queryUserList(queryMap);
		if (userList.isEmpty()) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("success", false);
			resultMap.put("msg", "账号不存在");
			return resultMap;
		}
		User user = userList.get(0);
		/*
		 * 验证权限
		 */
		Map<String, Object> queryMap2 = new HashMap<String, Object>();
		queryMap2.put("account", user.getAccount());
		// 获取登录方式对应的菜单编码
		String code = PropertiesUtils.getValue("logintype." + loginType);
		String loginTypeText = PropertiesUtils.getValue("logintypetext." + loginType);
		queryMap2.put("code", code);
		Integer funTotal = functionDao.getFunctionTotal(queryMap2);
		if (funTotal != 1) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("success", false);
			resultMap.put("msg", "没有" + loginTypeText + "权限");
			return resultMap;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", true);
		resultMap.put("msg", "验证成功");
		resultMap.put("name", userList.get(0).getName());
		return resultMap;
	}

	/**
	 * 验证密码,根据员工号、密码
	 * 
	 * @param user
	 * @return
	 */
	public User validatePasswordByAccount(String account, String password) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("jobNumber", account);
		queryMap.put("paymentPassword", password);
		List<User> resultList = userDao.queryUserList(queryMap);
		if (resultList.size() == 1) {
			log.info("员工工号，密码校验结果：" + JacksonJsonMapper.objectToJson(resultList));
			return resultList.get(0);
		} else {
			log.error("员工校验失败，查找不到该用户");
			return null;
		}
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User addUser(User user) {
		// 设置id
		if (!"0".equals(user.getChannelType())) {
			user.setId(AccountUtils.getId());
		}

		// 判断用户信息合法性
		if (validateUser(user) == false) {
			throw new BusinessException("创建用户失败");
		}
		String password = AccountUtils.getRandomPassword();
		user.setPassword(password);
		int result = userDao.addUser(user);
		if (result == 0) {
			return null;
		}
		// 保存用户角色关联
		if (user.getRoles() != null) {
			List<UserRole> roles = user.getRoles();
			for (UserRole ur : roles) {
				ur.setId(AccountUtils.getId());
				ur.setUserId(user.getId());
				userRoleDao.addUserRole(ur);
			}
		}
		return user;
	}

	/**
	 * 判断系统中是否存在重复账号、邮箱或者手机号的用户
	 * 
	 * @param user
	 * @return
	 */
	private boolean validateUser(User user) {
		Map userMap = new HashMap();
		String account = user.getAccount();
		if (account == null || account.trim().length() <= 0) {
			throw new BusinessException("缺少用户账号");
		}
		userMap.put("account", account);
		String email = user.getEmail();
		if (email != null && email.trim().length() > 0) {
			userMap.put("email", email);
		}
		String mobile = user.getMobile();
		if (mobile != null && mobile.trim().length() > 0) {
			userMap.put("mobile", mobile);
		}
		List<User> users = userDao.getDuplicateUser(userMap);
		if (users == null || users.size() <= 0) {
			return true;
		}
		User tempUser = null;
		for (int i = 0; i < users.size(); i++) {
			tempUser = users.get(i);
			// 主键相同说明是同一记录
			if (user.getId().equals(tempUser.getId())) {
				continue;
			}
			if (user.getAccount().equals(tempUser.getAccount())) {
				throw new BusinessException("系统内已经存在账号为（" + user.getAccount() + "）的用户。");
			}
			if (email != null && email.trim().length() > 0 && email.equals(tempUser.getEmail())) {
				throw new BusinessException("系统内已经存在邮箱为（" + email + "）的用户。");
			}
			if (mobile != null && mobile.trim().length() > 0 && mobile.equals(tempUser.getMobile())) {
				throw new BusinessException("系统内已经存在手机号为（" + mobile + "）的用户。");
			}
		}
		return true;
	}

	/**
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User updateUser(User user) {
		// 判断用户信息合法性
		if (validateUser(user) == false) {
			throw new BusinessException("编辑用户信息失败");
		}

		// 删除之前的角色用户关联
		Map<String, Object> delMap = new HashMap<String, Object>();
		delMap.put("userId", user.getId());
		userRoleDao.deleteUserRole(delMap);
		// 保存用户角色关联
		if (user.getRoles() != null) {
			List<UserRole> roles = user.getRoles();
			for (UserRole ur : roles) {
				ur.setId(AccountUtils.getId());
				ur.setUserId(user.getId());
				userRoleDao.addUserRole(ur);
			}
		}
		int result = userDao.updateUser(user);
		if (result != 0) {
			return user;
		} else {
			return null;
		}
	}

	/**
	 * 分页查询用户
	 * 
	 * @param parameterObject
	 * @param skipResults
	 * @param maxResults
	 * @return
	 */
	public Page<Map<String, Object>> queryUserPage(Map<String, Object> userMap, Integer currentPage, Integer pageSize) {
		Page<Map<String, Object>> page = userDao.queryUserPage(userMap, currentPage, pageSize);
		return page;
	}

	/**
	 * 不分页查询用户
	 * 
	 * @param parameterObject
	 * @param skipResults
	 * @param maxResults
	 * @return
	 */
	public List<User> queryUserList(Map<String, Object> userMap) {
		return userDao.queryUserList(userMap);
	}

	/**
	 * 根据id列查询用户
	 * 
	 * @param userId
	 * @return
	 */
	public User getUser(String userId) {
		if (userId == null || userId.trim().length() <= 0) {
			throw new BusinessException("缺少查询用户id");
		}
		User user = userDao.getUserById(userId);
		return user;
	}

	/**
	 * 获取授权给用户的url
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public HashSet<String> getAuthedUrls(User user) {
		HashSet<String> authed = new HashSet<String>();
		if (user == null) {
			return authed;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("userId", user.getId());
		List<UserRole> userRoleList = userRoleDao.queryUserRoleList(queryMap);
		user.setRoles(userRoleList);
		if (user.getRoles() == null || user.getRoles().size() <= 0) {
			return authed;
		}
		// 获取用户可以访问的url
		List<String> urls = null;
		for (int i = 0; i < userRoleList.size(); i++) {
			urls = roleDao.getPermitUrl(userRoleList.get(i).getRoleId());
			if (urls != null) {
				authed.addAll(urls);
			}
		}

		return authed;
	}

	/**
	 * 根据用户id获取关联门店信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserBranch> queryUserBranchListByUserId(String userId) {
		// 判断如果用户类型为总店用户，则获取它的相关门店
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("userId", userId);
		// 获取关联门店信息
		List<UserBranch> userBranchList = userBranchDao.queryUserBranchList(queryMap);
		return userBranchList;
	}

	/**
	 * 更新用户密码
	 * 
	 * @param name
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public boolean updatePassword(String id, String oldPwd, String newPwd) {
		if (validatePassword(id, oldPwd) != null) {
			User user = new User();
			user.setId(id);
			user.setPassword(newPwd);
			userDao.updateUser(user);
		} else {
			throw new BusinessException("修改密码失败：用户名或者旧密码错误");
		}
		return true;
	}

	/**
	 * 更新用户状态
	 * 
	 * @param name
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public boolean updateStatus(String id, String status) {
		User user = new User();
		user.setId(id);
		user.setStatus(status);
		userDao.updateUser(user);
		return true;
	}

	/**
	 * 获取用户,根据用户id
	 * 
	 * @param id
	 *            用户id
	 */
	public User getUserById(String id) {
		if (id == null || id.trim().length() <= 0) {
			throw new BusinessException("缺少查询用户id");
		}
		User user = userDao.getUserById(id);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("userId", id);
		List<UserRole> userRoleList = userRoleDao.queryUserRoleList(queryMap);
		user.setRoles(userRoleList);
		return user;
	}

	/**
	 * 帐号邮件发送
	 * 
	 * @param paramMap
	 *            参数
	 */
	public boolean accountEmailSend(Map<String, Object> paramMap) {
		return false;
	}

	/**
	 * 设置邮箱
	 * 
	 * @param id
	 * @param email
	 * @return
	 */
	public boolean setEmail(String id, String email) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		userDao.updateUser(user);
		return true;
	}

	/**
	 * 设置手机
	 * 
	 * @param id
	 * @param email
	 * @return
	 */
	public boolean setMobile(String id, String mobile) {
		User user = new User();
		user.setId(id);
		user.setMobile(mobile);
		userDao.updateUser(user);
		return true;
	}

	/**
	 * 找回密码，根据邮箱
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public boolean retrievePwdByEmail(String email, String password) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("email", email);
		List<User> userList = userDao.queryUserList(queryMap);
		if (userList.size() > 0) {
			User user = userList.get(0);
			user.setPassword(password);
			userDao.updateUser(user);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 找回密码，根据手机
	 * 
	 * @param mobile
	 * @param password
	 * @return
	 */
	public boolean retrievePwdByMobile(String email, String password) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("mobile", email);
		List<User> userList = userDao.queryUserList(queryMap);
		if (userList.size() > 0) {
			User user = userList.get(0);
			user.setPassword(password);
			userDao.updateUser(user);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算用户安全分数,根据用户id
	 * 
	 * @param id
	 *            用户id
	 */
	public Integer computeSafeScore(String id) {
		if (id == null || id.trim().length() <= 0) {
			throw new BusinessException("缺少查询用户id");
		}
		User user = userDao.getUserById(id);
		int score = 0;
		if (user.getPassword() != null && !"".equals(user.getPassword())) {
			score += 30;
		}
		if (user.getEmail() != null && !"".equals(user.getEmail())) {
			score += 30;
		}
		if (user.getMobile() != null && !"".equals(user.getMobile())) {
			score += 40;
		}
		return score;
	}

	@Override
	public User getUser(Map<String, Object> userMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getUserTotal(Map<String, Object> userMap) {
		int count = userDao.getUserTotal(userMap);
		return count;
	}

	@Override
	public Integer deleteUserById(String id) {
		int delCount = userDao.deleteUserById(id);
		Map<String, Object> delMap = new HashMap<String, Object>();
		delMap.put("userId", id);
		userRoleDao.deleteUserRole(delMap);
		return delCount;
	}

	@Override
	public Integer deleteUsersByIds(String ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer deleteUser(Map<String, Object> userMap) {
		int delCount = userDao.deleteUser(userMap);
		return delCount;
	}

	@Override
	public void updateLoginTime(String userid) {
		this.userDao.updateLoginTime(userid);
	}

	@Override
	public User findMaxOrderNum() {
		return this.userDao.findMaxOrderNum();
	}

	@Override
	public void updateUserOrderNum(String account, int ordernum) {
		userDao.updateUserOrderNum(account, ordernum);

	}

	@Override
	public User getUserByAccount(String account) {
		// 使用精确方式 根据账号 查找
		Map<String, Object> params = new HashMap();
		params.put("exactFind", true);
		params.put("account", account);
		List<User> l = this.userDao.queryUserList(params);
		if (null == l || l.size() < 1) {
			return null;
		} else {
			return l.get(0);
		}
	}

	@Override
	public User getCurrentBranchName() {
		return userDao.getCurrentBranchName();
	}

	@Override
	public User getTenantName() {
		return userDao.getTenantName();
	}

	@Override
	public User getUserByjobNum(String id) {
		// TODO Auto-generated method stub
		return userDao.getUserByjobNum(id);
	}

}
