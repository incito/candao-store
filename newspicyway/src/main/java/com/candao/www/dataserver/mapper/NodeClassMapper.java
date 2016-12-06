package com.candao.www.dataserver.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/21.
 */
public interface NodeClassMapper {
    String getMaxClassNo(Map<String,Object> param);

    String getMaxClassNoToday(@Param("preStr") String preStr);

    List<Map> getNodeClassByNo(@Param("jsOrder") String jsOrder, @Param("tipTotalAmount") Integer tipTotalAmount);

    List<Map> getJsListJsonByNo(String jsOrder);

    int insert(Map<String, Object> param);

    Integer getTipTotalAmountByClassNo(String jsOrder);
}
