package com.candao.www.permit.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.Constant;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.MD5;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.EmployeeUserDao;
import com.candao.www.data.dao.FunctionDao;
import com.candao.www.data.dao.RoleDao;
import com.candao.www.data.dao.UserDao;
import com.candao.www.data.dao.UserRoleDao;
import com.candao.www.data.model.Application;
import com.candao.www.data.model.EmployeeUser;
import com.candao.www.data.model.Function;
import com.candao.www.data.model.Role;
import com.candao.www.data.model.User;
import com.candao.www.data.model.UserRole;
import com.candao.www.permit.common.AccountUtils;
import com.candao.www.permit.common.BusinessException;
import com.candao.www.permit.common.Constants;
import com.candao.www.permit.service.EmployeeUserService;
import com.candao.www.permit.vo.RoleVO;
import com.candao.www.utils.SessionUtils;

@Service
public class EmployeeUserServiceImpl implements EmployeeUserService {

	@Autowired
	private EmployeeUserDao employeeUserDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private FunctionDao functionDao;
	  
	@Override
	public List<EmployeeUser> getAll() {
		return this.employeeUserDao.getAll();
	}

	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		
		Page page=this.employeeUserDao.page(params, current, pagesize);
		
		Collection coll=page.getRows();
		Iterator<EmployeeUser> iterator=coll.iterator();
		List l=new ArrayList();
		while (iterator.hasNext()) {
            EmployeeUser element = (EmployeeUser) iterator.next();
            //System.out.println( JacksonJsonMapper.objectToJson(element));
          //读取user对象
    		if( !StringUtils.isBlank(element.getUserId() ) ){
    			User user=this.userDao.getUserById(element.getUserId());
    			element.setUser(user);
    		}
    		Map<String,Function> fns=new HashMap();
    		//根据他得角色，获取他有权限的功能模块的列表。
    		Map< String ,Object > param = new HashMap();
    		param.put("userId", element.getUserId());
    		List<UserRole> urs=this.userRoleDao.queryUserRoleList(param);
    		
    		if( null!=urs){
	    		for(UserRole ur:urs ){
	    			Role r=new Role();
	    			r.setId( ur.getRoleId() );
	    			List<Function> tmFns=this.roleDao.getRoleFunction(r, null);
	    			for(Function f:tmFns){
	    				fns.put(f.getCode(), f);
	    			}
	    		}
    		}
    		element.setFunctions(fns);
    		l.add(element);
        }
		
