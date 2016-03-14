package com.candao.www.data.model;

import java.util.Date;

public class TtellerCash {

	private int id;
	
	private String username;
	
	private String ipaddress;
	
	private Date opendate;
	
	private int cashamount;
	
	private int status;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public Date getOpendate() {
		return opendate;
	}

	public void setOpendate(Date opendate) {
		this.opendate = opendate;
	}

	public int getCashamount() {
		return cashamount;
	}

	public void setCashamount(int cashamount) {
		this.cashamount = cashamount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
