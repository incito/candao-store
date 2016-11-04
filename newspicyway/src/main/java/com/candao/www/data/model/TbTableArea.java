package com.candao.www.data.model;


import java.text.DecimalFormat;
import java.util.List;

public class TbTableArea {
	private java.lang.String areaid; 
	private java.lang.Integer areaNo; 
	private java.lang.String areaname;
	private java.lang.Integer status;
	private java.lang.String custPrinter;
	private java.lang.Integer areaSort;
	private java.lang.String branchid;
	private List<TbTable> tables;

	public List<TbTable> getTables() {
		return tables;
	}
	public void setTables(List<TbTable> tables) {
		this.tables = tables;
	}
	public java.lang.String getBranchid() {
		return branchid;
	}
	public void setBranchid(java.lang.String branchid) {
		this.branchid = branchid;
	}
	public java.lang.Integer getAreaSort() {
		return areaSort;
	}
	public void setAreaSort(java.lang.Integer areaSort) {
		this.areaSort = areaSort;
	}
	public java.lang.String getCustPrinter() {
		return custPrinter;
	}
	public void setCustPrinter(java.lang.String custPrinter) {
		this.custPrinter = custPrinter;
	}
	public java.lang.String getAreaid() {
		return areaid;
	}
	public void setAreaid(java.lang.String areaid) {
		this.areaid = areaid;
	}
	public java.lang.Integer getAreaNo() {
		return areaNo;
	}
	public void setAreaNo(java.lang.Integer areaNo) {
		this.areaNo = areaNo;
	}
	public java.lang.String getAreaname() {
		return areaname;
	}
	public void setAreaname(java.lang.String areaname) {
		this.areaname = areaname;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("areaid");
        sb.append("{areaNo=").append(areaNo);
        sb.append(", areaname=").append(areaname);
        sb.append(", custPrinter=").append(custPrinter);
        sb.append(", areaSort=").append(areaSort);
		sb.append('}');
        return sb.toString();
    }
	
}
