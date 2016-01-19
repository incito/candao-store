package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 
 * tb_user_role表
 * @author mew
 *
 */
public class TbUserRole implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1674248100691168739L;
	private java.lang.String roleid; // 角色标识
	private java.lang.String userid; // 用户标识

	/**
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
	

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TbUserRole");
        sb.append("{roleid=").append(roleid);
        sb.append(", userid=").append(userid);
		sb.append('}');
        return sb.toString();
    }
}