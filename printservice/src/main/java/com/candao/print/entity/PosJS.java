package com.candao.print.entity;

import java.io.Serializable;

public class PosJS implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5126480116306879522L;

	private String bankcardno;

	private String couponNum;

	private String incometype;

	private String itemDesc;

	private String membercardno;

	private String orderid;

	private String payamount;

	private String payway;

	public String getBankcardno() {
		return bankcardno;
	}

	public void setBankcardno(String bankcardno) {
		this.bankcardno = bankcardno;
	}

	public String getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(String couponNum) {
		this.couponNum = couponNum;
	}

	public String getIncometype() {
		return incometype;
	}

	public void setIncometype(String incometype) {
		this.incometype = incometype;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getMembercardno() {
		return membercardno;
	}

	public void setMembercardno(String membercardno) {
		this.membercardno = membercardno;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getPayamount() {
		return payamount;
	}

	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

}
