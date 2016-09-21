package com.candao.inorder.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author liangdong 营业时间 以及营业类型
 */
public class Period implements Serializable {

	private static final long serialVersionUID = 1L;
	// 门店号
	private int outlet;
	// 类型编码，ID
	private int periodNo;
	// 营业类型，早市，中市，下午茶，晚餐等等
	private String typeOne;
	private String typeTwo;
	private String typeThree;
	private Date startTime;
	private Date endTime;

	public int getOutlet() {
		return outlet;
	}

	public void setOutlet(int outlet) {
		this.outlet = outlet;
	}

	public int getPeriodNo() {
		return periodNo;
	}

	public void setPeriodNo(int periodNo) {
		this.periodNo = periodNo;
	}

	public String getTypeOne() {
		return typeOne;
	}

	public void setTypeOne(String typeOne) {
		this.typeOne = typeOne;
	}

	public String getTypeTwo() {
		return typeTwo;
	}

	public void setTypeTwo(String typeTwo) {
		this.typeTwo = typeTwo;
	}

	public String getTypeThree() {
		return typeThree;
	}

	public void setTypeThree(String typeThree) {
		this.typeThree = typeThree;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
