package com.candao.www.weixin.dto;

public class WeixinRequestParam {

	private String body; 
	private String orderid;
	private String spbillCreateIp;
	private String totalFee;
	private String attach;
	private String infos;//额外信息   总消费金额 ;  微信支付金额
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	
	
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	
	public String getInfos() {
		return infos;
	}
	public void setInfos(String infos) {
		this.infos = infos;
	}
	@Override
	public String toString() {
		return "WeixinRequestParam [body=" + body + ", orderid=" + orderid + ", spbillCreateIp=" + spbillCreateIp
				+ ", totalFee=" + totalFee + ", attach=" + attach + ", infos=" + infos + "]";
	}


	

	
	
	
	
}
