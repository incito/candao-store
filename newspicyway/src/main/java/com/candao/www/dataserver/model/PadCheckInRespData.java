package com.candao.www.dataserver.model;

/**
 * Created by ytq on 2016/3/23.
 */
public class PadCheckInRespData extends PadLoginData {
    private String code;
    private String info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
