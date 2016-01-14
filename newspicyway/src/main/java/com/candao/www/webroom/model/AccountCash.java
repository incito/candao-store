package com.candao.www.webroom.model;

import java.io.Serializable;

public class AccountCash implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6105895319155555159L;

	private int actionType;
	
	private String userName;
	
	private String changeUserName;

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChangeUserName() {
		return changeUserName;
	}

	public void setChangeUserName(String changeUserName) {
		this.changeUserName = changeUserName;
	}
	
	
}
