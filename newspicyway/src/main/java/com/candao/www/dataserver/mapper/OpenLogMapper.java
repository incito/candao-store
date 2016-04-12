package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.OpenLog;

import java.util.Map;

/**
 * Created by lenovo on 2016/4/5.
 */
public interface OpenLogMapper {
    OpenLog selectAll();

    int insert(OpenLog log);
    int truncate();
    void procEndWork(Map<String,Object> param);
}
