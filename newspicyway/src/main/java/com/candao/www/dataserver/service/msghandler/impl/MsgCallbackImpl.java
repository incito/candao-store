package com.candao.www.dataserver.service.msghandler.impl;

import com.candao.communication.callback.MsgCallback;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ytq on 2016/3/9.
 */
@Service
public class MsgCallbackImpl implements MsgCallback {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MsgCallbackImpl.class);
    @Autowired
    private MsgProcessService msgProcessService;

    @Override
    public void forwardCallback(String respMsg) {
        try {
            LOGGER.info("### forwardCallback msg={} ###", respMsg);
            msgProcessService.processMsg(respMsg);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void analyzeFailCallback(String respMsg) {
        LOGGER.error("#### analyFailCallback respMsg={} ###", respMsg);
    }
}
