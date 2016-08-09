package com.candao.www.webroom.model;

import java.math.BigDecimal;

public class TjObj {
	private String baseCom; 
	private String selfcom; 
	private String value; 
	private BigDecimal objvalue;
	public String getBaseCom() {
		return baseCom;
	}
	public void setBaseCom(String baseCom) {
		this.baseCom = baseCom;
	}
	public String getSelfcom() {
		return selfcom;
	}
	public void setSelfcom(String selfcom) {
		this.selfcom = selfcom;
	}
	public BigDecimal getObjvalue() {
		return objvalue;
	}
	public void setObjvalue(BigDecimal objvalue) {
		this.objvalue = objvalue;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	} 

}
