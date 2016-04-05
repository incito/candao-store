package com.candao.www.dataserver.entity;

import java.util.Date;

/**
 * Created by lenovo on 2016/3/24.
 */
public class OrderMember {
    private int id;
    private String orderid;
    private String userid;
    private Date ordertime;
    private int operatetype;
    private String business;
    private String terminal;
    private String serial;
    private String batchno;
    private String businessname;
    private double score;
    private double coupons;
    private double stored;
    private double scorebalance;
    private double couponsbalance;
    private double storedbalance;
    private String cardno;
    private int valid;
    private String psexpansivity;
    private double netvalue;
    private double Inflated;
    private String remark;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public int getOperatetype() {
        return operatetype;
    }

    public void setOperatetype(int operatetype) {
        this.operatetype = operatetype;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public double getCoupons() {
        return coupons;
    }

    public void setCoupons(double coupons) {
        this.coupons = coupons;
    }

    public double getStored() {
        return stored;
    }

    public void setStored(double stored) {
        this.stored = stored;
    }

    public double getScorebalance() {
        return scorebalance;
    }

    public void setScorebalance(double scorebalance) {
        this.scorebalance = scorebalance;
    }

    public double getCouponsbalance() {
        return couponsbalance;
    }

    public void setCouponsbalance(double couponsbalance) {
        this.couponsbalance = couponsbalance;
    }

    public double getStoredbalance() {
        return storedbalance;
    }

    public void setStoredbalance(double storedbalance) {
        this.storedbalance = storedbalance;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public String getPsexpansivity() {
        return psexpansivity;
    }

    public void setPsexpansivity(String psexpansivity) {
        this.psexpansivity = psexpansivity;
    }

    public double getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(double netvalue) {
        this.netvalue = netvalue;
    }

    public double getInflated() {
        return Inflated;
    }

    public void setInflated(double inflated) {
        Inflated = inflated;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
