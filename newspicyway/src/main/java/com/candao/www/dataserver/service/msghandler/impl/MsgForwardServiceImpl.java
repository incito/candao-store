package com.candao.www.dataserver.service.msghandler.impl;

import com.alibaba.fastjson.JSON;
import com.candao.communication.factory.LockFactory;
import com.candao.www.constant.Constant;
import com.candao.www.dataserver.constants.MsgType;
import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.entity.SyncMsg;
import com.candao.www.dataserver.mapper.MsgProcessMapper;
import com.candao.www.dataserver.model.*;
import com.candao.www.dataserver.service.communication.CommunicationService;
import com.candao.www.dataserver.service.device.DeviceObjectService;
import com.candao.www.dataserver.service.device.DeviceService;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.dish.DishService;
import com.candao.www.dataserver.service.member.BusinessService;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.MsgHandler;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.util.PropertyUtil;
import com.candao.www.dataserver.util.WorkDateUtil;
import com.candao.www.utils.TsThread;
import org.apache.commons.lang.StringUtils;
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
    @Autowired
    private DishService dishService;
    @Autowired
    private BusinessService businessService;

    @Override
    public String broadCastMsg(String userId, String msgType, String msg) {
        LOGGER.info("#### broadCastMsg userId={},msgType={},msg={}###", userId, msgType, msg);
        ResponseData responseData = new ResponseData();
        try {
            new TsThread(Constant.TS_URL + msgType + "/" + msg).run();
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsg userId={},msgType={},msg={},error={}###", userId, msgType, msg, e);
        }
        return JSON.toJSONString(new ResultData(JSON.toJSONString(responseData)));
    }

    @Override
    public String broadCastMsg4Netty(String msgType, String msg) {
        LOGGER.info("#### broadCastMsgForNetty msgType={},msg={}###", msgType, msg);
        ResponseData responseData = new ResponseData();
        try {
            int msgId = (int) System.currentTimeMillis();
            MsgData msgData = new MsgData(msgId, Integer.valueOf(msgType), msg);
            broadCastMsgDevices(deviceObjectService.getAllDevice(), JSON.toJSONString(msgData), msgType, false);
            if (MsgType.MSG_1002.getValue().equals(msgType)) {
                String tableNo = businessService.getTableNoByOrderId(msg);
                if (StringUtils.isNotBlank(tableNo)) {
                    dishService.deletePosOperation(tableNo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsgForNetty msgType={},msg={},error={}###", msgType, msg, e);
        }
        return JSON.toJSONString(new ResultData(JSON.toJSONString(responseData)));
    }

    @Override
    public void broadCastMsg4Netty(String msgId, Object msgData,boolean isSingle) {
        String msg="";
        if(null!=msgData){
            msg=JSON.toJSONString(msgData);
        }
        LOGGER.info("#### broadCastMsgForNetty msgId={},msg={}###", msgId, msg);
        ResponseData responseData = new ResponseData();
        try {
            broadCastMsgDevices(deviceObjectService.getAllDevice(),msg, msgId, isSingle);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsgForNetty###",  e);
        }
    }

    @Override
    @Transactional
    public String broadCastOk(String client, String msgId) {
        LOGGER.info("###broadCastOk client={},msgId={}###", client, msgId);
        ResponseData responseData = new ResponseData();
        try {
        /*    msgProcessMapper.saveSyncClient(client, msgId);
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

            }*/
        } catch (Exception e) {
            LOGGER.error("###broadCastOk client={},msgId={},error={}###", client, msgId, e);
        }
        return JSON.toJSONString(new ResultData(JSON.toJSONString(responseData)));
    }

    public void broadCastMsgDevices(List<DeviceObject> objects, String msg, String msgType, boolean isSingle) {
        List<OfflineMsg> offlineMsgList = new ArrayList<>();
        for (final DeviceObject deviceObject : objects) {
            int single = 0;
            if (isSingle) {
                single = 1;
            }
            OfflineMsg offlineMsg = new OfflineMsg(msgType, msg, deviceObject.getDeviceGroup(), deviceObject.getDeviceId(), single);
            offlineMsgList.add(offlineMsg);
        }
        offlineMsgService.save(offlineMsgList, isSingle);
        for (final OfflineMsg offlineMsg : offlineMsgList) {
            Map<String, List<String>> target = new HashMap<>();
            target.put(offlineMsg.getDeviceGroup(), new ArrayList<String>() {{
                add(offlineMsg.getDeviceId());
            }});
            OfflineMsgData offlineMsgData = new OfflineMsgData(offlineMsg.getId(), offlineMsg.getContent());
            MsgForwardData offMsgData = MsgForwardTran.getOffLineSend(JSON.toJSONString(offlineMsgData));
            communicationService.forwardMsg(target, JSON.toJSONString(offMsgData));
        }
    }

    @Override
    public void broadCastMsgOnLine(String msgType, String msg, boolean isSingle) {
        LOGGER.info("#### broadCastMsgOnLine msgType={},msg={}###", msgType, msg);
        ResponseData responseData = new ResponseData();
        try {
            broadCastMsgDevices(deviceObjectService.getOnLineDevice(), msg, msgType, isSingle);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsgOnLine msgType={},msg={},error={}###", msgType, msg, e);
        }
    }

    @Override
    public void broadCastMsgOnLine(String msgType, String msg, boolean isSingle, int expireSeconds) {
        LOGGER.info("#### broadCastMsgOnLine msgType={},msg={},isSingle={},expireSeconds={}###", msgType, msg, isSingle, expireSeconds);
        List<DeviceObject> deviceObjectList = deviceObjectService.getAllDevice();
        List<OfflineMsg> offlineMsgList = new ArrayList<>();
        for (final DeviceObject deviceObject : deviceObjectList) {
            int single = 0;
            if (isSingle) {
                single = 1;
            }
            OfflineMsg offlineMsg = new OfflineMsg(msgType, msg, deviceObject.getDeviceGroup(), deviceObject.getDeviceId(), single);
            offlineMsg.setExpireTime(WorkDateUtil.getAfterSeconds(expireSeconds));
            offlineMsgList.add(offlineMsg);
        }
        offlineMsgService.save(offlineMsgList, isSingle);//todo 改为内存，删除消息异步
        for (final OfflineMsg offlineMsg : offlineMsgList) {
            Map<String, List<String>> target = new HashMap<>();
            target.put(offlineMsg.getDeviceGroup(), new ArrayList<String>() {{
                add(offlineMsg.getDeviceId());
            }});
            OfflineMsgData offlineMsgData = new OfflineMsgData(offlineMsg.getId(), offlineMsg.getContent());
            MsgForwardData offMsgData = MsgForwardTran.getOffLineSend(JSON.toJSONString(offlineMsgData));
            communicationService.forwardMsg(target, JSON.toJSONString(offMsgData));
        }
    }

    @Override
    public void broadCastMsgGroup(String group, String msgType, String msg, boolean isSingle) {
        LOGGER.info("#### broadCastMsgGroup msgType={},msg={}###", msgType, msg);
        ResponseData responseData = new ResponseData();
        try {
            broadCastMsgDevices(deviceObjectService.getOnLineDevice(group), msg, msgType, false);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setData("0");
            responseData.setData("发送广播消息异常");
            LOGGER.error("#### broadCastMsgGroup msgType={},msg={},error={}###", msgType, msg, e);
        }
    }

    @Override
    public String broadCastMsgSync(Integer id, String msg) {
        final Device device = deviceService.getDeviceById(id);
        Map<String, List<String>> target = new HashMap<>();
        target.put(device.getDeviceGroup(), new ArrayList<String>() {{
            add(device.getDeviceId());
        }});
        MsgForwardData msgForwardData = MsgForwardTran.getSendMsgSync(msg);
        return sendMsgSync(target, msgForwardData);
    }

    @Override
    public void broadCastMsg(Integer id, String msg) {
        final Device device = deviceService.getDeviceById(id);
        Map<String, List<String>> target = new HashMap<>();
        target.put(device.getDeviceGroup(), new ArrayList<String>() {{
            add(device.getDeviceId());
        }});
        MsgForwardData msgForwardData = MsgForwardTran.getSendMsgSync(msg);
        communicationService.forwardMsg(target, JSON.toJSONString(msgForwardData));
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
        int awaitTime = 10000;
        String awaitTimeStr = PropertyUtil.getProInfo("dataserver-config", "await_time_millis");
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
            } else {
                resp.setData("0");
                resp.setInfo("超时");
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
