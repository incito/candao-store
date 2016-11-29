package com.candao.www.webroom.service.impl;

import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.msghandler.obj.MsgForwardTran;
import com.candao.www.dataserver.service.msghandler.obj.Result;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.webroom.service.NotifyService;
import com.candao.www.webroom.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Autowired
    private TorderMapper orderMapper;
    /**
     * 发送异步消息的处理线程
     */
    private Executor executor = new ThreadPoolExecutor(0, 4, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    @Override
    public Result notifySettleOrder(final String orderId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //通知pad
                Map<String, Object> data = new HashMap<>(1);
                data.put("orderId", orderId);
                msgForwardService.sendMsgAsynWithOrderId(orderId, MsgForwardTran.msgConfig.getProperty("MSF_ID.SETTLE"), data, 4 * 60 * 60, false);

                //广播给手环
                Map<String, Object> table = tableService.getByOrderId(orderId);
                String tableNo = StringUtils.isEmpty(table.get("tableNo")) ? "" : table.get("tableNo").toString();
                String userId = StringUtils.isEmpty(table.get("userid")) ? "" : table.get("userid").toString();
                String msg = userId + "|" + tableNo + "|" + orderId;
                msgForwardService.broadCastMsg4Netty(Constant.MessageType.msg_2002, msg);
                String areaName = StringUtils.isEmpty(table.get("areaname")) ? "" : table.get("areaname").toString();
                msg = userId + "|17|0|" + areaName + "|" + tableNo + "|" + IDUtil.getID();
                msgForwardService.broadCastMsg4Netty(Constant.MessageType.msg_2011, msg);
            }
        });
        return null;
    }

    @Override
    public Result notifyClearTable(final String orderno) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("tableNo", tableNo);
//        List<Map<String, Object>> resultMapList = tableService.find(map);
//        if (null == resultMapList || resultMapList.isEmpty()) {
//            return null;
//        }
        Map<String, Object> data = new HashMap<>(1);
        String orderid = String.valueOf(orderno);
        data.put("orderId", orderid);
        msgForwardService.sendMsgAsynWithOrderId(orderid, MsgForwardTran.msgConfig.getProperty("MSF_ID.CLEAN_TABLE"), data, 4 * 60 * 60, false);

        return null;
    }

    @Override
    public Result notifyClearTable(String meid, String tableNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableNo", tableNo);
        List<Map<String, Object>> resultMapList = tableService.find(map);
        if (null == resultMapList || resultMapList.isEmpty()) {
            return null;
        }
        Object orderid = resultMapList.get(0).get("orderid");
        Map<Object, Object> order = orderMapper.findOne(String.valueOf(orderid));
        if (null == order) {
            return new Result(false, "该订单不存在");
        }
        if (null != meid && meid.equals(order.get("meid"))) {
            return new Result(false, "源MEID和目标MEID相同,不发送");
        }
        Map<String, Object> data = new HashMap<>(1);
        data.put("orderId", orderid);
        msgForwardService.sendMsgAsynWithOrderId(String.valueOf(orderid), MsgForwardTran.msgConfig.getProperty("MSF_ID.CLEAN_TABLE"), data, 4 * 60 * 60, false);

        return null;
    }

    @Override
    public Result notifyGiveGift(String receiveOrderId, String tableNo, String dishId, String giftLogId) {
        Map<String, Object> data = new HashMap<>(1);
        data.put("tableNo", tableNo);
        data.put("dishId", dishId);
        data.put("giftLogId", giftLogId);
        data.put("orderId", receiveOrderId);
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
    public Result notifyWXpay(final String orderId, final String payStatus, final String payAmount, final String yhAmount) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> data = new HashMap<>(1);
                data.put("orderId", orderId);
                data.put("payAmount", payAmount);
                data.put("yhAmount", yhAmount);
                data.put("payStatus", payStatus);
                msgForwardService.sendMsgAsynWithOrderId(orderId, MsgForwardTran.msgConfig.getProperty("MSF_ID.WXPAY"), data, 4 * 60 * 60, false);
            }
        });
        return null;
    }
}
