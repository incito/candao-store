package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.communication.CommunicationService;
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

    @RequestMapping("/communication")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("communication");
        return mav;
    }

    @RequestMapping(value = "/on", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String on() {
        return communicationService.on();
    }

    @RequestMapping(value = "/off", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String off() {
        return communicationService.off();
    }

    @RequestMapping(value = "/isOnline", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String isOnline(String targetType, String targetId) {
        return communicationService.isOnline(targetType, targetId);
    }

    @RequestMapping(value = "/queryTerminals", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String queryTerminals(String msg) {
        return communicationService.queryTerminals(msg);
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
        return communicationService.forwardMsgSync(target, msg);
    }
}
