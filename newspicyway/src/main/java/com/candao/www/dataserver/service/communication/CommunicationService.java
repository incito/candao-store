package com.candao.www.dataserver.service.communication;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/9.
 */
public interface CommunicationService {
    //开启通讯连接
    String on(String ip, Integer port);

    String off();

    String isOnline(String targetType, String targetId);

    //查询终端信息
    String queryTerminals(String msg);

    //根据ip查询终端信息
    String queryTerminalsByIp(String ip);

    //向目标转发消息
    void forwardMsg(Map<String, List<String>> targetMap, String msg);

    //向目标转发消息并响应
    String forwardMsgSync(Map<String, List<String>> targetMap, String msg);
}
