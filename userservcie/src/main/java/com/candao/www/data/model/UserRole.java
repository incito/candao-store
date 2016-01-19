package com.candao.www.data.model;

import java.io.Serializable;


/**
 * 
 * 用户角色关联-用户角色关联-实体类
 * @author lishoukun
 * @date 2015/04/21
 */
public class UserRole implements Serializable{
	//序列化
	private static final long serialVersionUID = 6905308258132311722L;
    //id,id
    private String id ;
    //用户id,user_id
    private String userId ;
    //角色id,role_id
    private String roleId ;
    public String getId(){
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    public String getUserId(){
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    public String getRoleId(){
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
