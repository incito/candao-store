package com.candao.www.webroom.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.candao.www.data.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.candao.common.page.Page;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.print.entity.PrintObj;
import com.candao.print.service.PrinterService;
import com.candao.print.service.TableOptionService;
import com.candao.www.constant.Constant;
import com.candao.www.data.dao.TbPrintObjDao;
import com.candao.www.data.dao.TbTableDao;
import com.candao.www.data.dao.TorderDetailMapper;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.dao.TsettlementMapper;
import com.candao.www.data.dao.UserDao;
import com.candao.www.permit.common.Constants;
import com.candao.www.permit.service.UserService;
import com.candao.www.utils.ReturnMap;
import com.candao.www.utils.TsThread;
import com.candao.www.webroom.model.AccountCash;
import com.candao.www.webroom.model.Table;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.NotifyService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.ToperationLogService;
import com.candao.www.webroom.service.WorkLogService;
import org.springframework.util.StringUtils;

@Service
public class TableServiceImpl implements TableService {

    private static final Logger logger = LoggerFactory.getLogger(TableServiceImpl.class);

    @Autowired
    @Qualifier("t_userService")
    UserService userService;

    @Autowired
    private TbTableDao tableDao;
    @Autowired
    TorderMapper torderMapper;

    @Autowired
    TsettlementMapper settlementMapper;

    @Autowired
    private ToperationLogService toperationLogService;

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private DataDictionaryService datadictionaryService;

    @Autowired
    TorderDetailMapper torderDetailMapper;
    @Autowired
    OrderServiceImpl orderServiceImpl;

    @Autowired
    PrinterService printerService;
    @Autowired
    TableOptionService tableOptionService;

    @Autowired
    TbPrintObjDao tbPrintObjDao;

    @Autowired
    UserDao userDao;

    @Autowired
    private NotifyService notifyService;

    @Override
    public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
        return tableDao.page(params, current, pagesize);
    }

    @Override
    public boolean save(TbTable tbTable) {
        return tableDao.insert(tbTable) > 0;
    }

    @Override
    public TbTable findById(String id) {
        return tableDao.get(id);
    }

    @Override
    public TbTable findByTableNo(String tableNo) {
        return tableDao.getByTableNO(tableNo);
    }

    @Override
    public boolean update(TbTable tbTable) {
        return tableDao.update(tbTable) > 0;
    }

    @Override
    public boolean deleteById(String id) {
        return tableDao.delete(id) > 0;
    }

    @Override
    public List<Map<String, Object>> find(Map<String, Object> params) {
        return tableDao.find(params);
    }

    @Override
    public List<Map<String, Object>> getTableTag() {
        return tableDao.getTableTag();
    }

    @Override
    public List<Map<String, Object>> getPrinterTag() {
        return tableDao.getPrinterTag();
    }

    public List<Map<String, Object>> getbuildingNoANDTableTypeTag() {
        return tableDao.getbuildingNoANDTableTypeTag();
    }

    public List<Map<String, Object>> getTableTag3() {
        return tableDao.getTableTag3();
    }


    public int updateStatus(TbTable tbTable) {
        return tableDao.updateStatus(tbTable);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String switchTable(Table switchTable, ToperationLog toperationLog) {
        //1.修改更换的桌号为  已经预定
        //2 ,修改原来的桌号位空闲
        //3.修改订单号 桌位 为更换后的桌位
//		TbTable table = new TbTable();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableNo", switchTable.getOrignalTableNo());
//		map.put("status", "1");
        List<Map<String, Object>> orignalMap = tableDao.find(map);
        if (orignalMap == null || orignalMap.size() == 0 || orignalMap.size() > 1) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("目标餐台不存在!"));
        }

        Map<String, Object> map2 = torderMapper.findOne(String.valueOf(orignalMap.get(0).get("orderid")));
        if (map2 == null || map2.size() == 0 || "3".equals(String.valueOf(map2.get("orderstatus")))) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("餐台信息错误"));
        }

