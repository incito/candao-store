package com.candao.www.security.model;

import java.io.Serializable;

public class UserRole implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -2183606321840238421L;
private String userid;
private String[] roleids;
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String[] getRoleids() {
	return roleids;
}
public void setRoleids(String[] roleids) {
	this.roleids = roleids;
}

}
