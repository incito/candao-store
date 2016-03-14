package com.candao.www.weixin.dto;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 微信支付结账json
 * @author snail
 *
 */
import java.util.List;
public class SettlementStrInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String orderNo;
	private List<PayDetail>  payDetail=new ArrayList<>();
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
	public List<PayDetail> getPayDetail() {
		return payDetail;
	}
	public void setPayDetail(List<PayDetail> payDetail) {
		this.payDetail = payDetail;
	}

}

