package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by lenovo on 2016/4/7.
 */
public interface ClearMachineMapper {
    int insert(@Param("userId") String userId);
}
