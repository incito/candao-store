package com.candao.www.dataserver.service.msghandler;

import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.service.device.obj.DeviceObject;

import java.util.List;
import java.util.Map;

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

    String broadCastOk(String client, String msgId);

    /**
     * 批量给设备发送消息
     *
     * @param objects
     * @param msg
     * @return
     */
    void broadCastMsg(List<DeviceObject> objects, String msg, String msgType);

    /**
     * 批量给在线设备发送消息
     *
     * @param msg
     * @return
     */
    void broadCastMsg(String userId, String msgType, String msg, boolean isSingle);

    /**
     * 批量给指定设备组发送消息
     *
     * @param msg
     * @return
     */
    void broadCastMsg(String group, String userId, String msgType, String msg, boolean isSingle);

    /**
     * 给指定设备发送消息
     *
     * @param id             设备id
     * @param msgForwardData 消息内容
     * @param msgType        消息类型
     * @param isSingle       是否是互斥消息
     * @return
     */
    String broadCastMsg(Integer id, MsgForwardData msgForwardData, String msgType, boolean isSingle);

    //向目标转发消息
    void forwardMsg(Map<String, List<String>> targetMap, String msg);
}
