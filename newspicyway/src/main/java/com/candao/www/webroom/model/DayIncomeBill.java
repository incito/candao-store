package com.candao.www.webroom.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DayIncomeBill implements Serializable{

	private static final long serialVersionUID = -8764668946152230657L;
//-----------------------------------------营业收入统计------------------------
	/**
	 * 营业日期
	 */
	private Date incomeDate;
    
	/**
	 * 应收入总额
	 */
    private BigDecimal totalIncome;
    
    /**
     * 实际收入
     */
    private BigDecimal realIncome;
    
    /**
     * 折扣总额
     */
    private BigDecimal totalDiscount;
    
    /**
     * 折扣率
     */
    private BigDecimal discountRate;

//-----------------------------------------实收明细统计统计------------------------
     /**
      * 人民币收入
      */
    private BigDecimal rmbIncome;
    
    /**
     * 挂账
     */
    private BigDecimal guazhangIncome;
    
    /**
     * 刷卡，工商银行
     */
    private BigDecimal ICBC_bank;
    
    /**
     * 刷卡，其他银行
     */
    private BigDecimal other_bank;
    
    /**
     * 会员储值消费净值
     */
    private BigDecimal storedIncome;
    
//-----------------------------------------会员数据统计------------------------
    /**
     * 会员消费券
     */
    private BigDecimal memberTicket;
    
    /**
     * 会员积分
     */
    private BigDecimal memberPoint;
    
    /**
     * 会员储值消费虚增
     */
    private BigDecimal memberstoredEmpty;
    
    /**
     * 会员消费汇总
     */
    private BigDecimal memberTotalIncome;
    
    /**
     * 会员卡销售张数
     */
    private int memberSellCard;
    
    /**
     * 会员卡赠送张数
     */
    private int memberGiveCard;
    
    /**
     * 会员卡总数
     */
    private int memberTotalCard;
    
//-----------------------------------------营业数据统计------------------------  
    /**
     * 桌数
     */
    private int tableNum;
    
    /**
     * 结算人数
     */
    private int personTotal;
    
    /**
     * 应收人均
     */
    private BigDecimal shouldAverage;
    
    /**
     * 实收人均
     */
    private BigDecimal realAverage;
    
    /**
     * 上座率
     */
    private BigDecimal attendance;
    
    /**
     * 平均消费时间
     */
    private BigDecimal averageTime;
    
//-----------------------------------------折扣明细统计------------------------   
    /**
     * 优免
     */
    private BigDecimal freeMoney;
    
    /**
     * 折扣优惠
     */
    private BigDecimal discountCoupon; 
    
    /**
     * 抹零收入
     */
    private BigDecimal delZeroIncome; 
    
    /**
     * 定额优惠
     */
    private BigDecimal quotaCoupon; 
    
    /**
     * 赠送金额
     */
    private BigDecimal giveMoney;

	public Date getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getRealIncome() {
		return realIncome;
	}

	public void setRealIncome(BigDecimal realIncome) {
		this.realIncome = realIncome;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public BigDecimal getRmbIncome() {
		return rmbIncome;
	}

	public void setRmbIncome(BigDecimal rmbIncome) {
		this.rmbIncome = rmbIncome;
	}

	public BigDecimal getGuazhangIncome() {
		return guazhangIncome;
	}

	public void setGuazhangIncome(BigDecimal guazhangIncome) {
		this.guazhangIncome = guazhangIncome;
	}

	public BigDecimal getICBC_bank() {
		return ICBC_bank;
	}

	public void setICBC_bank(BigDecimal iCBC_bank) {
		ICBC_bank = iCBC_bank;
	}

	public BigDecimal getOther_bank() {
		return other_bank;
	}

	public void setOther_bank(BigDecimal other_bank) {
		this.other_bank = other_bank;
	}

	public BigDecimal getStoredIncome() {
		return storedIncome;
	}

	public void setStoredIncome(BigDecimal storedIncome) {
		this.storedIncome = storedIncome;
	}

	public BigDecimal getMemberTicket() {
		return memberTicket;
	}

	public void setMemberTicket(BigDecimal memberTicket) {
		this.memberTicket = memberTicket;
	}

	public BigDecimal getMemberPoint() {
		return memberPoint;
	}

	public void setMemberPoint(BigDecimal memberPoint) {
		this.memberPoint = memberPoint;
	}

	public BigDecimal getMemberstoredEmpty() {
		return memberstoredEmpty;
	}

	public void setMemberstoredEmpty(BigDecimal memberstoredEmpty) {
		this.memberstoredEmpty = memberstoredEmpty;
	}

	public BigDecimal getMemberTotalIncome() {
		return memberTotalIncome;
	}

	public void setMemberTotalIncome(BigDecimal memberTotalIncome) {
		this.memberTotalIncome = memberTotalIncome;
	}

	public int getMemberSellCard() {
		return memberSellCard;
	}

	public void setMemberSellCard(int memberSellCard) {
		this.memberSellCard = memberSellCard;
	}

	public int getMemberGiveCard() {
		return memberGiveCard;
	}

	public void setMemberGiveCard(int memberGiveCard) {
		this.memberGiveCard = memberGiveCard;
	}

	public int getMemberTotalCard() {
		return memberTotalCard;
	}

	public void setMemberTotalCard(int memberTotalCard) {
		this.memberTotalCard = memberTotalCard;
	}

	public int getTableNum() {
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}

	public int getPersonTotal() {
		return personTotal;
	}

	public void setPersonTotal(int personTotal) {
		this.personTotal = personTotal;
	}

	public BigDecimal getShouldAverage() {
		return shouldAverage;
	}

	public void setShouldAverage(BigDecimal shouldAverage) {
		this.shouldAverage = shouldAverage;
	}

	public BigDecimal getRealAverage() {
		return realAverage;
	}

	public void setRealAverage(BigDecimal realAverage) {
		this.realAverage = realAverage;
	}

	public BigDecimal getAttendance() {
		return attendance;
	}

	public void setAttendance(BigDecimal attendance) {
		this.attendance = attendance;
	}

	public BigDecimal getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(BigDecimal averageTime) {
		this.averageTime = averageTime;
	}

	public BigDecimal getFreeMoney() {
		return freeMoney;
	}

	public void setFreeMoney(BigDecimal freeMoney) {
		this.freeMoney = freeMoney;
	}

	public BigDecimal getDiscountCoupon() {
		return discountCoupon;
	}

	public void setDiscountCoupon(BigDecimal discountCoupon) {
		this.discountCoupon = discountCoupon;
	}

	public BigDecimal getDelZeroIncome() {
		return delZeroIncome;
	}

	public void setDelZeroIncome(BigDecimal delZeroIncome) {
		this.delZeroIncome = delZeroIncome;
	}

	public BigDecimal getQuotaCoupon() {
		return quotaCoupon;
	}

	public void setQuotaCoupon(BigDecimal quotaCoupon) {
		this.quotaCoupon = quotaCoupon;
	}

	public BigDecimal getGiveMoney() {
		return giveMoney;
	}

	public void setGiveMoney(BigDecimal giveMoney) {
		this.giveMoney = giveMoney;
	}
    
   
}
