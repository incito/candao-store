package com.candao.inorder.pojo;/******************************************************************************* * javaBeans tbl_dayinfo --> TblDayinfo <table explanation> *  * @author 2016-07-07 14:31:00 *  */public class TblDayinfo implements java.io.Serializable {	// field	/**  **/	private String date;	/**  **/	private int count;	/**  **/	private String starttime;	/**  **/	private int startemp;	/**  **/	private char active;	public String getDate() {		return date;	}	public void setDate(String date) {		this.date = date;	}	public int getCount() {		return count;	}	public void setCount(int count) {		this.count = count;	}	public String getStarttime() {		return starttime;	}	public void setStarttime(String starttime) {		this.starttime = starttime;	}	public int getStartemp() {		return startemp;	}	public void setStartemp(int startemp) {		this.startemp = startemp;	}	public char getActive() {		return active;	}	public void setActive(char active) {		this.active = active;	}}