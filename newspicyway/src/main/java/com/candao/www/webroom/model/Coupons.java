package com.candao.www.webroom.model;

import java.io.Serializable;
import java.util.List;

import com.candao.www.data.model.TCouponRule;

public class Coupons implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4162463104833001343L;
	
	private String dishid;
	
	private String dishname;
	
	private String ordernum ;
	
	private String period;
	
	private String desc;
	
	private String ruleDesc;
	
	private String label;
	
	private List<DishUnitPrice> unitprices ;
	
	private List<TCouponRule> coupons;
	
	
	
 public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

public String getDishid() {
		return dishid;
	}

	public void setDishid(String dishid) {
		this.dishid = dishid;
	}

	public String getDishname() {
		return dishname;
	}

	public void setDishname(String dishname) {
		this.dishname = dishname;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public List<DishUnitPrice> getUnitprices() {
		return unitprices;
	}

	public void setUnitprices(List<DishUnitPrice> unitprices) {
		this.unitprices = unitprices;
	}

 

public List<TCouponRule> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<TCouponRule> coupons) {
		this.coupons = coupons;
	}



public  class DishUnitPrice {
	 
	 private String unitid;
	 
	 private String unitname;
	 
	 private String unitprice;
	 
	 private String unitvipprice;

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}

	public String getUnitvipprice() {
		return unitvipprice;
	}

	public void setUnitvipprice(String unitvipprice) {
		this.unitvipprice = unitvipprice;
	}
  
 }
	 
 public class DishCoupon{
 
    	 
    private String dishnum;
 
    private String couponname;
    
    private String dishunit;
    
    private String freedishid;
    
    private String freedishnum;
    
    private String couponway;
    
    private String couponrate;

	public String getDishnum() {
		return dishnum;
	}

	public void setDishnum(String dishnum) {
		this.dishnum = dishnum;
	}

	public String getCouponname() {
		return couponname;
	}

	public void setCouponname(String couponname) {
		this.couponname = couponname;
	}

	public String getDishunit() {
		return dishunit;
	}

	public void setDishunit(String dishunit) {
		this.dishunit = dishunit;
	}

	public String getFreedishid() {
		return freedishid;
	}

	public void setFreedishid(String freedishid) {
		this.freedishid = freedishid;
	}

	public String getFreedishnum() {
		return freedishnum;
	}

	public void setFreedishnum(String freedishnum) {
		this.freedishnum = freedishnum;
	}

	public String getCouponway() {
		return couponway;
	}

	public void setCouponway(String couponway) {
		this.couponway = couponway;
	}

	public String getCouponrate() {
		return couponrate;
	}

	public void setCouponrate(String couponrate) {
		this.couponrate = couponrate;
	}
 }
	   
}
