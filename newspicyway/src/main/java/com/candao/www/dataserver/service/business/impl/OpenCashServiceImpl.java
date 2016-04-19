package com.candao.www.dataserver.service.business.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.mapper.NodeClassMapper;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.model.ResponseJsonData;
import com.candao.www.dataserver.service.business.OpenCashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
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
            socket = new Socket(ip, 9100);
            writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write(String.valueOf(buff));
            writer.flush();
        } catch (Exception e) {
            responseData.setData("0");
            LOGGER_ERROR.error("###打开钱箱出错 ip={}, error={}", ip, e);
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
            List<Map> orderJsonList = nodeClassMapper.getNodeClassByNo(classNo);
            List<Map> jsJsonList = nodeClassMapper.getJsListJsonByNo(classNo);
            if (!orderJsonList.isEmpty()) {
                responseJsonData.setOrderJson(orderJsonList);
            }
            responseJsonData.setJsJson(jsJsonList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("获取清机单内容异常");
            LOGGER.error("###getClearMachineData userId={},jsOrder={},posId={},error={}###", aUserId, jsOrder, posId, e);
        }
        return JSON.toJSONString(responseJsonData);
    }
}
