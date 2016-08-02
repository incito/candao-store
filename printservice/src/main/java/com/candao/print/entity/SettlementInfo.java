package com.candao.print.entity;

import java.io.Serializable;

public class SettlementInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 746505783693986036L;

	private String name;
	
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
