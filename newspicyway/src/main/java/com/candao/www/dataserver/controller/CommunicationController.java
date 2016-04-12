package com.candao.www.dataserver.controller;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.service.communication.CommunicationService;
import com.candao.www.dataserver.service.device.DeviceObjectService;
import com.candao.www.dataserver.service.device.DeviceService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/9.
 */
@Controller
public class CommunicationController {
    @Autowired
    CommunicationService communicationService;
    @Autowired
    private DeviceObjectService deviceObjectService;
    @Autowired
    private OfflineMsgService offlineMsgService;

    @RequestMapping(value = "/getAllDevice", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllDevice() {
        return JSON.toJSONString(deviceObjectService.getAllDevice());
    }

    @RequestMapping(value = "/offlineMsgService", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String offlineMsgService(String group, String id) {
        return JSON.toJSONString(offlineMsgService.getAllOffLineMsg(group, id));
    }

    @RequestMapping("/communication")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("communication");
        return mav;
    }

    @RequestMapping(value = "/on", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String on(String ip, Integer port) {
        return JSON.toJSONString(communicationService.on(ip, port));
    }

    @RequestMapping(value = "/off", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String off() {
        return JSON.toJSONString(communicationService.off());
    }

    @RequestMapping(value = "/isOnline", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String isOnline(String targetType, String targetId) {
        return JSON.toJSONString(communicationService.isOnline(targetType, targetId));
    }

    @RequestMapping(value = "/queryTerminals", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String queryTerminals(String msg) {
        return JSON.toJSONString(communicationService.queryTerminals(msg));
    }

    @RequestMapping(value = "/forward", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public void forward(String add, String msg) {
        Map<String, List<String>> target = new HashMap<String, List<String>>();
        for (String a : add.split(",")) {
            if (a.contains(":")) {
                String type = a.split(":")[0];
                final String id = a.split(":")[1];
                if (target.containsKey(type)) {
                    target.get(type).add(id);
                } else {
                    target.put(type, new ArrayList<String>() {{
                        add(id);
                    }});
                }
            }
        }
        communicationService.forwardMsg(target, msg);
    }

    @RequestMapping(value = "/forwardSync", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String forwardSync(String add, String msg) {
        Map<String, List<String>> target = new HashMap<String, List<String>>();
        for (String a : add.split(",")) {
            if (a.contains(":")) {
                String type = a.split(":")[0];
                final String id = a.split(":")[1];
                if (target.containsKey(type)) {
                    target.get(type).add(id);
                } else {
                    target.put(type, new ArrayList<String>() {{
                        add(id);
                    }});
                }
            }
        }
        return JSON.toJSONString(communicationService.forwardMsgSync(target, msg));
    }
}
