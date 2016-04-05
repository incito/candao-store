package com.candao.www.dataserver.model;

/**
 * 会员充值请求参数data
 * Created by lenovo on 2016/3/21.
 */
public class RechargeData {
    private String userId; //员工号
    private String memberNo;//会员卡号
    private String amount;//储值金额
    private String serial;
    private String transType;//储值类型:0
    private String payType;//0 现金 1 银行卡

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
