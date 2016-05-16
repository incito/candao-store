package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by lenovo on 2016/4/8.
 */
public interface OperationLogMapper {
    int deleteByTableNo(@Param("tableNo") String tableNo);

    String selectMaxSequence(@Param("tableNo") String tableNo);

    int deletePosOperation(String tableNo);
}
