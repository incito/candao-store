package com.candao.www.dataserver.msghandler.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.Pad;
import com.candao.www.dataserver.entity.Watch;
import com.candao.www.dataserver.mapper.DeviceMapper;
import com.candao.www.dataserver.mapper.OpenLogMapper;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.PadLoginData;
import com.candao.www.dataserver.model.WatchCheckInRespData;
import com.candao.www.dataserver.model.WatchLoginData;
import com.candao.www.dataserver.service.business.OpenCashService;
import com.candao.www.dataserver.service.device.DeviceObjectService;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by ytq on 2016/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:branch.xml", "classpath:branchmq.xml", "classpath:ApplicationContext-service.xml"})
public class MsgProcessServiceSpringTest {

    @Autowired
    private MsgProcessService msgProcessService;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DeviceObjectService deviceObjectService;
    @Autowired
    private OpenCashService openCashService;
    @Autowired
    private OpenLogMapper openLogMapper;

    @Test
    public void testOpenDate() {
        Date openDate = openLogMapper.getOpenDate();
        System.out.println();
    }

    @Test
    public void testWatchLogin() {
        openCashService.openCash("192.168.10.10");
        deviceMapper.save(new Pad("1", "2", "3", "4", "5"));
        deviceMapper.save(new Watch("1", "3", "3", "4"));
        WatchLoginData watchLoginData = new WatchLoginData();
        watchLoginData.setGroup("watch");
        watchLoginData.setId("123456");
        watchLoginData.setUserId("user_test");
        watchLoginData.setMeId("meId");
        MsgForwardData msgForwardData = new MsgForwardData("100001", JSON.toJSONString(watchLoginData));
        msgProcessService.processMsg(JSON.toJSONString(msgForwardData));
    }

    @Test
    public void testPadLogin() {
        PadLoginData loginData = new PadLoginData();
        loginData.setGroup("pad");
        loginData.setId("123456");
        loginData.setUserId("user_test");
        loginData.setUsername("test");
        loginData.setPassword("test");
        loginData.setTableNo("1");
        MsgForwardData msgForwardData = new MsgForwardData("200001", JSON.toJSONString(loginData));
        msgProcessService.processMsg(JSON.toJSONString(msgForwardData));
    }

    @Test
    public void testWatchChInRService() {
        WatchCheckInRespData data = new WatchCheckInRespData();
        data.setCode("0");
        data.setGroup("watch");
        data.setId("123456");
        data.setUserId("user_test");
        data.setMeId("meId");
        MsgForwardData msgForwardData = new MsgForwardData("100003", JSON.toJSONString(data));
        msgProcessService.processMsg(JSON.toJSONString(msgForwardData));
    }
}
