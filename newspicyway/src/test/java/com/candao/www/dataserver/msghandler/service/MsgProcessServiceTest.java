package com.candao.www.dataserver.msghandler.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.WatchLoginData;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.device.impl.WatchServiceImpl;
import com.candao.www.dataserver.service.msghandler.MsgHandler;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.service.msghandler.impl.MsgProcessServiceImpl;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.utils.HttpUtil;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/15.
 */

public class MsgProcessServiceTest {
    @Tested
    MsgProcessService msgProcessService = new MsgProcessServiceImpl();
    private
    WatchServiceImpl watchService = new WatchServiceImpl();
    @Mocked
    HttpUtil httpUtil;
    @Mocked
    MsgForwardTran msgForwardTran;
    private
    @Mocked
    SpringContextUtils springContextUtils;

    @Injectable
    MsgProcessService msgProcessService1;

    @Before
    public void before() {
        Map<String, List<MsgHandler>> stringListMap = new HashMap<>();
        stringListMap.put("100001", new ArrayList<MsgHandler>() {{
            add(watchService);
        }});
        ((MsgProcessServiceImpl) msgProcessService).setMsgHandlerMap(stringListMap);
    }

//    @Test
    public void test() {
//        new Expectations() {
//            {
//                msgProcessService.forwardMsgSync((Map<String, List<String>>) any, anyString);
//                result = "";
//            }
//        };
//        new Expectations(watchService) {
//            {
//                invoke(watchService, "braceletLoginIn", new WatchLoginData());
//                result = "";
//            }
//        };
//        new Expectations() {
//            {
//                httpUtil.doRestfulByHttpConnection(anyString, anyString);
//                result = "";
//            }
//        };
//        new Expectations(watchService) {
//            {
//                // 对私有int类型的memberCounts进行设值
//                this.setField(watchService, "msgProcessService", msgProcessService);
//            }
//        };
//        new Expectations() {
//            {
//                msgProcessService.forwardMsgSync(null, null);
//                result = null;
//            }
//        };
        WatchLoginData watchLoginData = new WatchLoginData();
        watchLoginData.setGroup("watch");
        watchLoginData.setId("123456");
        watchLoginData.setUserId("user_test");
        watchLoginData.setMeId("meId");

        MsgForwardData msgForwardData = new MsgForwardData("100001", JSON.toJSONString(watchLoginData));
        msgProcessService.processMsg(JSON.toJSONString(msgForwardData));
    }
}
