package com.candao.www.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TbPreferenceDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String preferential;
	private String coupondetailid;
	private String dish;
	private String dish_title;
	private BigDecimal price;

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

	public String getDish() {
		return dish;
	}

	public void setDish(String dish) {
		this.dish = dish;
	}

	public String getDish_title() {
		return dish_title;
	}

	public void setDish_title(String dish_title) {
		this.dish_title = dish_title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCoupondetailid() {
		return coupondetailid;
	}

	public void setCoupondetailid(String coupondetailid) {
		this.coupondetailid = coupondetailid;
	}
}
