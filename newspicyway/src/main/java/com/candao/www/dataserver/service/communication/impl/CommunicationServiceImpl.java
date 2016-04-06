package com.candao.www.dataserver.service.communication.impl;

import com.candao.communication.callback.MsgCallback;
import com.candao.communication.client.NettyService;
import com.candao.www.dataserver.service.communication.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/9.
 */
@Service
public class CommunicationServiceImpl implements CommunicationService {
    @Autowired
    private MsgCallback msgCallback;

    public String on() {
        try {
            return NettyService.on(msgCallback);
        } catch (Exception e) {
            return "";
        }
    }

    public String off() {
        return NettyService.off();
    }

    public String isOnline(String targetType, String targetId) {
        try {
            return NettyService.isOnline(targetType, targetId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String queryTerminals(String msg) {
        String respMsg = null;
        try {
            respMsg = NettyService.queryTerminals(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMsg;
    }

    @Override
    public String queryTerminalsByIp(String ip) {
        String respMsg = null;
        try {
            respMsg = NettyService.queryTerminalsByIp(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMsg;
    }

    public void forwardMsg(Map<String, List<String>> targetMap, String msg) {
        try {
            NettyService.forwardMsg(targetMap, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String forwardMsgSync(Map<String, List<String>> targetMap, String msg) {
        String respMsg = null;
        try {
            respMsg = NettyService.forwardMsgSync(targetMap, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMsg;
    }
}
