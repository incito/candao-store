package com.candao.www.dataserver.service.msghandler.impl;

import com.alibaba.fastjson.JSON;
import com.candao.communication.factory.LockFactory;
import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.entity.SyncMsg;
import com.candao.www.dataserver.mapper.MsgProcessMapper;
import com.candao.www.dataserver.model.MsgData;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.service.communication.CommunicationService;
import com.candao.www.dataserver.service.device.DeviceObjectService;
import com.candao.www.dataserver.service.device.DeviceService;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.MsgHandler;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by ytq on 2016/4/6.
 */
public class MsgForwardServiceImpl implements MsgForwardService, MsgHandler {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MsgProcessServiceImpl.class);
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private MsgProcessMapper msgProcessMapper;
    @Autowired
    private OfflineMsgService offlineMsgService;
    @Autowired
    private DeviceObjectService deviceObjectService;
    @Autowired
    private DeviceService deviceService;

    @Override
    public String broadCastMsg(String userId, String msgType, String msg) {
        LOGGER.info("#### broadCastMsg userId={},msgType={},msg={}###", userId, msgType, msg);
        ResponseData responseData = new ResponseData();
        try {
            SyncMsg syncMsg = new SyncMsg(msgType, msg);
            msgProcessMapper.saveTSyncMsg(syncMsg);
            MsgData msgData = new MsgData(syncMsg.getId(), Integer.valueOf(msgType), msg);
            broadCastMsg(deviceObjectService.getAllDevice(), JSON.toJSONString(msgData), msgType, false);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsg userId={},msgType={},msg={},error={}###", userId, msgType, msg, e);
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    @Transactional
    public String broadCastOk(String client, String msgId) {
        LOGGER.info("###broadCastOk client={},msgId={}###", client, msgId);
        ResponseData responseData = new ResponseData();
        try {
            msgProcessMapper.saveSyncClient(client, msgId);
            //清除历史记录
            String msgId_500 = (Integer.valueOf(msgId) - 500) + "";
            msgProcessMapper.deleteSyncClient(msgId_500);
            Integer msgType = msgProcessMapper.selectMsgTypeByMsgId(msgId);
            if (null != msgType) {
                if (((msgType == 1002) || (msgType == 1005)) || ((msgType > 2000) && (msgType < 3000))) {
                    msgProcessMapper.updateSyncMsgRec(msgId);
                    msgProcessMapper.updateSyncMsgByMsgType1002();
                }
                String msg = String.format("%s;%s", msgId, "ok");
                Map<String, List<String>> target = new HashMap<>();
                for (final DeviceObject deviceObject : deviceObjectService.getAllDevice()) {
                    if (target.containsKey(deviceObject.getDeviceGroup())) {
                        target.get(deviceObject.getDeviceGroup()).add(deviceObject.getDeviceId());
                    } else {
                        target.put(deviceObject.getDeviceGroup(), new ArrayList<String>() {{
                            add(deviceObject.getDeviceId());
                        }});
                    }
                }
                communicationService.forwardMsg(target, msg);

            }
        } catch (Exception e) {
            LOGGER.error("###broadCastOk client={},msgId={},error={}###", client, msgId, e);
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    public void broadCastMsg(List<DeviceObject> objects, String msg, String msgType) {
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
        communicationService.forwardMsgSync(target, msg);
    }

    private void broadCastMsg(List<DeviceObject> objects, String msg, String msgType, boolean isSingle) {
        for (final DeviceObject deviceObject : objects) {
            Map<String, List<String>> target = new HashMap<>();
            target.put(deviceObject.getDeviceGroup(), new ArrayList<String>() {{
                add(deviceObject.getDeviceId());
            }});
            int single = 0;
            if (isSingle) {
                single = 1;
            }
            OfflineMsg offlineMsg = new OfflineMsg(msgType, msg, deviceObject.getDeviceGroup(), deviceObject.getDeviceId(), single);
            offlineMsgService.save(offlineMsg);
            OfflineMsgData offlineMsgData = new OfflineMsgData(offlineMsg.getId(), offlineMsg.getContent());
            MsgForwardData offMsgData = MsgForwardTran.getOffLineSend(JSON.toJSONString(offlineMsgData));
            communicationService.forwardMsg(target, JSON.toJSONString(offMsgData));
        }
    }

    @Override
    public void broadCastMsg(String userId, String msgType, String msg, boolean isSingle) {
        LOGGER.info("#### broadCastMsg userId={},msgType={},msg={}###", userId, msgType, msg);
        ResponseData responseData = new ResponseData();
        try {
//            SyncMsg syncMsg = new SyncMsg(msgType, msg);
//            msgProcessMapper.saveTSyncMsg(syncMsg);
//            MsgData msgData = new MsgData(syncMsg.getId(), Integer.valueOf(msgType), msg);
//            broadCastMsg(deviceObjectService.getOnLineDevice(), JSON.toJSONString(msgData), msgType, false);
            broadCastMsg(deviceObjectService.getOnLineDevice(), msg, msgType, false);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsg userId={},msgType={},msg={},error={}###", userId, msgType, msg, e);
        }
    }

    @Override
    public void broadCastMsg(String group, String userId, String msgType, String msg, boolean isSingle) {
        LOGGER.info("#### broadCastMsg userId={},msgType={},msg={}###", userId, msgType, msg);
        ResponseData responseData = new ResponseData();
        try {
//            SyncMsg syncMsg = new SyncMsg(msgType, msg);
//            msgProcessMapper.saveTSyncMsg(syncMsg);
//            MsgData msgData = new MsgData(syncMsg.getId(), Integer.valueOf(msgType), msg);
//            broadCastMsg(deviceObjectService.getOnLineDevice(), JSON.toJSONString(msgData), msgType, false);
            broadCastMsg(deviceObjectService.getOnLineDevice(group), msg, msgType, false);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsg userId={},msgType={},msg={},error={}###", userId, msgType, msg, e);
        }
    }

    @Override
    public String broadCastMsg(Integer id, MsgForwardData msgForwardData, String msgType, boolean isSingle) {
        final Device device = deviceService.getDeviceById(id);
        Map<String, List<String>> target = new HashMap<>();
        target.put(device.getDeviceGroup(), new ArrayList<String>() {{
            add(device.getDeviceId());
        }});
        int single = 0;
        if (isSingle) {
            single = 1;
        }
        OfflineMsg offlineMsg = new OfflineMsg(JSON.toJSONString(msgForwardData), msgType, device.getDeviceGroup(), device.getDeviceId(), single);
        offlineMsgService.save(offlineMsg);
        OfflineMsgData offlineMsgData = new OfflineMsgData(offlineMsg.getId(), offlineMsg.getContent());
        MsgForwardData offMsgData = MsgForwardTran.getOffLineSend(JSON.toJSONString(offlineMsgData));
        return sendMsgSync(target, offMsgData);
    }

    @Override
    public void forwardMsg(Map<String, List<String>> targetMap, String msg) {
        communicationService.forwardMsg(targetMap, msg);
    }

    /**
     * 发送同步消息
     */
    private String sendMsgSync(Map<String, List<String>> targetMap, MsgForwardData msgForwardData) {
        Lock lock = LockFactory.getLock();
        Condition condition = lock.newCondition();
        String serialNumber = msgForwardData.getSerialNumber();
        mapLocks.put(serialNumber, lock);
        mapConditions.put(serialNumber, condition);
        int awaitTime = 30000;
        String awaitTimeStr = PropertiesUtil.getValue("await_time_millis");
        if (awaitTimeStr != null) {
            awaitTime = Integer.valueOf(awaitTimeStr);
        }
        ResponseData resp = new ResponseData();
        try {
            lock.lock();
            communicationService.forwardMsg(targetMap, JSON.toJSONString(msgForwardData));
            if (!mapResults.containsKey(serialNumber)) {
                condition.await(awaitTime, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            resp.setData("0");
            resp.setInfo(e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
            if (mapResults.containsKey(serialNumber)) {
                resp.setInfo(mapResults.get(serialNumber));
            }
            mapLocks.remove(serialNumber);
            mapConditions.remove(serialNumber);
            mapResults.remove(serialNumber);
        }
        return JSON.toJSONString(resp);
    }

    @Override
    public void handler(DeviceObject deviceObject, String serialNumber, String msg) {

    }
}
