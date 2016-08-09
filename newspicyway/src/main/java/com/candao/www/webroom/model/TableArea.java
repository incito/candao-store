package com.candao.www.webroom.model;

import java.io.Serializable;
public class TableArea implements Serializable{

	private static final long serialVersionUID = 4162463104833001343L;
	
	private String areaNo;
	
	private String areaname;

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
