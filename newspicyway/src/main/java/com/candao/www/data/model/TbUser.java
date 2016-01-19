package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 
 * tb_user表
 * @author mew
 *
 */
public class TbUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5364648151834155949L;
	private java.lang.String userid; // 用户标识
	private java.lang.String username; // 登录名
	private java.lang.String password; // 用户密码
	private java.lang.String fullname; // 用户姓名
	private java.lang.String phone; // 联系电话
	private java.lang.String usertype;//用户类型 1：超级管理员 2：系统管理员  3：pad用户
	private java.lang.String createtime; // 创建时间
	private java.lang.Integer status; // 状态
	
	private String lastlogintime;
	/**
	 * 有效
	 */
	public static final int STATUS_ENABLE = 1;
	/**
	 * 失效
	 */
	public static final int STATUS_DISABLE = 0;
	/**
	 * 注销
	 */
	public static final int STATUS_DELETE = 2;
	
	//用户排序字段
	private Integer  ordernum;
	
	
	
	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	/**
     * 获取用户标识属性
     *
     * @return userid
     */
	public java.lang.String getUserid() {
		return userid;
	}
	
	/**
	 * 设置用户标识属性
	 *
	 * @param userid
	 */
	public void setUserid(java.lang.String userid) {
		this.userid = userid;
	}
	
	/**
     * 获取登录名属性
     *
     * @return username
     */
	public java.lang.String getUsername() {
		return username;
	}
	
	/**
	 * 设置登录名属性
	 *
	 * @param username
	 */
	public void setUsername(java.lang.String username) {
		this.username = username;
	}
	
	/**
     * 获取用户密码属性
     *
     * @return password
     */
	public java.lang.String getPassword() {
		return password;
	}
	
	/**
	 * 设置用户密码属性
	 *
	 * @param password
	 */
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	
	/**
     * 获取用户姓名属性
     *
     * @return fullname
     */
	public java.lang.String getFullname() {
		return fullname;
	}
	
	/**
	 * 设置用户姓名属性
	 *
	 * @param fullname
	 */
	public void setFullname(java.lang.String fullname) {
		this.fullname = fullname;
	}
	
	/**
     * 获取联系电话属性
     *
     * @return phone
     */
	public java.lang.String getPhone() {
		return phone;
	}
	
	/**
	 * 设置联系电话属性
	 *
	 * @param phone
	 */
	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	
	/**
     * 获取状态属性
     *
     * @return status
     */
	public java.lang.Integer getStatus() {
		return status;
	}
	
	/**
	 * 设置状态属性
	 *
	 * @param status
	 */
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	
	/**
     * 获取创建时间属性
     *
     * @return createtime
     */
	public java.lang.String getCreatetime() {
		return createtime;
	}
	
	/**
	 * 设置创建时间属性
	 *
	 * @param createtime
	 */
	public void setCreatetime(java.lang.String createtime) {
		this.createtime = createtime;
	}
	

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TbUser");
        sb.append("{userid=").append(userid);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", fullname=").append(fullname);
        sb.append(", phone=").append(phone);
        sb.append(", usertype=").append(usertype);
        sb.append(", createtime=").append(createtime);
        sb.append(", status=").append(status);
		sb.append('}');
        return sb.toString();
    }

	public java.lang.String getUsertype() {
		return usertype;
	}

	public void setUsertype(java.lang.String usertype) {
		this.usertype = usertype;
	}

	public String getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	
	
}