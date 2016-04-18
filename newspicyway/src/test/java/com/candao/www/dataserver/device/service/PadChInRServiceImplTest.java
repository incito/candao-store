package com.candao.www.dataserver.device.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.OfflineMsg;
import com.candao.www.dataserver.entity.Pad;
import com.candao.www.dataserver.model.PadCheckInRespData;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.device.impl.DeviceServiceImpl;
import com.candao.www.dataserver.service.device.impl.PadChInRServiceImpl;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.OfflineMsgService;
import com.candao.www.dataserver.service.msghandler.impl.MsgProcessServiceImpl;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by ytq on 2016/3/21.
 */
public class PadChInRServiceImplTest {
    @Tested
    private PadChInRServiceImpl padChInRService = new PadChInRServiceImpl();

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
//        new Expectations(DeviceServiceImpl.class) {
//            {
//                padChInRService.saveOrUpdateDevice(new Pad(anyString, anyString, anyString, anyString, anyString));
//            }
//        };
        new NonStrictExpectations(DeviceServiceImpl.class) {
            {
                padChInRService.saveOrUpdateDevice(new Pad(anyString, anyString, anyString, anyString, anyString));
            }
        };
        new NonStrictExpectations(MsgProcessServiceImpl.class) {
            {
                offlineMsgService.getByGroupAndId(anyString, anyString);
                result = new ArrayList<OfflineMsg>();
            }
        };
//        new Expectations(MsgProcessServiceImpl.class) {
//            {
//                msgProcessService.getByGroupAndId(anyString, anyString);
//                result = new ArrayList<OfflineMsg>();
//            }
//        };
//        new Expectations() {
//            {
//                msgProcessService.forwardMsgSync((Map<String, List<String>>) any, anyString);
//                result = "";
//            }
//        };

        PadCheckInRespData loginData = new PadCheckInRespData();
        loginData.setGroup("watch");
        loginData.setId("123456");
        loginData.setUserId("user_test");
        loginData.setTableNo("9");
        loginData.setUsername("test");
        loginData.setPassword("test");
        loginData.setSsId("test");
        loginData.setCode("0");
        padChInRService.handler(null, null, JSON.toJSONString(loginData));
    }
}
