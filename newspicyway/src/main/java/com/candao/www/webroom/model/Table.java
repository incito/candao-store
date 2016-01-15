package com.candao.www.webroom.model;

public class Table {

	public String tableNo;
	
	public String orignalTableNo;

	public String orderNo;

	public String status;
	
	public String username;
	
	public Integer manNum;
	
	public Integer womanNum;
	
	public Integer childNum;
	
	public String specialrequied;
	
	public String result;
	
	private int operationType;//（1：下单;2 :退菜 3：并台  4换台） 
	
	private String sequence;//顺序
	/**
	 * 授权人id
	 */
	private  String discardUserId; 
	
	

	public String getDiscardUserId() {
		return discardUserId;
	}

	public void setDiscardUserId(String discardUserId) {
		this.discardUserId = discardUserId;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}


	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrignalTableNo() {
		return orignalTableNo;
	}

	public void setOrignalTableNo(String orignalTableNo) {
		this.orignalTableNo = orignalTableNo;
	}

	public String getTableNo() {
		return tableNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getManNum() {
		return manNum;
	}

	public void setManNum(Integer manNum) {
		this.manNum = manNum;
	}

	public Integer getWomanNum() {
		return womanNum;
	}

	public void setWomanNum(Integer womanNum) {
		this.womanNum = womanNum;
	}

	public Integer getChildNum() {
		return childNum;
	}

	public void setChildNum(Integer childNum) {
		this.childNum = childNum;
	}

	public String getSpecialrequied() {
		return specialrequied;
	}

	public void setSpecialrequied(String specialrequied) {
		this.specialrequied = specialrequied;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}
 
	
	
	
}
