package com.candao.www.dataserver.model;

import com.candao.www.dataserver.util.IDUtil;

/**
 * Created by ytq on 2016/3/18.
 */
public class MsgForwardData {
    private String msgId;
    private String serialNumber;
    private String msgData;

    public MsgForwardData() {

    }

    public MsgForwardData(String msgId, String msgData) {
        this.serialNumber = IDUtil.getID();
        this.msgId = msgId;
        this.msgData = msgData;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgData() {
        return msgData;
    }

    public void setMsgData(String msgDta) {
        this.msgData = msgDta;
    }
}
