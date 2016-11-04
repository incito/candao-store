package com.candao.www.dataserver.controller;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.obj.Result;
import com.candao.www.dataserver.task.DemoTimerTask;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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
    @Autowired
    private NotifyService notifyService;

    @RequestMapping(value = "/broadcastmsg/{userId}/{msgId}/{msg}", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String broadCastMsg(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg) {
        String result = msgForwardService.broadCastMsg4Netty(msgId, msg);
        // TODO: 2016/8/9 照顾服务员PAD和咖啡PAD
        msgForwardService.broadCastMsg(userId, msgId, msg);
        return result;
//        return StringUtil.string2Unicode(result);
    }

//    @RequestMapping(value = "/broadcastok/{client}/{msgId}", produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public String broadCastOk(@PathVariable("client") String client, @PathVariable("msgId") String msgId) {
//        String result = msgForwardService.broadCastOk(client, msgId);
//        return result;
////        return StringUtil.string2Unicode(result);
//    }

    @RequestMapping(value = "/broadcastmsg/{userId}/{msgId}/{msg}/{isSingle}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("isSingle") boolean isSingle) {
        msgForwardService.broadCastMsgOnLine(msgId, msg, isSingle);
    }

    @RequestMapping(value = "/broadcastmsg/{group}/{userId}/{msgId}/{msg}/{isSingle}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg(@PathVariable("group") String group, @PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("isSingle") boolean isSingle) {
        msgForwardService.broadCastMsgGroup(group, msgId, msg, isSingle);
    }

    ////////////////////////////////////////////////////////////
    @RequestMapping(value = "/broadcastmsg1/{userId}/{msgId}/{msg}/{seconds}/{isPlus}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg1(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("seconds") Integer seconds, @PathVariable("isPlus") boolean isPlus) {
        demoTimerTask.run(null, userId, msgId, msg, null, seconds, isPlus);
    }

    @RequestMapping(value = "/broadcastmsg1/{userId}/{msgId}/{msg}/{isSingle}/{seconds}/{isPlus}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg1(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg, @PathVariable("isSingle") boolean isSingle,
                              @PathVariable("seconds") Integer seconds, @PathVariable("isPlus") boolean isPlus) {
        demoTimerTask.run(null, userId, msgId, msg, isSingle, seconds, isPlus);
    }

    @RequestMapping(value = "/broadcastmsg1/{group}/{userId}/{msgId}/{msg}/{isSingle}/{seconds}/{isPlus}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg1(@PathVariable("group") String group, @PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msg") String msg,
                              @PathVariable("isSingle") boolean isSingle, @PathVariable("seconds") Integer seconds, @PathVariable("isPlus") boolean isPlus) {
        demoTimerTask.run(group, userId, msgId, msg, isSingle, seconds, isPlus);
    }

    @RequestMapping(value = "/broadcastmsg1/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void broadCastMsg1() {
        demoTimerTask.cancel();
    }

    @RequestMapping(value = "/sendMsgAsyn", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String sendMsgAsyn(@RequestBody String body) {
        Map map = JSON.parseObject(body, Map.class);
        Integer type = (Integer) map.get("type");
        String orderId = (String) map.get("orderId");
        if (null == type || null == orderId || orderId.isEmpty()) {
            return JSON.toJSONString(ReturnMap.getFailureMap("缺少参数"));
        }
        Result result = null;
        switch (type) {
            case 1:
                result = notifyService.notifySettleOrder(orderId);
                break;
            default:
                result = new Result(false, "未知的消息类型");
        }
        if (null == result || result.isSuccess()) {
            return JSON.toJSONString(ReturnMap.getSuccessMap());
        }
        return JSON.toJSONString(ReturnMap.getFailureMap(result.getMsg()));
    }
}
