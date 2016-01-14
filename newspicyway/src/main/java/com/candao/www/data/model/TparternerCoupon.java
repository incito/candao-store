package com.candao.www.data.model;

import java.math.BigDecimal;

public class TparternerCoupon {
	/**
	 * 主键
	 */
    private String id;
    /**
     * 优惠主键
     */
    private String couponid;
    /**
     * 合作伙伴主键
     */
    private String parternerid;
    /**
     * 菜品主键
     */
    private String dishid;
    /**
     * 打折金额
     */
    private BigDecimal discountamount;
    /**
     * 打折折扣
     */
    private BigDecimal discountrate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCouponid() {
		return couponid;
	}

	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}

	public String getParternerid() {
        return parternerid;
    }

    public void setParternerid(String parternerid) {
        this.parternerid = parternerid;
    }

    public String getDishid() {
        return dishid;
    }

    public void setDishid(String dishid) {
        this.dishid = dishid;
    }

    public BigDecimal getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(BigDecimal discountamount) {
        this.discountamount = discountamount;
    }

    public BigDecimal getDiscountrate() {
        return discountrate;
    }

    public void setDiscountrate(BigDecimal discountrate) {
        this.discountrate = discountrate;
    }
}