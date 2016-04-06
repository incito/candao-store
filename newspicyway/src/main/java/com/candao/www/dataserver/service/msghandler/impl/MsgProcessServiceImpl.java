package com.candao.www.dataserver.service.msghandler.impl;

import com.alibaba.fastjson.JSON;
import com.candao.communication.factory.LockFactory;
import com.candao.utils.PropertiesUtil;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.mapper.MsgProcessMapper;
import com.candao.www.dataserver.model.MsgData;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.service.communication.CommunicationService;
import com.candao.www.dataserver.service.device.DeviceObjectService;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgHandler;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by ytq on 2016/3/16.
 */
public class MsgProcessServiceImpl implements MsgProcessService, MsgHandler {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MsgProcessServiceImpl.class);
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private MsgProcessMapper msgProcessMapper;
    @Autowired
    private OfflineMsgService offlineMsgService;
    @Autowired
    private DeviceObjectService deviceObjectService;
    private Map<String, List<MsgHandler>> msgHandlerMap;

    @Override
    public String broadCastMsg(String userId, String msgType, String msg) {
        LOGGER.info("#### broadCastMsg userId={},msgType={},msg={}###", userId, msgType, msg);
        ResponseData responseData = new ResponseData();
        try {
            Integer msgId = msgProcessMapper.saveTSyncMsg(msgType, msg);
            MsgData msgData = new MsgData(msgId, Integer.valueOf(msgType), msg);
            broadCastMsg(deviceObjectService.getAllDevice(), JSON.toJSONString(msgData), null, false);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsg userId={},msgType={},msg={},error={}###", userId, msgType, msg, e);
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    public String broadCastMsg(List<DeviceObject> objects, String msg, String msgType, boolean isSingle) {
        Map<String, List<String>> target = new HashMap<>();
        for (final DeviceObject deviceObject : objects) {
            if (target.containsKey(deviceObject.getDeviceGroup())) {
                target.get(deviceObject.getDeviceGroup()).add(deviceObject.getDeviceId());
            } else {
                target.put(deviceObject.getDeviceGroup(), new ArrayList<String>() {{
                    add(deviceObject.getDeviceId());
                }});
            }
        }
        String resp = communicationService.forwardMsgSync(target, msg);
        List<String> offDeviceList = MsgAnalyzeTool.analyzeMsgForwardResp(resp);
        for (String deviceStr : offDeviceList) {
            String group = deviceStr.split(":")[0];
            String id = deviceStr.split(":")[1];
            int single = 0;
            if (isSingle) {
                single = 1;
            }
            offlineMsgService.save(new OfflineMsg(group, msg, msgType, id, single));
        }
        return resp;
    }

    @Override
    public void processMsg(String msg) {
        MsgForwardData msgForwardData = MsgAnalyzeTool.analyzeMsgForward(msg);
        String msgId = msgForwardData.getMsgId();
        if (this.msgHandlerMap.containsKey(msgId)) {
            for (MsgHandler msgHandler : this.msgHandlerMap.get(msgId)) {
                try {
                    String msgNum = msgForwardData.getMessageNumber();
                    if (mapLocks.containsKey(msgNum)) {
                        mapResults.put(msgNum, msgForwardData.getMsgData());
                        try {
                            mapLocks.get(msgNum).lock();
                            mapConditions.get(msgNum).signal();
                        } finally {
                            mapLocks.get(msgNum).unlock();
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

    @Override
    public Integer save(OfflineMsg offlineMsg) {
        return offlineMsgService.save(offlineMsg);
    }

    @Override
    public List<OfflineMsg> getByGroupAndId(String group, String id) {
        return offlineMsgService.getByGroupAndId(group, id);
    }

    @Override
    public void deleteById(Integer id) {
        offlineMsgService.deleteById(id);
    }

    /**
     * 发送同步消息
     */
    private String sendMsgSync(Map<String, List<String>> targetMap, MsgForwardData msgForwardData) {
        Lock lock = LockFactory.getLock();
        Condition condition = lock.newCondition();
        mapLocks.put(msgForwardData.getMessageNumber(), lock);
        mapConditions.put(msgForwardData.getMessageNumber(), condition);
        int awaitTime = 30;
        String awaitTimeStr = PropertiesUtil.getValue("await_time");
        if (awaitTimeStr != null) {
            awaitTime = Integer.valueOf(awaitTimeStr);
        }
        String resp = null;
        try {
            lock.lock();
            communicationService.forwardMsg(targetMap, JSON.toJSONString(msgForwardData));
            if (!mapResults.containsKey(msgForwardData.getMessageNumber())) {
                condition.await(awaitTime, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            if (mapResults.containsKey(msgForwardData.getMessageNumber())) {
                resp = mapResults.get(msgForwardData.getMessageNumber());
            }
            mapLocks.remove(msgForwardData.getMessageNumber());
            mapConditions.remove(msgForwardData.getMessageNumber());
            mapResults.remove(msgForwardData.getMessageNumber());
        }
        return resp;
    }

    public void setMsgHandlerMap(Map msgHandlerMap) {
        this.msgHandlerMap = msgHandlerMap;
    }

    @Override
    public void handler(String msg) {

    }
}
