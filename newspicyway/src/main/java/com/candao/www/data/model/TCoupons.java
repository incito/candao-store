package com.candao.www.data.model;

import java.math.BigDecimal;
import java.util.Date;

public class TCoupons  extends TCouponRule{
	/**
	 * 主键
	 */
	private String couponid;
	/**
	 * 优惠的大分类 对应字典项中的id 
	 * 1.菜品优惠 	 7.特价  8.满就减  9.满就送
       2.订单优惠 	 10.满就减 11.满就送
       3.储值优惠 	 
       4.团购优惠 	
       5.信用卡优惠 	
       6.其他优惠            12.优惠券 13内部员工优惠 14 合作单位优惠 
	 */
	private String couponparent;
	/**
	 * 优惠的小分类
	 * 7.特价  8.满就减  9.满就送
	 * 10.满就减 11.满就送
	 * 12.优惠券 13内部员工优惠 14 合作单位优惠 
	 */
	private String couponchild;
	/**
	 * 0 PAD端 1 POS 端
	 */
	private Integer showposition;
	/**
	 * 优惠对象 0 所有顾客 1 仅限会员
	 */
	private Integer couponcustomer;
	/**
	 * 优惠名称
	 */
	private String couponname;
	/**
	 * 活动简介
	 */
	private String description;

	/**
	 * 整单 满金额
	 */
	private BigDecimal totalamount;
	/**
	 * 间隔 每周 1,3,5 每月 12,13,15
	 */
	private String couponperiod;
	/**
	 * 0 每天 1 每周2 每月
	 */
	private Integer coupontype;
	/**
	 * 开始时间
	 */
	private String begintime;
	/**
	 * 结束时间
	 */
	private String endtime;
	/**
	 * 用户id 暂时没用到的字段
	 */
	private String userid;
	/**
	 * 插入时间
	 */
	private Date inserttime;
	/**
	 * 是否可用 0可用 1不可用
	 */
	private Integer status;

	/**
	 * 规则描述
	 */
	private String ruledescription;
	
    private String wholesingle;
    
    private String parternerid;
    
    

	public String getParternerid() {
		return parternerid;
	}

	public void setParternerid(String parternerid) {
		this.parternerid = parternerid;
	}

	public String getRuledescription() {
		return ruledescription;
	}

	public void setRuledescription(String ruledescription) {
		this.ruledescription = ruledescription;
	}

	public String getWholesingle() {
		return wholesingle;
	}

	public void setWholesingle(String wholesingle) {
		this.wholesingle = wholesingle;
	}

	public String getCouponid() {
		return couponid;
	}

	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}

	public String getCouponparent() {
		return couponparent;
	}

	public void setCouponparent(String couponparent) {
		this.couponparent = couponparent;
	}

	public String getCouponchild() {
		return couponchild;
	}

	public void setCouponchild(String couponchild) {
		this.couponchild = couponchild;
	}

	public Integer getShowposition() {
		return showposition;
	}

	public void setShowposition(Integer showposition) {
		this.showposition = showposition;
	}

	public Integer getCouponcustomer() {
		return couponcustomer;
	}

	public void setCouponcustomer(Integer couponcustomer) {
		this.couponcustomer = couponcustomer;
	}

	public String getCouponname() {
		return couponname;
	}

	public void setCouponname(String couponname) {
		this.couponname = couponname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}

	public String getCouponperiod() {
		return couponperiod;
	}

	public void setCouponperiod(String couponperiod) {
		this.couponperiod = couponperiod;
	}

	public Integer getCoupontype() {
		return coupontype;
	}

	public void setCoupontype(Integer coupontype) {
		this.coupontype = coupontype;
	}


	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Date getInserttime() {
		return inserttime;
	}

	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}