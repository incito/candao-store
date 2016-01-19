package com.candao.www.webroom.model;

import java.math.BigDecimal;

public class CouponsRept {
	private String BaseCom; 
	private String Paytype; 
	private String orderid;
	private String couponsname;
	private String couponsnameType;
	private String payway;
	private String code;
	private String type;
	private String typeName;
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
	public String getPaytype() {
		return Paytype;
	}
	public void setPaytype(String paytype) {
		Paytype = paytype;
	}
	public Integer getNum() {
		return Num;
	}
	public void setNum(Integer num) {
		Num = num;
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
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getCouponsname() {
		return couponsname;
	}
	public void setCouponsname(String couponsname) {
		this.couponsname = couponsname;
	}
	public String getPayway() {
		return payway;
	}
	public void setPayway(String payway) {
		this.payway = payway;
	}
	public String getCouponsnameType() {
		return couponsnameType;
	}
	public void setCouponsnameType(String couponsnameType) {
		this.couponsnameType = couponsnameType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}

