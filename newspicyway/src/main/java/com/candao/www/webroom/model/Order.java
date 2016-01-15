package com.candao.www.webroom.model;

import java.util.List;

import com.candao.www.data.model.TorderDetail;

public class Order implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8233310669582906554L;
	
    private String orderid;

    private String userid;

    private Integer orderstatus;

    private Integer custnum;
    
    private String tableids;

    private String currenttableid;
 
    private String globalsperequire;
    
    List<TorderDetail> rows ;
    
	private int operationType;//（1：下单;2 :退菜 3：并台  4换台） 
	
	private String sequence;//顺序
	




	public int getOperationType() {
		return operationType;
	}


	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}




	public String getSequence() {
		return sequence;
	}


	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	public String getGlobalsperequire() {
		return globalsperequire;
	}


	public void setGlobalsperequire(String globalsperequire) {
		this.globalsperequire = globalsperequire;
	}


	public String getOrderid() {
		return orderid;
	}


	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public Integer getOrderstatus() {
		return orderstatus;
	}


	public void setOrderstatus(Integer orderstatus) {
		this.orderstatus = orderstatus;
	}


	public Integer getCustnum() {
		return custnum;
	}


	public void setCustnum(Integer custnum) {
		this.custnum = custnum;
	}


	public String getTableids() {
		return tableids;
	}


	public void setTableids(String tableids) {
		this.tableids = tableids;
	}




	public String getCurrenttableid() {
		return currenttableid;
	}


	public void setCurrenttableid(String currenttableid) {
		this.currenttableid = currenttableid;
	}


	public List<TorderDetail> getRows() {
		return rows;
	}
	public void setRows(List<TorderDetail> rows) {
		this.rows = rows;
	}

}
