package com.candao.www.dataserver.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.DateUtils;
import com.candao.www.dataserver.entity.OpenLog;
import com.candao.www.dataserver.entity.OrderRule;
import com.candao.www.dataserver.mapper.NodeClassMapper;
import com.candao.www.dataserver.mapper.OpenLogMapper;
import com.candao.www.dataserver.mapper.OrderDetailMapper;
import com.candao.www.dataserver.mapper.OrderRuleMapper;
import com.candao.www.dataserver.service.member.BusinessService;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.dataserver.util.WorkDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/4/5.
 */
@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OpenLogMapper openLogMapper;
    @Autowired
    private OrderRuleMapper orderRuleMapper;
    @Autowired
    private NodeClassMapper nodeClassMapper;

    @Override
    public String getServerTableList(String userId, String orderId) {
        userId = StringUtil.clean(userId);
        orderId = StringUtil.clean(orderId);
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(orderId)) {
            return "{\"Data\":\"0\",\"workdate\":\"\",\"Info\":缺少参数\"\"}";
        }
        // TODO: 2016/4/5  call caleTableAmount();
        List<Map<String, Object>> orderStat = orderDetailMapper.selectStatByOrderId(orderId);
        if (null == orderStat || orderStat.isEmpty()) {
            return "{\"Data\":\"0\"}";
        }
        return JSON.toJSONString(orderStat);
    }

    @Override
    public String checkOpen() {
        OpenLog openLog = openLogMapper.selectAll();
        if (null == openLog) {
            return "{\"Data\":\"0\",\"workdate\":\"\",\"Info\":\"未开业\"}";
        }
        //开业记录存在且开业时间为当天，则已开业，否则未开业
        String openDate = DateUtils.toString(openLog.getOpenDate(), "yyyy-MM-dd");
        String today = WorkDateUtil.getWorkDate();
        if (openDate.equals(today)) {
            return "{\"Data\":\"1\",\"workdate\":\"" + openDate + "\",\"Info\":\"已经开业\"}";
        }
        return "{\"Data\":\"0\",\"workdate\":\"\",\"Info\":\"未开业\"}";
    }

    @Transactional
    @Override
    public String open(String userId, String userPwd, String ip) {
        userId = StringUtil.clean(userId);
        ip = StringUtil.clean(ip);
        if (StringUtil.isEmpty(userId)) {
            return "{\"Data\":\"0\",\"workdate\":\"\",\"Info\":缺少参数\"\"}";
        }
        String checkOpen = checkOpen();
        Map<String, String> checkResult = JSON.parseObject(checkOpen, HashMap.class);
        if ("1".equals(checkResult.get("Data"))) {
            return checkOpen;
        }
        Date today = WorkDateUtil.getWorkDate1();
        OpenLog log = new OpenLog();
        log.setIpaddress(ip);
        log.setInsertTime(today);
        log.setOpenDate(today);
        log.setUserName(userId);
        openLogMapper.insert(log);
        return "{\"Data\":\"1\",\"workdate\":\"" + DateUtils.toString(today, "yyyy-MM-dd HH:mm:ss") + "\",\"Info\":\"开业成功!\"}";
    }

    @Transactional
    @Override
    public String saveOrderPreferential(String userId, String orderId, String preferential) {
        userId = StringUtil.clean(userId);
        orderId = StringUtil.clean(orderId);
        preferential = StringUtil.clean(preferential);
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(orderId) || StringUtil.isEmpty(preferential)) {
            return "{\"Data\":\"0\",\"workdate\":\"\",\"Info\":缺少参数\"\"}";
        }

        preferential = preferential.substring(6, preferential.length() - 4);//原始数据什么样？
        if (preferential.length() < 10) {
            orderRuleMapper.deleteByOrderId(orderId);
            return "{\"Data\":\"1\"}";
        }
        List<Map<String, Object>> preList = JSON.parseObject(preferential, List.class);
        orderRuleMapper.deleteByOrderId(orderId);
        if (null == preList) {
            return "{\"Data\":\"1\"}";
        }
        for (Map<String, Object> map : preList) {
            OrderRule orderRule = new OrderRule();
            orderRule.setOrderId(orderId);
            orderRule.setYhname(map.get("yhname").toString());
            orderRule.setPartnerName(map.get("partnername").toString());
            orderRule.setCouponRate(Double.parseDouble(map.get("couponrate").toString()));
            orderRule.setFreeAmount(Double.parseDouble(map.get("freeamount").toString()));
            orderRule.setDebitAmount(Double.parseDouble(map.get("debitamount").toString()));
            orderRule.setMemo(map.get("memo").toString());
            orderRule.setCouponsNo(map.get("couponsno").toString());
            orderRule.setNum(Integer.parseInt(map.get("num").toString()));
            orderRule.setFtype(Integer.parseInt(map.get("ftype").toString()));
            orderRule.setBankType(map.get("banktype").toString());
            orderRule.setRuleId(map.get("ruleid").toString());
            orderRule.setCouponId(map.get("couponid").toString());
            orderRule.setAmount(Double.parseDouble(map.get("amount").toString()));
            orderRuleMapper.insert(orderRule);

        }
        return "{\"Data\":\"1\"}";
    }

    @Override
    public String clearMachine(String userId, String userName, String ip, String posId, String authorizer) {
        String classNo = getJbNo();

        return null;
    }

    /**
     * 获取班机号
     *
     * @return
     */
    private String getJbNo() {
        Date workDateDate = WorkDateUtil.getWorkDate1();
        String preStr = "JS000401" + DateUtils.toString(workDateDate, "yyMMdd");
        String maxClassNoToday = nodeClassMapper.getMaxClassNoToday(preStr);
        if (StringUtil.isEmpty(maxClassNoToday)) {
            maxClassNoToday = preStr + "0001";
        } else {
            String serial = maxClassNoToday.substring(maxClassNoToday.length() - 4);
            int serialInt = StringUtil.str2Int(serial, 0);
            maxClassNoToday = String.format("%04d", serialInt + 1);
        }
        return maxClassNoToday;
    }

    public static void main(String[] args) {
        String str = "111";
        System.out.println(String.format("%04d", str));
    }
}
