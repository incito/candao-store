package com.candao.www.dataserver.entity;

/**
 * Created by ytq on 2016/4/7.
 */
public class SyncMsg {
    private Integer id;
    private String msgtype;
    private String msg;

    public SyncMsg(String msgtype, String msg) {
        this.msgtype = msgtype;
        this.msg = msg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
