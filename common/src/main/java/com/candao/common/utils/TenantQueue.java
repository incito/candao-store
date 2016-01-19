package com.candao.common.utils;

import java.util.Date;

public class TenantQueue implements java.io.Serializable{

	 
	private static final long serialVersionUID = 1L;

	private String nodename;
	
	private String tenantid;
	
	private String sendqueue;
	
	private String receivequeue;
	
	private Date inserttime;
	
	private String tenantAccount;
	
	private String userAccount;
	
	private String userId;
	
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTenantAccount() {
		return tenantAccount;
	}

	public void setTenantAccount(String tenantAccount) {
		this.tenantAccount = tenantAccount;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getNodename() {
		return nodename;
	}

	public void setNodename(String nodename) {
		this.nodename = nodename;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	public String getSendqueue() {
		return sendqueue;
	}

	public void setSendqueue(String sendqueue) {
		this.sendqueue = sendqueue;
	}

	public String getReceivequeue() {
		return receivequeue;
	}

	public void setReceivequeue(String receivequeue) {
		this.receivequeue = receivequeue;
	}

	public Date getInserttime() {
		return inserttime;
	}

	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}
	
	
	 
}
