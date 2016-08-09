package com.candao.print.entity;

import java.util.Date;

public class TbPrinterArea {
	private  String id; 
	private String areaid;
	private String printerid;
	private Date inserttime;
	private String tableid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAreaid() {
		return areaid;
	}
	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	public String getPrinterid() {
		return printerid;
	}
	public void setPrinterid(String printerid) {
		this.printerid = printerid;
	}
	public Date getInserttime() {
		return inserttime;
	}
	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}
	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	
}
