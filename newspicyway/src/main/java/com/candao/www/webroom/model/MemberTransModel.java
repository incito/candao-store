package com.candao.www.webroom.model;

import java.util.Date;

public class MemberTransModel {
    private int id;
    private short system;
    private Date ctime;
    private String operatorId;
    private String operatorName;
    private short operateType;
    private String orderId;
    private double realAmount;
    private double addedAmount;
    private short payway;
    private String serial;
    private String cardno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getSystem() {
        return system;
    }

    public void setSystem(short system) {
        this.system = system;
    }

    public Date getCtime() {
        return ctime ;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public short getOperateType() {
        return operateType;
    }

    public void setOperateType(short operateType) {
        this.operateType = operateType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

    public double getAddedAmount() {
        return addedAmount;
    }

    public void setAddedAmount(double addedAmount) {
        this.addedAmount = addedAmount;
    }

    public short getPayway() {
        return payway;
    }

    public void setPayway(short payway) {
        this.payway = payway;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

}
