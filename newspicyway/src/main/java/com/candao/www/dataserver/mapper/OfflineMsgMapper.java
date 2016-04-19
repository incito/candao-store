package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.OfflineMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ytq on 2016/3/17.
 */
public interface OfflineMsgMapper {
    Integer save(OfflineMsg offlineMsg);

    void deleteById(Integer id);

    void deleteMsg(@Param("deviceGroup") String deviceGroup, @Param("deviceId") String deviceId, @Param("msgType") String msgType);

    List<OfflineMsg> getByGroupAndId(@Param("deviceGroup") String deviceGroup, @Param("deviceId") String deviceId);

    List<OfflineMsg> getAllOffLineMsg(@Param("deviceGroup") String deviceGroup, @Param("deviceId") String deviceId);

    void deleteMsgByExpireTime();
}
