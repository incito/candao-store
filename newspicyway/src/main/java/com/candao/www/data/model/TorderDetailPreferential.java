package com.candao.www.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author LiangDong 订单使用优惠卷情况
 */
public class TorderDetailPreferential implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String dishNum;
	private String orderid;
	private String dishid;
	private String preferential;
	private BigDecimal deAmount=new BigDecimal("0");
	private BigDecimal discount;
	/** 0:使用优惠 1：服务员优惠 2：系统自动查找优惠 4：赠送菜优惠 **/
	private int isCustom;
	private int isGroup;
	private int isUse;
	private Date insertime;
	/***
	 * 优免金额
	 */
	private BigDecimal toalFreeAmount = new BigDecimal("0");
	/***
	 * 挂账金额
	 */
	private BigDecimal toalDebitAmount = new BigDecimal("0");
	/***
	 * 挂账多收
	 * */
	private BigDecimal toalDebitAmountMany=new BigDecimal("0");
	private TbPreferentialActivity activity;
	// 优惠子ID
	private String coupondetailid;

	public TorderDetailPreferential() {

	}

	public TorderDetailPreferential(String orderid, String preferential, BigDecimal deAmount, String dishid) {
		this.orderid = orderid;
		this.preferential = preferential;
		this.deAmount = deAmount;
		this.dishid = dishid;
	}

	public TorderDetailPreferential(String orderid, String preferential, BigDecimal deAmount, int isGroup) {
		this.orderid = orderid;
		this.preferential = preferential;
		this.deAmount = deAmount;
		this.isGroup = isGroup;
	}

	public TorderDetailPreferential(String orderid, String preferential, BigDecimal deAmount, int isGroup, int isUse) {
		this.orderid = orderid;
		this.preferential = preferential;
		this.deAmount = deAmount;
		this.isGroup = isGroup;
		this.isUse = isUse;
	}

	public TorderDetailPreferential(String id, String orderid, String dishid, String preferential, BigDecimal deAmount,
			String dishNum, int isGroup, int isUse, BigDecimal discount, int isCustom,Date insertime) {
		this.orderid = orderid;
		this.preferential = preferential;
		this.dishid = dishid;
		this.deAmount = deAmount;
		this.isGroup = isGroup;
		this.isUse = isUse;
		this.dishNum = dishNum;
		this.discount = discount;
		this.isCustom = isCustom;
		this.id = id;
		this.insertime = insertime;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getDishid() {
		return dishid;
	}

	public void setDishid(String dishid) {
		this.dishid = dishid;
	}

	public String getPreferential() {
		return preferential;
	}

	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}

	public int getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(int isGroup) {
		this.isGroup = isGroup;
	}

	public int getIsUse() {
		return isUse;
	}

	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}

	public BigDecimal getDeAmount() {
		return deAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void setDeAmount(BigDecimal deAmount) {
		this.deAmount = deAmount;
	}

	public String getDishNum() {
		return dishNum;
	}

	public void setDishNum(String dishNum) {
		this.dishNum = dishNum;
	}

	public TbPreferentialActivity getActivity() {
		return activity;
	}

	public void setActivity(TbPreferentialActivity activity) {
		this.activity = activity;
	}

	public BigDecimal getDiscount() {
		return discount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public int getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(int isCustom) {
		this.isCustom = isCustom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getInsertime() {
		return insertime;
	}

	public void setInsertime(Date insertime) {
		this.insertime = insertime;
	}

	public BigDecimal getToalFreeAmount() {
		return toalFreeAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void setToalFreeAmount(BigDecimal toalFreeAmount) {
		this.toalFreeAmount = toalFreeAmount;
	}

	public BigDecimal getToalDebitAmount() {
		return toalDebitAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void setToalDebitAmount(BigDecimal toalDebitAmount) {
		this.toalDebitAmount = toalDebitAmount;
	}

	public String getCoupondetailid() {
		return coupondetailid;
	}

	public void setCoupondetailid(String coupondetailid) {
		this.coupondetailid = coupondetailid;
	}

	public BigDecimal getToalDebitAmountMany() {
		return toalDebitAmountMany.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void setToalDebitAmountMany(BigDecimal toalDebitAmountMany) {
		this.toalDebitAmountMany = toalDebitAmountMany;
	}

}
