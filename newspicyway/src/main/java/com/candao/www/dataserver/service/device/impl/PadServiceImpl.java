package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.entity.Pad;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.PadLoginData;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/21.
 */
public class PadServiceImpl extends DeviceServiceImpl {
    @Autowired
    private MsgForwardService msgForwardService;
    @Autowired
    private OfflineMsgService offlineMsgService;

    @Override
    public void handler(DeviceObject deviceObject, String serialNumber, String msg) {
        checkIn(deviceObject, serialNumber, msg);
    }

    private void checkIn(DeviceObject deviceObject, String serialNumber, String msg) {
        final PadLoginData padLoginData = MsgAnalyzeTool.analyzeToPadDevice(msg);
        Map<String, List<String>> target = new HashMap<>();
        target.put(padLoginData.getGroup(), new ArrayList<String>() {{
            add(padLoginData.getId());
        }});
        saveOrUpdateDevice(new Pad(padLoginData.getGroup(), padLoginData.getId(), padLoginData.getSsId(), padLoginData.getMeid()));
        MsgForwardData msgForwardData = MsgForwardTran.getPadCheckInConfirm(JSON.toJSONString(new ResponseData()));
        msgForwardData.setSerialNumber(serialNumber);
        msgForwardService.forwardMsg(target, JSON.toJSONString(msgForwardData));
        for (OfflineMsg offlineMsg : offlineMsgService.getByGroupAndId(padLoginData.getGroup(), padLoginData.getId())) {
            MsgForwardData offMsgData = new MsgForwardData(offlineMsg.getMsgType(), offlineMsg.getId(), offlineMsg.getContent());
            msgForwardService.forwardMsg(target, JSON.toJSONString(offMsgData));
        }
    }
}
