package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.model.ReConnectData;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
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
    private MsgProcessService msgProcessService;

    @Override
    public void handler(String msg) {
        try {
            LOGGER.info("### reconnect msg={} ###", msg);
            final ReConnectData reConnectData = MsgAnalyzeTool.analyzeToReConData(msg);
            Map<String, List<String>> target = new HashMap<>();
            target.put(reConnectData.getGroup(), new ArrayList<String>() {{
                add(reConnectData.getId());
            }});
            for (OfflineMsg offlineMsg : msgProcessService.getByGroupAndId(reConnectData.getGroup(), reConnectData.getId())) {
                String msgData = JSON.toJSONString(new OfflineMsgData(offlineMsg.getId(), offlineMsg.getContent()));
                msgProcessService.forwardMsgSync(target, JSON.toJSONString(MsgForwardTran.getOffLineSend(msgData)));
            }
        } catch (Exception e) {
            LOGGER_ERROR.error("#### reconnect msg={},error={} ###", msg, e);
        }
    }
}