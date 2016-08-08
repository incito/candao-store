package com.candao.www.dataserver.service.msghandler;

import com.candao.communication.vo.Response;

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
    Response queryTerminals(String msg);

    //查询终端信息
    Response queryTerminalsByIp(String ip);

}
