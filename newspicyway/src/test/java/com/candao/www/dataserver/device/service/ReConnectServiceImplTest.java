package com.candao.www.dataserver.device.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.model.ReConnectData;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.device.impl.ReConnectServiceImpl;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.service.msghandler.impl.MsgProcessServiceImpl;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/21.
 */
public class ReConnectServiceImplTest {
    @Tested
    private ReConnectServiceImpl reConnectService = new ReConnectServiceImpl();

    @Injectable
    private OfflineMsgService offlineMsgService;
    @Injectable
    private MsgForwardService msgForwardService;
    private
    @Mocked
    MsgForwardTran msgForwardTran;
    private
    @Mocked
    SpringContextUtils springContextUtils;

    @Test
    public void test1() {
        new NonStrictExpectations(MsgProcessServiceImpl.class) {
            {
                offlineMsgService.getByGroupAndId(anyString, anyString);
                result = new ArrayList<OfflineMsg>();
            }
        };
        ReConnectData reConnectData = new ReConnectData();
        reConnectData.setGroup("watch");
        reConnectData.setId("123456");
        reConnectData.setSsId("test");
        reConnectService.handler(null, null, JSON.toJSONString(reConnectData));
    }

    @Test
    public void test2() {
        new NonStrictExpectations(MsgProcessServiceImpl.class) {
            {
                offlineMsgService.getByGroupAndId(anyString, anyString);
                result = new ArrayList<OfflineMsg>() {{
                    add(new OfflineMsg(null, null, null, null, null));
                }};
            }
        };
        new NonStrictExpectations() {
            {
                msgForwardService.forwardMsg((Map<String, List<String>>) any, anyString);
                result = "";
            }
        };
        ReConnectData reConnectData = new ReConnectData();
        reConnectData.setGroup("watch");
        reConnectData.setId("123456");
        reConnectData.setSsId("test");
        reConnectService.handler(null, null, JSON.toJSONString(reConnectData));
    }

}
