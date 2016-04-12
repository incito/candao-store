package com.candao.www.dataserver.service.communication.impl;

import com.candao.communication.callback.MsgCallback;
import com.candao.communication.client.NettyService;
import com.candao.communication.vo.Response;
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

    public Response on(String ip, Integer port) {
        try {
            return NettyService.on(msgCallback, ip, port);
        } catch (Exception e) {
            return null;
        }
    }

    public Response off() {
        return NettyService.off();
    }

    public Response isOnline(String targetType, String targetId) {
        try {
            return NettyService.isOnline(targetType, targetId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response queryTerminals(String msg) {
        Response respMsg = null;
        try {
            respMsg = NettyService.queryTerminals(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMsg;
    }

    @Override
    public Response queryTerminalsByIp(String ip) {
        Response respMsg = null;
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

    public Response forwardMsgSync(Map<String, List<String>> targetMap, String msg) {
        Response respMsg = null;
        try {
            respMsg = NettyService.forwardMsgSync(targetMap, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMsg;
    }
}
