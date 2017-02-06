package com.candao.www.webroom.service;

import com.candao.www.webroom.model.LogData;

import java.util.List;

/**
 * 服务费日志
 * Created by liaoy on 2016/11/29.
 */
public interface H5LogService {
    void log(List<LogData> logData);
}
