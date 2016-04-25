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
        String a="{\"result\":[\"{\\\"Data\\\":[{\\\"begintime\\\":\\\"20160425 18:30:22\\\",\\\"childdishtype\\\":\\\"0\\\",\\\"couponid\\\":null,\\\"debitamount\\\":null,\\\"discardreason\\\":null,\\\"discarduserid\\\":null,\\\"discountamount\\\":\\\"0.\\\",\\\"discountrate\\\":\\\"1.\\\",\\\"dishid\\\":\\\"ebd6b01d-1d28-4d50-b89d-8b4d72312685\\\",\\\"dishnum\\\":\\\"1.0\\\",\\\"dishstatus\\\":\\\"0\\\",\\\"dishtype\\\":\\\"0\\\",\\\"dishunit\\\":\\\"份\\\",\\\"disuserid\\\":\\\"031\\\",\\\"endtime\\\":null,\\\"fishcode\\\":null,\\\"isadddish\\\":\\\"0\\\",\\\"islatecooke\\\":\\\"0\\\",\\\"ismaster\\\":\\\"1\\\",\\\"ispot\\\":\\\"0\\\",\\\"orderdetailid\\\":\\\"b1ff92fa-0ad0-11e6-9e57-480fcf3c011a\\\",\\\"orderid\\\":\\\"H20160425645141010575\\\",\\\"orderprice\\\":\\\"20.\\\",\\\"orderseq\\\":\\\"1\\\",\\\"ordertype\\\":\\\"0\\\",\\\"orignalprice\\\":\\\"20.\\\",\\\"parentkey\\\":\\\"fa37186c-415a-47bb-b30f-c07055ebbfd3\\\",\\\"payamount\\\":\\\"20.\\\",\\\"predisamount\\\":\\\"20.\\\",\\\"pricetype\\\":\\\"0\\\",\\\"primarykey\\\":\\\"fa37186c-415a-47bb-b30f-c07055ebbfd3\\\",\\\"relatedishid\\\":\\\"\\\",\\\"relateorderid\\\":null,\\\"sperequire\\\":\\\"\\\",\\\"status\\\":\\\"0\\\",\\\"superkey\\\":\\\"fa37186c-415a-47bb-b30f-c07055ebbfd3\\\",\\\"userName\\\":\\\"031\\\"}]}\"]}";

        Float f=0.00f;
        System.out.println(f);
        PadLoginData loginData = new PadLoginData();
        loginData.setSsId("test");
        loginData.setGroup("test");
        loginData.setId("test");
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
