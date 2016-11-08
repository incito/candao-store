package com.candao.www.dataserver.service.msghandler.obj;

import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.service.SpringContextUtils;

import java.util.Properties;

/**
 * Created by ytq on 2016/3/24.
 */
public class MsgForwardTran {
    public static Properties msgConfig = SpringContextUtils.getBean("msgConfig");

    public static MsgForwardData getWatchCheckInConfirm(String msg) {
        String msgId = msgConfig.getProperty("MSG_ID.WATCH_CHECK_IN.CONFIRM");
        return new MsgForwardData(msgId, msg);
    }

    public static MsgForwardData getWatchCheckInRespConfirm(String msg) {
        String msgId = msgConfig.getProperty("MSG_ID.WATCH_CHECK_IN.RESP.CONFIRM");
        return new MsgForwardData(msgId, msg);
    }

    public static MsgForwardData getPadCheckInConfirm(String msg) {
        String msgId = msgConfig.getProperty("MSG_ID.PAD_CHECK_IN.CONFIRM");
        return new MsgForwardData(msgId, msg);
    }

    public static MsgForwardData getPadCheckInRespConfirm(String msg) {
        String msgId = msgConfig.getProperty("MSG_ID.PAD_CHECK_IN.RESP.CONFIRM");
        return new MsgForwardData(msgId, msg);
    }

    public static MsgForwardData getOffLineSend(String msg) {
        String msgId = msgConfig.getProperty("MSG_ID.OFFLINE.SEND");
        return new MsgForwardData(msgId, msg);
    }

    public static MsgForwardData getReconnectResp(String msg) {
        String msgId = msgConfig.getProperty("MSG_ID.RECONNECT.RESP");
        return new MsgForwardData(msgId, msg);
    }

    public static MsgForwardData getSendMsgSync(String msg) {
        String msgId = msgConfig.getProperty("MSG_ID.SEND_MSG.SYNC");
        return new MsgForwardData(msgId, msg);
    }

    public static MsgForwardData getLogCollectResp(String msg) {
        String msgId = msgConfig.getProperty("MSF_ID.LOG.COLLECT.RESP");
        return new MsgForwardData(msgId, msg);
    }
}
