package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/21.
 */
public interface NodeClassMapper {
    String getMaxClassNo();

    String getMaxClassNoToday(@Param("preStr") String preStr);

    List<Map> getNodeClassByNo(String jsOrder);

    List<Map> getJsListJsonByNo(String jsOrder);

    int insert(Map<String, Object> param);
}
