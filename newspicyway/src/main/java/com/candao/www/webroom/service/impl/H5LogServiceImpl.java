package com.candao.www.webroom.service.impl;

import com.candao.www.webroom.model.LogData;
import com.candao.www.webroom.service.H5LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liaoy on 2016/11/29.
 */
@Service
public class H5LogServiceImpl implements H5LogService {
    private Logger logger = LoggerFactory.getLogger(H5LogServiceImpl.class);
    private short DEBUG = 1;
    private short INFO = 2;
    private short WARN = 3;
    private short ERROR = 4;

    @Override
    public void log(List<LogData> logDatas) {
        if (null == logDatas) {
            return;
        }
        for (LogData logData : logDatas) {
            String format = "REAL TIME[" + logData.getTime() + "] " + logData.getInfo();
            if (INFO == logData.getLevel()) {
                logger.info(format);
            } else if (ERROR == logData.getLevel()) {
                logger.error(format);
            } else if (DEBUG == logData.getLevel()) {
                logger.debug(format);
            } else if (WARN == logData.getLevel()) {
                logger.warn(format);
            }
        }
    }
}
