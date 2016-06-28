package com.candao.www.dataserver.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.DateUtils;
import com.candao.www.dataserver.constants.PrintType;
import com.candao.www.dataserver.mapper.CaleTableAmountMapper;
import com.candao.www.dataserver.mapper.OrderOpMapper;
import com.candao.www.dataserver.mapper.TableMapper;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.model.ResponseJsonData;
import com.candao.www.dataserver.model.ResultData;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.dataserver.util.DataServerJsonFormat;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.dataserver.util.WorkDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
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
    @Autowired
    private TableMapper tableMapper;

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
            float zdAmount = orderMapper.getZdAmountByOrderId(orderId);
            List<Map> orderJson = orderMapper.getOrderJson(zdAmount + "", orderId);
            List<Map> listJson = orderMapper.getListJson(orderId);
            List<Map> jsJson = orderMapper.getJsJson(orderId);
            responseJsonData.setOrderJson(DataServerJsonFormat.jsonFormat(orderJson, "|"));
            responseJsonData.setListJson(DataServerJsonFormat.jsonFormat(listJson, "|"));
            responseJsonData.setJsJson(DataServerJsonFormat.jsonFormat(jsJson, "&quot"));
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
//        return JSON.toJSONString(responseData);
        return "1";
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
            responseJsonData.setOrderJson(DataServerJsonFormat.jsonFormat(resultMapList, "|"));
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
                return JSON.toJSONString(DataServerJsonFormat.jsonFormat(resultMapList));
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
            String workDate = WorkDateUtil.getWorkTime();
            List<Map> orderJson = orderMapper.getAllOrderInfo2(workDate);
            if (null != orderJson && !orderJson.isEmpty()) {
                for (Map map : orderJson) {
                    Object begintime = map.get("begintime");
                    if (!StringUtil.isEmpty(begintime)) {
                        //begintime需要格式化
                        long beginTimeL = ((Timestamp) begintime).getTime();
                        map.put("begintime", DateUtils.toString(new Date(beginTimeL), "yyyyMMdd HH:mm:ss"));
                    }
                    Object endtime = map.get("endtime");
                    if (!StringUtil.isEmpty(endtime)) {
                        //begintime需要格式化
                        long endTimeL = ((Timestamp) endtime).getTime();
                        map.put("endtime", DateUtils.toString(new Date(endTimeL), "yyyyMMdd HH:mm:ss"));
                    }
                }
            }
            responseJsonData.setOrderJson(DataServerJsonFormat.jsonFormat(orderJson));
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
            responseJsonData.setOrderJson(DataServerJsonFormat.jsonFormat(orderJson, "|"));
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
            responseJsonData.setOrderJson(DataServerJsonFormat.jsonFormat(orderJson, "|"));
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

    @Override
    public List<Map> getInfoByOrderId(String orderId) {
        return orderMapper.getInfoByOrderId(orderId);
    }

    @Override
    @Transactional
    public String reBackOrder(String userId, String orderId) {
        LOGGER.info("###reBackOrder userId={} orderId={}###", userId, orderId);
        try {
            String workDate = WorkDateUtil.getWorkDate();
            Map mapOrder = orderMapper.getReBackOrderByOrderId(orderId);
            String orderStatus = mapOrder.get("orderstatus") + "";
            String isClear = mapOrder.get("isclear") + "";
            String tableId = mapOrder.get("tableid") + "";
            Map mapTable = orderMapper.getReBackOrderByTableId(tableId);
            String tableNo = mapTable.get("tableno") + "";
            String tableStatus = mapTable.get("status") + "";
            if (!"0".equals(tableStatus)) {
                return "{\"Data\":\"0\",\"Info\":\"帐单当前桌号还未结帐!\"}";
            }
            if ("1".equals(isClear)) {
                return "{\"Data\":\"0\",\"Info\":\"帐单已经生成了清机单!\"}";
            }
            tableMapper.updateTableByTableId(orderId, tableId);
            return "{\"Data\":\"1\",\"Info\":\"" + tableNo + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"Data\":\"0\",\"Info\":\"反结算异常\"}";
        }
    }

    @Override
    @Transactional
    public String cancelOrder(String userId, String orderId, String tableNo) {
        LOGGER.info("###cancelOrder userId={} orderId={} tableNo={}###", userId, orderId, tableNo);
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            orderMapper.deleteByOrderId(orderId);
            tableMapper.updaStatus0(tableNo);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("###cancelOrder userId={} orderId={} tableNo={} error={}###", userId, orderId, tableNo, e);
            responseJsonData.setData("0");
            return JSON.toJSONString(new ResultData(JSON.toJSONString(responseJsonData)));
        }
        return JSON.toJSONString(new ResultData(JSON.toJSONString(responseJsonData)));
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.dateToString(new Date(1461924010000l)));
    }
}
