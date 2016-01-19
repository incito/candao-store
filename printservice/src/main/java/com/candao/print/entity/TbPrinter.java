package com.candao.print.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
//printerid varchar,printerNo varchar,printername varchar,printertype int
//createuserid varchar,inserttime varchar,ipaddress varchar,port varchar
//status int
public class TbPrinter {
	private  String printerid; 
	private  String printerNo; 
	private  String printername; 
	/**
	 * 打印机类型  1串口打印 2并口打印  3网络打印 
	 */
	private  Integer printertype; 
	private  String createuserid;
	private  String inserttime;
	private  String ipaddress;
	private  String port;
	private Integer status;
	private  Integer printNum;
	private String areaid;
	
	
	
	public String getAreaid() {
		return areaid;
	}
	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	public Integer getPrintNum() {
		return printNum;
	}
	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}
	public String getPrinterid() {
		return printerid;
	}
	public void setPrinterid(String printerid) {
		this.printerid = printerid;
	}
	public String getPrinterNo() {
		return printerNo;
	}
	public void setPrinterNo(String printerNo) {
		this.printerNo = printerNo;
	}
	public String getPrintername() {
		return printername;
	}
	public void setPrintername(String printername) {
		this.printername = printername;
	}
	public Integer getPrintertype() {
		return printertype;
	}
	public void setPrintertype(Integer printertype) {
		this.printertype = printertype;
	}
	public String getCreateuserid() {
		return createuserid;
	}
	public void setCreateuserid(String createuserid) {
		this.createuserid = createuserid;
	}
	public String getInserttime() {
		return inserttime;
	}
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("printerid");
        sb.append("{printerNo=").append(printerNo);
        sb.append(", printername=").append(printername);
        sb.append(", printertype=").append(printertype);
        sb.append(", createuserid=").append(createuserid);
        sb.append(", inserttime=").append(inserttime);
        sb.append(", ipaddress=").append(ipaddress);
        sb.append(", port=").append(port);
        sb.append(", status=").append(status);
       	sb.append('}');
        return sb.toString();
    }
	
}
