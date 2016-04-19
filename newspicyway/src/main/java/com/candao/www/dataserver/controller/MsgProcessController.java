package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.task.DemoTimerTask;
import com.candao.www.dataserver.util.StringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ytq on 2016/3/16.
 * 消息处理控制器
 */
@Controller
@RequestMapping("/datasnap/rest/TServerMethods1")
public class MsgProcessController {
    @Autowired
    private MsgForwardService msgForwardService;
    @Autowired
    private DemoTimerTask demoTimerTask;

    @RequestMapping(value = "/broadcastmsg/{userId}/{msgId}/{msg}", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String broadCastMsg(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg) {
        String result = msgForwardService.broadCastMsg(userId, msgId, msg);
//        result = "{\"result\":[\"" + result + "\"]}";
        return result;
//        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/broadcastok/{client}/{msgId}", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String broadCastOk(@PathVariable("client") String client, @PathVariable("msgId") String msgId) {
        String result = msgForwardService.broadCastOk(client, msgId);
//        result = "{\"result\":[\"" + result + "\"]}";
        return result;
//        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/broadcastmsg/{userId}/{msgId}/{msg}/{isSingle}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("isSingle") boolean isSingle) {
        msgForwardService.broadCastMsg(userId, msgId, msg, isSingle);
    }

    @RequestMapping(value = "/broadcastmsg/{group}/{userId}/{msgId}/{msg}/{isSingle}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg(@PathVariable("group") String group, @PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("isSingle") boolean isSingle) {
        msgForwardService.broadCastMsg(group, userId, msgId, msg, isSingle);
    }

    @RequestMapping(value = "/broadcastmsg/{id}/{msg}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String broadCastMsg(@PathVariable("id") Integer id, @PathVariable("msg") String msg) {
        return msgForwardService.broadCastMsg(id, msg);
    }

    ////////////////////////////////////////////////////////////
    @RequestMapping(value = "/broadcastmsg1/{userId}/{msgId}/{msg}/{seconds}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg1(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("seconds") Integer seconds) {
        demoTimerTask.run(null, userId, msgId, msg, null, seconds);
    }

    @RequestMapping(value = "/broadcastmsg1/{userId}/{msgId}/{msg}/{isSingle}/{seconds}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg1(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("isSingle") boolean isSingle, @PathVariable("seconds") Integer seconds) {
        demoTimerTask.run(null, userId, msgId, msg, isSingle, seconds);
    }

    @RequestMapping(value = "/broadcastmsg1/{group}/{userId}/{msgId}/{msg}/{isSingle}/{seconds}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg1(@PathVariable("group") String group, @PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("isSingle") boolean isSingle, @PathVariable("seconds") Integer seconds) {
        demoTimerTask.run(group, userId, msgId, msg, isSingle, seconds);
    }

    @RequestMapping(value = "/broadcastmsg1/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg1() {
        demoTimerTask.cancel();
    }
}
