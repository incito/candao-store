package com.candao.www.webroom.model;

import java.util.Date;

/**
 * 品项销售报表
 * @author weizhifang
 * @since 2015-5-15
 *
 */
public class ItemDetail implements java.io.Serializable {

	private static final long serialVersionUID = -6798425999927738529L;
	private String tbId;            
	private String dishId;               //菜ID
	private Integer itemId;              //品类编号
	private String itemDesc;             //品类名称
	private String itemProNo;            //品项编号 '0 单品 1 火锅 2 套餐',
	private String itemProName;          //品项名称 
	private Float price;                 //单价
	private String unit;                 //单位
	private String number;               //数量
	private Float amount;                //金额
	private Float share;                 //份额
	private String title;                //菜名
	private String shiftId;              //营业时间类型   0早市  2晚班
	private Date insertDate;             //时间
	private Date insertTimeStartDate;    //查询起始时间
	private Date insertTimeEndDate;      //查询结束时间
	private String dateType;             //日期类型
	
	public String getTbId() {
		return tbId;
	}
	public void setTbId(String tbId) {
		this.tbId = tbId;
	}
	public String getDishId() {
		return dishId;
	}
	public void setDishId(String dishId) {
		this.dishId = dishId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getItemProNo() {
		return itemProNo;
	}
	public void setItemProNo(String itemProNo) {
		this.itemProNo = itemProNo;
	}
	public String getItemProName() {
		return itemProName;
	}
	public void setItemProName(String itemProName) {
		this.itemProName = itemProName;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public Float getShare() {
		return share;
	}
	public void setShare(Float share) {
		this.share = share;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public Date getInsertTimeStartDate() {
		return insertTimeStartDate;
	}
	public void setInsertTimeStartDate(Date insertTimeStartDate) {
		this.insertTimeStartDate = insertTimeStartDate;
	}
	public Date getInsertTimeEndDate() {
		return insertTimeEndDate;
	}
	public void setInsertTimeEndDate(Date insertTimeEndDate) {
		this.insertTimeEndDate = insertTimeEndDate;
	}
	
}
