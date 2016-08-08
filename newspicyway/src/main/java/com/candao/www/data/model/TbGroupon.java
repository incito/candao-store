package com.candao.www.data.model;

import java.math.BigDecimal;

/**
 * 优惠券->团购券
 * @author YHL
 * @version 1.0
 * @date 2015-03-17
 */
public class TbGroupon {
	/**
	 * 主键
	 */
	public String id;
	
	/**
	 * 优惠券ID
	 */
	public String preferential;
	
	/**
	 * 记账金额
	 */
	public BigDecimal bill_amount;
	
	/**
	 * 抵用金额
	 */
	public BigDecimal token_amount;

	//-------------GETTER / SETTER------------------\\
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPreferential() {
		return preferential;
	}

	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}

	public BigDecimal getBill_amount() {
		return bill_amount;
	}

	public void setBill_amount(BigDecimal bill_amount) {
		this.bill_amount = bill_amount;
	}

	public BigDecimal getToken_amount() {
		return token_amount;
	}

	public void setToken_amount(BigDecimal token_amount) {
		this.token_amount = token_amount;
	}
	
}
