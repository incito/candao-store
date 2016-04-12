package com.candao.www.dataserver.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.constants.PrintType;
import com.candao.www.dataserver.mapper.CaleTableAmountMapper;
import com.candao.www.dataserver.mapper.OrderOpMapper;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.model.ResponseJsonData;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.dataserver.util.WorkDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/15.
 */
@Service
public class OrderOpServiceImpl implements OrderOpService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OrderOpServiceImpl.class);
    @Autowired
    private OrderOpMapper orderMapper;
    @Autowired
    private CaleTableAmountMapper caleTableAmountMapper;

    @Override
    @Transactional
    public String getOrderInfo(String aUserId, String orderId, String printType) {
        LOGGER.info("###getOrderInfo aUserId={},orderId={},printType={}###", aUserId, orderId, printType);
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            switch (printType) {
                case PrintType.BEF_PRINT:
                    orderMapper.updateBefPrintCount(orderId);
                    break;
                case PrintType.PRINT:
                    orderMapper.updatePrintCount(orderId);
                    break;
            }
            int zdAmount = orderMapper.getZdAmountByOrderId(orderId);
            Object orderJson = orderMapper.getOrderJson(zdAmount + "", orderId);
            Object listJson = orderMapper.getListJson(orderId);
            Object jsJson = orderMapper.getJsJson(orderId);
            responseJsonData.setOrderJson(orderJson);
            responseJsonData.setListJson(listJson);
            responseJsonData.setJsJson(jsJson);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("查询异常");
            LOGGER.error("#### getOrderInfo aUserId={},orderId={},printType={} error={} ###", aUserId, orderId, printType, e);
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String pCaleTableAmount(String aUserId, String orderId) {
        LOGGER.info("###pCaleTableAmount aUserId={}, orderId={}###", aUserId, orderId);
        ResponseData responseData = new ResponseData();
        try {
            caleTableAmountMapper.pCaleTableAmount(orderId);
        } catch (Exception e) {
            responseData.setData("0");
            responseData.setInfo("结算前计算帐单金额异常");
            LOGGER.error("###pCaleTableAmount aUserId={}, orderId={},error={}###", aUserId, orderId, e);
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    public String wmOrder(String orderId) {
        LOGGER.info("###wmOrder orderId={}###", orderId);
        ResponseData responseData = new ResponseData();
        try {
            orderMapper.updateOrderTypeById(orderId, "1");
        } catch (Exception e) {
            responseData.setData("0");
            responseData.setInfo("更新外卖账单异常");
            LOGGER.error("###wmOrder orderId={},error={}###", orderId, e);
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    public String getMemberSaleInfo(String aUserId, String orderId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            LOGGER.info("###getMemberSaleInfo userId={}, orderId={}###", aUserId, orderId);
            List<Map> resultMapList = orderMapper.getMemberSaleInfo(orderId);
            responseJsonData.setOrderJson(resultMapList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("更新外卖账单异常");
            LOGGER.error("###getMemberSaleInfo userId={}, orderId={},error={}###", aUserId, orderId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String getOrderCouponList(String aUserId, String orderId) {
        ResponseData responseData = new ResponseData();
        try {
            LOGGER.info("###getOrderCouponList userId={}, orderId={}###", aUserId, orderId);
            pCaleTableAmount(aUserId, orderId);
            List<Map> resultMapList = orderMapper.getOrderRuleByOrderId(orderId);
            if (resultMapList.isEmpty()) {
                responseData.setData("0");
                return JSON.toJSONString(responseData);
            } else {
                return JSON.toJSONString(resultMapList);
            }
        } catch (Exception e) {
            responseData.setData("0");
            LOGGER.error("###getOrderCouponList userId={}, orderId={},error={}###", aUserId, orderId, e);
        }
        return JSON.toJSONString(responseData);
    }

    @Override
    public String getAllOrderInfo2(String aUserId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            LOGGER.info("###getAllOrderInfo2 userId={}###", aUserId);
            String workDate = WorkDateUtil.getWorkDate();
            List<Map> orderJson = orderMapper.getAllOrderInfo2(workDate);
            responseJsonData.setOrderJson(orderJson);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("回当天全部帐单（用于帐单查询）异常");
            LOGGER.error("###getAllOrderInfo2 userId={},error={}###", aUserId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String getAllGZDW(String aUserId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            LOGGER.info("###getAllGZDW userId={}###", aUserId);
            orderMapper.updateParternerPY();
            List<Map> orderJson = orderMapper.getAllGZDW();
            responseJsonData.setOrderJson(orderJson);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("回当天全部帐单（用于挂帐单查询）异常");
            LOGGER.error("###getAllGZDW userId={},error={}###", aUserId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String saveSettlement(String sDetailId, String orderId, String payJsonArray, String userId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            LOGGER.info("###saveSettlement sDetailId={} orderId={} payJsonArray={} userId={}###", sDetailId, orderId, payJsonArray, userId);
            orderMapper.saveSettlement(sDetailId, orderId, payJsonArray, userId);
            responseJsonData.setInfo("保存完成");
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("保存结算信息异常");
            LOGGER.error("###saveSettlement sDetailId={} orderId={} payJsonArray={} userId={} error={}###", sDetailId, orderId, payJsonArray, userId, e);

        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String getSettlementDetailBatch(String orderId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            LOGGER.info("###getSettlementDetailBatch orderId={}###", orderId);
            List<Map> orderJson = orderMapper.getSettlementDetailBatch(orderId);
            responseJsonData.setOrderJson(orderJson);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("查询结算信息异常");
            LOGGER.error("###getSettlementDetailBatch orderId={},error={}###", orderId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }

    @Override
    public String deleteDetailBatch(String sDetailId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            LOGGER.info("###deleteDetailBatch sDetailId={}###", sDetailId);
            orderMapper.deleteDetailBatch(sDetailId);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("删除结算信息异常");
            LOGGER.error("###deleteDetailBatch sDetailId={},error={}###", sDetailId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }
}
