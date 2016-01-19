package com.candao.www.webroom.model;

import java.io.Serializable;

public class TableStatus implements Serializable{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -7227067878358743139L;

	public String status;
 
	public String result;
	
	public String orderid ;
	
	
	

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
  
	
	
}
