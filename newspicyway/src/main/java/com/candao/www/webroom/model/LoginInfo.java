package com.candao.www.webroom.model;

import java.io.Serializable;
import java.sql.Date;

public class LoginInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4524264817808181891L;
	
	String userId;
	
//	String userName;
	
	/** The username. */
	  String username;
	/** The password. */
	  String password;
	
	
	String macAddress;
	
	String padLicenceNo;
	
	Date  loginDate;
	
	String loginType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

 
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getPadLicenceNo() {
		return padLicenceNo;
	}

	public void setPadLicenceNo(String padLicenceNo) {
		this.padLicenceNo = padLicenceNo;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
	

}