		page.setRows(l);
		return page;
	}

	@Override
	public int save(EmployeeUser employeeUser) {

		boolean needAccount=false;
		String account="";
		String  str_a = PropertiesUtils.getValue("employee.function.need_account"); //从配置文件获取手动创建账号的模块编号
		String  str_ab = PropertiesUtils.getValue("employee.function.need_payment_password"); //从配置文件获取手动创建账号的模块编号
		String SPLIT_CHAR=","; //分隔符

		String[] functions=null;
		
		//保存USER
		User user=employeeUser.getUser();
		user.setId(UUID.randomUUID().toString());
		user.setCreatetime( new Timestamp( new Date().getTime()));
		user.setUserType( Constants.EMPLOYEE);
		user.setStatus( Constants.ENABLE); //默认启用
		User usercurrent = SessionUtils.getCurrentUser();
		user.setTenantid(usercurrent.getTenantid());
		//先清空账号，需要后面根据权限判断账号原则。
		account=user.getAccount();
		user.setAccount(null);
		
		userDao.addUser(user);
		
		//如果选择权限，那么保存关联的权限角色
		List<Role> roles=employeeUser.getRoles();
		for(Role role:roles){
			UserRole ur=new UserRole();
			ur.setId( UUID.randomUUID().toString());
			ur.setRoleId(role.getId());
			ur.setUserId(user.getId());
			this.userRoleDao.addUserRole(ur);
		}
		
		//遍历权限，判断创建账号规则 
		Map selectedMap=new HashMap();
		Role r;
		for(Role role:roles){
			r=this.roleDao.get(role.getId());
			if(null!=r){ 
				List<Function> l= this.roleDao.getRoleFunction(r,  new Application().getId());
				for( Function f:l){
					selectedMap.put( f.getCode(), f);
				}
			}
		}
		
		functions=str_a.split( SPLIT_CHAR );
		if( null != functions || functions.length > 0 ){
			  for(String f:functions){
				  if( null!= selectedMap.get( f ) ){
					  needAccount=true;
					  break;
				  }
			  }
		}
		//根据权限，认为需要手动创建的账号
		if( needAccount ){
			user.setAccount(account);
			user.setPassword( MD5.md5( AccountUtils.getRandomPassword() ));
			userDao.updateUser(user);
			
			//判断是否需要创建 支付密码
			boolean needPaymentPassword=false;
			functions=str_ab.split( SPLIT_CHAR );
			if( null != functions || functions.length > 0 ){
				  for(String f:functions){
					  if( null!= selectedMap.get( f ) ){
						  needPaymentPassword=true;
						  break;
					  }
				  }
			}
			
			if( needPaymentPassword ){
				//此处创建默认职务权限密码为123456的Md5文
				employeeUser.setPaymentPassword( MD5.md5(AccountUtils.DEFAULT_PASSWORD ));
			}
			
		}else{
			// 随机生成 16位小写字符串账号
			user.setAccount( RandomStringUtils.randomAlphabetic(16).toLowerCase() );
			user.setPassword( MD5.md5( AccountUtils.getRandomPassword() ));
			userDao.updateUser(user);
		}
		
		employeeUser.setId( UUID.randomUUID().toString());
		employeeUser.setUserId(user.getId());
		return employeeUserDao.save(employeeUser);
	}

	@Override
	public int update(EmployeeUser employeeUser) {
		/*
		 * 更新员工需要注意如下事项。
		 * 1.根据员工的角色权限，判断账号是否保存（手动创建或者自动创建）
		 */
		String account="";
		
		EmployeeUser oldObj= this.employeeUserDao.get( employeeUser.getId() );
		//更新用户基本信息
		account=employeeUser.getUser().getAccount(); //先保存页面提交过来的账号
		employeeUser.getUser().setAccount(null);
		this.userDao.updateUser(employeeUser.getUser());
		//删除用户-角色映射记录。
		Map<String ,Object> param=new HashMap();
		param.put("userId", employeeUser.getUserId());
		this.userRoleDao.deleteUserRole( param );
		
		//重新保存用户--角色映射
		List<Role> roles = employeeUser.getRoles();
		for(Role role : roles ){
			UserRole ur=new UserRole();
			ur.setId( UUID.randomUUID().toString());
			ur.setRoleId( role.getId());
			ur.setUserId( employeeUser.getUserId());
			this.userRoleDao.addUserRole(ur);
		}
		
		//根据选择的角色，判断 如何创建/保存 账户，甚至是 支付密码。
		boolean needAccount = false;
		String str_a = PropertiesUtils.getValue("employee.function.need_account"); // 从配置文件获取手动创建账号的模块编号
		String str_ab = PropertiesUtils.getValue("employee.function.need_payment_password"); // 从配置文件获取手动创建账号的模块编号
		String SPLIT_CHAR = ","; // 分隔符
		User user=this.userDao.getUserById( employeeUser.getUserId() );
		String[] functions = null;

		// 遍历权限，判断创建账号规则
		Map selectedMap = new HashMap();
		Role r;
		for (Role role : roles) {
			r = this.roleDao.get(role.getId());
			if (null != r) {
				List<Function> l = this.roleDao.getRoleFunction(r,
						new Application().getId());
				for (Function f : l) {
					selectedMap.put(f.getCode(), f);
				}
			}
		}

		functions = str_a.split(SPLIT_CHAR);
		if (null != functions || functions.length > 0) {
			for (String f : functions) {
				if (null != selectedMap.get(f)) {
					needAccount = true;
					break;
				}
			}
		}
		// 根据权限，认为需要手动创建的账号
		if (needAccount) {
			//账号不变，则密码不变
			if( !user.getAccount().equals( account )){
				user.setAccount(account);
				user.setPassword(MD5.md5(AccountUtils.getRandomPassword()));
				userDao.updateUser(user);
			}
			// 判断是否需要创建 支付密码
			boolean needPaymentPassword = false;
			functions = str_ab.split(SPLIT_CHAR);
			if (null != functions || functions.length > 0) {
				for (String f : functions) {
					if (null != selectedMap.get(f)) {
						needPaymentPassword = true;
						break;
					}
				}
			}

			if (needPaymentPassword ) { //如果有权限，判断原来是否有支付密码，如果有，则不变化。如果没有，则保存默认支付密码
				
				if(  StringUtils.isBlank( oldObj.getPaymentPassword() ) ){
					employeeUser.setPaymentPassword(MD5.md5(AccountUtils.DEFAULT_PASSWORD));
				}else{
					//这一句代码应该执行不到，因为前台不会传递职务权限密码的
					employeeUser.setPaymentPassword( oldObj.getPaymentPassword() );
				}
			
			}

		} else {
			// 随机生成 16位小写字符串账号
			user.setAccount(RandomStringUtils.randomAlphabetic(16)
					.toLowerCase());
			user.setPassword(MD5.md5(AccountUtils.getRandomPassword()));
			userDao.updateUser(user);
			
			//取消 该员工的支付密码
			employeeUser.setPaymentPassword("");
		}
	
		//部分信息保留
		employeeUser.setBranchId( oldObj.getBranchId() );
		employeeUser.setBranchName( oldObj.getBranchName());
		
		return employeeUserDao.update(employeeUser);
	}

	@Override
	public int updateUserStatus(String user_id, boolean status) {
		if( status ){
			return employeeUserDao.updateUserStatus(user_id, Constants.ENABLE);
		}else{
			return employeeUserDao.updateUserStatus(user_id, Constants.DISABLE);
		}
		
	}
	/**
	   * 更新用户职务权限密码
	   * @param id
	   * @param oldPwd
	   * @param newPwd
	   * @return
	   */
	  public boolean updatePaymentPassword(String id, String oldPwd, String newPwd){
		  Map<String,Object> queryMap = new HashMap<String,Object>();
		  queryMap.put("id",id);
		  queryMap.put("paymentPassword", oldPwd);
		  List<EmployeeUser> userList = employeeUserDao.get(queryMap);
		  if(userList.size()==1){
			  EmployeeUser user = new EmployeeUser();
		      user.setId(id);
		      user.setPaymentPassword(newPwd);
		      employeeUserDao.update(user);
		  }else{
			  throw new BusinessException("修改密码失败：用户名或者旧密码错误");
		  }
		  return true;
	  }

	@Override
	public EmployeeUser get(String id) {
		EmployeeUser employee=employeeUserDao.get(id);
		
		//读取user对象
		if( !StringUtils.isBlank(employee.getUserId() ) ){
			User user=this.userDao.getUserById(employee.getUserId());
			employee.setUser(user);
		}
		//读取role
		Map<String ,Object> param = new HashMap();
		param.put("userId", employee.getUserId());
		List<UserRole> roleList=this.userRoleDao.queryUserRoleList(param);
		List<Role> roles=new ArrayList(0);
		
		for(UserRole ur:roleList){
			Role role=this.roleDao.get(ur.getRoleId());
			roles.add(role);
		}
		
		employee.setRoles(roles);
		
		return employee;
	}
	
	/**
	 * 获取用户,根据用户id
	 * @param id
	 * @return
	 */
	public EmployeeUser getUserById(String userId) {
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("user_id", userId);
		List<EmployeeUser> employeeList = employeeUserDao.get(queryMap);
		if(employeeList.size()>0){
			EmployeeUser employee = employeeList.get(0);
			//读取用户
			User user=this.userDao.getUserById(userId);
			employee.setUser(user);
			//读取role
			Map<String ,Object> param = new HashMap();
			param.put("userId", employee.getUserId());
			List<UserRole> roleList=this.userRoleDao.queryUserRoleList(param);
			List<Role> roles=new ArrayList(0);
			
			for(UserRole ur:roleList){
				Role role=this.roleDao.get(ur.getRoleId());
				roles.add(role);
			}
			employee.setRoles(roles);
			
			return employee;
		}else{
			return null;
		}
	}

	@Override
	public int delete(String id) {
		EmployeeUser emp= this.employeeUserDao.get(id);
		Map<String ,Object> param=new HashMap();
		param.put("userId", emp.getUserId());
		this.userRoleDao.deleteUserRole(param);
		//删除员工信息数据
		employeeUserDao.delete(id);
		//删除用户信息数据
		
		return this.userDao.deleteUserById( emp.getUserId());
	}

	@Override
	public Map<String, Object> getBranchById(String branch_id) {
		return this.employeeUserDao.getBranchById(branch_id);
	}

	@Override
	public List<EmployeeUser> findAllServiceUserForCurrentStore()  {
		Map currentStoreMap= this.roleDao.getCurrentStoreInfo();
		List<EmployeeUser> l=new ArrayList();
		if(null!=currentStoreMap){
			String branchid=(String) currentStoreMap.get("branchid");
			String waiterFuns=PropertiesUtils.getValue("employee.role.waiter");
			//l=this.employeeUserDao.getEmployeeUserByRole4Store(serviceRoleId, branchid);
			String[] funs=waiterFuns.split(",");
			List<Role> roles=this.roleDao.getRoleListByFunctionsCodes( Arrays.asList(funs));
			l=this.employeeUserDao.getEmployeeUserByRoles4Store(roles, branchid);
		}else{
			throw new RuntimeException("未设置默认门店！");
		}
		return l;
		
	}

	@Override
	public boolean isExistJobNumberExcludeUser(String jobNumber, String userId , String branch_id) {
		
		List<EmployeeUser> list=this.employeeUserDao.getEmployeeUser4Store(branch_id);
		boolean isExist=false;
		for(EmployeeUser eu : list){
			if( !StringUtils.isBlank(userId)){ //排除某个用户
				if( eu.getJobNumber().equals(jobNumber) && !eu.getUserId().equals(userId)){
					isExist=true;
					break;
				}
			}else{
				if( eu.getJobNumber().equals(jobNumber)){
					isExist=true;
					break;
				}
			}
		}
		
		return isExist;
	}

	@Override
	public boolean isExistBackAccountExcludeUser(String backAccount,
			String userId, String branch_id) {
		//List<EmployeeUser> list=this.employeeUserDao.getEmployeeUser4Store(branch_id);
		//List<EmployeeUser> list=this.employeeUserDao.getAll();
		List<User> list=this.userDao.queryUserList(new HashMap());
		boolean isExist=false;
//		for(EmployeeUser eu : list){
//			if( !StringUtils.isBlank(userId)){ //排除某个用户
//				if( !StringUtils.isBlank(eu.getUser().getAccount() ) && eu.getUser().getAccount().equals(backAccount) && !eu.getUserId().equals(userId)){
//					isExist=true;
//					break;
//				}
//			}else{
//				if( !StringUtils.isBlank(eu.getUser().getAccount() ) && eu.getUser().getAccount().equals(backAccount) ){
//					isExist=true;
//					break;
//				}
//			}
//		}
		
		for(User u : list){
			if( !StringUtils.isBlank(userId)){ //排除某个用户
				if( !StringUtils.isBlank(u.getAccount() ) && u.getAccount().equals(backAccount) && !u.getId().equals(userId)){
					isExist=true;
					break;
				}
			}else{
				if( !StringUtils.isBlank(u.getAccount() ) && u.getAccount().equals(backAccount) ){
					isExist=true;
					break;
				}
			}
		}
		return isExist;
	}

	@Override
	public EmployeeUser getByParams(Map<String, Object> map) {
		return employeeUserDao.getByparams(map);
	}

}
