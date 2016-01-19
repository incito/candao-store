package com.candao.www.webroom.model;

import java.util.List;

public class CouponsInterface implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8775796878692880532L;
	
	public String result ;
	
	public List<Coupons> coupons;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<Coupons> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupons> coupons) {
		this.coupons = coupons;
	}
	
	
}
