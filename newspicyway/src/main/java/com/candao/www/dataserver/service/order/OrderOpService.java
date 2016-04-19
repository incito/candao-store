package com.candao.www.dataserver.service.order;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/15.
 */
public interface OrderOpService {
    //获取帐单信息（用于打印预结单、结帐单）
    String getOrderInfo(String aUserId, String orderId, String printType);

    //结算前计算帐单金额
    String pCaleTableAmount(String aUserId, String orderId);

    //标记帐单为外卖帐单
    String wmOrder(String orderId);

    //获取会员交易凭条内容（打印会员交易凭条）
    String getMemberSaleInfo(String aUserId, String orderId);

    //获取帐单的已选优惠信息
    String getOrderCouponList(String aUserId, String orderId);

    //返回当天全部帐单（用于帐单查询）
    String getAllOrderInfo2(String aUserId);

    //返回当天全部帐单（用于挂帐单查询）
    String getAllGZDW(String aUserId);

    //保存结算信息
    String saveSettlement(String sDetailId, String orderId,
                          String payJsonArray, String userId);

    //查询结算信息
    String getSettlementDetailBatch(String orderId);

    //删除结算信息
    String deleteDetailBatch(String sDetailId);

    List<Map> getInfoByOrderId(String orderId);
}
