package com.candao.www.dataserver.service.msghandler.impl.offline;

import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.mapper.OfflineMsgMapper;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by ytq on 2016/3/17.
 */
public class OfflineMsgServiceImpl implements OfflineMsgService {
    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OfflineMsgServiceImpl.class);
    @Autowired
    private OfflineMsgMapper offlineMsgMapper;

    @Override
    public void handler(DeviceObject deviceObject, String serialNumber, String msg) {
        try {
            LOGGER.info("### offlinemsg delete msg={} ###", msg);
            OfflineMsgData offLineMsgData = MsgAnalyzeTool.analyzeOffLineResp(msg);
            offlineMsgMapper.deleteById(offLineMsgData.getOffLineMsgId());
        } catch (Exception e) {
            LOGGER_ERROR.error("### offlinemsg delete msg={},error={} ###", msg, e.getCause().getStackTrace());
        }
    }


    @Override
    @Transactional
    public Integer save(List<OfflineMsg> offlineMsgList, Boolean isSingle) {
        if (isSingle) {
            offlineMsgMapper.deleteMsgByList(offlineMsgList);
        }
        if (null != offlineMsgList && !offlineMsgList.isEmpty()) {
            return offlineMsgMapper.saveList(offlineMsgList);
        }
        return 0;
    }

    @Override
    public List<OfflineMsg> getByGroupAndId(String group, String id) {
        Date curDate = new Date();
        deleteMsgByExpireTime(curDate);
        return offlineMsgMapper.getByGroupAndId(group, id, curDate);
    }

    @Override
    public List<OfflineMsg> getAllOffLineMsg(String group, String id) {
        return offlineMsgMapper.getAllOffLineMsg(group, id);
    }


    @Override
    public void deleteMsgByExpireTime(final Date curDate) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                offlineMsgMapper.deleteMsgByExpireTime(curDate);

            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