//		Map<String, Object> orignalMap2 =  orignalMap.get(0);
//		String orignalTableId = String.valueOf(orignalMap2.get("tableid"));

        map = new HashMap<String, Object>();
        map.put("tableNo", switchTable.getTableNo());
        map.put("status", "0");
        List<Map<String, Object>> currentMap = tableDao.find(map);
        if (currentMap == null || currentMap.size() == 0 || currentMap.size() > 1) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("此桌已开台"));
        }


        //执行存储过程
        String result = "1";
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("orginalTableNo", switchTable.getOrignalTableNo());
        mapParam.put("targetTableNo", switchTable.getTableNo());
        mapParam.put("result", result);
        torderMapper.selectSwitchTable(mapParam);
        result = String.valueOf(mapParam.get("result"));

        if (!"0".equals(result)) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("换台失败"));
        } else {
            if (toperationLogService.save(toperationLog)) {
                String userId = (String) map2.get("userid");
                User mapUser = userService.getUserByjobNum(userId);
                User disUser = userService.getUserByjobNum(switchTable.getDiscardUserId());
                PrintObj pj = new PrintObj();
                pj.setUserName(mapUser.getName());
                pj.setBillName("换台单");
                pj.setAbbrbillName("换");
                pj.setOrderNo(String.valueOf(orignalMap.get(0).get("orderid")));
                pj.setTimeMsg(DateUtils.dateToString(new Date()));
                pj.setWelcomeMsg(switchTable.getTableNo());//换到的台
                pj.setTableNo(switchTable.getOrignalTableNo());
                pj.setDiscardUserId(disUser.getName());

                printTableChangeBill(pj, "7");
                return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
            } else {
                return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("换台失败end"));
            }
        }
    }

    //printerType 6并台  7换台
    private void printTableChangeBill(PrintObj pj, String printerType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("printertype", printerType);
        List<Map<String, Object>> list = printerService.find(params);
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                pj.setCustomerPrinterIp(String.valueOf(map.get("ipaddress")));
                pj.setCustomerPrinterPort(String.valueOf(map.get("port")));
                pj.setRePeatID(UUID.randomUUID().toString());
                new Thread(new PrintBillThread(pj)).run();
            }
        }

    }

    public class PrintBillThread extends Thread {
        PrintObj printObj;

        PrintBillThread(PrintObj printObj) {
            this.printObj = printObj;
        }

        @Override
        public void run() {
            //根据动作打印不同的小票
            tableOptionService.sendMessage(printObj);
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String mergetableMultiMode(Table mergeTable, ToperationLog toperationLog) throws Exception {
        //是否清空目标餐台
        boolean isCleanTarget = mergeTable.getCleanTable();

        String sourceTableNo = mergeTable.getOrignalTableNo();
        String targetTableNo = mergeTable.getTableNo();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableNo", sourceTableNo);
        List<Map<String, Object>> resultMap = tableDao.find(map);
        if (resultMap == null || resultMap.size() == 0 || resultMap.size() > 1) {
            logger.error("源餐台不存在或者重复，tableNo:" + sourceTableNo + ",size:" + ((resultMap == null) ? 0 : resultMap.size()));
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("源餐台不存在或者重复"));
        }
        //源餐台数据
        Map<String, Object> sourceTable = resultMap.get(0);
        String sourceOrderId = String.valueOf(sourceTable.get("orderid"));
        Map<String, Object> sourceOrder = torderMapper.findOne(sourceOrderId);
        if (sourceOrder == null || sourceOrder.size() == 0 || !"0".equals(String.valueOf(sourceOrder.get("orderstatus")))) {
            logger.error("源餐台未开台或者账单状态异常，ordersize:" + ((sourceOrder == null) ? 0 : sourceOrder.size()) + ",orderstatus:" + String.valueOf(sourceOrder.get("orderstatus")));
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("源餐台未开台或者账单状态异常"));
        }

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("tableNo", targetTableNo);
        List<Map<String, Object>> listTables = tableDao.find(map1);
        if (listTables == null || listTables.size() == 0 || listTables.size() > 1) {
            logger.error("目标餐台不存在或者重复，tableNo:" + targetTableNo + ",size:" + ((listTables == null) ? 0 : listTables.size()));
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("目标餐台不存在或者重复"));
        }
        //目标餐台数据
        Map<String, Object> targetTable = listTables.get(0);
        String targetTableId = String.valueOf(targetTable.get("tableid"));
        String targetOrderId = String.valueOf(targetTable.get("orderid"));
        // 如果目标桌和原始桌的订单号相同，直接返回并台成功
        if (sourceOrderId.equals(targetOrderId)) {
            logger.error("目标餐台与源餐台是同一个账单，tableNo:" + targetTableNo);
            return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap("目标餐台与源餐台是同一个账单"));
        }
        Map<String, Object> targetOrder = torderMapper.findOne(targetOrderId);

        //清空以前的日志
        toperationLogService.deleteToperationLogByTableNo(sourceTableNo);
        toperationLogService.deleteToperationLogByTableNo(targetTableNo);
        //封装账单合并数据
        Map<String, Object> mergeMap = new HashMap<>();
        mergeMap.put("orderid", sourceOrderId);
        mergeMap.put("targetTableNo", targetTableNo);
        mergeMap.put("sourceTableNo", sourceTableNo);

        // ---------------------------------------------------------
        // 并桌的时候查询目标桌是否开台，是否下单了 下单了，并桌的时候把下单的数据返回给pad。并桌只能并一个桌
        Map<String, Object> resultmap = new HashMap<String, Object>();
