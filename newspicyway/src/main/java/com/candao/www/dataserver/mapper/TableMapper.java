package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by lenovo on 2016/4/8.
 */
public interface TableMapper {
    int updaStatus0(@Param("tableNo") String tableNo);

}
