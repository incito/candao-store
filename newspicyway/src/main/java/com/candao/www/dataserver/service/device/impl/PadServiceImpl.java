package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.PadLoginData;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import com.candao.www.utils.HttpUtil;
import com.candao.www.utils.PropertiesUtil;
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
    private MsgProcessService msgProcessService;

    @Override
    public void handler(String msg) {
        checkIn(msg);
    }

    private void checkIn(String msg) {
        final PadLoginData padLoginData = MsgAnalyzeTool.analyzeToPadDevice(msg);
        String respData = padLoginIn(padLoginData);
        Map<String, List<String>> target = new HashMap<>();
        target.put(padLoginData.getGroup(), new ArrayList<String>() {{
            add(padLoginData.getId());
        }});
        msgProcessService.forwardMsg(target, JSON.toJSONString(MsgForwardTran.getPadCheckInConfirm(respData)));
    }

    @Transactional
    private String padLoginIn(PadLoginData loginData) {
        LOGGER.info("### padLoginIn group={},id={},userId={} ###", loginData.getGroup(), loginData.getId(), loginData.getUserId());
        String resp = null;
        try {
            resp = HttpUtil.doRestfulByHttpConnection(PropertiesUtil.getValue("pad_login_url"), JSON.toJSONString(loginData));
        } catch (Exception e) {
            LOGGER.error("### padLoginIn group={},id={},userId={},error={} ###", loginData.getGroup(), loginData.getId(), loginData.getUserId(), e);
        }
        return resp;
    }
}
