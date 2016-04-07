package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.entity.Pad;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.model.PadCheckInRespData;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/23.
 */
public class PadChInRServiceImpl extends DeviceServiceImpl {
    @Autowired
    private MsgProcessService msgProcessService;

    @Autowired
    private OfflineMsgService offlineMsgService;

    @Override
    public void handler(String msg) {
        try {
            LOGGER.info("### pad checkIn resp msg={} ###", msg);
            final PadCheckInRespData checkInRespData = MsgAnalyzeTool.analyzePadCheckInResp(msg);
            Map<String, List<String>> target = new HashMap<>();
            target.put(checkInRespData.getGroup(), new ArrayList<String>() {{
                add(checkInRespData.getId());
            }});
            saveOrUpdateDevice(new Pad(checkInRespData.getGroup(), checkInRespData.getId(), checkInRespData.getSsId(), checkInRespData.getUserId(), checkInRespData.getTableNo()));
            for (OfflineMsg offlineMsg : offlineMsgService.getByGroupAndId(checkInRespData.getGroup(), checkInRespData.getId())) {
                msgProcessService.forwardMsg(target, JSON.toJSONString(new OfflineMsgData(offlineMsg.getId(), offlineMsg.getContent())));
            }
        } catch (Exception e) {
            LOGGER_ERROR.error("### pad checkIn resp msg={},error={} ###", msg, e);
        }
    }
}
