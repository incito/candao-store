package com.candao.www.data.model;

import java.util.Date;


public class TbOpenBizLog implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1724133372980672242L;

	private int  id;

	private String username;

	private Date inserttime;
	
    private Date   opendate;
	 
    private String ipaddress;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getInserttime() {
		return inserttime;
	}

	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}

	public Date getOpendate() {
		return opendate;
	}

	public void setOpendate(Date opendate) {
		this.opendate = opendate;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
    
    
	
}

 