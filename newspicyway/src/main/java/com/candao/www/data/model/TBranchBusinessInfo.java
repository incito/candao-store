package com.candao.www.data.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;



public class TBranchBusinessInfo {
	
	private static DecimalFormat dataformat = new DecimalFormat(".00");
	/**
	 * 主键
	 */
	public Long id;
	
	/**
	 * 门店名称
	 */
	public String branchName;
	
	/**
	 * jde编号
	 */
	public String jdeNo;
	/**
	 * 门店ID
	 */
	public String branchId;
	/**
	 * 日期
	 */
	public String date;
	/**
	 * 应收总额
	 */
	public BigDecimal shouldamount;
	
	/**
	 * 实收总额
	 */
	public BigDecimal paidinamount;
	/**
	 * 折扣
	 */
	public BigDecimal discountamount;
	
	/**
	 * 现金
	 */
	public BigDecimal cash;
	/**
	 * 挂账
	 */
	public BigDecimal credit;
	
	/**
	 * 刷卡
	 */
	public BigDecimal card;
	
	/**
	 * 刷卡
	 */
	public BigDecimal othercard;
	
	/**
	 * 刷卡
	 */
	public BigDecimal weixin;
	
	/**
	 * 刷卡
	 */
	public BigDecimal zhifubao;
	/**
	 * 会员消费净值
	 */
	public BigDecimal merbervaluenet;
	
	/**
	 * 会员消费虚值
	 */
	public BigDecimal mebervalueadd;
	/**
	 * 会员积分消费
	 */
	public BigDecimal integralconsum;
	
	/**
	 * 会员优惠券消费
	 */
	public BigDecimal meberTicket;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getJdeNo() {
		return jdeNo;
	}

	public void setJdeNo(String jdeNo) {
		this.jdeNo = jdeNo;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getShouldamount() {
		return shouldamount;
	}

	public void setShouldamount(BigDecimal shouldamount) {
		this.shouldamount = shouldamount.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.shouldamount=new BigDecimal(dataformat.format(shouldamount));
		
	}

	public BigDecimal getPaidinamount() {
		return paidinamount;
	}

	public void setPaidinamount(BigDecimal paidinamount) {
		this.paidinamount = paidinamount.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.paidinamount=new BigDecimal(dataformat.format(paidinamount));
	}

	public BigDecimal getDiscountamount() {
		return discountamount;
	}

	public void setDiscountamount(BigDecimal discountamount) {
		this.discountamount = discountamount.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.discountamount=new BigDecimal(dataformat.format(discountamount));
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.cash=new BigDecimal(dataformat.format(cash));
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.credit=new BigDecimal(dataformat.format(credit));
	}

	public BigDecimal getCard() {
		return card;
	}

	public void setCard(BigDecimal card) {
		this.card = card.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.card=new BigDecimal(dataformat.format(card));
	}

	public BigDecimal getMerbervaluenet() {
		return merbervaluenet;
	}

	public void setMerbervaluenet(BigDecimal merbervaluenet) {
		this.merbervaluenet = merbervaluenet.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.merbervaluenet=new BigDecimal(dataformat.format(merbervaluenet));
	}

	public BigDecimal getMebervalueadd() {
		return mebervalueadd;
	}

	public void setMebervalueadd(BigDecimal mebervalueadd) {
		this.mebervalueadd = mebervalueadd.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.mebervalueadd=new BigDecimal(dataformat.format(mebervalueadd));
	}

	public BigDecimal getIntegralconsum() {
		return integralconsum;
	}

	public void setIntegralconsum(BigDecimal integralconsum) {
		this.integralconsum = integralconsum.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.integralconsum=new BigDecimal(dataformat.format(integralconsum));
	}

	public BigDecimal getMeberTicket() {
		return meberTicket;
	}

	public void setMeberTicket(BigDecimal meberTicket) {
		this.meberTicket = meberTicket.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.meberTicket=new BigDecimal(dataformat.format(meberTicket));
	}

	public BigDecimal getOthercard() {
		return othercard;
	}

	public void setOthercard(BigDecimal othercard) {
		this.othercard = othercard.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.othercard=new BigDecimal(dataformat.format(othercard));
	}

	public BigDecimal getWeixin() {
		return weixin;
	}

	public void setWeixin(BigDecimal weixin) {
		this.weixin = weixin.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.weixin=new BigDecimal(dataformat.format(weixin));
	}

	public BigDecimal getZhifubao() {
		return zhifubao;
	}

	public void setZhifubao(BigDecimal zhifubao) {
		this.zhifubao = zhifubao.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.zhifubao=new BigDecimal(dataformat.format(zhifubao));
	}

}
