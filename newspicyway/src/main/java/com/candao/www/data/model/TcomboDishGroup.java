package com.candao.www.data.model;

import java.util.Date;
import java.util.List;

public class TcomboDishGroup {
	private String id;

	private String dishid;

	private Integer status;

	private Date insertime;

	private String insertuserid;
	/**
	 * 排序
	 */
	private int ordernum;

	private String selecttype;
	/**
	 * 分类
	 */
	private String columnid;
	private String columnname;
	private String itemDesc;
	/**
	 * 5-2 表示 5选2 填写 5
	 */
	private Integer startnum;
	/**
	 * 5-2 表示 5选2 填写 2
	 */
	private Integer endnum;
	
	private List<TgroupDetail> groupDetailList;




	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getColumnid() {
		return columnid;
	}

	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public List<TgroupDetail> getGroupDetailList() {
		return groupDetailList;
	}

	public void setGroupDetailList(List<TgroupDetail> groupDetailList) {
		this.groupDetailList = groupDetailList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDishid() {
		return dishid;
	}

	public void setDishid(String dishid) {
		this.dishid = dishid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getInsertime() {
		return insertime;
	}

	public void setInsertime(Date insertime) {
		this.insertime = insertime;
	}

	public String getInsertuserid() {
		return insertuserid;
	}

	public void setInsertuserid(String insertuserid) {
		this.insertuserid = insertuserid;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public String getSelecttype() {
		return selecttype;
	}

	public void setSelecttype(String selecttype) {
		this.selecttype = selecttype;
	}



	public Integer getStartnum() {
		return startnum;
	}

	public void setStartnum(Integer startnum) {
		this.startnum = startnum;
	}

	public Integer getEndnum() {
		return endnum;
	}

	public void setEndnum(Integer endnum) {
		this.endnum = endnum;
	}
}