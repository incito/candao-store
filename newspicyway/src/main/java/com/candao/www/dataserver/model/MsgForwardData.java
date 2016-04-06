package com.candao.www.dataserver.model;

/**
 * Created by ytq on 2016/3/18.
 */
public class MsgForwardData {
    private String msgId;
    private String messageNumber;
    private String msgData;

    public MsgForwardData() {

    }

    public MsgForwardData(String msgId, String msgData) {
        this.messageNumber = System.currentTimeMillis() + "";
        this.msgId = msgId;
        this.msgData = msgData;
    }

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
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
