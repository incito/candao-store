package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.OpenLog;

/**
 * Created by lenovo on 2016/4/5.
 */
public interface OpenLogMapper {
    OpenLog selectAll();

    int insert(OpenLog log);
    int truncate();
}
