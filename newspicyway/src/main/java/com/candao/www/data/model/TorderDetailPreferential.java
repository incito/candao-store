package com.candao.www.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author LiangDong 订单使用优惠卷情况
 */
public class TorderDetailPreferential implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String orderid;
	private String preferential;
	/**单个优惠总额：优免+挂账**/
	private BigDecimal deAmount=new BigDecimal("0");
	private BigDecimal discount;
	/** 0:使用优惠 1：服务员优惠 2：系统自动查找优惠 3：特价优惠卷 4：赠送菜优惠 5 雅座优惠 **/
	private int isCustom;
	private int isGroup;
	private Date insertime;
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
	private List<TbOrderDetailPreInfo> detailPreInfos=new ArrayList<>();
	// 优惠子ID
	private String coupondetailid;

	public TorderDetailPreferential() {

	}
	/**
	 * 
	 * @param id
	 * 订单优惠关系ID
	 * @param orderid
	 * 菜品ID
	 * @param preferential
	 * 优惠
	 * @param deAmount
	 * 菜品优惠个数
	 * @param isGroup
	 * 是否使用
	 * @param discount
	 * 折扣
	 * @param isCustom
	 * 优惠交易类型
	 * @param insertime
	 * 插入时间
	 */
	public TorderDetailPreferential(String id, String orderid,  String preferential, BigDecimal deAmount,
			 int isGroup, BigDecimal discount, int isCustom,Date insertime) {
		this.id = id;
		this.orderid = orderid;
		this.preferential = preferential;
		this.deAmount = deAmount;
		this.isGroup = isGroup;
		this.discount = discount;
		this.isCustom = isCustom;
		this.insertime = insertime;
	}
	

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
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


	public BigDecimal getDeAmount() {
		return deAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void setDeAmount(BigDecimal deAmount) {
		this.deAmount = deAmount;
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
	public List<TbOrderDetailPreInfo> getDetailPreInfos() {
		return detailPreInfos;
	}
	public void setDetailPreInfos(List<TbOrderDetailPreInfo> detailPreInfos) {
		this.detailPreInfos = detailPreInfos;
	}

}
