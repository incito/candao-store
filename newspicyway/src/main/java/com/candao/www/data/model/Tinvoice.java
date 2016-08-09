package com.candao.www.data.model;

import java.math.BigDecimal;

public class Tinvoice {

	private String id;
	
	private String cardno;
	
	private String invoice_title ;
	
	private String device_no;
 
	private String orderid;
	
	//t_dictionary.INVOICE_STATUS
	private int status;
	//t_dictionary.INVOICE_TYPE
	private int invoiceType;
     //开发发票金额
	private BigDecimal  invoice_amount;
	
  

	public BigDecimal getInvoice_amount() {
		return invoice_amount;
	}

	public void setInvoice_amount(BigDecimal invoice_amount) {
		this.invoice_amount = invoice_amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getInvoice_title() {
		return invoice_title;
	}

	public void setInvoice_title(String invoice_title) {
		this.invoice_title = invoice_title;
	}

	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

}
