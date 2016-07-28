package com.candao.www.dataserver.service.msghandler.impl;

import com.alibaba.fastjson.JSON;
import com.candao.communication.vo.Response;
import com.candao.www.dataserver.mapper.OfflineMsgMapper;
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
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

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
    @Autowired
    private OfflineMsgMapper offlineMsgMapper;
    private Executor msgExecutor= new ThreadPoolExecutor(0,2,30, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    @Override
    public void processMsg(String msg) {
        LOGGER.info("###processMsg msg={}###", msg);
        final MsgForwardData msgForwardData = MsgAnalyzeTool.analyzeMsgForward(msg);
        String msgId = msgForwardData.getMsgId();
        String serialNumber = msgForwardData.getSerialNumber();
            Lock lock = mapLocks.get(serialNumber);
            if (null!=lock) {
                lock.lock();
                try {
                    mapResults.put(serialNumber, msgForwardData.getMsgData());
                    mapConditions.get(serialNumber).signal();
                } finally {
                    lock.unlock();
                }
            }
        DeviceObject deviceObject = null;
        try {
            BaseData baseData = JSON.parseObject(msgForwardData.getMsgData(), BaseData.class);
            deviceObject = deviceObjectService.getByGroupAndId(baseData.getGroup(), baseData.getId());
        } catch (Exception e) {
            LOGGER.error("###processMsg msg={},error={}###", msg, e.getCause().getStackTrace());
            e.printStackTrace();
        }
        if (this.msgHandlerMap.containsKey(msgId)) {
            //清除离线消息记录
            msgExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    offlineMsgMapper.deleteById(msgForwardData.getSerialNumber());
                }
            });
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
