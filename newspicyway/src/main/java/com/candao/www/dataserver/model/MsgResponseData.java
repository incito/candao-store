package com.candao.www.dataserver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ytq on 2016/3/17.
 */
public class MsgResponseData {
    private String code;
    private List<DeviceData> data = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DeviceData> getData() {
        return data;
    }

    public void setData(List<DeviceData> data) {
        this.data = data;
    }
}
