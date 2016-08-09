package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by lenovo on 2016/4/7.
 */
public interface ClearMachineMapper {
    int insert(@Param("userId") String userId, @Param("beginTime") Date beginTime);
}
