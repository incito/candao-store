package com.candao.www.webroom.model;

import java.io.Serializable;
import java.math.BigDecimal;
public class DataStatistics implements Serializable{
	private static final long serialVersionUID = 4162463104833001343L;
	private String shiftId;
	private String tableId;
	private String area;
	private String areaId;
	private String dateTime;
	private String name;
	private BigDecimal values;
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getValues() {
		return values;
	}
	public void setValues(BigDecimal values) {
		this.values = values;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
}
