package com.candao.www.dataserver.entity;

import java.util.Date;

/**
 * 结算详情表实体类
 * Created by lenovo on 2016/3/23.
 */
public class SettlmentDetail {
    private String sdetailid;
    private String orderid;
    private String dishid;
    private String normalprice;
    private String discountamount;
    private String discountid;
    private String couponid;
    private String couponamount;
    private String payamount;
    private int couponNum;
    private String feeamount;
    private String settledid;
    private int incometype;
    private String membercardno;
    private String bankcardno;
    private Date opendate;
    private String username;
    private int payway;
    private short isclear;
    private String debitParterner;
    private Date inserttime;
    private String coupondetailid;

    public String getSdetailid() {
        return sdetailid;
    }

    public void setSdetailid(String sdetailid) {
        this.sdetailid = sdetailid;
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

    public String getNormalprice() {
        return normalprice;
    }

    public void setNormalprice(String normalprice) {
        this.normalprice = normalprice;
    }

    public String getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(String discountamount) {
        this.discountamount = discountamount;
    }

    public String getDiscountid() {
        return discountid;
    }

    public void setDiscountid(String discountid) {
        this.discountid = discountid;
    }

    public String getCouponid() {
        return couponid;
    }

    public void setCouponid(String couponid) {
        this.couponid = couponid;
    }

    public String getCouponamount() {
        return couponamount;
    }

    public void setCouponamount(String couponamount) {
        this.couponamount = couponamount;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public String getFeeamount() {
        return feeamount;
    }

    public void setFeeamount(String feeamount) {
        this.feeamount = feeamount;
    }

    public String getSettledid() {
        return settledid;
    }

    public void setSettledid(String settledid) {
        this.settledid = settledid;
    }

    public int getIncometype() {
        return incometype;
    }

    public void setIncometype(int incometype) {
        this.incometype = incometype;
    }

    public String getMembercardno() {
        return membercardno;
    }

    public void setMembercardno(String membercardno) {
        this.membercardno = membercardno;
    }

    public String getBankcardno() {
        return bankcardno;
    }

    public void setBankcardno(String bankcardno) {
        this.bankcardno = bankcardno;
    }

    public Date getOpendate() {
        return opendate;
    }

    public void setOpendate(Date opendate) {
        this.opendate = opendate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPayway() {
        return payway;
    }

    public void setPayway(int payway) {
        this.payway = payway;
    }

    public short getIsclear() {
        return isclear;
    }

    public void setIsclear(short isclear) {
        this.isclear = isclear;
    }

    public String getDebitParterner() {
        return debitParterner;
    }

    public void setDebitParterner(String debitParterner) {
        this.debitParterner = debitParterner;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public String getCoupondetailid() {
        return coupondetailid;
    }

    public void setCoupondetailid(String coupondetailid) {
        this.coupondetailid = coupondetailid;
    }
}
