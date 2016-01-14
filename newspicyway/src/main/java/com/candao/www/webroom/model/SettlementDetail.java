package com.candao.www.webroom.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class SettlementDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3863967272173112558L;
	
	private String payWay;
	
	private BigDecimal payAmount;
	
	private String memerberCardNo;
	
	private String bankCardNo;
	
	private String debitParterner;
	
	private int couponnum;
	
	private String couponid;
	
	private String coupondetailid;
	
	
	
	
	public String getCoupondetailid() {
		return coupondetailid;
	}

	public void setCoupondetailid(String coupondetailid) {
		this.coupondetailid = coupondetailid;
	}

	public String getCouponid() {
		return couponid;
	}

	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}

	public int getCouponnum() {
		return couponnum;
	}

	public void setCouponnum(int couponnum) {
		this.couponnum = couponnum;
	}

	public String getDebitParterner() {
		return debitParterner;
	}

	public void setDebitParterner(String debitParterner) {
		this.debitParterner = debitParterner;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getMemerberCardNo() {
		return memerberCardNo;
	}

	public void setMemerberCardNo(String memerberCardNo) {
		this.memerberCardNo = memerberCardNo;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

}
