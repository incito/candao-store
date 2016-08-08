/**
 * 
 */
package com.candao.www.data.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhao
 *
 */
public class EmployeeUser  {

	private String id; // 
	private String userId; //用户基本信息id
	private String sex;    //性别   1：男， 0：女
	private Date birthdate;
	private String paymentPassword; //pad端支付密码
	private String jobNumber;    //员工工号，每个门店内唯一
	private String position; //职位
	private String homeAddress; //家庭住址
	private int branchId; //门店ID
	private String branchName;//门店名称
	private String creator ;// 创建人
	private Date createTime;//创建时间
	
	//[start] 与数据库字典无关，用作页面数据存放(VO)
	
	private User user; //关联的user对象
	
	private List<Role> roles ;//员工关联的 权限角色
	
	/**
	 * 用做判断用户是否有某个权限。
	 * key 是 code
	 * 
	 */
	private Map<String, Function> functions; //该员工对应的所有有权限的功能模块。
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	
	//[end]
	
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the birthdate
	 */
	public Date getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate
	 *            the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * @return the paymentPassword
	 */
	public String getPaymentPassword() {
		return paymentPassword;
	}

	/**
	 * @param paymentPassword
	 *            the paymentPassword to set
	 */
	public void setPaymentPassword(String paymentPassword) {
		this.paymentPassword = paymentPassword;
	}

	/**
	 * @return the jobNumber
	 */
	public String getJobNumber() {
		return jobNumber;
	}

	/**
	 * @param jobNumber
	 *            the jobNumber to set
	 */
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the homeAddress
	 */
	public String getHomeAddress() {
		return homeAddress;
	}

	/**
	 * @param homeAddress
	 *            the homeAddress to set
	 */
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	/**
	 * @return the branchId
	 */
	public int getBranchId() {
		return branchId;
	}

	/**
	 * @param branchId
	 *            the branchId to set
	 */
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName
	 *            the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Map<String, Function> getFunctions() {
		return functions;
	}

	public void setFunctions(Map<String, Function> functions) {
		this.functions = functions;
	}
	

}
