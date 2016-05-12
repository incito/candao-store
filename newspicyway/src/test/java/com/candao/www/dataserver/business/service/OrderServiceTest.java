package com.candao.www.dataserver.business.service;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.mapper.CaleTableAmountMapper;
import com.candao.www.dataserver.mapper.OrderOpMapper;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.model.ResponseJsonData;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.dataserver.service.order.impl.OrderOpServiceImpl;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * Created by ytq on 2016/3/15.
 */

public class OrderServiceTest {
    @Tested
    OrderOpService orderService = new OrderOpServiceImpl();
    @Injectable
    private OrderOpMapper orderMapper;
    @Injectable
    private CaleTableAmountMapper caleTableAmountMapper;

    /**
     * 获取帐单信息（用于打印预结单、结帐单）
     * 答应类型为1的流程
     */
    @Test
    public void testGetOrderInfo1() {
        new Expectations() {
            {
                orderMapper.updateBefPrintCount(anyString);
                result = null;
            }
        };
        new Expectations() {
            {
                orderMapper.getZdAmountByOrderId(anyString);
                result = 0;
            }
        };
        new Expectations() {
            {
                orderMapper.getOrderJson(anyString, anyString);
                result = null;
            }
        };
        new Expectations() {
            {
                orderMapper.getListJson(anyString);
                result = null;
            }
        };
        new Expectations() {
            {
                orderMapper.getJsJson(anyString);
                result = null;
            }
        };
        String resp = orderService.getOrderInfo("12345", "H20160303245503000001", "1");
        ResponseData responseData = JSON.parseObject(resp, ResponseData.class);
        Assert.assertEquals(responseData.getData(), "1");
    }

    /**
     * 获取帐单信息（用于打印预结单、结帐单）
     * 答应类型为2的流程
     */
    @Test
    public void testGetOrderInfo2() {
        new Expectations() {
            {
                orderMapper.updatePrintCount(anyString);
                result = null;
            }
        };
        new Expectations() {
            {
                orderMapper.getZdAmountByOrderId(anyString);
                result = 0;
            }
        };
        new Expectations() {
            {
                orderMapper.getOrderJson(anyString, anyString);
                result = null;
            }
        };
        new Expectations() {
            {
                orderMapper.getListJson(anyString);
                result = null;
            }
        };
        new Expectations() {
            {
                orderMapper.getJsJson(anyString);
                result = null;
            }
        };
        String resp = orderService.getOrderInfo("12345", "H20160303245503000001", "2");
        ResponseData responseData = JSON.parseObject(resp, ResponseData.class);
        Assert.assertEquals(responseData.getData(), "1");
    }

    /**
     * 获取帐单信息（用于打印预结单、结帐单）
     * 异常流程
     */
    @Test
    public void testGetOrderInfo3() {
        new Expectations() {
            {
                orderMapper.updatePrintCount(anyString);
                result = null;
            }
        };
        new Expectations() {
            {
                orderMapper.getZdAmountByOrderId(anyString);
                result = null;
            }
        };
        String resp = orderService.getOrderInfo("12345", "H20160303245503000001", "2");
        ResponseJsonData responseData = JSON.parseObject(resp, ResponseJsonData.class);
        Assert.assertEquals(responseData.getData(), "0");
    }

//    /**
//     * 结算前计算帐单金额
//     */
//    @Test
//    public void testPCaleTableAmount() {
//        new Expectations() {
//            {
//                caleTableAmountMapper.pCaleTableAmount(anyString);
//                result = new Exception();
//            }
//        };
//        String resp = orderService.pCaleTableAmount("123", "H20160303245503000001");
//        ResponseData responseData = JSON.parseObject(resp, ResponseData.class);
//        Assert.assertEquals(responseData.getData(), "0");
//    }
}
