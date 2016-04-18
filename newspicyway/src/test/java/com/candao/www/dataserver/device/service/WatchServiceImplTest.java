package com.candao.www.dataserver.device.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.WatchLoginData;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.device.impl.WatchServiceImpl;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/21.
 */
public class WatchServiceImplTest {
    @Tested
    private WatchServiceImpl watchService = new WatchServiceImpl();
    @Injectable
    MsgProcessService msgProcessService;
    private
    @Mocked
    MsgForwardTran msgForwardTran;
    private
    @Mocked
    SpringContextUtils springContextUtils;
    @Injectable
    MsgForwardService msgForwardService;

    @Test
    public void test() {
        new Expectations() {
            {
                msgForwardService.forwardMsg((Map<String, List<String>>) any, anyString);
                result = "";
            }
        };
        WatchLoginData watchLoginData = new WatchLoginData();
        watchLoginData.setGroup("watch");
        watchLoginData.setId("123456");
        watchLoginData.setUserId("user_test");
        watchLoginData.setMeId("meId");
        watchService.handler(null, null, JSON.toJSONString(watchLoginData));
    }
}
