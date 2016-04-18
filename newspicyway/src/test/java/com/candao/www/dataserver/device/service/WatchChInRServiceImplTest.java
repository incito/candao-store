package com.candao.www.dataserver.device.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.entity.Watch;
import com.candao.www.dataserver.model.WatchCheckInRespData;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.device.impl.DeviceServiceImpl;
import com.candao.www.dataserver.service.device.impl.WatchChInRServiceImpl;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by ytq on 2016/3/21.
 */
public class WatchChInRServiceImplTest {
    @Tested
    private WatchChInRServiceImpl watchService = new WatchChInRServiceImpl();
    private
    @Mocked
    MsgForwardTran msgForwardTran;
    private
    @Mocked
    SpringContextUtils springContextUtils;

    @Injectable
    private MsgForwardService msgForwardService;
    @Injectable
    private OfflineMsgService offlineMsgService;

    @Test
    public void test() {
        new Expectations(DeviceServiceImpl.class) {
            {
                watchService.saveOrUpdateDevice(new Watch(anyString, anyString, anyString, anyString));
            }
        };
        new Expectations() {
            {
                offlineMsgService.getByGroupAndId(anyString, anyString);
                result = new ArrayList<OfflineMsg>();
            }
        };
//        new Expectations() {
//            {
//                msgProcessService.forwardMsgSync((Map<String, List<String>>) any, anyString);
//                result = "";
//            }
//        };

        WatchCheckInRespData watchLoginData = new WatchCheckInRespData();
        watchLoginData.setGroup("watch");
        watchLoginData.setId("123456");
        watchLoginData.setUserId("user_test");
        watchLoginData.setMeId("meId");
        watchLoginData.setCode("0");
        watchService.handler(null, null, JSON.toJSONString(watchLoginData));
    }
}
