package com.candao.inorder.data;

/**
 * 
 * @author Candao 上传文件数据
 */
public class LinuxPrintEmpBean {
	private String printQname;
	private String tableNum;
	private String empName;
	private String cover;
	private String checkNum;
	private String date;
	private String time;
	private String menuNum;
	private String menuName;
	private String fileName;
	//做法
	private String doMethod;

	public LinuxPrintEmpBean(String printQname, String tableNum, String empName, String cover, String checkNum,
			String date, String time, String menuNum, String menuName) {
		this.printQname = printQname;
		this.tableNum = tableNum;
		this.empName = empName;
		this.cover = cover;
		this.checkNum = checkNum;
		this.date = date;
		this.time = time;
		this.menuNum = menuNum;
		this.menuName = menuName;

	}

	public String getPrintQname() {
		return printQname;
	}

	public void setPrintQname(String printQname) {
		this.printQname = printQname;
	}

	public String getTableNum() {
		return tableNum;
	}

	public void setTableNum(String tableNum) {
		this.tableNum = tableNum;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMenuNum() {
		return menuNum;
	}

	public void setMenuNum(String menuNum) {
		this.menuNum = menuNum;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getDoMethod() {
		return doMethod;
	}

	public void setDoMethod(String doMethod) {
		this.doMethod = doMethod;
	}
}
