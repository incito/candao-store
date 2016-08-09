package com.candao.print.entity;

public class TbPrinterDetail {
	private String id;
	private String printerid;
	private String dishid;
	private Integer status;
	private String columnid;
	private String groupSequence;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrinterid() {
		return printerid;
	}
	public void setPrinterid(String printerid) {
		this.printerid = printerid;
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
	public String getColumnid() {
		return columnid;
	}
	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}
	public String getGroupSequence() {
		return groupSequence;
	}
	public void setGroupSequence(String groupSequence) {
		this.groupSequence = groupSequence;
	}
	
}
