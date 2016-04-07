package com.candao.www.dataserver.service.msghandler;

import com.candao.www.dataserver.entity.OfflineMsg;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/16.
 */
public interface MsgProcessService {
    /**
     * 处理机具转发消息
     *
     * @param msg
     */
    void processMsg(String msg);

    //查询终端信息
    String queryTerminals(String msg);

    //查询终端信息
    String queryTerminalsByIp(String ip);

    //向目标转发消息
    void forwardMsg(Map<String, List<String>> targetMap, String msg);

    //向目标转发消息并响应
    String forwardMsgSync(Map<String, List<String>> targetMap, String msg);

}
