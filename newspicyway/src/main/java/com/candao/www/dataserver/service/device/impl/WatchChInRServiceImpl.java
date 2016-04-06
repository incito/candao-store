package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.entity.Watch;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.model.WatchCheckInRespData;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by ytq on 2016/3/23.
 */
public class WatchChInRServiceImpl extends DeviceServiceImpl {
    @Autowired
    private MsgProcessService msgProcessService;


    @Override
    public void handler(String msg) {
        try {
            LOGGER.info("### watch checkIn resp msg={} ###", msg);
            final WatchCheckInRespData checkInRespData = MsgAnalyzeTool.analyzeWatchCheckInResp(msg);
            Map<String, List<String>> target = new HashMap<>();
            target.put(checkInRespData.getGroup(), new ArrayList<String>() {{
                add(checkInRespData.getId());
            }});
            saveOrUpdateDevice(new Watch(checkInRespData.getGroup(), checkInRespData.getId(), checkInRespData.getSsId(), checkInRespData.getUserId()));
            for (OfflineMsg offlineMsg : msgProcessService.getByGroupAndId(checkInRespData.getGroup(), checkInRespData.getId())) {
                msgProcessService.forwardMsgSync(target, JSON.toJSONString(new OfflineMsgData(offlineMsg.getId(), offlineMsg.getContent())));
            }
        } catch (Exception e) {
            LOGGER_ERROR.error("### watch checkIn resp msg={},error={} ###", msg, e);
        }
    }
}
