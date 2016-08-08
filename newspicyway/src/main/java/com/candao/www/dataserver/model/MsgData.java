package com.candao.www.dataserver.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by ytq on 2016/3/16.
 */
public class MsgData implements Serializable {
    @JSONField(name = "msgid")
    private Integer msgId;
    @JSONField(name = "msgtype")
    private Integer msgType;
    @JSONField(name = "msg")
    private String msg;

    public MsgData(Integer msgId, Integer msgType, String msg) {
        this.msgId = msgId;
        this.msgType = msgType;
        this.msg = msg;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
