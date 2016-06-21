package com.candao.www.dataserver.entity;

/**
 * Created by lenovo on 2016/4/5.
 */
public class OrderRule {
    private int id;
    private String orderId;
    private String yhname;
    private String partnerName;
    private double couponRate;
    private double freeAmount;
    private double debitAmount;
    private String memo;
    private String couponsNo;
    private int num;
    private int ftype;
    private String bankType;
    private String ruleId;
    private String couponId;
    private double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getYhname() {
        return yhname;
    }

    public void setYhname(String yhname) {
        this.yhname = yhname;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public double getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(double couponRate) {
        this.couponRate = couponRate;
    }

    public double getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(double freeAmount) {
        this.freeAmount = freeAmount;
    }

    public double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCouponsNo() {
        return couponsNo;
    }

    public void setCouponsNo(String couponsNo) {
        this.couponsNo = couponsNo;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
