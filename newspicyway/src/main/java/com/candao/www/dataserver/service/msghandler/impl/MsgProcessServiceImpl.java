package com.candao.www.dataserver.service.msghandler.impl;

import com.alibaba.fastjson.JSON;
import com.candao.communication.vo.Response;
import com.candao.www.dataserver.model.BaseData;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.service.communication.CommunicationService;
import com.candao.www.dataserver.service.device.DeviceObjectService;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
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
    @Autowired
    private DeviceObjectService deviceObjectService;

    @Override
    public void processMsg(String msg) {
        LOGGER.info("###processMsg msg={}###", msg);
        MsgForwardData msgForwardData = MsgAnalyzeTool.analyzeMsgForward(msg);
        String msgId = msgForwardData.getMsgId();
        String serialNumber = msgForwardData.getSerialNumber();
        try {
            if (mapLocks.containsKey(serialNumber)) {
                mapResults.put(serialNumber, msgForwardData.getMsgData());
                try {
                    mapLocks.get(serialNumber).lock();
                    mapConditions.get(serialNumber).signal();
                } finally {
                    mapLocks.get(serialNumber).unlock();
                }
            }
            removeMsgNum4Map(serialNumber);
        } catch (Exception e) {
            LOGGER.info("###processMsg msg={},error={}###", msg, e);
            e.printStackTrace();
        }
        DeviceObject deviceObject = null;
        try {
            BaseData baseData = JSON.parseObject(msgForwardData.getMsgData(), BaseData.class);
            deviceObject = deviceObjectService.getByGroupAndId(baseData.getGroup(), baseData.getId());
        } catch (Exception e) {
            LOGGER.error("###processMsg msg={},error={}###", msg, e);
            e.printStackTrace();
        }
        if (this.msgHandlerMap.containsKey(msgId)) {
            for (MsgHandler msgHandler : this.msgHandlerMap.get(msgId)) {
                msgHandler.handler(deviceObject, serialNumber, msgForwardData.getMsgData());
            }
        }
    }

    private static void removeMsgNum4Map(String serialNumber) {
        mapLocks.remove(serialNumber);
        mapConditions.remove(serialNumber);
        mapResults.remove(serialNumber);
    }

    @Override
    public Response queryTerminals(String msg) {
        return communicationService.queryTerminals(msg);
    }

    @Override
    public Response queryTerminalsByIp(String ip) {
        return communicationService.queryTerminalsByIp(ip);
    }

    public void setMsgHandlerMap(Map msgHandlerMap) {
        this.msgHandlerMap = msgHandlerMap;
    }

    @Override
    public void handler(DeviceObject deviceObject, String serialNumber, String msg) {

    }
}
