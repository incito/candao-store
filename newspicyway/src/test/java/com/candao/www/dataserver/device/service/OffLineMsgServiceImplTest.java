package com.candao.www.dataserver.device.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.service.msghandler.impl.MsgProcessServiceImpl;
import com.candao.www.dataserver.service.msghandler.impl.offline.OfflineMsgServiceImpl;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;
import org.testng.annotations.Test;

/**
 * Created by ytq on 2016/3/21.
 */
public class OffLineMsgServiceImplTest {
    @Tested
    private OfflineMsgServiceImpl offLineMsgService = new OfflineMsgServiceImpl();

    @Injectable
    private MsgProcessService msgProcessService;
    private
    @Mocked
    MsgForwardTran msgForwardTran;
    private
    @Mocked
    SpringContextUtils springContextUtils;

    @Injectable
    private OfflineMsgService offlineMsgService;

    @Test
    public void test() {
        new NonStrictExpectations(MsgProcessServiceImpl.class) {
            {
                offlineMsgService.deleteById(anyInt);
            }
        };
        OfflineMsgData offlineMsgData = new OfflineMsgData(1, "test");
        offLineMsgService.handler(null, null, JSON.toJSONString(offlineMsgData));
    }
}
