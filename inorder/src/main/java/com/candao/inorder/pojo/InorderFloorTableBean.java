package com.candao.inorder.pojo;

import java.io.Serializable;

/**
 * 
 * @author梁东
 *
 */
public class InorderFloorTableBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String outlet;
	private String floor;
	private String tableNo;
	private String status;
	public String getOutlet() {
		return outlet;
	}
	public void setOutlet(String outlet) {
		this.outlet = outlet;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getTableNo() {
		return tableNo;
	}
	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
