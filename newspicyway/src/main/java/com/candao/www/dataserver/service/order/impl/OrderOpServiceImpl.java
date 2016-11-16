package com.candao.www.dataserver.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.DateUtils;
import com.candao.www.data.dao.TbPrintObjDao;
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
import com.candao.www.webroom.service.TorderDetailPreferentialService;
import com.candao.www.webroom.service.impl.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

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
    private TorderDetailPreferentialService torderDetailPreferentialService;
    @Autowired
    private TableMapper tableMapper;

    @Autowired
    TbPrintObjDao tbPrintObjDao;
    @Autowired
    TipService tipService;

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
            //获取小费
            String tipMoney = tipService.getTipMoney(orderId);
            List<Map> orderJson = orderMapper.getOrderJson(zdAmount + "", StringUtils.isEmpty(tipMoney)?"0":tipMoney, orderId);
            //处理时间格式
            if (null != orderJson) {
                for (Map<String, Object> orderMap : orderJson) {
                    Object begintime = orderMap.get("begintime");
                    if (null != begintime) {
                        Date begintimeDate = (Date) begintime;
                        orderMap.put("begintime", DateUtils.toString(begintimeDate, "yyyyMMdd HH:mm:ss"));
                    }
                    Object endtime = orderMap.get("endtime");
                    if (null != endtime) {
                        Date endtimeDate = (Date) endtime;
                        orderMap.put("endtime", DateUtils.toString(endtimeDate, "yyyyMMdd HH:mm:ss"));
                    }
                }
            }
            List<Map> listJson = orderMapper.getListJson(orderId);
            reorderListJson(listJson);
            List<Map> jsJson = orderMapper.getJsJson(orderId);
            responseJsonData.setOrderJson(orderJson);
            responseJsonData.setListJson(listJson);
            responseJsonData.setJsJson(jsJson);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("查询异常");
            LOGGER.error("#### getOrderInfo aUserId={},orderId={},printType={} error={} ###", aUserId, orderId, printType, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseJsonData);
    }

    /**
     * 菜品列表重排序
     *
     * @param listJson
     */
    private void reorderListJson(List<Map> listJson) {
        if (null != listJson && !listJson.isEmpty()) {
            Map<String, List<Map>> reorderMap = new HashMap<>();
            for (Map<String, Object> map : listJson) {
                String parentkey = map.get("parentkey").toString();
                List<Map> group = reorderMap.get(parentkey);
                if (null == group) {
                    group = new ArrayList<>();
                    reorderMap.put(parentkey, group);
                }
                group.add(map);
            }
            listJson.clear();
            for (List list : reorderMap.values()) {
                Collections.sort(list, new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        int ismaster1 = Integer.parseInt(o1.get("ismaster").toString());
                        int ismaster2 = Integer.parseInt(o2.get("ismaster").toString());
                        if (ismaster1 == ismaster2) {
                            return 0;
                        }
                        return ismaster1 > ismaster2 ? -1 : 1;
                    }
                });
                listJson.addAll(list);
            }
        }
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
            LOGGER.error("###pCaleTableAmount aUserId={}, orderId={},error={}###", aUserId, orderId, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseData);

    }

    @Override
    public String calcOrderAmount(String orderId) {
//    	caleTableAmountMapper.updateOrderDetailPayAmount(orderId);
//        int updated = caleTableAmountMapper.updateOrderDueAmount(orderId);
//        if(updated == 1){
//        	return "1";
//        }else{
//        	return "0";
//        }
        caleTableAmountMapper.pCaleTableAmount(orderId);
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
            LOGGER.error("###wmOrder orderId={},error={}###", orderId, e.getCause().getStackTrace());
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
            LOGGER.error("###getMemberSaleInfo userId={}, orderId={},error={}###", aUserId, orderId, e.getCause().getStackTrace());
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
            LOGGER.error("###getOrderCouponList userId={}, orderId={},error={}###", aUserId, orderId, e.getCause().getStackTrace());
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
            responseJsonData.setOrderJson(orderJson);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("回当天全部帐单（用于帐单查询）异常");
            LOGGER.error("###getAllOrderInfo2 userId={},error={}###", aUserId, e.getCause().getStackTrace());
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
            LOGGER.error("###getAllGZDW userId={},error={}###", aUserId, e.getCause().getStackTrace());
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
            LOGGER.error("###saveSettlement sDetailId={} orderId={} payJsonArray={} userId={} error={}###", sDetailId, orderId, payJsonArray, userId, e.getCause().getStackTrace());

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
            LOGGER.error("###getSettlementDetailBatch orderId={},error={}###", orderId, e.getCause().getStackTrace());
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
            LOGGER.error("###deleteDetailBatch sDetailId={},error={}###", sDetailId, e.getCause().getStackTrace());
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
            //结账才能反结
            if (!"3".equals(orderStatus)) {
                return "{\"Data\":\"0\",\"Info\":\"该账单还未结账!\"}";
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
        	//删除订单的优惠信息
            Map<String, Object> params = new HashMap<>();
            params.put("orderid", orderId);
            params.put("clear", "1");
            torderDetailPreferentialService.deleteDetilPreFerInfo(params);
            //删除打印主表的数据,防止后续开台的订单号一样，打印单数据错乱
            Map<String, Object> map2 = new HashMap<>();
            map2.put("orderno", orderId);
            tbPrintObjDao.deletePrintObj(map2);
            //删除订单数据
            orderMapper.deleteByOrderId(orderId);
            tableMapper.clearTable(tableNo, orderId);

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("###cancelOrder userId={} orderId={} tableNo={} error={}###", userId, orderId, tableNo, e.getCause().getStackTrace());
            responseJsonData.setData("0");
            return JSON.toJSONString(new ResultData(responseJsonData));
        }
        return JSON.toJSONString(new ResultData(responseJsonData));
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.dateToString(new Date(1461924010000l)));
    }
}
