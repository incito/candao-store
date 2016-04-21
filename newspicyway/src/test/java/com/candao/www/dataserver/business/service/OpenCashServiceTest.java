package com.candao.www.dataserver.business.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.service.business.OpenCashService;
import com.candao.www.dataserver.service.business.impl.OpenCashServiceImpl;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Before;
import org.testng.annotations.Test;

/**
 * Created by ytq on 2016/3/15.
 */

public class OpenCashServiceTest {
    @Tested
    OpenCashService openCashService = new OpenCashServiceImpl();

    @Test
    public void testOpenCash() {
        String resp = openCashService.openCash("127.0.0.1");
        ResponseData responseData = JSON.parseObject(resp, ResponseData.class);
        Assert.assertEquals(responseData.getData(), "0");
    }
}
