package com.candao.www.data.model;

import java.util.Date;

public class Tprintcenter {
    private String id;

    private String orderid;

    private Integer printcount;

    private String userid;

    private Integer printtype;

    private Integer printstatus;

    private String printcontent;

    private Date printtime;

    private String printipdress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public Integer getPrintcount() {
        return printcount;
    }

    public void setPrintcount(Integer printcount) {
        this.printcount = printcount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getPrinttype() {
        return printtype;
    }

    public void setPrinttype(Integer printtype) {
        this.printtype = printtype;
    }

    public Integer getPrintstatus() {
        return printstatus;
    }

    public void setPrintstatus(Integer printstatus) {
        this.printstatus = printstatus;
    }

    public String getPrintcontent() {
        return printcontent;
    }

    public void setPrintcontent(String printcontent) {
        this.printcontent = printcontent;
    }

    public Date getPrinttime() {
        return printtime;
    }

    public void setPrinttime(Date printtime) {
        this.printtime = printtime;
    }

    public String getPrintipdress() {
        return printipdress;
    }

    public void setPrintipdress(String printipdress) {
        this.printipdress = printipdress;
    }
}