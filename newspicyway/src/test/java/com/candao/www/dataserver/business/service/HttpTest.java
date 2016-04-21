package com.candao.www.dataserver.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.candao.communication.utils.MD5Util;
import com.candao.www.dataserver.model.PadLoginData;
import com.candao.www.dataserver.util.BeanUtilEx;
import com.candao.www.utils.HttpUtil;
import com.candao.www.utils.PropertyUtil;
import com.candao.www.webroom.model.LoginInfo;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ytq on 2016/4/20.
 */
public class HttpTest {
    @Test
    public void testPadLogin() throws InvocationTargetException, IllegalAccessException {
        PadLoginData loginData = new PadLoginData();
        loginData.setSsId("test");
        loginData.setGroup("test");
        loginData.setId("test");
        loginData.setUsername("0");
        loginData.setPassword("0");
        loginData.setUsername("001");
        loginData.setPassword(MD5Util.MD5("0").toUpperCase());
        loginData.setLoginType("0");
        Map<String, String> mapParam = new HashMap<String, String>() {
            {
                put("meid", "1");
                put("userid", "001");
            }
        };
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(PadLoginData.class, "userId", "username", "password", "macAddress", "padLicenceNo", "loginType");
        String resp = HttpUtil.doRestfulByHttpConnection(PropertyUtil.getProInfo("dataserver-config", "pad_login_url"), JSON.toJSONString(loginData, filter));
        String resp1 = HttpUtil.doRestfulByHttpConnection(PropertyUtil.getProInfo("dataserver-config", "bracelet_login_url"), JSON.toJSONString(mapParam));
        System.out.println(resp);
        System.out.println(resp1);

    }
}
