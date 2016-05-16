package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.WatchLoginData;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import com.candao.www.utils.HttpUtil;
import com.candao.www.utils.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/21.
 */
public class WatchServiceImpl extends DeviceServiceImpl {
    @Autowired
    private MsgForwardService msgForwardService;

    @Override
    public void handler(DeviceObject deviceObject, String serialNumber, String msg) {
        checkIn(deviceObject, serialNumber, msg);
    }

    private void checkIn(DeviceObject deviceObject, String serialNumber, String msg) {
        try {
            LOGGER.info("### watch checkIn msg={} ###", msg);
            final WatchLoginData watchLoginData = MsgAnalyzeTool.analyzeToWatchDevice(msg);
            String respData = braceletLoginIn(watchLoginData);
            Map<String, List<String>> target = new HashMap<>();
            target.put(watchLoginData.getGroup(), new ArrayList<String>() {{
                add(watchLoginData.getId());
            }});
            MsgForwardData msgForwardData = MsgForwardTran.getWatchCheckInConfirm(respData);
            msgForwardData.setSerialNumber(serialNumber);
            msgForwardService.forwardMsg(target, JSON.toJSONString(msgForwardData));
        } catch (Exception e) {
            LOGGER_ERROR.error("### watch checkIn msg={}  error={} ###", msg, e);
        }
    }

    private String braceletLoginIn(final WatchLoginData loginData) {
        LOGGER.info("### group={},id={},userId={} ###", loginData.getGroup(), loginData.getId(), loginData.getUserId());
        String resp = null;
        try {
            Map<String, String> mapParam = new HashMap<String, String>() {
                {
                    put("meid", loginData.getMeId());
                    put("userid", loginData.getUserId());
                }
            };
            resp = HttpUtil.doRestfulByHttpConnection(PropertyUtil.getProInfo("dataserver-config", "bracelet_login_url"), JSON.toJSONString(mapParam));
        } catch (Exception e) {
            LOGGER.error("### group={},id={},userId={},error={} ###", loginData.getGroup(), loginData.getId(), loginData.getUserId(), e);
        }
        return resp;
    }
}
