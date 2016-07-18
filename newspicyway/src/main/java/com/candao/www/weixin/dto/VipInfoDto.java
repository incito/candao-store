package com.candao.www.weixin.dto;

import java.io.Serializable;

public class VipInfoDto implements Serializable{

	private String branch_id;
	
	private String securityCode;
	
	private String cardno;
	
	private String password;
	
	private String Serial;
	
	private String FCash;
	
	private String FIntegral;
	
	private String FStore;
	
	private String FTicketList;
	
	private String FWeChat;

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSerial() {
		return Serial;
	}

	public void setSerial(String serial) {
		Serial = serial;
	}

	public String getFCash() {
		return FCash;
	}

	public void setFCash(String fCash) {
		FCash = fCash;
	}

	public String getFIntegral() {
		return FIntegral;
	}

	public void setFIntegral(String fIntegral) {
		FIntegral = fIntegral;
	}

	public String getFStore() {
		return FStore;
	}

	public void setFStore(String fStore) {
		FStore = fStore;
	}

	public String getFTicketList() {
		return FTicketList;
	}

	public void setFTicketList(String fTicketList) {
		FTicketList = fTicketList;
	}

	public String getFWeChat() {
		return FWeChat;
	}

	public void setFWeChat(String fWeChat) {
		FWeChat = fWeChat;
	}

	@Override
	public String toString() {
		return "[branch_id=" + branch_id + ", securityCode=" + securityCode + ", cardno=" + cardno
				+ ", password=" + password + ", Serial=" + Serial + ", FCash=" + FCash + ", FIntegral=" + FIntegral
				+ ", FStore=" + FStore + ", FTicketList=" + FTicketList + ", FWeChat=" + FWeChat + "]";
	}
	
	
}
