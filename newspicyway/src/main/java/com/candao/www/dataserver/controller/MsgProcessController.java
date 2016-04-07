package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.msghandler.MsgForwardService;
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
public class MsgProcessController {
    @Autowired
    private MsgForwardService msgForwardService;

    @RequestMapping(value = "/broadcastmsg/{userId}/{msgId}/{msg}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String broadCastMsg(@PathVariable("userId") String userId, @PathVariable("msgId") String msgId, @PathVariable("msgId") String msg) {
        return msgForwardService.broadCastMsg(userId, msgId, msg);
    }
}