//		resultmap.put("result", "0");
        //目标餐台被占用
        if (isTableUsing(targetTable)) {
            //目标餐台还没结账
            if (isOrderNotPay(targetOrder)) {
                //封装返回给PAD的数据----只能放在第一行，因为订单关系改变后，不能获取到数据
                packResultMap(targetTableNo, targetTable, targetOrderId, targetOrder, resultmap);
                // 封装订单主表需要合并的数据
                packMergeMap(targetOrder, mergeMap);
                // 更新会员价
                updateVipPrice(sourceOrderId, sourceOrder, targetOrderId, targetOrder);
                // 更新餐台表--多次并台后会更新多条记录，不能省略
                if (tableDao.updateByOrderNo(sourceOrderId, targetOrderId) < 1) {
                    logger.error("更新餐台订单号失败");
                    throw new Exception("更新餐台订单号失败");
                }
                // 更新订单详细 表关系
                torderDetailMapper.updateOrderDetailForMergeTable(sourceOrderId, targetOrderId);
                // 更新退菜表的关系
                torderDetailMapper.updateOrderDetailDiscard(sourceOrderId, targetOrderId);
                //TODO
                updatePrintTableRelation(sourceOrder, sourceTable, sourceOrderId, targetOrderId);
                // 通知目标餐台的PAD，必须在删除订单主表数据前调用
                notifyService.notifyClearTable(mergeTable.getMeid(), targetTableNo);
                // 删除订单主表
                if (torderMapper.deleteByPrimaryKey(targetOrderId) < 1) {
                    logger.error("删除目标餐台账单失败");
                    throw new Exception("删除目标餐台账单失败");
                }

            }
            //更新餐台的订单号
            /**
             * 该方法存在一个问题
             * 开台ABC三个餐台，现在用A并C餐台成功后，再用B并C餐台后A餐台的订单号不会更新
             */
            //updateOrdernoForTable(sourceOrderId, targetTableId, false);
            /**
             * 监视人蔡华宇
             */
            updateOrdernoForOrderid(sourceOrderId, targetOrderId, false);

            //判断是否需要释放餐台
            if (isCleanTarget) {
                cleanTargetTable(targetTableId);
            }
        } else {
            //更新餐台的订单号并占用餐台
            updateOrdernoForTable(sourceOrderId, targetTableId, true);
        }
        //更新源账单并台信息
        if (torderMapper.updateOrderForMergeTable(mergeMap) < 1) {
            logger.error("合并账单失败");
            throw new Exception("合并账单失败");
        }

        updatePrintObj(sourceOrderId);

        //记录并台日志
        if (!toperationLogService.save(toperationLog)) {
            logger.error("向t_operation_log表记录并台日志失败");
            throw new Exception("记录并台日志失败");
        }
        //打印合并小票
        printMergeTableTicket(mergeTable, sourceOrderId, sourceOrder);

        return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(resultmap));

    }

    private void updatePrintObj(String orderid) {
        if (!StringUtils.hasText(orderid))
            return;
        Torder torder = torderMapper.get(orderid);
        Map<String, Object> param = new HashMap<>();
        param.put("targetOrderid", orderid);
        param.put("custnum", torder.getCustnum());
        //TODO
        tbPrintObjDao.updateByOrderno(param);
    }

    /**
     * 更新餐台的订单号
     *
     * @param sourceOrderId
     * @param targetOrderId
     * @param b
     */
    private void updateOrdernoForOrderid(String sourceOrderId, String targetOrderId, boolean markUsing) {
        Map<String, Object> map = new HashMap<>();
        map.put("oldorderid", targetOrderId);
        map.put("orderid", sourceOrderId);
        if (markUsing) {
            map.put("status", 1);
        }
        tableDao.updateTableByOrderId(map);
    }

    /**
     * 更新打印表(t_printobj,t_printdish)的关系
     *
     * @param sourceOrder
     * @param sourceTable
     * @param sourceOrderId
     * @param targetOrderId
     * @throws Exception
     */
    private void updatePrintTableRelation(Map<String, Object> sourceOrder, Map<String, Object> sourceTable, String sourceOrderId, String targetOrderId) throws Exception {
        //查询源餐台的打印主表是否有记录
        Map<String, Object> params = new HashMap<>();
        params.put("orderno", sourceOrderId);
        boolean sourceHasDish = tbPrintObjDao.find(params) != null;
        //查询目标餐台的打印主表是否有记录
        params.put("orderno", targetOrderId);
        boolean targetHasDish = tbPrintObjDao.find(params) != null;
        //原餐台和目标餐台都点过菜
        if (sourceHasDish && targetHasDish) {
            tbPrintObjDao.updatePrintdishForMerge(sourceOrderId, targetOrderId);
            //删除打印主表的数据,防止后续开台的订单号一样，打印单数据错乱
            Map<String, Object> map2 = new HashMap<>();
            map2.put("orderno", targetOrderId);
            tbPrintObjDao.deletePrintObj(map2);
        } else if (!sourceHasDish && targetHasDish) {
            User userByjobNum = userDao.getUserByjobNum((String) sourceOrder.get("userid"));

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("sourceOrderid", sourceOrderId);
            paramMap.put("targetOrderid", targetOrderId);
            paramMap.put("username", userByjobNum.getName());
            paramMap.put("tableid", sourceTable.get("tableid"));
            paramMap.put("tableno", "台号: " + sourceTable.get("tableNo"));
            paramMap.put("tableArea", sourceTable.get("areaname"));
            if (tbPrintObjDao.updateByOrderno(paramMap) < 1) {
                throw new Exception("并台时更新t_printobj表的关系失败");
            }
        }
    }

    /**
     * 更新餐台的订单号
     *
     * @param sourceOrderId
     * @param targetTableId
     * @param markUsing
     */
    private void updateOrdernoForTable(String sourceOrderId, String targetTableId, boolean markUsing) {
        Map<String, Object> map = new HashMap<>();
        map.put("tableid", targetTableId);
        map.put("orderid", sourceOrderId);
        if (markUsing) {
            map.put("status", 1);
        }
        tableDao.updateTableById(map);
    }

    /**
     * 清空目标餐台
     *
     * @param targetTableId
     * @throws Exception
     */
    private void cleanTargetTable(String targetTableId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("tableid", targetTableId);
        map.put("status", 0);
        if (tableDao.updateTableById(map) < 1) {
            logger.error("释放目标餐台失败");
            throw new Exception("释放目标餐台失败");
        }
    }

    /**
     * 封装订单主表合并的数据
     *
     * @param targetOrder
     * @param mergeMap
     */
    private void packMergeMap(Map<String, Object> targetOrder, Map<String, Object> mergeMap) {
        mergeMap.put("targetCustnum", targetOrder.get("custnum") == null ? 0 : targetOrder.get("custnum"));
        mergeMap.put("targetWomannum", targetOrder.get("womanNum") == null ? 0 : targetOrder.get("womanNum"));
        mergeMap.put("targetChildnum", targetOrder.get("childNum") == null ? 0 : targetOrder.get("childNum"));
        mergeMap.put("targetMannum", targetOrder.get("manNum") == null ? 0 : targetOrder.get("manNum"));
        mergeMap.put("targetAgeperiod", targetOrder.get("ageperiod") == null ? 0 : targetOrder.get("ageperiod"));
        mergeMap.put("targetMealsNum", targetOrder.get("numOfMeals") == null ? 0 : targetOrder.get("numOfMeals"));
        //只要有一个台的餐具是否，并台后则为收费
        mergeMap.put("targetIsfree", (targetOrder.get("isFree") != null && (int) targetOrder.get("isFree") == 0) ? "0" : "1");
    }

    /**
     * 更新菜品的价格为会员价
     *
     * @param sourceOrderId
     * @param sourceOrder
     * @param targetOrderId
     * @param targetOrder
     * @throws Exception
     */
    private void updateVipPrice(String sourceOrderId, Map<String, Object> sourceOrder, String targetOrderId,
                                Map<String, Object> targetOrder) throws Exception {
        String sourceMemberno = (String) sourceOrder.get("memberno");
        String targetMemberno = (String) targetOrder.get("memberno");
        boolean sourceIsVip = sourceMemberno != null && !sourceMemberno.isEmpty();
        boolean targetIsVip = targetMemberno != null && !targetMemberno.isEmpty();
        if (sourceIsVip && !targetIsVip) {//源桌台登录了会员
            torderMapper.updateVipPrice(targetOrderId);
        } else if (!sourceIsVip && targetIsVip) {//目标桌台登录了会员
            torderMapper.updateVipPrice(sourceOrderId);
            if (torderMapper.updateMemberno(sourceOrderId, targetMemberno) < 1) {
                throw new Exception("更新订单的会员号失败");
            }
        }
    }

    /**
     * 封裝数据返回给源PAD
     *
     * @param targetTableNo
     * @param targetTable
     * @param targetOrderId
     * @param targetOrder
     * @param resultmap
     */
    private void packResultMap(String targetTableNo, Map<String, Object> targetTable, String targetOrderId,
                               Map<String, Object> targetOrder, Map<String, Object> resultmap) {
        Map<String, Object> mappa = new HashMap<String, Object>();
        mappa.put("orderid", targetOrderId);
        TbDataDictionary dd = datadictionaryService.findById("backpsd");
        TbDataDictionary vipaddress = datadictionaryService.findById("vipaddress");
        TbDataDictionary locktime = datadictionaryService.findById("locktime");
        TbDataDictionary delaytime = datadictionaryService.findById("delaytime");
        /*resultmap.put("flag", "1");
		resultmap.put("desc", "获取数据成功");*/
        resultmap.put("currenttableid", targetTableNo);
        resultmap.put("orderid", targetTable.get("orderid"));
        resultmap.put("memberno", targetOrder.get("memberno"));
        resultmap.put("manNum", targetOrder.get("manNum"));
        resultmap.put("womanNum", targetOrder.get("womanNum"));
        resultmap.put("waiterNum", targetOrder.get("userid"));
        resultmap.put("ageperiod", targetOrder.get("ageperiod"));
        resultmap.put("begintime", targetOrder.get("begintime"));
//		resultmap.put("result", "0");
        resultmap.put("orderid", targetOrderId);
        resultmap.put("backpsd", dd == null ? "" : dd.getItemid());// 退菜密码
        resultmap.put("vipaddress", vipaddress == null ? "" : vipaddress.getItemid()); // 雅座的VIP地址
        resultmap.put("locktime", locktime == null ? "" : locktime.getItemid()); // 屏保锁屏时间
        resultmap.put("delaytime", delaytime == null ? "" : delaytime.getItemid()); // 屏保停留时间
        resultmap.put("rows", orderServiceImpl.getMapData(targetOrderId));
    }

    /**
     * 判断账单是否已结账
     *
     * @param targetOrder
     * @return true:未结账，false:已结账或已取消
     */
    private boolean isOrderNotPay(Map<String, Object> targetOrder) {
        return targetOrder != null && targetOrder.size() != 0 && "0".equals(String.valueOf(targetOrder.get("orderstatus")));
    }

    /**
     * 判断餐台是否被占用
     *
     * @param targetTable
     * @return
     */
    private boolean isTableUsing(Map<String, Object> targetTable) {
        return targetTable.get("status") != null && "1".equals(String.valueOf(targetTable.get("status")));
    }

    /**
     * 打印并台小票
     *
     * @param mergeTable
     * @param sourceOrderId
     * @param sourceOrder
     */
    private void printMergeTableTicket(Table mergeTable, String sourceOrderId, Map<String, Object> sourceOrder) {
        String userId = (String) sourceOrder.get("userid");
        User mapUser = userService.getUserByjobNum(userId);
        User disUser = userService.getUserByjobNum(mergeTable.getDiscardUserId());
        PrintObj pj = new PrintObj();
        pj.setUserName(mapUser.getName());
        pj.setBillName("并台单");
        pj.setAbbrbillName("并");
        pj.setOrderNo(sourceOrderId);
        pj.setTimeMsg(DateUtils.dateToString(new Date()));
        pj.setWelcomeMsg(mergeTable.getTableNo());// 换到的台
        pj.setTableNo(mergeTable.getOrignalTableNo());
        pj.setDiscardUserId(disUser.getName());

        printTableChangeBill(pj, "6");
    }


    /**
     * 通知目标餐台的PAD
     *
     * @param orderMap
     */
    private void notifyTargetPad(Map<String, Object> orderMap) {
        // 订单不为空，调用推送接口，清空目标pad的数据
        if (orderMap.get("meid") != null) {
            StringBuffer str = new StringBuffer(Constant.TS_URL);
            str.append(Constant.MessageType.msg_1005 + "/" + String.valueOf(orderMap.get("meid")));
            new Thread(new TsThread(str.toString())).run();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String mergetable(Table mergeTable, ToperationLog toperationLog) {
        //1.设定当前桌 为占用
        //2.设定关联桌号为 占用
        //3.把相关菜品订单关联到主桌
        //4.订单状态为关联结算
        String orignalTableno = mergeTable.getOrignalTableNo();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableNo", orignalTableno);
        List<Map<String, Object>> resultMap = tableDao.find(map);
        if (resultMap == null || resultMap.size() == 0 || resultMap.size() > 1) {
            return Constant.FAILUREMSG;
        }

        Map<String, Object> map2 = torderMapper.findOne(String.valueOf(resultMap.get(0).get("orderid")));
        if (map2 == null || map2.size() == 0 || "3".equals(String.valueOf(map2.get("orderstatus")))) {
            return Constant.FAILUROPEMSG;
        }

//		String status = String.valueOf(resultMap.get(0).get("status"));
//		String orignalTableId = String.valueOf(resultMap.get(0).get("tableid"));

        String tableNo = mergeTable.getTableNo();
        String[] tables = tableNo.split(",");
        List<String> listTableNos = Arrays.asList(tables);
        List<Map<String, Object>> listTables = tableDao.findIds(listTableNos);
        if (listTables == null || listTables.size() == 0) {
            return Constant.FAILUREMSG;
        }
        //如果目标桌和原始桌的订单号相同，直接返回并台成功
        if (String.valueOf(resultMap.get(0).get("orderid")).equals(listTables.get(0).get("orderid"))) {
            return Constant.SUCCESSMSG;
        }
        //---------------------------------------------------------
        //并桌的时候查询目标桌是否开台，是否下单了    下单了，并桌的时候把下单的数据返回给pad。并桌只能并一个桌
        Map<String, Object> tablemap = listTables.get(0);
        Map<String, Object> resultmap = new HashMap<String, Object>();
        resultmap.put("result", "0");
        if (tablemap.get("status") != null && "1".equals(String.valueOf(tablemap.get("status").toString()))) {
            Map<String, Object> orderMap = torderMapper.findOne(String.valueOf(tablemap.get("orderid")));
            if (orderMap != null && orderMap.size() != 0 && "0".equals(String.valueOf(orderMap.get("orderstatus")))) {
                resultmap.put("flag", "1");
                resultmap.put("desc", "获取数据成功");
                resultmap.put("currenttableid", tablemap.get("tableNo"));
                resultmap.put("orderid", tablemap.get("orderid"));
                resultmap.put("memberno", orderMap.get("memberno"));
                resultmap.put("manNum", orderMap.get("manNum"));
                resultmap.put("womanNum", orderMap.get("womanNum"));
                resultmap.put("waiterNum", orderMap.get("userid"));
                resultmap.put("ageperiod", orderMap.get("ageperiod"));
                resultmap.put("begintime", orderMap.get("begintime"));
                Map<String, Object> mappa = new HashMap<String, Object>();
                mappa.put("orderid", tablemap.get("orderid"));
                TbDataDictionary dd = datadictionaryService.findById("backpsd");
                TbDataDictionary vipaddress = datadictionaryService.findById("vipaddress");
                TbDataDictionary locktime = datadictionaryService.findById("locktime");
                TbDataDictionary delaytime = datadictionaryService.findById("delaytime");
                resultmap.put("result", "0");
                resultmap.put("orderid", tablemap.get("orderid"));
                resultmap.put("backpsd", dd == null ? "" : dd.getItemid());//退菜密码
                resultmap.put("vipaddress", vipaddress == null ? "" : vipaddress.getItemid()); //雅座的VIP地址
                resultmap.put("locktime", locktime == null ? "" : locktime.getItemid()); //屏保锁屏时间
                resultmap.put("delaytime", delaytime == null ? "" : delaytime.getItemid()); //屏保停留时间
                resultmap.put("rows", orderServiceImpl.getMapData(String.valueOf(tablemap.get("orderid"))));

                //订单不为空，调用推送接口，清空目标pad的数据
//				orderMap.get("meid")
                if (orderMap.get("meid") != null) {
                    StringBuffer str = new StringBuffer(Constant.TS_URL);
                    str.append(Constant.MessageType.msg_1005 + "/" + String.valueOf(orderMap.get("meid")));
                    new Thread(new TsThread(str.toString())).run();
                }
                toperationLogService.deleteToperationLogByTableNo(String.valueOf(tablemap.get("tableNo")));
            }
        }


        //---------------------------------------------------------

        //执行存储过程
        String result = "1";
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("orginalTableNo", mergeTable.getOrignalTableNo());
        mapParam.put("targetTableNo", mergeTable.getTableNo());
        mapParam.put("result", result);
        torderMapper.selectMergeTable(mapParam);
        result = String.valueOf(mapParam.get("result"));
        if (!"0".equals(result)) {
            return Constant.FAILUREMSG;
        } else {
            if (toperationLogService.save(toperationLog)) {
                String userId = (String) map2.get("userid");
                User mapUser = userService.getUserByjobNum(userId);
                User disUser = userService.getUserByjobNum(mergeTable.getDiscardUserId());
                PrintObj pj = new PrintObj();
                pj.setUserName(mapUser.getName());
                pj.setBillName("并台单");
                pj.setAbbrbillName("并");
                pj.setOrderNo(String.valueOf(resultMap.get(0).get("orderid")));
                pj.setTimeMsg(DateUtils.dateToString(new Date()));
                pj.setWelcomeMsg(mergeTable.getTableNo());//换到的台
                pj.setTableNo(mergeTable.getOrignalTableNo());
                pj.setDiscardUserId(disUser.getName());

                printTableChangeBill(pj, "6");
                return JacksonJsonMapper.objectToJson(resultmap);
            } else {
                return Constant.FAILUREMSG;
            }
        }
    }
    //推送的线程
//	public class TsThread extends Thread{
//		   String  str ;
//		   TsThread(String  str){
//			   this.str = str;
//		   }
//		   @Override
//		   public void run(){
//			   //根据动作打印不同的小票
//				URL urlobj;
//				try {
//				urlobj = new URL(str);
//				URLConnection	urlconn = urlobj.openConnection();
//				urlconn.connect();
//				InputStream myin = urlconn.getInputStream();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
//				String content = reader.readLine();
//				JSONObject object = JSONObject.fromObject(content.trim());
//				@SuppressWarnings("unchecked")
//				List<Map<String,Object>> resultList = (List<Map<String, Object>>) object.get("result");
//				if("1".equals(String.valueOf(resultList.get(0).get("Data")))){
//					logger.info("清空pad推送成功:" + str);
//				}else{
//					logger.info("清空pad推送失败:" + str);
//				}
//				} catch (IOException e) {
//					logger.error("-->",e);
//					e.printStackTrace();
//				}
//		   }
//	   }

    public String accountcash(AccountCash accountCash) {
        //判断当前用户是不是经理

        // 1. 换班，换班是要根据值班经理的开业时间来确定到目前为止的营业款项
        //2.清机问题  清机是清算当天开业也来得所有的营业收入
        //1.1 查询今天经理开业的时间
        //1.2 查询当日的营业额，
        //1.3 现金营业额，银行卡营业额，会员营业额 ，优惠票营业额
        //2.1 清机和换班的区别是 清机要更新订单的状态为已经清机 （入账）

        //1 查询 t_worklog 表查询当天的开业日期，条件是经理+开业存在
        //2 计算开业当天有多少营业额
        //3.插入每天入账表

        Map<String, Object> param = new HashMap();
        param.put("account", accountCash.getUserName());
        User user = userService.getUserByAccount(accountCash.getUserName());
        if (user == null || !user.getUserType().equals(Constants.SUPER_ADMIN)) {
            return Constant.FAILUREMSG;
        }

//		  Date loginTime = null;
//		  Tsettlement settlement = settlementMapper.getTotalAmount(loginTime);
        //添加日志-----------------------------------------------------
        Tworklog tworklog = new Tworklog();
        tworklog.setWorkid(UUID.randomUUID().toString());
        List<Map<String, Object>> list = datadictionaryService.getDatasByType("WORKTYPE");
        tworklog.setUserid(accountCash.getUserName());
        tworklog.setBegintime(new Date());
        tworklog.setEndtime(new Date());
        tworklog.setIpaddress("127.0.0.1");
        tworklog.setStatus(1);
        //-----------------------------------------------------------
        if ("0".equals(accountCash.getActionType())) {
            //换班
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("itemDesc").equals("换班")) {
                    tworklog.setWorktype(list.get(i).get("itemid").toString());
                }
                ;
            }
        } else if ("1".equals(accountCash.getActionType())) {
            //清机
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("itemDesc").equals("清机")) {
                    tworklog.setWorktype(list.get(i).get("itemid").toString());
                }
                ;
            }
        }
        workLogService.saveLog(tworklog);
        return Constant.SUCCESSMSG;
    }

    @Override
    public TbTable findTableNoAndAreaNameById(String tableId) {
        return tableDao.findTableNoAndAreaNameById(tableId);
    }

    @Override
    public int updateCleanStatus(TbTable tbTable) {
        return tableDao.updateCleanStatus(tbTable);
    }

    @Override
    public int updateSettleStatus(TbTable tbTable) {
        return tableDao.updateSettleStatus(tbTable);
    }

    @Override
    public int updateSettleOrderNull(TbTable tbTable) {
        return tableDao.updateSettleOrderNull(tbTable);
    }

    @Override
    public List<Map<String, Object>> findDetail(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return tableDao.findDetail(params);
    }

    @Override
    public List<Map<String, Object>> getTableTag2() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TbTable> getTablesByTableType(String areaid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("areaid", areaid);
        return tableDao.getTablesByTableType(params);
    }

    @Override
    public boolean deleteTablesByAreaid(String areaid) {
        return tableDao.deleteTablesByAreaid(areaid) > 0;
    }

    @Override
    public TbTable findByOrder(Map<String, Object> map) {
        // TODO Auto-generated method stub
        return tableDao.findByOrder(map);
    }

    @Override
    public TbTable findTableByOrder(String orderid) {
        return tableDao.findTableByOrder(orderid);
    }

    @Override
    public long getMenuInfoByCount(Map<String, Object> params) {
        return tableDao.getMenuInfoByCount(params);
    }

    @Override
    public String generatePrintObjId() {
        return tableDao.generatePrintObjId();
    }

    @Override
    public Map<String, Object> getByOrderId(String orderId) {
        return tableDao.getByOrderId(orderId);
    }
}

