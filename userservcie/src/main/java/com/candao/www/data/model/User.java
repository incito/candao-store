package com.candao.www.data.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 
 * 基本用户-实体类
 * @author lishoukun
 * @date 2015/04/21
 */
public class User implements Serializable {
	
	// 序列化
	private static final long serialVersionUID = 6905308258132311722L;
	// id,id
	private String id;
	// 帐号,account
	private String account;
	// 密码,password
	private String password;
	// 姓名,name
	private String name;
	// 邮箱,email
	private String email;
	// 手机号,mobile
	private String mobile;
	// 区号,area_code
	private String areaCode;
	// 固定电话,telephone
	private String telephone;
	// 状态(0为否，1为是),status
	private String status;
	// 创建时间,createtime
	private Timestamp createtime;
	// 创建人,creator
	private String creator;
	// 用户类型,user_type  类型参见 Constants中几种用户类型描述
	private String userType;
	
	private String channelType;
	
	
	private String tenantid;
	
	
	
	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	/**
	 * 排序。
	 */
	private int orderNum ;
	
	private Timestamp lastlogintime;

	// 角色信息列表,与数据库字段无关
	private List<UserRole> roles;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	

	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public Timestamp getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Timestamp lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
}
 