package com.candao.www.dataserver.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by ytq on 2016/3/15.
 */
public class ResponseData implements Serializable {
    @JSONField(name = "Data")
    private String data = "1";
    @JSONField(name = "Info")
    private String info;

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
}
