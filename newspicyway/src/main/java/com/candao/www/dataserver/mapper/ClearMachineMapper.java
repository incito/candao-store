package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * Created by lenovo on 2016/4/7.
 */
public interface ClearMachineMapper {
    int insert(@Param("openDate") Date openDate, @Param("userId") String userId);
}
