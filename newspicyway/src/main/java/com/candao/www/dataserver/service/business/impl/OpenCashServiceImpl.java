package com.candao.www.dataserver.service.business.impl;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.DateUtils;
import com.candao.www.dataserver.mapper.NodeClassMapper;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.model.ResponseJsonData;
import com.candao.www.dataserver.service.business.OpenCashService;
import com.candao.www.printer.v2.Printer;
import com.candao.www.printer.v2.PrinterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/15.
 */
@Service
public class OpenCashServiceImpl implements OpenCashService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OpenCashServiceImpl.class);
    @Autowired
    private NodeClassMapper nodeClassMapper;

    public String openCash(String ip) {
        ResponseData responseData = new ResponseData();
        char buff[] = {27, 112, 0, 100, 100};
        Writer writer = null;
        Socket socket = null;
        try {
            LOGGER.info("###打开钱箱 ip={}###", ip);
            Printer printer = PrinterManager.getPrinter(ip);
            if (null == printer) {
                LOGGER.error("开钱箱失败：打印机[" + ip + "]不存在");
            } else {
                printer.openCash();
            }
        } catch (Exception e) {
            responseData.setData("0");
            LOGGER_ERROR.error("###打开钱箱出错 ip={}, error={}", ip, e.getCause().getStackTrace());
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return JSON.toJSONString(responseData);
        return "1";
    }

    @Override
    public String getClearMachineData(String aUserId, String jsOrder, String posId) {
        ResponseJsonData responseJsonData = new ResponseJsonData();
        try {
            LOGGER.info("###getClearMachineData userId={},jsOrder={},posId={}###", aUserId, jsOrder, posId);
            String classNo = jsOrder;
            if ("".equals(classNo)) {
                classNo = nodeClassMapper.getMaxClassNo();
            }
            Integer tipTotalAmount = nodeClassMapper.getTipTotalAmountByClassNo(classNo);
            List<Map> orderJsonList = nodeClassMapper.getNodeClassByNo(classNo, tipTotalAmount);
            List<Map> jsJsonList = nodeClassMapper.getJsListJsonByNo(classNo);
            if (!orderJsonList.isEmpty()) {
                String dateFormat = "yyyyMMdd HH:mm:ss";
                for (Map orderjson : orderJsonList) {
                    Object priterTime = orderjson.get("priterTime");
                    if (null != priterTime) {
                        Date priterTimeDate = (Date) priterTime;
                        orderjson.put("priterTime", DateUtils.toString(priterTimeDate, dateFormat));
                    }
                    Object workdate = orderjson.get("workdate");
                    if (null != workdate) {
                        Date workdateDate = (Date) workdate;
                        orderjson.put("workdate", DateUtils.toString(workdateDate, dateFormat));
                    }
                    Object vIn = orderjson.get("vIn");
                    if (null != vIn) {
                        Date vInDate = (Date) vIn;
                        orderjson.put("vIn", DateUtils.toString(vInDate, dateFormat));
                    }
                    Object vOut = orderjson.get("vOut");
                    if (null != vOut) {
                        Date vOutDate = (Date) vOut;
                        orderjson.put("vOut", DateUtils.toString(vOutDate, dateFormat));
                    }
                }
                responseJsonData.setOrderJson(orderJsonList);
            }
            responseJsonData.setJsJson(jsJsonList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("获取清机单内容异常");
            LOGGER.error("###getClearMachineData userId={},jsOrder={},posId={},error={}###", aUserId, jsOrder, posId, e.getCause().getStackTrace());
        }
        return JSON.toJSONString(responseJsonData);
    }
}
