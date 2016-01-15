package com.candao.www.data.model;

import java.math.BigDecimal;

public class TCouponRule {
	/**
	 * 主键
	 */
	private String ruleid;
	/**
	 * 优惠表的主键
	 */
	private String couponid;
	/**
	 * 菜品id
	 */
	private String dishid;
	/**
	 * 满足菜品的份数
	 */
	private Integer dishnum;
	/**
	 * 免费的菜品id
	 */
	private String freedishid;
	/**
	 * 免费的菜品数量
	 */
	private Integer freedishnum;
	/**
	 * 优惠方式 0 单品打折比率 1 打折金额 2 储值赠送 3 代金券赠送 4 整单打折
	 */
	private Integer couponway;
	/**
	 * 消费方式 0 储值优惠 1 消费优惠
	 */
	private Integer comsumeway;
	/**
	 * 打折比率
	 */
	private BigDecimal couponrate;
	/**
	 * 打折金额
	 */
	private BigDecimal couponamount;
	/**
	 * 整单金额
	 */
	private BigDecimal totalamount;
	/**
	 * 赠送代金券金额
	 */
	private BigDecimal couponcash;
	/**
	 * 可使用代金券数量
	 */
	private Integer couponnum;
	/**
	 * 代金券每张抵用金额
	 */
	private BigDecimal freeamount;
	/**
	 * 银行类型
	 */
	private String banktype;
	/**
	 * 合作单位
	 */
	private String partnername;
	/**
	 * 团购网站
	 */
	private String groupweb;
	/**
	 * 计量单位
	 */
	private String unitid;
	
	/**
	 * 挂账金额
	 */
	private BigDecimal debitamount;

	public String getRuleid() {
		return ruleid;
	}

	public void setRuleid(String ruleid) {
		this.ruleid = ruleid;
	}

	public String getCouponid() {
		return couponid;
	}

	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}

	public String getDishid() {
		return dishid;
	}

	public void setDishid(String dishid) {
		this.dishid = dishid;
	}

	public Integer getDishnum() {
		return dishnum;
	}

	public void setDishnum(Integer dishnum) {
		this.dishnum = dishnum;
	}

	public String getFreedishid() {
		return freedishid;
	}

	public void setFreedishid(String freedishid) {
		this.freedishid = freedishid;
	}

	public Integer getFreedishnum() {
		return freedishnum;
	}

	public void setFreedishnum(Integer freedishnum) {
		this.freedishnum = freedishnum;
	}

	public Integer getCouponway() {
		return couponway;
	}

	public void setCouponway(Integer couponway) {
		this.couponway = couponway;
	}

	public Integer getComsumeway() {
		return comsumeway;
	}

	public void setComsumeway(Integer comsumeway) {
		this.comsumeway = comsumeway;
	}

	public BigDecimal getCouponrate() {
		return couponrate;
	}

	public void setCouponrate(BigDecimal couponrate) {
		this.couponrate = couponrate;
	}

	public BigDecimal getCouponamount() {
		return couponamount;
	}

	public void setCouponamount(BigDecimal couponamount) {
		this.couponamount = couponamount;
	}

	public BigDecimal getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}

	public BigDecimal getCouponcash() {
		return couponcash;
	}

	public void setCouponcash(BigDecimal couponcash) {
		this.couponcash = couponcash;
	}

	public Integer getCouponnum() {
		return couponnum;
	}

	public void setCouponnum(Integer couponnum) {
		this.couponnum = couponnum;
	}

	public BigDecimal getFreeamount() {
		return freeamount;
	}

	public void setFreeamount(BigDecimal freeamount) {
		this.freeamount = freeamount;
	}

	public String getBanktype() {
		return banktype;
	}

	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}

	public String getPartnername() {
		return partnername;
	}

	public void setPartnername(String partnername) {
		this.partnername = partnername;
	}

	public String getGroupweb() {
		return groupweb;
	}

	public void setGroupweb(String groupweb) {
		this.groupweb = groupweb;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public BigDecimal getDebitamount() {
		return debitamount;
	}

	public void setDebitamount(BigDecimal debitamount) {
		this.debitamount = debitamount;
	}
	
}