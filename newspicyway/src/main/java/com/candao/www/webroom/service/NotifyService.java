package com.candao.www.webroom.service;

import com.candao.www.dataserver.service.msghandler.obj.Result;

/**
 * 消息通知服务
 * Created by liaoy on 2016/7/26.
 */
public interface NotifyService {
    /**
     * 结账通知PAD
     *
     * @param orderId
     * @return
     */
    Result notifySettleOrder(String orderId);

    /**
     * 清台通知PAD
     *
     * @param tableNo
     * @return
     */
    Result notifyClearTable(String tableNo);

    /**
     * 赠送礼物通知
     *
     * @param tableNo   赠送者桌台
     * @param dishId    菜品ID
     * @param giftLogId 赠送记录ID
     * @return
     */
    Result notifyGiveGift(String receiveOrderId, String tableNo, String dishId, String giftLogId);

    /**
     * 接受礼物通知
     *
     * @param sendTableNo    赠送者桌号
     * @param receiveOrderId 接受者订单号
     * @param tableNo        接受者桌台号
     * @param dishId         菜品ID
     * @param giftStatus     处理状态
     * @param primaryKey
     * @return
     */
    Result notifyReceiveGift(String sendTableNo, String receiveOrderId, String tableNo, String dishId, String giftStatus, String primaryKey);

    /**
     * 订单改变通知
     *
     * @param orderId
     * @return
     */
    Result notifyOrderChange(String orderId);

    /**
     * 微信支付结果通知
     *
     * @param orderId
     * @param payStatus
     * @param payAmount
     * @param yhAmount
     * @return
     */
    Result notifyWXpay(String orderId, String payStatus, String payAmount, String yhAmount);
}
