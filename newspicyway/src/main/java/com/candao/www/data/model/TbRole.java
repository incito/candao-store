package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 
 * tb_role表
 * @author mew
 *
 */
public class TbRole implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3073943981129022237L;
	private java.lang.String roleid; // 角色标识
	private java.lang.String userid; // 创建人
	private java.lang.String rolename; // 角色名称
	private java.lang.String roledesc; // 角色描述
	private java.lang.Integer roletype; // 角色类型  1 系统  2 用户定义
	private java.lang.String createtime; // 创建时间
	private java.lang.Integer status; // 状态   0：无效  1：有效
	/**
	 * 有效
	 */
	public static final int STATUS_ENABLE=1;
	
	/**
	 * 失效
	 */
	public static final int STATUS_DISABLE = 0;
	/**;
     * 获取角色标识属性
     *
     * @return roleid
     */
	public java.lang.String getRoleid() {
		return roleid;
	}
	
	/**
	 * 设置角色标识属性
	 *
	 * @param roleid
	 */
	public void setRoleid(java.lang.String roleid) {
		this.roleid = roleid;
	}
	
	/**
     * 获取创建人属性
     *
     * @return userid
     */
	public java.lang.String getUserid() {
		return userid;
	}
	
	/**
	 * 设置创建人属性
	 *
	 * @param userid
	 */
	public void setUserid(java.lang.String userid) {
		this.userid = userid;
	}
	
	/**
     * 获取角色名称属性
     *
     * @return rolename
     */
	public java.lang.String getRolename() {
		return rolename;
	}
	
	/**
	 * 设置角色名称属性
	 *
	 * @param rolename
	 */
	public void setRolename(java.lang.String rolename) {
		this.rolename = rolename;
	}
	
	/**
     * 获取角色描述属性
     *
     * @return roledesc
     */
	public java.lang.String getRoledesc() {
		return roledesc;
	}
	
	/**
	 * 设置角色描述属性
	 *
	 * @param roledesc
	 */
	public void setRoledesc(java.lang.String roledesc) {
		this.roledesc = roledesc;
	}
	
	/**
     * 获取角色类型 01 系统 02 用户定义属性
     *
     * @return roletype
     */
	public java.lang.Integer getRoletype() {
		return roletype;
	}
	
	/**
	 * 设置角色类型 01 系统 02 用户定义属性
	 *
	 * @param roletype
	 */
	public void setRoletype(java.lang.Integer roletype) {
		this.roletype = roletype;
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
	
	/**
     * 获取状态   0：无效  1：有效属性
     *
     * @return status
     */
	public java.lang.Integer getStatus() {
		return status;
	}
	
	/**
	 * 设置状态   0：无效  1：有效属性
	 *
	 * @param status
	 */
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TbRole");
        sb.append("{roleid=").append(roleid);
        sb.append(", userid=").append(userid);
        sb.append(", rolename=").append(rolename);
        sb.append(", roledesc=").append(roledesc);
        sb.append(", roletype=").append(roletype);
        sb.append(", createtime=").append(createtime);
        sb.append(", status=").append(status);
		sb.append('}');
        return sb.toString();
    }
}