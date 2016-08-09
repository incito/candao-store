package com.candao.www.webroom.model;

import java.util.List;

import com.candao.www.data.model.TCouponRule;
import com.candao.www.data.model.TCoupons;
import com.candao.www.data.model.TparternerCoupon;

/**
 * 该实体类对应的是TCoupons和TCouponRule的合并 TparternerCoupon
 *
 */ 
public class TCouponGroup {
	private TCoupons tCoupons;
	private List<TCouponRule> list;
	private List<TparternerCoupon> listpac;
	public List<TparternerCoupon> getListpac() {
		return listpac;
	}
	public void setListpac(List<TparternerCoupon> listpac) {
		this.listpac = listpac;
	}
	public TCoupons gettCoupons() {
		return tCoupons;
	}
	public void settCoupons(TCoupons tCoupons) {
		this.tCoupons = tCoupons;
	}
	public List<TCouponRule> getList() {
		return list;
	}
	public void setList(List<TCouponRule> list) {
		this.list = list;
	}
}
