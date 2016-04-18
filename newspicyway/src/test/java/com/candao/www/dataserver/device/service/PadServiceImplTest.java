package com.candao.www.dataserver.device.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.PadLoginData;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.device.impl.PadServiceImpl;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
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

public class PadServiceImplTest {
    @Tested
    private PadServiceImpl padService = new PadServiceImpl();
    @Injectable
    MsgForwardService msgProcessService;
    private
    @Mocked
    MsgForwardTran msgForwardTran;
    private
    @Mocked
    SpringContextUtils springContextUtils;

    @Test
    public void test() {
        new Expectations() {
            {
                msgProcessService.forwardMsg((Map<String, List<String>>) any, anyString);
                result = "";
            }
        };
        PadLoginData loginData = new PadLoginData();
        loginData.setGroup("watch");
        loginData.setId("123456");
        loginData.setUserId("user_test");
        loginData.setTableNo("9");
        loginData.setUsername("test");
        loginData.setPassword("test");
        loginData.setSsId("test");
        padService.handler(null, null, JSON.toJSONString(loginData));
    }
}
