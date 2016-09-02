package com.candao.www.dataserver.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.constant.Constant;
import com.candao.www.dataserver.entity.OpenLog;
import com.candao.www.dataserver.entity.OrderRule;
import com.candao.www.dataserver.mapper.*;
import com.candao.www.dataserver.model.ResponseData;
import com.candao.www.dataserver.service.dish.DishService;
import com.candao.www.dataserver.service.member.BusinessService;
import com.candao.www.dataserver.service.member.MemberService;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.dataserver.util.DataServerJsonFormat;
import com.candao.www.dataserver.util.IDUtil;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.dataserver.util.WorkDateUtil;
import com.candao.www.utils.HttpUtil;
import com.candao.www.webroom.service.impl.OrderDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by lenovo on 2016/4/5.
 */
@Service
public class BusinessServiceImpl implements BusinessService {
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessServiceImpl.class);

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OpenLogMapper openLogMapper;
    @Autowired
    private OrderRuleMapper orderRuleMapper;
    @Autowired
    private NodeClassMapper nodeClassMapper;
    @Autowired
    private TellerCashMapper tellerCashMapper;
    @Autowired
    private ClearMachineMapper clearMachineMapper;
    @Autowired
    private NodeClassDetailMapper nodeClassDetailMapper;
    @Autowired
    private SettlementMapper settlementMapper;
    @Autowired
    private SettlementDetailMapper settlementDetailMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private OperationLogMapper operationLogMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderOpService orderOpService;
    @Autowired
    private DishService dishService;
    @Autowired
    private OrderMemberMapper orderMemberMapper;
    @Autowired
    private OrderDetailServiceImpl orderdetailservice;

    @Override
    public String getServerTableList(String userId, String orderId) {
        userId = StringUtil.clean(userId);
        orderId = StringUtil.clean(orderId);
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(orderId)) {
            return "{\"Data\":\"0\",\"workdate\":\"\",\"Info\":缺少参数\"\"}";
        }
        orderOpService.pCaleTableAmount(userId, orderId);
        List<Map<String, Object>> orderStat = orderDetailMapper.selectStatByOrderId(orderId);
        if (null == orderStat || orderStat.isEmpty()) {
            return "{\"Data\":\"0\"}";
        }
        return "{\"Data\":" + JSON.toJSONString(orderStat) + "}";
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
        openLogMapper.truncate();
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
        Date today = new Date();
        OpenLog log = new OpenLog();
        log.setIpaddress(ip);
        log.setInsertTime(new Date());
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
            orderRule.setYhname(StringUtil.toString(map.get("yhname")));
            orderRule.setPartnerName(StringUtil.toString(map.get("partnername")));
            orderRule.setCouponRate(StringUtil.str2Float(map.get("couponrate"), 0));
            orderRule.setFreeAmount(StringUtil.str2Float(map.get("freeamount"), 0));
            orderRule.setDebitAmount(StringUtil.str2Float(map.get("debitamount"), 0));
            orderRule.setMemo(StringUtil.toString(map.get("memo")));
            orderRule.setCouponsNo(StringUtil.toString(map.get("couponsno")));
            orderRule.setNum(StringUtil.str2Int(map.get("num"), 0));
            orderRule.setFtype(StringUtil.str2Int(map.get("ftype"), 0));
            orderRule.setBankType(StringUtil.toString(map.get("banktype")));
            orderRule.setRuleId(StringUtil.toString(map.get("ruleid")));
            orderRule.setCouponId(StringUtil.toString(map.get("couponid")));
            orderRule.setAmount(StringUtil.str2Float(map.get("amount"), 0));
            orderRuleMapper.insert(orderRule);

        }
        return "{\"Data\":\"1\"}";
    }

    @Override
    public String clearMachine(String userId, String userName, String ip, String posId, String authorizer) {
        OpenLog openLog = openLogMapper.selectAll();
        if (null == openLog) {
            return "{\"Data\":\"0\",\"Info\":\"还未开业，不能清机\"}";
        }
        Map<String, Object> tellerCash = tellerCashMapper.selectInsertDate(userId, ip);
        if (null == tellerCash) {
            return "{\"Data\":\"0\",\"Info\":\"本机没有您的零找金记录，不能清机\"}";
        }
        String classNo = getJbNo();// 结帐单号
        String openDate = DateUtils.toString((Date) tellerCash.get("opendate"), "yyyy-MM-dd");
        Date now = new Date();//清机时间
        Date insertDate = (Date) tellerCash.get("insertdate");

        String beginTime = DateUtils.toString(insertDate, "yyyy-MM-dd HH:mm:ss");
        //前班未结台数
        int lastNonTable = tellerCashMapper.selectLastNonTable(beginTime);
        // 前班未结押金
        int lastNonDeposit = 0;
        //备用金
        String prettyCash = tellerCash.get("cashamount").toString();
        // TODO: 2016/7/14  问题：多POS下会将多个POS的都统计
        //本班开台人数
        String tBeginPeople = tellerCashMapper.selectBeginPeople(beginTime);
        //本班开台总数
        int tBeginTableTotal = tellerCashMapper.selectBeginTableTotal(beginTime);
        //本班未结台数
        int tNonClosingTable = tellerCashMapper.selectNonClosingTable(beginTime);
        //本班未结金额
        String tNonClosingMoney = tellerCashMapper.selectNotClosingMoney(beginTime);
        //本班已结台数
        int tClosingTable = tellerCashMapper.selectClosingTable(beginTime);
        //本班已结人数
        String tClosingPeople = tellerCashMapper.selectClosingPeople(beginTime);
        //本班退菜金额
        String tRFoodMoney = tellerCashMapper.selectFoodMoney(beginTime);
        // 本班未退押金
        int tNonClosingDeposit = 0;
        // 本班赠单金额
        int tPresentedMoney = 0;
        // 服务费
        int serviceMoney = 0;
        // 包房费
        int roomMoney = 0;
        // 最低消费补齐
        int lowConsComp = 0;
        // 定额优惠金额
        int ratedPreferenceMoney = 0;
        //品项消费
        String itemMoney = tellerCashMapper.selectItemMoney(insertDate, userId);
        float itemMoneyFloat = StringUtil.str2Float(itemMoney, 0);
        //优惠金额
        String preferenceMoney = tellerCashMapper.selectPreferenceMoney(insertDate, userId);
        float preferenceMoneyFloat = StringUtil.str2Float(preferenceMoney, 0);
        //应收小计=品项总额-优惠金额。
        float accountsReceivableSubtotal = itemMoneyFloat - preferenceMoneyFloat;
        //抹零金额
        String removeMoney = tellerCashMapper.selectRemoveMoney(insertDate, userId);
        float removeMoneyFloat = StringUtil.str2Float(removeMoney, 0);
        // 应收合计
        float accountsReceivableTotal = accountsReceivableSubtotal - removeMoneyFloat;
        //合计
        String TotalMoney = tellerCashMapper.selectTotalMoney(insertDate, userId);
        float TotalMoneyFloat = StringUtil.str2Float(TotalMoney, 0);
        //计入收入合计
        String includedMoneyTotal = tellerCashMapper.selectIncludedTotalMoney(insertDate, userId);
        float includedMoneyTotalFloat = StringUtil.str2Float(includedMoneyTotal, 0);
        // 不计收入合计
        float noIncludedMoneyTotal = TotalMoneyFloat - includedMoneyTotalFloat+removeMoneyFloat;
        clearMachineMapper.insert(userId, insertDate);
        settlementDetailMapper.setClear(userId, insertDate);
        tellerCashMapper.updateStatus(ip, userId);

        // 餐具
        int tableware = 0;
        // 酒水
        int drinks = 0;
        // 酒水烟汤面
        int drinksSmokeNoodle = 0;
        // 本日营业总额
        int todayTurnover = 0;

        // 生成清机报表
        // t_nodeclass
        Map<String, Object> param = new HashMap<>();
        param.put("classNo", classNo);
        param.put("posID", posId);
        param.put("operatorID", userId);
        param.put("operatorName", userName);
        param.put("vIn", insertDate);
        param.put("vOut", now);
        param.put("prettyCash", prettyCash);
        param.put("lastNonTable", lastNonTable);
        param.put("lastNonDeposit", lastNonDeposit);
        param.put("tBeginPeople", tBeginPeople);
        param.put("tBeginTableTotal", tBeginTableTotal);
        param.put("tNonClosingTable", tNonClosingTable);
        param.put("tNonClosingMoney", tNonClosingMoney);
        param.put("tNonClosingDeposit", tNonClosingDeposit);
        param.put("tClosingTable", tClosingTable);
        param.put("tClosingPeople", tClosingPeople);
        param.put("tPresentedMoney", tPresentedMoney);
        param.put("tRFoodMoney", tRFoodMoney);
        param.put("itemMoney", itemMoney);
        param.put("serviceMoney", serviceMoney);
        param.put("roomMoney", roomMoney);
        param.put("lowConsComp", lowConsComp);
        param.put("preferenceMoney", preferenceMoney);
        param.put("itemMaccountsReceivableSubtotaloney", accountsReceivableSubtotal);
        param.put("removeMoney", removeMoney);
        param.put("ratedPreferenceMoney", ratedPreferenceMoney);
        param.put("accountsReceivableTotal", accountsReceivableTotal);
        param.put("includedMoneyTotal", includedMoneyTotal);
        param.put("noIncludedMoneyTotal", noIncludedMoneyTotal);
        param.put("TotalMoney", TotalMoney);
        param.put("tableware", tableware);
        param.put("drinks", drinks);
        param.put("drinksSmokeNoodle", drinksSmokeNoodle);
        param.put("todayTurnover", todayTurnover);
        param.put("priterTime", now);
        param.put("ipaddress", ip);
        param.put("workdate", openDate);
        param.put("shiftid", getShiftID());
        param.put("authorizer", authorizer);
        nodeClassMapper.insert(param);

        // 结算方式明细    把
        // t_nodeclass_detail
        param = new HashMap<>();
        param.put("userId", userId);
        param.put("classNo", classNo);
        param.put("workDate", openDate);
        param.put("beginTime", insertDate);
        nodeClassDetailMapper.insert(param);

        settlementMapper.setClear(userId);
        return "{\"Data\":\"1\",\"workdate\":\"" + openDate + "\",\"Info\":\"清机成功\"}";
    }

    @Override
    public String endWork() {
        Date workDate = WorkDateUtil.getWorkDate1();
        // 检查是否已经有清机，如果该员工已经清机提示
        // 还有未清机
        int notClear = tellerCashMapper.selectNotClear(workDate);
        String workDateStr = DateUtils.toString(workDate, "yyyy-MM-dd");
        if (notClear > 0) {
            return "{\"Data\":\"0\",\"workdate\":\"" + workDateStr + "\",\"Info\":\"还有未清机，不能结业！\"}";
        }
        // 还有未结帐  当天还有未结帐的不能结业
        int tableCount = tellerCashMapper.selectNotEndTable();
        if (tableCount > 0) {
            return "{\"Data\":\"0\",\"workdate\":\"" + workDateStr + "\",\"Info\":\"还有未结帐帐单，不能结业！\"}";
        }
        //如果当天还有外卖帐单没有结帐就不能结业
        int notPayOrder = orderMapper.selectNotPay();
        if (notPayOrder > 0) {
            return "{\"Data\":\"0\",\"workdate\":\"" + workDateStr + "\",\"Info\":\"还有未结帐外卖帐单，不能结业！\"}";
        }
        HashMap<String, Object> param = new HashMap<>();
        logger.info("调用procEndWork");
        openLogMapper.procEndWork(param);
        logger.info("调用procEndWork返回值：" + JacksonJsonMapper.objectToJson(param));
        long endfinish = (long) param.get("endfinish");
        if (endfinish != 1) {
            return "{\"Data\":\"0\",\"workdate\":\"" + workDateStr + "\",\"Info\":\"存储过程执行失败!\"}";
        }
        return "{\"Data\":\"1\",\"workdate\":\"" + workDateStr + "\",\"Info\":\"结业成功\"}";
    }

    @Override
    public String checkTellerCash(String ip, String userId) {
        Date workDate = WorkDateUtil.getWorkDate1();
        String workDateStr = DateUtils.toString(workDate, "yyyy-MM-dd");
        Map<String, Object> todayInfo = tellerCashMapper.selectTodayInfo(workDateStr, ip, userId);
        if (null == todayInfo || todayInfo.isEmpty()) {
            return "{\"Data\":\"0\",\"workdate\":\"\",\"Info\":\"\"}";
        }
        return "{\"Data\":\"1\",\"workdate\":\"" + workDateStr + "\",\"Info\":\"已输入\"}";
    }

    @Override
    public String inputTellerCash(String userId, String ip, float cashAmount) {
        Date workDate = WorkDateUtil.getWorkDate1();
        String workDateStr = DateUtils.toString(workDate, "yyyy-MM-dd");
        Map<String, Object> todayInfo = tellerCashMapper.selectTodayInfo(workDateStr, ip, userId);
        if (null == todayInfo || todayInfo.isEmpty()) {
            Map<String, Object> param = new HashMap<>();
            param.put("username", userId);
            param.put("opendate", workDate);
            param.put("ipaddress", ip);
            param.put("cashamount", cashAmount);
            param.put("shiftid", getShiftID());
            param.put("insertdate", new Date());
            tellerCashMapper.insert(param);
            param = new HashMap<>();
            param.put("sdetailid", IDUtil.getID());
            param.put("orderid", DateUtils.toString(workDate, "yyyyMMdd") + userId);
            param.put("normalprice", cashAmount);
            param.put("payamount", cashAmount);
            param.put("opendate", workDate);
            param.put("username", userId);
            settlementDetailMapper.insertAfterInputCash(param);
        }
        return "{\"Data\":\"1\",\"workdate\":\"" + workDateStr + "\",\"Info\":\"已输入\"}";
    }

    @Override
    public String putOrder(String tableNo, String orderId, String gzCode, String gzName, String telephone, String relaperson) {
        tableMapper.updaStatus0(tableNo);
        orderMapper.updatePutOrder(orderId, gzCode, gzName, telephone, relaperson);
        //外卖挂单以后开启打印  咖啡模式
        if (orderId != null) {
            orderdetailservice.afterprint(orderId);
        }
        operationLogMapper.deleteByTableNo(tableNo);
        return "{\"Data\":\"1\"}";
    }

    @Override
    public String getOrderSequence(String tableNo) {
        String sequence = operationLogMapper.selectMaxSequence(tableNo);
        return "{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"" + sequence + "\"}";
    }

    @Override
    public String getServerTableInfo(String tableNo, String userId) {
        ResponseData responseData = new ResponseData();
        String orderId = tableMapper.selectOrderIdOfStatusN5(tableNo);
        dishService.updateCj(orderId, userId);
        if (null == orderId || "".equals(orderId)) {
            responseData.setData("0");
            return JSON.toJSONString(responseData);
        } else {
            orderOpService.pCaleTableAmount(userId, orderId);
            return DataServerJsonFormat.jsonFormat(orderOpService.getInfoByOrderId(orderId));
        }
    }

    @Override
    public String getServerTableInfoByOrderId(String orderId, String userId) {
        ResponseData responseData = new ResponseData();
        dishService.updateCj(orderId, userId);
        if (null == orderId || "".equals(orderId)) {
            responseData.setData("0");
            return JSON.toJSONString(responseData);
        } else {
            orderOpService.pCaleTableAmount(userId, orderId);
            return DataServerJsonFormat.jsonFormat(orderOpService.getInfoByOrderId(orderId));
        }
    }

    @Override
    public String getOrder(String tableNo, String userId) {
        String orderIdExist = tableMapper.getOrderIdByTableNo(tableNo);
        if (null == orderIdExist || orderIdExist.trim().isEmpty()) {
            return "{\"Data\":\"0\",\"Info\":\"餐桌不存在或已被删除。\"}";
        }
        //获取单头信息
        String tableJson = getServerTableInfo2(tableNo, userId);
        //获取单体信息
        String orderId = tableMapper.selectOrderIdOfStatusN5(tableNo);
        String tableListJson = getServerTableList2(orderId, userId);
        return "{\"Data\":\"1\",\"Info\":\"\",\"OrderJson\":" + tableJson + ",\"JSJson\":" + tableListJson + "}";
    }

    @Override
    public String getOrderByOrderID(String orderid, String userId) {
        //获取单头信息
        String tableJson = getServerTableInfo3(orderid, userId);
        //获取单体信息
        String tableListJson = getServerTableList2(orderid, userId);
        return "{\"Data\":\"1\",\"Info\":\"\",\"OrderJson\":" + tableJson + ",\"JSJson\":" + tableListJson + "}";
    }


    @Override
    public String getServerTableInfo3(String orderId, String userId) {
        //还原会员价
        memberService.revertMemberPrice(userId, orderId);
        dishService.updateCj(orderId, userId);
        if (StringUtil.isEmpty(orderId)) {
            return "[]";
        }
        orderOpService.pCaleTableAmount(userId, orderId);
        List<Map<String, Object>> tableOrder = orderMapper.selectTableOrder(orderId);

        return JSON.toJSONString(tableOrder, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);
    }

    @Override
    public String getServerTableInfo2(String tableNo, String userId) {
        String orderId = tableMapper.selectOrderIdOfStatusN5(tableNo);
        //还原会员价
        memberService.revertMemberPrice(userId, orderId);
        dishService.updateCj(orderId, userId);
        if (StringUtil.isEmpty(orderId)) {
            return "[]";
        }
        orderOpService.pCaleTableAmount(userId, orderId);
        List<Map<String, Object>> tableOrder = orderMapper.selectTableOrder(orderId);

        return JSON.toJSONString(tableOrder, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);
    }

    @Override
    public String getServerTableList2(String orderId, String userId) {
        List<Map<String, Object>> orderStat = orderDetailMapper.selectStatByOrderId1(orderId);
        if (null == orderStat || orderStat.isEmpty()) {
            return "[]";
        }
        return JSON.toJSONString(orderStat, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);
    }

    @Override
    public Date getOpenDate() {
        return openLogMapper.getOpenDate();
    }

    /**
     * 获取班次号
     *
     * @return
     */
    private int getShiftID() {
        Calendar calendar = Calendar.getInstance();
        int houre = calendar.get(Calendar.HOUR_OF_DAY);
        if (houre >= 3 && houre < 16) {
            return 0;
        }
        return 1;
    }

    @Override
    public String accountsOrder(String userId, String orderId) {
//        Integer orderStatus = orderMapper.selectOrderStatus(orderId);
        String isClear = settlementMapper.selectClearByOrderId(orderId);
        String currentTableId = orderMapper.selectCurrentTableId(orderId);
        String tableNo = tableMapper.selectTableNo(currentTableId);
        Integer tableStatus = tableMapper.selectStatus(currentTableId);
        if (null != tableStatus && tableStatus != Constant.TABLESTATUS.FREE_STATUS) {
            return "{\"Data\":\"0\",\"Info\":\"帐单当前桌号还未结帐!\"}";
        }
        if ("1".equals(isClear)) {
            return "{\"Data\":\"0\",\"Info\":\"帐单已经生成了清机单!\"}";
        }
        tableMapper.updaStatus1(orderId, currentTableId);
        return "{\"Data\":\"1\",\"Info\":\"" + tableNo + "\"}";
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
            maxClassNoToday = preStr + String.format("%04d", serialInt + 1);
        }
        return maxClassNoToday;
    }

    @Override
    public String getTableNoByOrderId(String orderId) {
        return tableMapper.getTableNoByOrderId(orderId);
    }

    @Override
    public String posrebacksettleorder(String orderId, String userId, String addr) {
        String valid = orderMemberMapper.selectValid(orderId);
        if (!StringUtil.isEmpty(valid)) {
            return "{\"Data\":\"0\"}";
        }
        rebackSettleOrder(orderId, userId, addr);
        return "{\"Data\":\"1\"}";
    }

    private String rebackSettleOrder(String orderId, String userId, String addr) {
        String content = "{\"orderNo\" : \"" + orderId + "\",\"userName\" : \"" + userId + "\",\"reason\" : \"会员结算失败,系统自动反结\"}";
        String result = "";
        try {
            result = HttpUtil.doRestfulByHttpConnection("http://" + addr + "/newspicyway/padinterface/rebacksettleorder.json", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
    }
}
