package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.dataserver.util.StringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ytq on 2016/3/15.
 * 订单接口控制器
 */
@Controller
@RequestMapping("/datasnap/rest/TServerMethods1")
public class OrderInterfaceController {
    @Autowired
    private OrderOpService orderService;

    @RequestMapping(value = "/getOrderInfo/{aUserId}/{orderId}/{printType}", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getOrderInfo(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId, @PathVariable("printType") String printType) {
        String result = orderService.getOrderInfo(aUserId, orderId, printType);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/caleTableAmount/{orderId}/{aUserId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String pCaleTableAmount(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
        String result = orderService.pCaleTableAmount(aUserId, orderId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/wmOrder/{orderId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String wmOrder(@PathVariable("orderId") String orderId) {
        String result = orderService.wmOrder(orderId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/getMemberSaleInfo/{aUserId}/{orderId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getMemberSaleInfo(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
        String result = orderService.getMemberSaleInfo(aUserId, orderId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/GetOrderCouponList/{orderId}/{aUserId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getOrderCouponList(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
        String result = orderService.getOrderCouponList(aUserId, orderId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/getAllOrderInfo2/{aUserId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllOrderInfo2(@PathVariable("aUserId") String aUserId) {
        String result = orderService.getAllOrderInfo2(aUserId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/getAllGZDW/{aUserId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllGZDW(@PathVariable("aUserId") String aUserId) {
        String result = orderService.getAllGZDW(aUserId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/SaveSettlementDetailBatch/{sDetailId}/{aUserId}/{orderId}/{payJsonArray}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String saveSettlementDetailBatch(@PathVariable("sDetailId") String sDetailId, @PathVariable("aUserId") String aUserId,
                                            @PathVariable("orderId") String orderId, @PathVariable("payJsonArray") String payJsonArray) {
        String result = orderService.saveSettlement(sDetailId, orderId, payJsonArray, aUserId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/GetSettlementDetailBatch/{orderId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getSettlementDetailBatch(@PathVariable("orderId") String orderId) {
        String result = orderService.getSettlementDetailBatch(orderId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

    @RequestMapping(value = "/deleteDetailBatch/{sDetailId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String deleteDetailBatch(@PathVariable("sDetailId") String sDetailId) {
        String result = orderService.deleteDetailBatch(sDetailId);
        result = "{\"result\":[\"" + StringEscapeUtils.escapeJava(result) + "\"]}";
        return StringUtil.string2Unicode(result);
    }

}
