package com.candao.www.dataserver.model;

/**
 * 会员消费请求data
 * Created by lenovo on 2016/3/21.
 */
public class ConsumerData {
    private String userId;//员工号
    private String orderId;//账单号
    private String memberNo;//会员号
    private String serail;//可填帐单号
    private String cash;//现金加银行卡金额
    private String point;//使用积分
    private String transType;//消费类型:0
    private String store;//使用储值金额
    private String ticketList;//使用劵列表
    private String memberPwd;//会员密码
    private String memberyhqamount;//优惠券余额
    private String server;//Java后台服务IP端口

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getSerail() {
        return serail;
    }

    public void setSerail(String serail) {
        this.serail = serail;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getTicketList() {
        return ticketList;
    }

    public void setTicketList(String ticketList) {
        this.ticketList = ticketList;
    }

    public String getMemberPwd() {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public String getMemberyhqamount() {
        return memberyhqamount;
    }

    public void setMemberyhqamount(String memberyhqamount) {
        this.memberyhqamount = memberyhqamount;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
