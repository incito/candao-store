package com.candao.www.webroom.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class SettlementInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3755774317241643672L;

	private String userName;
	
	private String orderNo;
	
	private String tableNo;
	
	private String incometype;
	
	private BigDecimal payamount;
	 
	//退菜原因 
	private String reason;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	//是否打印厨打单等
	private boolean flag = false;
	
	
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getIncometype() {
		return incometype;
	}

	public void setIncometype(String incometype) {
		this.incometype = incometype;
	}

	public BigDecimal getPayamount() {
		return payamount;
	}

	public void setPayamount(BigDecimal payamount) {
		this.payamount = payamount;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	private ArrayList<SettlementDetail> payDetail;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public ArrayList<SettlementDetail> getPayDetail() {
		return payDetail;
	}

	public void setPayDetail(ArrayList<SettlementDetail> payDetail) {
		this.payDetail = payDetail;
	}
	
}
