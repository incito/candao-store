package com.candao.www.webroom.model;

import java.math.BigDecimal;


public class Dishsalerept {
	
	private String itemDesc;
	private String itemID;
    private String dishtype;
    private String  pinNum;
    private String  pinName;
    private String   unit;
    private String  nums;
    private BigDecimal price; 
    private BigDecimal share;
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	public String getDishtype() {
		return dishtype;
	}
	public void setDishtype(String dishtype) {
		this.dishtype = dishtype;
	}
	public String getPinNum() {
		return pinNum;
	}
	public void setPinNum(String pinNum) {
		this.pinNum = pinNum;
	}
	public String getPinName() {
		return pinName;
	}
	public void setPinName(String pinName) {
		this.pinName = pinName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNums() {
		return nums;
	}
	public void setNums(String nums) {
		this.nums = nums;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getShare() {
		return share;
	}
	public void setShare(BigDecimal share) {
		this.share = share;
	}
}
