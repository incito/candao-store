package com.candao.www.data.model;

import java.sql.Timestamp;


public class TJsonRecord {

	private int id;
	
	private String json;
	
	private String padpath ;
	
	private Timestamp inserttime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getPadpath() {
		return padpath;
	}

	public void setPadpath(String padpath) {
		this.padpath = padpath;
	}

	public Timestamp getInserttime() {
		return inserttime;
	}

	public void setInserttime(Timestamp inserttime) {
		this.inserttime = inserttime;
	}
	 
}
