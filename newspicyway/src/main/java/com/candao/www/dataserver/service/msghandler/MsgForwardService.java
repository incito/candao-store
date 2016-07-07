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

    /**
     * 消息推送接口 兼容原版本广播
     *
     * @param msgType MSG命令ID（发送方和接收方协定：比如1002为通知PAD已结算）
     * @param msg     消息内容 （内容发送方和接收方自协定）
     * @return
     */
    String broadCastMsg4Netty(String msgType, String msg);

    /**
     *消息推送接口  不兼容原版本广播
     * @param msgId MSG命令ID（发送方和接收方协定，见msg-config.properties）
     * @param msgData 发送的数据
     * @return
     */
    void broadCastMsg4Netty(String msgId, Object msgData,boolean isSingle);


    /**
     * @param client
     * @param msgId
     * @return
     */
    String broadCastOk(String client, String msgId);

    /**
     * 批量给设备发送消息
     *
     * @param objects
     * @param msg
     * @return
     */
    void broadCastMsgDevices(List<DeviceObject> objects, String msg, String msgType, boolean isSingle);

    /**
     * 批量给在线设备发送消息
     *
     * @param msg
     * @return
     */
    void broadCastMsgOnLine(String msgType, String msg, boolean isSingle);

    /**
     * 批量给在线设备发送消息,指定的过期时间后删除
     * expireSeconds消息持久化周期
     *
     * @param msg
     */
    void broadCastMsgOnLine(String msgType, String msg, boolean isSingle, int expireSeconds);

    /**
     * 批量给指定设备组发送消息
     *
     * @param msg
     * @return
     */
    void broadCastMsgGroup(String group, String msgType, String msg, boolean isSingle);

    /**
     * 给指定设备发送同步消息
     *
     * @param id 设备id
     * @return
     */
    String broadCastMsgSync(Integer id, String msg);

    /**
     * 给指定设备发送异步消息
     *
     * @param id 设备id
     * @return
     */
    void broadCastMsg(Integer id, String msg);

    /**
     * 向目标转发消息
     *
     * @param targetMap
     * @param msg
     */

    void forwardMsg(Map<String, List<String>> targetMap, String msg);
}
