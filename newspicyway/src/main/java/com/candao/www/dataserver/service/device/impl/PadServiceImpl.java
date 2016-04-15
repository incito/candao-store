package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.PadLoginData;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import com.candao.www.utils.HttpUtil;
import com.candao.www.utils.PropertiesUtil;
import com.candao.www.utils.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public void handler(DeviceObject deviceObject, String serialNumber, String msg) {
        checkIn(deviceObject, serialNumber, msg);
    }

    private void checkIn(DeviceObject deviceObject, String serialNumber, String msg) {
        final PadLoginData padLoginData = MsgAnalyzeTool.analyzeToPadDevice(msg);
        String respData = padLoginIn(padLoginData);
        Map<String, List<String>> target = new HashMap<>();
        target.put(padLoginData.getGroup(), new ArrayList<String>() {{
            add(padLoginData.getId());
        }});
        MsgForwardData msgForwardData = MsgForwardTran.getPadCheckInConfirm(respData);
        msgForwardData.setSerialNumber(serialNumber);
        msgForwardService.forwardMsg(target, JSON.toJSONString(msgForwardData));
    }

    @Transactional
    private String padLoginIn(PadLoginData loginData) {
        LOGGER.info("### padLoginIn group={},id={},userId={} ###", loginData.getGroup(), loginData.getId(), loginData.getUserId());
        String resp = null;
        try {
            resp = HttpUtil.doRestfulByHttpConnection(PropertyUtil.getProInfo("dataserver-config", "pad_login_url"), JSON.toJSONString(loginData));
        } catch (Exception e) {
            LOGGER.error("### padLoginIn group={},id={},userId={},error={} ###", loginData.getGroup(), loginData.getId(), loginData.getUserId(), e);
        }
        return resp;
    }
}
