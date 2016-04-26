package com.candao.www.dataserver.service.device.obj;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/22.
 */
public class DeviceObject extends Device {
    private MsgForwardService msgForwardService = SpringContextUtils.getBean("msgForwardService");

    public DeviceObject() {
        super();

    }

    public DeviceObject(String deviceGroup, String deviceId, String tableNo, String userId) {
        super(deviceGroup, deviceId, tableNo, userId);
    }

    private String sendMsgSync(Integer id, String msg) {
        return this.msgForwardService.broadCastMsgSync(getId(), msg);
    }

    private void sendMsg(Integer id, String msg) {
        this.msgForwardService.broadCastMsg(getId(), msg);
    }

    private void sendMsg(MsgForwardData msgForwardData) {
        Map<String, List<String>> target = new HashMap<>();
        target.put(getDeviceGroup(), new ArrayList<String>() {{
            add(getDeviceId());
        }});
        this.msgForwardService.forwardMsg(target, JSON.toJSONString(msgForwardData));
    }
}
