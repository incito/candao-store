package com.candao.www.dataserver.service.msghandler;


import com.candao.www.dataserver.entity.OfflineMsg;

import java.util.List;

/**
 * Created by ytq on 2016/3/17.
 */
public interface OfflineMsgService extends MsgHandler {
    Integer save(OfflineMsg offlineMsg);

    List<OfflineMsg> getByGroupAndId(String group, String id);

    void deleteById(Integer id);
}
