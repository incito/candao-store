package com.candao.www.dataserver.model;

/**
 * Created by lenovo on 2016/3/23.
 */
public class MemberAPIData {
    private String errMsg;
    private Object data;

    public MemberAPIData() {
    }

    public MemberAPIData(Object data, String errMsg) {
        this.data = data;
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
