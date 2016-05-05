package com.candao.www.dataserver.service.msghandler;


import com.candao.www.dataserver.entity.OfflineMsg;

import java.util.List;

/**
 * Created by ytq on 2016/3/17.
 */
public interface OfflineMsgService extends MsgHandler {
    Integer save(OfflineMsg offlineMsg);

    Integer save(List<OfflineMsg> offlineMsgList, Boolean isSingle);

    List<OfflineMsg> getByGroupAndId(String group, String id);

    List<OfflineMsg> getAllOffLineMsg(String group, String id);

    void deleteById(String id);

    void deleteMsgByExpireTime();
}
