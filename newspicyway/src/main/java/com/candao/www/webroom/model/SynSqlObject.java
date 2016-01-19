package com.candao.www.webroom.model;
import java.io.Serializable;

public class SynSqlObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String id;

   public String branchid;
   
   public String generattime;
   
   public String sql;
   
   public int status;
   
   public String sqltext;
   
   public String path;
   
   String  flag;
   
   String sequenceNo;
   
   String tenantid;
   
   String result;
   
   int orderSeqno;
   
   

public int getOrderSeqno() {
	return orderSeqno;
}



public void setOrderSeqno(int orderSeqno) {
	this.orderSeqno = orderSeqno;
}



public String getResult() {
	return result;
}



public void setResult(String result) {
	this.result = result;
}



public String getTenantid() {
	return tenantid;
}



public void setTenantid(String tenantid) {
	this.tenantid = tenantid;
}



public String getSequenceNo() {
	return sequenceNo;
}



public void setSequenceNo(String sequenceNo) {
	this.sequenceNo = sequenceNo;
}



public String getFlag() {
	return flag;
}



public void setFlag(String flag) {
	this.flag = flag;
}



public  SynSqlObject(){
	   
   }

   
   
public String getPath() {
	return path;
}



public void setPath(String path) {
	this.path = path;
}



public String getSqltext() {
	return sqltext;
}

public void setSqltext(String sqltext) {
	this.sqltext = sqltext;
}

public int getStatus() {
	return status;
}

public void setStatus(int status) {
	this.status = status;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

 

public String getBranchid() {
	return branchid;
}

public void setBranchid(String branchid) {
	this.branchid = branchid;
}

 

public String getGenerattime() {
	return generattime;
}

public void setGenerattime(String generattime) {
	this.generattime = generattime;
}

public String getSql() {
	return sql;
}

public void setSql(String sql) {
	this.sql = sql;
}
   
   
   
}
