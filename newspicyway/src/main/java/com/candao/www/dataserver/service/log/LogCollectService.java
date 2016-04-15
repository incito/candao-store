package com.candao.www.dataserver.service.log;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.LogCollectData;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.ReConnectData;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.MsgHandler;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/4/15.
 */
@Service
public class LogCollectService implements MsgHandler {
    private final static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("device_error");
    @Autowired
    private MsgForwardService msgForwardService;

    @Override
    public void handler(DeviceObject deviceObject, String serialNumber, String msg) {
        LOGGER.error(msg);
        final LogCollectData logCollectData = MsgAnalyzeTool.analyzeToLogCoData(msg);
        Map<String, List<String>> target = new HashMap<>();
        target.put(logCollectData.getGroup(), new ArrayList<String>() {{
            add(logCollectData.getId());
        }});
        MsgForwardData msgForwardData = MsgForwardTran.getLogCollectResp(JSON.toJSONString(new ResponseData()));
        msgForwardData.setSerialNumber(serialNumber);
        msgForwardService.forwardMsg(target, JSON.toJSONString(msgForwardData));
    }
}
