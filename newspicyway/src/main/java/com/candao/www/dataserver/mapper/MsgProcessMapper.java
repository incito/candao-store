package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.SyncMsg;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ytq on 2016/3/16.
 */
public interface MsgProcessMapper {
    int saveTSyncMsg(SyncMsg syncMsg);
}
