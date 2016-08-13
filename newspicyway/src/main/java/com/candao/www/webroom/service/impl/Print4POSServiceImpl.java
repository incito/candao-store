package com.candao.www.webroom.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
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

import com.candao.www.dataserver.mapper.OrderOpMapper;
import com.candao.www.dataserver.service.order.OrderOpService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.candao.common.utils.Constant;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.DishItem;
import com.candao.print.entity.OrderInfo4Pos;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.ResultInfo4Pos;
import com.candao.print.entity.ResultTip4Pos;
import com.candao.print.entity.SettlementInfo4Pos;
import com.candao.print.entity.TipItem;
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.Tinvoice;
import com.candao.www.spring.SpringContext;
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

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(5000));

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

    @Override
    public void print(List<SettlementInfo4Pos> settlementInfos, String printType) throws Exception {
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
        param.put("type", "ACCURACY");
        List<Map<String, Object>> resultmap = dataDictionaryService.findByParams(param);
        if (!CollectionUtils.isEmpty(resultmap)) {
            for (SettlementInfo4Pos it : settlementInfos) {
                it.init(Integer.parseInt(String.valueOf(resultmap.get(0).get("itemid"))));
            }
        }
        SettlementInfo4Pos settlementInfo4Pos = settlementInfos.get(0);
        settlementInfo4Pos.setBranchName(String.valueOf(branchInfo.get("branchname") == null ? "" : branchInfo.get("branchname")));
        settlementInfo4Pos.setTel(String.valueOf(branchInfo.get("managertel") == null ? "" : branchInfo.get("managertel")));
        settlementInfo4Pos.setAddress(String.valueOf(branchInfo.get("branchaddress") == null ? "" : branchInfo.get("branchaddress")));
        obj.setSettlementInfo4Pos(settlementInfo4Pos);
        // TODO
        Map<String, Object> params = new HashMap<>();
        params.put("printertype", "10");
        sendToPrint(params, obj);
    }

    @Override
    public void printClearMachine(List<SettlementInfo4Pos> settlementInfos) {
        if (CollectionUtils.isEmpty(settlementInfos)) {
            return;
        }
        PrintObj obj = new PrintObj();
        obj.setSettlementInfo4Pos(settlementInfos.get(0));
        obj.setListenerType(Constant.ListenerType.ClearMachineDataTemplate);

        // TODO
        Map<String, Object> params = new HashMap<>();
        params.put("printertype", "10");
        sendToPrint(params, obj);
    }

    @Override
    public void printMemberSaleInfo(List<SettlementInfo4Pos> settlementInfos) throws Exception {
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
            obj.setSettlementInfo4Pos(settlementInfo4Pos);
            Map<String, Object> params = new HashMap<>();
            params.put("printertype", "10");
            sendToPrint(params, obj);
        }
    }

    private void sendToPrint(Map<String, Object> param, PrintObj obj) {
        // TODO
        if (param == null) {
            return;
        }
        List<Map<String, Object>> res = tbPrinterManagerDao.find(param);
        if (CollectionUtils.isEmpty(res)) {
            return;
        }
        for (int i = 0; i < res.size(); i++) {
            Map<String, Object> map = res.get(i);
            obj.setPrinterid(String.valueOf(map.get("printerid")));
            obj.setCustomerPrinterIp(String.valueOf(map.get("ipaddress")));
            Print4POSProcedure print4POSProcedure = (Print4POSProcedure) SpringContext
                    .getBean(Print4POSProcedure.class);
            print4POSProcedure.setSource(obj);
            executor.execute(print4POSProcedure);
        }
    }

    @Override
    public void printItemSellDetail(ResultInfo4Pos resultInfo4Pos) {
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
        sendToPrint(params, obj);
    }

    private String stringAdd(String i1, String i2) {
        i1 = StringUtils.isEmpty(i1) ? "0" : i1;
        i2 = StringUtils.isEmpty(i1) ? "0" : i2;
        return new BigDecimal(i1).add(new BigDecimal(i2)).toString();
    }

    @Override
    public void printTip(ResultTip4Pos resultInfo4Pos) {
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
        sendToPrint(param, obj);
    }

    private List<Map<String, Object>> generatePreferList(Map<String, Object> map) {
        Assert.notEmpty(map, "固定优惠不能为空");
        List<Map<String, Object>> res = new LinkedList<>();
        String[] name = {"优免", "会员积分消费", "会员券消费", "折扣优惠", "抹零", "赠送金额", "四舍五入", "会员储值消费虚增"};
        String[] valueName = {"bastfree", "integralconsum", "meberTicket", "discountmoney", "malingincom", "give",
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
        String[] name = {"现金", "挂账", "微信", "支付宝", "刷卡-工行", "刷卡-他行", "会员储值消费净值"};
        String[] valueName = {"money", "card", "weixin", "zhifubao", "icbc", "otherbank", "merbervaluenet"};
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("name", name[i]);
            temp.put("value", map.get(valueName[i]));
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
                    if (preferentialInfo instanceof Map) {
                        Map prefer = ((Map) preferentialInfo);
                        settlementInfo = new ArrayList<>();
                        String[] name = {"合计", prefer.get("moneyWipeName").toString(), "赠送金额", "总优惠", "实收"};
                        String[] value = {prefer.get("menuAmount").toString(),
                                prefer.get("moneyWipeAmount").toString(), prefer.get("zdAmount").toString(),
                                prefer.get("amount").toString(), prefer.get("payamount").toString()};
                        for (int i = 0; i < name.length; i++) {
                            Map<String, String> tempMap = new HashMap<>();
                            if (i > 0 && i < 3) {
                                if (StringUtils.isEmpty(value[i]) || 0 >= new BigDecimal(value[i]).compareTo(new BigDecimal(0))) {
                                    continue;
                                }
                            }
                            tempMap.put("name", name[i]);
                            tempMap.put("value", value[i]);
                            settlementInfo.add(tempMap);
                        }
                    }
                    posdata.put("settlementInfo", settlementInfo);
                    //电话地址
                    posdata.put("branchName", branchInfo.get("branchname") == null ? "" : branchInfo.get("branchname").toString());
                    posdata.put("tel", branchInfo.get("managertel") == null ? "" : branchInfo.get("managertel").toString());
                    posdata.put("address", branchInfo.get("branchaddress") == null ? "" : branchInfo.get("branchaddress").toString());
                    obj.setPosData(posdata);
                    // TODO
                    Map<String, Object> param = new HashMap<>();
                    param.put("printertype", "10");
                    sendToPrint(param, obj);
                    //更新打印数量
                    updatePresettelmentCount((Map<String, Object>) posdata.get("userOrderInfo"));
                }
            }
        }

    }

    private List<Map<String, Object>> parseRows(List<Map<String, Object>> rows) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map<String, Object> it : rows) {
            it.put("payamount",
                    strMulti(String.valueOf(it.get("orderprice")), String.valueOf(it.get("dishnum"))));
            res.add(it);
            if (it.get("dishes") != null) {
                List<Map<String, Object>> temp3 = (List<Map<String, Object>>) it.get("dishes");
                for (Map<String, Object> item : temp3) {
                    item.put("payamount", strMulti(String.valueOf(item.get("orderprice")),
                            String.valueOf(item.get("dishnum"))));
                    res.add(item);
                }
            }
        }
        return res;
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
