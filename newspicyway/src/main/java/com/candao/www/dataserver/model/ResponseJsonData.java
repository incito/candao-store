package com.candao.www.dataserver.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by ytq on 2016/3/15.
 */
public class ResponseJsonData implements Serializable {
    @JSONField(name = "Data")
    private String data = "1";
    @JSONField(name = "Info")
    private String info = "";
    @JSONField(name = "OrderJson")
    private Object orderJson = "{|Data|:[]}";
    @JSONField(name = "ListJson")
    private Object listJson = "{|Data|:[]}";
    @JSONField(name = "DoubleJson")
    private Object doubleJson = "{|Data|:[]}";
    @JSONField(name = "JSJson")
    private Object jsJson;
    @JSONField(name = "workdate")
    private String workDate = "";

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getOrderJson() {
        return orderJson;
    }

    public void setOrderJson(Object orderJson) {
        this.orderJson = orderJson;
    }

    public Object getListJson() {
        return listJson;
    }

    public void setListJson(Object listJson) {
        this.listJson = listJson;
    }

    public Object getDoubleJson() {
        return doubleJson;
    }

    public void setDoubleJson(Object doubleJson) {
        this.doubleJson = doubleJson;
    }

    public Object getJsJson() {
        return jsJson;
    }

    public void setJsJson(Object jsJson) {
        this.jsJson = jsJson;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }
}
