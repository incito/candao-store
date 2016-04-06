package com.candao.www.dataserver.service.msghandler;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.service.device.obj.DeviceObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/16.
 */
public interface MsgProcessService {
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
    String broadCastMsg(List<DeviceObject> objects, String msg, String msgType, boolean isSingle);

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

    /**
     * 保存离线消息
     *
     * @param offlineMsg
     * @return
     */
    Integer save(OfflineMsg offlineMsg);

    /**
     * 查询设备离线消息
     *
     * @param group
     * @param id
     * @return
     */
    List<OfflineMsg> getByGroupAndId(String group, String id);

    /**
     * 删除消息
     *
     * @param id
     */
    void deleteById(Integer id);
}
