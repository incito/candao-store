package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by ytq on 2016/3/16.
 */
public interface MsgProcessMapper {
    int saveTSyncMsg(@Param("msgType") String msgType, @Param("msg") String msg);
}
