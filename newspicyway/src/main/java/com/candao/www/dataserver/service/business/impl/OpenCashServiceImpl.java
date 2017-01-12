package com.candao.www.dataserver.service.business.impl;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.DateUtils;
import com.candao.www.constant.Constant;
import com.candao.www.dataserver.mapper.MemberLogMapper;
import com.candao.www.dataserver.mapper.NodeClassMapper;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.model.ResponseJsonData;
import com.candao.www.dataserver.service.business.OpenCashService;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.printer.v2.Printer;
import com.candao.www.printer.v2.PrinterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.*;

/**
 * Created by ytq on 2016/3/15.
 */
@Service
public class OpenCashServiceImpl implements OpenCashService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OpenCashServiceImpl.class);
    @Autowired
    private NodeClassMapper nodeClassMapper;
    @Autowired
    private MemberLogMapper memberLogMapper;

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
                Map<String, Object> param = new HashMap<>();
                param.put("operatorID", aUserId);
                param.put("posID", posId);
                classNo = nodeClassMapper.getMaxClassNo(param);
            }
            Integer tipTotalAmount = nodeClassMapper.getTipTotalAmountByClassNo(classNo);
            List<Map> orderJsonList = nodeClassMapper.getNodeClassByNo(classNo, tipTotalAmount);
            List<Map> jsJsonList = nodeClassMapper.getJsListJsonByNo(classNo);
            if (!orderJsonList.isEmpty()) {
                String dateFormat = "yyyyMMdd HH:mm:ss";
                Map orderjson = orderJsonList.get(0);
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
                Date vInDate = (Date) orderjson.get("vIn");
                orderjson.put("vIn", DateUtils.toString(vInDate, dateFormat));
                Date vOutDate = (Date) orderjson.get("vOut");
                orderjson.put("vOut", DateUtils.toString(vOutDate, dateFormat));
                //处理优惠明细
                //优惠金额=套餐优惠+会员价优惠+会员储值虚增+赠菜+会员积分消费+优免（特价菜金额+折扣券+代金券+团购+手工优免）+雅座优惠+抹零+赠菜券*+四舍五入
                Object preferenceDetail = orderjson.get("preferenceDetail");
                List<String> preferenceDetailList = new ArrayList<>();
                StringTokenizer token = new StringTokenizer(preferenceDetail.toString(), "|");
                while (token.hasMoreElements()) {
                    preferenceDetailList.add(token.nextElement().toString());
                }
                //处理虚增 会员储值消费-虚增
                if (!jsJsonList.isEmpty()) {
                    for (Map jsJson : jsJsonList) {
                        String itemid = jsJson.get("itemid").toString();
                        if (itemid.equals("8")) {
                            String payamount = jsJson.get("payamount").toString();
                            //会员消费净值
                            float v = StringUtil.str2Float(payamount, 0) - StringUtil.str2Float(preferenceDetailList.get(2), 0);
                            jsJson.put("payamount", v);
                            break;
                        }
                    }
                }
                orderjson.put("preferenceDetail", preferenceDetailList);
                //       储值记录
                List<Map<String, Object>> memberLogs = memberLogMapper.getStaticByUser(aUserId, vInDate, vOutDate);
                Map<String, Object> chuzhiMap = new HashMap();
                BigDecimal realAmount = new BigDecimal(0);
                BigDecimal addedAmount = new BigDecimal(0);
                for (Map<String, Object> memberLog : memberLogs) {
                    realAmount=realAmount.add((BigDecimal) memberLog.get("realAmount"));
                    addedAmount= addedAmount.add((BigDecimal) memberLog.get("addedAmount"));
                    switch ((int) memberLog.get("payway")) {
                        case Constant.MEMBER_PAYWAY.ALIPAY:
                            memberLog.put("title", "支付宝");
                            break;
                        case Constant.MEMBER_PAYWAY.CARD:
                            memberLog.put("title", "银行卡");
                            break;
                        case Constant.MEMBER_PAYWAY.CASH:
                            memberLog.put("title", "现金");
                            break;
                        case Constant.MEMBER_PAYWAY.WEPAY:
                            memberLog.put("title", "微信支付");
                            break;
                    }
                }
                chuzhiMap.put("totalAmount", realAmount.add(addedAmount));
                chuzhiMap.put("realAmount", realAmount);
                chuzhiMap.put("addedAmount", addedAmount);
                chuzhiMap.put("detail", memberLogs);
                responseJsonData.setDoubleJson(chuzhiMap);
            }
            responseJsonData.setOrderJson(orderJsonList);
            responseJsonData.setJsJson(jsJsonList);
        } catch (Exception e) {
            responseJsonData.setData("0");
            responseJsonData.setInfo("获取清机单内容异常");
            LOGGER.error("###getClearMachineDat", e);
        }
        return JSON.toJSONString(responseJsonData);
    }
}
