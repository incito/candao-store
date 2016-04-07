package com.candao.www.dataserver.service.msghandler.impl;

import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.mapper.OfflineMsgMapper;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ytq on 2016/3/17.
 */
public class OfflineMsgServiceImpl implements OfflineMsgService {
    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OfflineMsgServiceImpl.class);
    @Autowired
    private OfflineMsgMapper offlineMsgMapper;

    @Override
    public void handler(String msg) {
        try {
            LOGGER.info("### offlinemsg delete msg={} ###", msg);
            OfflineMsgData offLineMsgData = MsgAnalyzeTool.analyzeOffLineResp(msg);
            offlineMsgMapper.deleteById(offLineMsgData.getId());
        } catch (Exception e) {
            LOGGER_ERROR.error("### offlinemsg delete msg={},error={} ###", msg, e);
        }
    }

    @Override
    @Transactional
    public Integer save(OfflineMsg offlineMsg) {
        if (1 == offlineMsg.getIsSingle()) {
            offlineMsgMapper.deleteMsg(offlineMsg.getDeviceGroup(), offlineMsg.getDeviceId(), offlineMsg.getMsgType());
        }
        return offlineMsgMapper.save(offlineMsg);
    }

    @Override
    public List<OfflineMsg> getByGroupAndId(String group, String id) {
        return offlineMsgMapper.getByGroupAndId(group, id);
    }

    @Override
    public void deleteById(Integer id) {
        offlineMsgMapper.deleteById(id);
    }
}
