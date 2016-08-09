package com.candao.www.data.model;

public class Ttable {
    private Long tableid;

    private String position;

    private String number;
	/**
	 * 0 空闲 1 就餐 3 预定 4 已结账 5删除
	 */
    private Integer status;

    private Long restaurantid;

    private Integer isvip;

    private Integer iscompartment;

    private Long isavailable;

    public Long getTableid() {
        return tableid;
    }

    public void setTableid(Long tableid) {
        this.tableid = tableid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(Long restaurantid) {
        this.restaurantid = restaurantid;
    }

    public Integer getIsvip() {
        return isvip;
    }

    public void setIsvip(Integer isvip) {
        this.isvip = isvip;
    }

    public Integer getIscompartment() {
        return iscompartment;
    }

    public void setIscompartment(Integer iscompartment) {
        this.iscompartment = iscompartment;
    }

    public Long getIsavailable() {
        return isavailable;
    }

    public void setIsavailable(Long isavailable) {
        this.isavailable = isavailable;
    }
}