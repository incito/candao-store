package com.candao.www.webroom.service.impl;

import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.service.msghandler.obj.Result;
import com.candao.www.webroom.service.NotifyService;
import com.candao.www.webroom.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liaoy on 2016/7/26.
 */
@Service
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    private MsgForwardService msgForwardService;
    @Autowired
    private TableService tableService;
    /**
     * 发送异步消息的处理线程
     */
    private Executor executor = new ThreadPoolExecutor(0, 4, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    @Override
    public Result notifySettleOrder(final String orderId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> data = new HashMap<>(1);
                data.put("orderId", orderId);
                msgForwardService.sendMsgAsynWithOrderId(orderId, MsgForwardTran.msgConfig.getProperty("MSF_ID.SETTLE"), data, 4 * 60 * 60, false);
            }
        });
        return null;
    }

    @Override
    public Result notifyClearTable(final String tableNo) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("tableNo", tableNo);
                List<Map<String, Object>> resultMapList = tableService.find(map);
                if (null == resultMapList || resultMapList.isEmpty()) {
                    return;
                }
                msgForwardService.sendMsgAsynWithOrderId(String.valueOf(resultMapList.get(0).get("orderid")), MsgForwardTran.msgConfig.getProperty("MSF_ID.CLEAN_TABLE"), "", 4 * 60 * 60, false);

            }
        });
        return null;
    }

    @Override
    public Result notifyGiveGift(String receiveOrderId, String tableNo, String dishId, String giftLogId) {
        Map<String, Object> data = new HashMap<>(1);
        data.put("tableNo", tableNo);
        data.put("dishId", dishId);
        data.put("giftLogId", giftLogId);
        return msgForwardService.sendMsgSynWithOrderId(receiveOrderId, MsgForwardTran.msgConfig.getProperty("MSF_ID.GIFT_SEND"), data);
    }

    @Override
    public Result notifyReceiveGift(String sendTableNo, String receiveOrderId, String tableNo, String dishId, String giftStatus, String primaryKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableNo", sendTableNo);
        List<Map<String, Object>> resultMapList = tableService.find(map);
        if (null == resultMapList || resultMapList.isEmpty()) {
            return new Result(false, "桌台[" + sendTableNo + "]不存在");
        }
        Map<String, Object> data = new HashMap<>(1);
        data.put("tableNo", tableNo);
        data.put("dishId", dishId);
        data.put("giftStatus", giftStatus);
        data.put("orderId", receiveOrderId);
        data.put("primarykey", primaryKey);
        return msgForwardService.sendMsgSynWithOrderId(String.valueOf(resultMapList.get(0).get("orderid")), MsgForwardTran.msgConfig.getProperty("MSF_ID.GIFT_HANDLE"), data);
    }

    @Override
    public Result notifyOrderChange(final String orderId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> data = new HashMap<>(1);
                data.put("orderId", orderId);
                msgForwardService.sendMsgAsynWithOrderId(orderId, MsgForwardTran.msgConfig.getProperty("MSF_ID.UPDATE_ORDER"), data, 4 * 60 * 60, false);
            }
        });
        return null;
    }

    @Override
    public Result notifyWXpay(final String orderId, final String payStatus) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> data = new HashMap<>(1);
                data.put("orderId", orderId);
                data.put("payStatus", payStatus);
                msgForwardService.sendMsgAsynWithOrderId(orderId, MsgForwardTran.msgConfig.getProperty("MSF_ID.WXPAY"), data, 4 * 60 * 60, false);
            }
        });
        return null;
    }
}