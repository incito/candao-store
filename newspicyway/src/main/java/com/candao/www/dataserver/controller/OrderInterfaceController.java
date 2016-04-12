package com.candao.www.dataserver.controller;

import com.candao.www.dataserver.service.order.OrderOpService;
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
        return orderService.getOrderInfo(aUserId, orderId, printType);
    }

    @RequestMapping(value = "/caleTableAmount/{orderId}/{aUserId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String pCaleTableAmount(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
        return orderService.pCaleTableAmount(aUserId, orderId);
    }

    @RequestMapping(value = "/wmOrder/{orderId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String wmOrder(@PathVariable("orderId") String orderId) {
        return orderService.wmOrder(orderId);
    }

    @RequestMapping(value = "/getMemberSaleInfo/{aUserId}/{orderId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getMemberSaleInfo(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
        return orderService.getMemberSaleInfo(aUserId, orderId);
    }

    @RequestMapping(value = "/GetOrderCouponList/{orderId}/{aUserId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getOrderCouponList(@PathVariable("aUserId") String aUserId, @PathVariable("orderId") String orderId) {
        return orderService.getOrderCouponList(aUserId, orderId);
    }

    @RequestMapping(value = "/getAllOrderInfo2/{aUserId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllOrderInfo2(@PathVariable("aUserId") String aUserId) {
        return orderService.getAllOrderInfo2(aUserId);
    }

    @RequestMapping(value = "/getAllGZDW/{aUserId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllGZDW(@PathVariable("aUserId") String aUserId) {
        return orderService.getAllGZDW(aUserId);
    }

    @RequestMapping(value = "/SaveSettlementDetailBatch/{sDetailId}/{aUserId}/{orderId}/payJsonArray/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String saveSettlementDetailBatch(@PathVariable("sDetailId") String sDetailId, @PathVariable("aUserId") String aUserId,
                                            @PathVariable("orderId") String orderId, @PathVariable("payJsonArray") String payJsonArray) {
        return orderService.saveSettlement(sDetailId, orderId, payJsonArray, aUserId);
    }

    @RequestMapping(value = "/GetSettlementDetailBatch/{orderId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getSettlementDetailBatch(@PathVariable("orderId") String orderId) {
        return orderService.getSettlementDetailBatch(orderId);
    }

    @RequestMapping(value = "/deleteDetailBatch/{sDetailId}/", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String deleteDetailBatch(@PathVariable("sDetailId") String sDetailId) {
        return orderService.deleteDetailBatch(sDetailId);
    }

}
