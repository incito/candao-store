package com.candao.www.dataserver.service.msghandler;

import com.candao.www.dataserver.service.device.obj.DeviceObject;

import java.util.List;

/**
 * Created by ytq on 2016/4/6.
 */
public interface MsgForwardService {
    /**
     * 消息推送接口
     *
     * @param userId  员工号
     * @param msgType MSG命令ID（发送方和接收方协定：比如1002为通知PAD已结算）
     * @param msg     消息内容 （内容发送方和接收方自协定）
     * @return
     */
    String broadCastMsg(String userId, String msgType, String msg);

    /**
     * 批量给设备发送消息
     *
     * @param objects
     * @param msg
     * @return
     */
    void broadCastMsg(List<DeviceObject> objects, String msg, String msgType, boolean isSingle);

    /**
     * 指定服务员的所有设备发送消息
     *
     * @param userId
     * @param msg
     * @param msgType
     * @param isSingle
     */
//    void sendMsgToUser(String userId, String msg, String msgType, boolean isSingle);


}
