package com.candao.inorder.pojo;import java.util.Date;/******************************************************************************* * javaBeans * tbl_floortable --> TblFloortable  * <table explanation> * @author 2016-06-27 10:12:00 *  */	public class TblFloortable implements java.io.Serializable {	//field	/**  **/	private int outlet;	/**  **/	private int floor;	/**  **/	private String tableno;	/**  **/	private String name1;	/**  **/	private String name2;	/**  **/	private String name3;	/**  **/	private int cover;	/**  **/	private int toppos;	/**  **/	private int leftpos;	/**  **/	private Date lastoccupytime;	/**  **/	private int status;	/**  **/	private String remark1;	/**  **/	private String remark2;	/**  **/	private String remark3;	/**  **/	private String memo;	/**  **/	private String isdisabled;	public int getOutlet() {		return outlet;	}	public void setOutlet(int outlet) {		this.outlet = outlet;	}	public int getFloor() {		return floor;	}	public void setFloor(int floor) {		this.floor = floor;	}	public String getTableno() {		return tableno;	}	public void setTableno(String tableno) {		this.tableno = tableno;	}	public String getName1() {		return name1;	}	public void setName1(String name1) {		this.name1 = name1;	}	public String getName2() {		return name2;	}	public void setName2(String name2) {		this.name2 = name2;	}	public String getName3() {		return name3;	}	public void setName3(String name3) {		this.name3 = name3;	}	public int getCover() {		return cover;	}	public void setCover(int cover) {		this.cover = cover;	}	public int getToppos() {		return toppos;	}	public void setToppos(int toppos) {		this.toppos = toppos;	}	public int getLeftpos() {		return leftpos;	}	public void setLeftpos(int leftpos) {		this.leftpos = leftpos;	}	public Date getLastoccupytime() {		return lastoccupytime;	}	public void setLastoccupytime(Date lastoccupytime) {		this.lastoccupytime = lastoccupytime;	}	public int getStatus() {		return status;	}	public void setStatus(int status) {		this.status = status;	}	public String getRemark1() {		return remark1;	}	public void setRemark1(String remark1) {		this.remark1 = remark1;	}	public String getRemark2() {		return remark2;	}	public void setRemark2(String remark2) {		this.remark2 = remark2;	}	public String getRemark3() {		return remark3;	}	public void setRemark3(String remark3) {		this.remark3 = remark3;	}	public String getMemo() {		return memo;	}	public void setMemo(String memo) {		this.memo = memo;	}	public String getIsdisabled() {		return isdisabled;	}	public void setIsdisabled(String isdisabled) {		this.isdisabled = isdisabled;	}}