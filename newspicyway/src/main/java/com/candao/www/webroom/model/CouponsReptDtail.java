package com.candao.www.webroom.model;

import java.math.BigDecimal;


public class CouponsReptDtail {
	private String BaseCom; 
	private String couponsname;
	private String insertTime;
	private String orderid;
	private String code;
	private BigDecimal Payamount; 
	private BigDecimal Total;
	private Integer Num;
	private BigDecimal Shishou;
	private BigDecimal Yinshou;
	public String getBaseCom() {
		return BaseCom;
	}
	public void setBaseCom(String baseCom) {
		BaseCom = baseCom;
	}
	public Integer getNum() {
		return Num;
	}
	public void setNum(Integer num) {
		Num = num;
	}
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public BigDecimal getPayamount() {
		return Payamount;
	}
	public void setPayamount(BigDecimal payamount) {
		Payamount = payamount;
	}
	public BigDecimal getTotal() {
		return Total;
	}
	public void setTotal(BigDecimal total) {
		Total = total;
	}
	public BigDecimal getShishou() {
		return Shishou;
	}
	public void setShishou(BigDecimal shishou) {
		Shishou = shishou;
	}
	public BigDecimal getYinshou() {
		return Yinshou;
	}
	public void setYinshou(BigDecimal yinshou) {
		Yinshou = yinshou;
	}
	public String getCouponsname() {
		return couponsname;
	}
	public void setCouponsname(String couponsname) {
		this.couponsname = couponsname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
