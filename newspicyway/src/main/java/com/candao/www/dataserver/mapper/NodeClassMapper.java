package com.candao.www.dataserver.mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/21.
 */
public interface NodeClassMapper {
    String getMaxClassNo();

    List<Map> getNodeClassByNo(String jsOrder);

    List<Map> getJsListJsonByNo(String jsOrder);
}
