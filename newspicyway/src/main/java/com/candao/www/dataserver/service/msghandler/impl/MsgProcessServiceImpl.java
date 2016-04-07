package com.candao.www.dataserver.service.msghandler.impl;

import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.service.communication.CommunicationService;
import com.candao.www.dataserver.service.msghandler.MsgHandler;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/16.
 */
public class MsgProcessServiceImpl implements MsgProcessService, MsgHandler {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MsgProcessServiceImpl.class);
    @Autowired
    private CommunicationService communicationService;
    private Map<String, List<MsgHandler>> msgHandlerMap;
    @Override
    public void processMsg(String msg) {
        MsgForwardData msgForwardData = MsgAnalyzeTool.analyzeMsgForward(msg);
        String msgId = msgForwardData.getMsgId();
        if (this.msgHandlerMap.containsKey(msgId)) {
            for (MsgHandler msgHandler : this.msgHandlerMap.get(msgId)) {
                try {
                    String serialNumber = msgForwardData.getSerialNumber();
                    if (mapLocks.containsKey(serialNumber)) {
                        mapResults.put(serialNumber, msgForwardData.getMsgData());
                        try {
                            mapLocks.get(serialNumber).lock();
                            mapConditions.get(serialNumber).signal();
                        } finally {
                            mapLocks.get(serialNumber).unlock();
                        }
                    }
                    msgHandler.handler(msgForwardData.getMsgData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String queryTerminals(String msg) {
        return communicationService.queryTerminals(msg);
    }

    @Override
    public String queryTerminalsByIp(String ip) {
        return communicationService.queryTerminalsByIp(ip);
    }

    @Override
    public void forwardMsg(Map<String, List<String>> targetMap, String msg) {
        communicationService.forwardMsg(targetMap, msg);
    }

    @Override
    public String forwardMsgSync(Map<String, List<String>> targetMap, String msg) {
        return communicationService.forwardMsgSync(targetMap, msg);
    }

    public void setMsgHandlerMap(Map msgHandlerMap) {
        this.msgHandlerMap = msgHandlerMap;
    }

    @Override
    public void handler(String msg) {

    }
}
