package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.entity.Watch;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.model.ReConnectData;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/23.
 */
public class ReConnectServiceImpl extends DeviceServiceImpl {
    @Autowired
    private MsgForwardService msgForwardService;
    @Autowired
    private OfflineMsgService offlineMsgService;

    @Override
    public void handler(DeviceObject deviceObject, String serialNumber, String msg) {
        try {
            LOGGER.info("### reconnect msg={} ###", msg);
            final ReConnectData reConnectData = MsgAnalyzeTool.analyzeToReConData(msg);
            Map<String, List<String>> target = new HashMap<>();
            target.put(reConnectData.getGroup(), new ArrayList<String>() {{
                add(reConnectData.getId());
            }});
            saveOrUpdateDevice(new Device(reConnectData.getGroup(), reConnectData.getId(), reConnectData.getSsId()));
            //登录需要ack
            MsgForwardData msgForwardData = MsgForwardTran.getReconnectResp(JSON.toJSONString(new ResponseData()));
            msgForwardData.setSerialNumber(serialNumber);
            msgForwardService.forwardMsg(target, JSON.toJSONString(msgForwardData));
            for (OfflineMsg offlineMsg : offlineMsgService.getByGroupAndId(reConnectData.getGroup(), reConnectData.getId())) {
                OfflineMsgData offlineMsgData = new OfflineMsgData(offlineMsg.getId(), offlineMsg.getContent());
                MsgForwardData offMsgData = MsgForwardTran.getOffLineSend(JSON.toJSONString(offlineMsgData));
                msgForwardService.forwardMsg(target, JSON.toJSONString(offMsgData));
            }
        } catch (Exception e) {
            LOGGER_ERROR.error("#### reconnect msg={},error={} ###", msg, e);
        }
    }
}