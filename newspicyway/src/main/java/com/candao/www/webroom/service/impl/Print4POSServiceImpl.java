package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.candao.print.entity.*;
import com.candao.www.data.model.*;
import com.candao.www.data.pos.TPrinterDeviceMapper;
import com.candao.www.data.pos.TPrinterDeviceprinterMapper;
import com.candao.www.dataserver.mapper.OrderOpMapper;
import com.candao.www.permit.service.UserService;

import com.candao.www.spring.SpringContext;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.candao.common.utils.Constant;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.utils.CloneUtil;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.InvoiceService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.Print4POSService;
import com.candao.www.webroom.service.TableService;

@Service
public class Print4POSServiceImpl implements Print4POSService {

    public static final String SETTLEMENT = "2";

    public static final String PRESETTLEMENT = "1";

    public static final String CUSTTEMPLATE = "3";

    public static final String MEMBERSALEINFO_CUST = "客户联";

    public static final String MEMBERSALEINFO_STORE = "商户联";

    public static final String FREE_DISH_TYPE = "1";

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(5000));

    private Log log = LogFactory.getLog(this.getClass());

    // private Print4POSProcedure print4POSProcedure;
    @Autowired
    TbPrinterManagerDao tbPrinterManagerDao;
    @Autowired
    private TbBranchDao tbBranchDao;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private OrderDetailServiceImpl orderDetail;
    @Autowired
    private InvoiceService invoiceinfo;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderOpMapper orderMapper;
    @Autowired
    @Qualifier("t_userService")
    private UserService userService;
    @Autowired
    private TPrinterDeviceMapper tPrinterDeviceMapper;
    @Autowired
    private TPrinterDeviceprinterMapper tPrinterDeviceprinterMapper;

    @Override
    public void print(List<SettlementInfo4Pos> settlementInfos, String printType, String deviceid) throws Exception {
        if (CollectionUtils.isEmpty(settlementInfos) || StringUtils.isEmpty(printType)) {
            return;
        }
        PrintObj obj = new PrintObj();
        // 三种类型类型单据
        switch (printType.trim()) {
            case SETTLEMENT:
                obj.setListenerType(Constant.ListenerType.SettlementDishListener);
                break;
            case PRESETTLEMENT:
                obj.setListenerType(Constant.ListenerType.PreSettlementTemplate);
                break;
            case CUSTTEMPLATE: {
                SettlementInfo4Pos settlement = settlementInfos.get(0);
                if (!CollectionUtils.isEmpty(settlement.getOrderJson())) {
                    OrderInfo4Pos orderinfo = settlement.getOrderJson().get(0);
                    orderDetail.printCust(orderinfo.getOrderid(), true);
                }
                return;
            }
            default:
                return;
        }
        Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
        Map<String, Object> param = new HashMap<>();
        param.clear();
        // 零头处理方式
        param.put("type", "ROUNDING");
        List<Map<String, Object>> resultmap = dataDictionaryService.findByParams(param);
        if (!CollectionUtils.isEmpty(resultmap)) {
            for (SettlementInfo4Pos it : settlementInfos) {
                it.init(Integer.parseInt(String.valueOf(resultmap.get(0).get("itemid"))));
            }
        }
        SettlementInfo4Pos settlementInfo4Pos = settlementInfos.get(0);
        settlementInfo4Pos.setBranchName(
                String.valueOf(branchInfo.get("branchname") == null ? "" : branchInfo.get("branchname")));
        settlementInfo4Pos
                .setTel(String.valueOf(branchInfo.get("managertel") == null ? "" : branchInfo.get("managertel")));
        settlementInfo4Pos.setAddress(
                String.valueOf(branchInfo.get("branchaddress") == null ? "" : branchInfo.get("branchaddress")));
        obj.setSettlementInfo4Pos(settlementInfo4Pos);
        // TODO
        Map<String, Object> params = new HashMap<>();
        params.put("printertype", "10");
        params.put("deviceid", deviceid);
        sendToPrint(params, obj);
    }

    @Override
    public void printClearMachine(List<SettlementInfo4Pos> settlementInfos, String posId) throws Exception {
        if (CollectionUtils.isEmpty(settlementInfos)) {
            return;
        }
        PrintObj obj = new PrintObj();
        obj.setSettlementInfo4Pos(settlementInfos.get(0));
        obj.setListenerType(Constant.ListenerType.ClearMachineDataTemplate);

        // TODO
        Map<String, Object> params = new HashMap<>();
        params.put("printertype", "10");
        params.put("deviceid", posId);
        sendToPrint(params, obj);
    }

    @Override
    public void printMemberSaleInfo(List<SettlementInfo4Pos> settlementInfos, String deviceid) throws Exception {
        if (CollectionUtils.isEmpty(settlementInfos)) {
            return;
        }
        String[] types = {MEMBERSALEINFO_CUST, MEMBERSALEINFO_STORE};
        for (int i = 0; i < types.length; i++) {
            PrintObj obj = new PrintObj();
            obj.setListenerType(Constant.ListenerType.MemberSaleInfoTemplate);
            SettlementInfo4Pos settlementInfo4Pos = (SettlementInfo4Pos) CloneUtil.clone(settlementInfos.get(0), -1);
            OrderInfo4Pos temp = (OrderInfo4Pos) settlementInfo4Pos.getOrderJson().get(0);
            temp.setType(types[i]);
            String time = temp.getOrdertime();
            Date date = new Date(Long.parseLong(time));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            temp.setOrderday(sdf.format(date));
            sdf = new SimpleDateFormat("HH:mm:ss");
            temp.setOrdertime(sdf.format(date));
            obj.setSettlementInfo4Pos(settlementInfo4Pos);
            Map<String, Object> params = new HashMap<>();
            params.put("printertype", "10");
            params.put("deviceid", deviceid);
            sendToPrint(params, obj);
        }
    }

    private void sendToPrint(Map<String, Object> param, PrintObj obj) throws Exception {
        // TODO
        if (MapUtils.isEmpty(param)) {
            return;
        }
        if (param.containsKey("deviceid")) {
            TPrinterDeviceExample example = new TPrinterDeviceExample();
            example.or().andDevicecodeEqualTo(param.get("deviceid").toString()).andDevicestatusEqualTo(0).andDevicetypeEqualTo(2);
            List<TPrinterDevice> list = tPrinterDeviceMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(list))
                throw new RuntimeException("查找不到该POS对应的记录");
            if (list.size() > 1)
                throw new RuntimeException("该POS记录有多条");
            TPrinterDeviceprinterExample example1 = new TPrinterDeviceprinterExample();
            example1.or().andDevicecodeEqualTo(list.get(0).getDevicecode());
            List<TPrinterDeviceprinter> list1 = tPrinterDeviceprinterMapper.selectByExample(example1);
            if (CollectionUtils.isEmpty(list1)) {
                log.error("------>该POS机没有配置打印机");
                throw new RuntimeException("该POS机没有配置打印机");
            }
            List<Map> ipAddress = JSON.parseArray(JSON.toJSONString(list1), Map.class);
            doSend(ipAddress, obj);
        } else {
            List<Map> res = tbPrinterManagerDao.find(param);
            if (CollectionUtils.isEmpty(res)) {
                log.error("------>没有配置POS打印机");
                throw new RuntimeException("没有配置POS打印机");
            }
            doSend(res, obj);
        }

    }

    private void doSend(List<Map> res, PrintObj obj) throws Exception {
        if (CollectionUtils.isEmpty(res))
            return;
        for (int i = 0; i < res.size(); i++) {
            Map<String, Object> map = res.get(i);
            PrintObj tempObj = (PrintObj) CloneUtil.clone(obj, -1);
            TbPrinterManager printerManager = tbPrinterManagerDao.get(String.valueOf(map.get("printerid")));
            tempObj.setPrinterid(String.valueOf(map.get("printerid")));
            tempObj.setCustomerPrinterIp(printerManager.getIpaddress());
            Print4POSProcedure print4POSProcedure = (Print4POSProcedure) SpringContext
                    .getBean(Print4POSProcedure.class);
            print4POSProcedure.setSource(tempObj);
            executor.execute(print4POSProcedure);
        }
    }

    @Override
    public void printItemSellDetail(ResultInfo4Pos resultInfo4Pos, String deviceid) throws Exception {
        if (resultInfo4Pos == null) {
            return;
        }
        Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
        if (!MapUtils.isEmpty(branchInfo)) {
            resultInfo4Pos.setBranname(String.valueOf(branchInfo.get("branchname")));
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resultInfo4Pos.setDatetime(sdf.format(date));
        String total = "0";

        if (!CollectionUtils.isEmpty(resultInfo4Pos.getData())) {
            int index = 0;
            for (DishItem it : resultInfo4Pos.getData()) {
                it.setIndex(++index + "");
                total = stringAdd(total, StringUtils.isEmpty(it.getTotlePrice()) ? "0" : it.getTotlePrice());
            }
        }
        resultInfo4Pos.setTotal(total);

        PrintObj obj = new PrintObj();
        obj.setItem(resultInfo4Pos);
        obj.setListenerType(Constant.ListenerType.ItemSellDetailTemplate);

        // TODO
        Map<String, Object> params = new HashMap<>();
        params.put("printertype", "10");
        params.put("deviceid", deviceid);
        sendToPrint(params, obj);
    }

    private String stringAdd(String i1, String i2) {
        i1 = StringUtils.isEmpty(i1) ? "0" : i1;
        i2 = StringUtils.isEmpty(i1) ? "0" : i2;
        return new BigDecimal(i1).add(new BigDecimal(i2)).toString();
    }

    @Override
    public void printTip(ResultTip4Pos resultInfo4Pos, String deviceid) throws Exception {
        if (resultInfo4Pos == null) {
            return;
        }
        Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
        if (!MapUtils.isEmpty(branchInfo)) {
            resultInfo4Pos.setBranchname(String.valueOf(branchInfo.get("branchname")));
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resultInfo4Pos.setDatetime(sdf.format(date));
        String total = "0";

        if (!CollectionUtils.isEmpty(resultInfo4Pos.getData())) {
            int index = 0;
            for (TipItem it : resultInfo4Pos.getData()) {
                it.setIndex(++index + "");
                total = stringAdd(total, it.getTipMoney());
            }
        }
        resultInfo4Pos.setTotal(total);

        PrintObj obj = new PrintObj();
        obj.setTip(resultInfo4Pos);
        obj.setListenerType(Constant.ListenerType.TipListTemplate);

        // TODO
        Map<String, Object> params = new HashMap<>();
        params.put("printertype", "10");
        params.put("deviceid", deviceid);
        sendToPrint(params, obj);
    }

    @Override
    public void printInvoice(Map<String, Object> map) throws Exception {
        Assert.notEmpty(map);
        if (!map.containsKey("orderid")) {
            throw new Exception("参数错误");
        }
        List<Tinvoice> inList = invoiceinfo.findInvoiceByOrderid(map);
        Assert.notEmpty(inList);
        Map<String, Object> order = orderService.findOrderById(String.valueOf(map.get("orderid")));
        Assert.notEmpty(order, "找不到该订单");
        TbTable table = tableService.findById(String.valueOf(order.get("currenttableid")));

        Map<String, Object> posData = new HashMap<>();
        posData.put("tableno", table.getTableNo());
        posData.put("orderid", order.get("orderid"));
        posData.put("title", inList.get(0).getInvoice_title());
        posData.put("amount", map.get("amount"));

        PrintObj obj = new PrintObj();
        obj.setPosData(posData);
        obj.setListenerType(Constant.ListenerType.InvoiceTemplate);

        // TODO
        Map<String, Object> params = new HashMap<>();
        params.put("printertype", "10");
        params.put("deviceid", map.get("deviceid"));
        sendToPrint(params, obj);
    }

    @Override
    public void printStoredCard(Map<String, Object> map) throws Exception {
        Assert.notEmpty(map);

        String[] types = {MEMBERSALEINFO_CUST, MEMBERSALEINFO_STORE};
        for (int i = 0; i < types.length; i++) {
            PrintObj obj = new PrintObj();
            obj.setListenerType(Constant.ListenerType.StoreCardToNewPosTemplate);
            @SuppressWarnings("unchecked")
            Map<String, Object> temp = (Map<String, Object>) CloneUtil.clone(map, -1);
            temp.put("type", types[i]);
            obj.setPosData(temp);
            Map<String, Object> params = new HashMap<>();
            params.put("printertype", "10");
            params.put("deviceid", map.get("deviceid"));
            sendToPrint(params, obj);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void printBusinessDetail(Map<String, Object> map, String... params) throws Exception {
        Assert.noNullElements(params, "参数错误！");
        Assert.state(params.length == 5, "前面接口错误");

        Map<String, Object> posData = new HashMap<>();

        Map<String, List<Map<String, Object>>> itemList = JSON.parseObject(params[0], Map.class);

        Map<String, List<Map<String, Object>>> preferList = JSON.parseObject(params[1], Map.class);

        Map<String, List<Map<String, Object>>> billCountList = JSON.parseObject(params[3], Map.class);

        Map<String, List<Map<String, Object>>> dayReportList = JSON.parseObject(params[2], Map.class);

        Map<String, Object> tipList = JSON.parseObject(params[4], Map.class);

        if (!MapUtils.isEmpty(billCountList)) {
            billCountList.get("resultList");
        }
        // 品项
        posData.put("item", itemList.get("result"));
        // 券
        posData.put("prefer", preferList.get("CouponsReptList"));
        // 明细
        posData.put("billCount", billCountList.get("resultList"));
        // 营业报表
        posData.put("dayReport", dayReportList.get("reportlist").get(0));
        // POS人员确认写死
        List<Map<String, Object>> fixedPreferList = generatePreferList(dayReportList.get("reportlist").get(0));
        posData.put("fixedPrefer", fixedPreferList);
        List<Map<String, Object>> fixedSettlementList = generateSettlementList(dayReportList.get("reportlist").get(0));
        posData.put("fixedSettlemen", fixedSettlementList);

        // 消费金额
        posData.put("tip", tipList.get("tipMoney"));

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("date", sdf.format(date));
        // POS传参
        posData.put("params", map);

        Assert.notEmpty(posData);

        PrintObj obj = new PrintObj();
        obj.setPosData(posData);
        obj.setListenerType(Constant.ListenerType.BillDetailTemplate);
        // TODO
        Map<String, Object> param = new HashMap<>();
        param.put("printertype", "10");
        param.put("deviceid", map.get("deviceid"));
        sendToPrint(param, obj);
    }

    private List<Map<String, Object>> generatePreferList(Map<String, Object> map) {
        Assert.notEmpty(map, "固定优惠不能为空");
        List<Map<String, Object>> res = new LinkedList<>();
        String[] name = {"优免", "会员积分消费", "会员券消费", "会员优惠", "抹零", "赠送金额", "四舍五入", "会员储值消费虚增"};
        String[] valueName = {"bastfree", "integralconsum", "meberTicket", "memberDishPriceFree", "malingincom", "give",
                "handervalue", "mebervalueadd"};
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("name", name[i]);
            temp.put("value", map.get(valueName[i]));
            res.add(temp);
        }
        return res;
    }

    private List<Map<String, Object>> generateSettlementList(Map<String, Object> map) {
        Assert.notEmpty(map, "固定结算信息不能为空");
        List<Map<String, Object>> res = new LinkedList<>();
        //自定义支付方式
        List settlements = (List)map.get("settlements");
        List settlementDesc = (List)map.get("settlementDescList");
        for (int i = 0; i < settlementDesc.size(); i++) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("name",settlementDesc.get(i));
            temp.put("value", settlements.get(i));
            res.add(temp);
        }
        return res;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void printPreSettlement(Map<String, Object> map, String... params) throws Exception {
        Assert.notEmpty(map, "接口返回空！");
        String json = JSON.toJSONString(map);
        Map<String, Object> temp = JSON.parseObject(json, Map.class);
        List<Map<String, Object>> order = JSON.parseObject(params[1], List.class);

        if (temp.containsKey("code")) {
            if ("0".equals(temp.get("code"))) {
                Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
                PrintObj obj = new PrintObj();
                obj.setListenerType(Constant.ListenerType.PreSettlementTemplate);
                Object data = temp.get("data");
                if (data instanceof Map) {
                    Map posdata = ((Map) data);

                    // 鱼锅
                    List<Map<String, Object>> rows = (List<Map<String, Object>>) posdata.get("rows");
                    if (!CollectionUtils.isEmpty(rows)) {
                        List<Map<String, Object>> temp2 = parseRows(rows);
                        posdata.put("rows", temp2);
                    }

                    // 优惠
                    Object preferentialInfo = posdata.get("preferentialInfo");
                    List<Map<String, String>> settlementInfo = null;
                    List<Map<String, String>> preferListInfo = null;
                    if (preferentialInfo instanceof Map) {
                        Map prefer = ((Map) preferentialInfo);
                        //组合结算备注
                        settlementInfo = new ArrayList<>();
                        String[] name = {"合计：", resolveNullType(prefer.get("moneyWipeName")) + ":", "赠送金额:", "总优惠:", "应收:"};
                        String[] value = {resolveNullType(prefer.get("menuAmount")),
                                resolveNullType(prefer.get("moneyWipeAmount")), resolveNullType(prefer.get("zdAmount")),
                                stringAdd(resolveNullType(prefer.get("toalFreeAmount")), resolveNullType(prefer.get("moneyWipeAmount"))), resolveNullType(prefer.get("reserveAmout"))};
                        for (int i = 0; i < name.length; i++) {
                            Map<String, String> tempMap = new HashMap<>();
                            if (i > 0 && i < 3) {
                                //只有为0时才不显示
                                if (StringUtils.isEmpty(value[i])
                                        || 0 == new BigDecimal(value[i]).compareTo(new BigDecimal(0))) {
                                    continue;
                                }
                            }
                            tempMap.put("name", name[i]);
                            tempMap.put("value", "￥" + value[i]);
                            settlementInfo.add(tempMap);
                        }
                        //组合优惠备注
                        preferListInfo = new ArrayList<>();
                        List<Map<String, Object>> prefers = (List<Map<String, Object>>) prefer.get("detailPreferentials");
                        if (!CollectionUtils.isEmpty(prefers)) {
                            for (Map<String, Object> it : prefers) {
                                String activeName = it.get("activity") == null ? "" : resolveNullType(((Map<String, Object>) it.get("activity")).get("name"));
                                preferListInfo.add(createItem(activeName, it.get("toalFreeAmount")));
                                /*if (0 != new BigDecimal(0).compareTo(new BigDecimal(resolveNullType(it.get("toalDebitAmount"))))) {
                                    preferListInfo.add(createItem(activeName, it.get("toalDebitAmount")));
                                }*/
                            }
                        }
                    }
                    //结算备注
                    posdata.put("settlementInfo", settlementInfo);
                    //优惠备注
                    posdata.put("preferListInfo", preferListInfo);
                    // 优惠（元）
                    Map<String, Object> orderJson = ((List<Map<String, Object>>) order.get(0).get("OrderJson")).get(0);
                    String free = resolveNullType(orderJson.get("discountamount"));
                    posdata.put("totalfree", free);
                    // 品项费
                    String pxFee = stringAdd(free, resolveNullType(orderJson.get("dueamount")));
                    posdata.put("pxFee", pxFee);
                    // 打印人
                    Map<String, Object> param = new HashMap<>();
                    param.put("jobNumber", params[2]);
                    List<User> users = userService.queryUserList(param);
                    posdata.put("printname", users.get(0).getName());

                    // 电话地址
                    posdata.put("branchName", resolveNullType(branchInfo.get("branchname")));
                    posdata.put("tel", resolveNullType(branchInfo.get("managertel")));
                    posdata.put("address", resolveNullType(branchInfo.get("branchaddress")));
                    obj.setPosData(posdata);
                    // TODO
                    param.clear();
                    param.put("printertype", "10");
                    param.put("deviceid", params[3]);
                    sendToPrint(param, obj);
//                    // 更新打印数量
//                    updatePresettelmentCount((Map<String, Object>) posdata.get("userOrderInfo"));
                }
            }
        }
    }

    private Map<String, String> createItem(Object name, Object value) {
        Map<String, String> temp = new HashMap<>();
        temp.put("name", resolveNullType(name));
        temp.put("value", resolveNullType(value));
        return temp;
    }

    private String resolveNullType(Object param) {
        return com.candao.common.utils.StringUtils.resolveNullType(param);
    }

    /**
     * 解析品项,判断赠菜和鱼锅套餐
     *
     * @param rows
     * @return
     */
    private List<Map<String, Object>> parseRows(List<Map<String, Object>> rows) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map<String, Object> it : rows) {
            resolveSpecialRequire(it);
            res.add(it);
            if (it.get("dishes") != null) {
                List<Map<String, Object>> temp3 = (List<Map<String, Object>>) it.get("dishes");
                for (Map<String, Object> item : temp3) {
                    resolveSpecialRequire(item);
                    res.add(item);
                }
            }
        }
        return res;
    }

    /**
     * 解决打印单据展现的特殊需求
     *
     * @param it
     */
    private void resolveSpecialRequire(Map<String, Object> it) {
        String dishname = resolveNullType(it.get("dishname"));
        String dishunit = resolveNullType(it.get("dishunit"));
        String[] dishnames = StringUtils.split(dishname, "#");
        String[] dishunits = StringUtils.split(dishunit, "#");

        dishname = (dishnames == null ? dishname : dishnames[0]) + "(" + (dishunits == null ? dishunit : dishunits[0]) + ")";
        if (FREE_DISH_TYPE.equals(it.get("pricetype"))) {
            dishname += "(赠)";
        }
        if (dishnames != null || dishunits != null) {
            dishname += "\n";
            dishname += (dishnames == null ? "" : dishnames[1]) + (dishunits == null ? "" : "(" + dishunits[1] + ")");
        }
        it.put("payamount",
                strMulti(resolveNullType(it.get("orderprice")), resolveNullType(it.get("dishnum"))));
        it.put("dishname", dishname);
        String dishnum = resolveNullType(it.get("dishnum"));
        it.put("dishnum", dishnum);
    }

    private void updatePresettelmentCount(Map<String, Object> params) throws Exception {
        if (StringUtils.isEmpty(params) || !params.containsKey("orderid")) {
            throw new Exception("更新预结单次数失败：参数错误!");
        }
        orderMapper.updateBefPrintCount(params.get("orderid").toString());
    }

    private String strMulti(String i1, String i2) {
        if (StringUtils.isEmpty(i1) || "null".equals(i1) || StringUtils.isEmpty(i2) || "null".equals(i2)) {
            return null;
        }
        return new BigDecimal(i1.toString()).multiply(new BigDecimal(i2.toString()))
                .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }
}
