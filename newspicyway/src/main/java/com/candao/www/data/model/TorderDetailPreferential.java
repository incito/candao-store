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
	/** 0:使用优惠 1：服务员优惠 2：系统自动查找优惠 4：赠送菜优惠 5 雅座优惠 **/
	private int isCustom;
	private int isGroup;
	private int isUse;
	private Date insertime;
	/**菜品单位*/
	private String unit="";
	/**优惠类型*/
    /**雅座折扣：9902 雅座优免：9903 雅座团购：9905*/
	private String  preType="";
	/**优惠名称*/
	private String preName="";
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
	/**
	 * 
	 * @param id
	 * 订单优惠关系ID
	 * @param orderid
	 * 订单ID
	 * @param dishid
	 * 菜品ID
	 * @param preferential
	 * 优惠
	 * @param deAmount
	 * 折扣钱
	 * @param dishNum
	 * 菜品优惠个数
	 * @param isGroup
	 * 是否是全局优化
	 * @param isUse
	 * 是否使用
	 * @param discount
	 * 折扣
	 * @param isCustom
	 * 优惠交易类型
	 * @param insertime
	 * 插入时间
	 */
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getPreName() {
		return preName;
	}

	public void setPreName(String preName) {
		this.preName = preName;
	}

	public String getPreType() {
		return preType;
	}

	public void setPreType(String preType) {
		this.preType = preType;
	}

}
