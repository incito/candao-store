package com.candao.www.webroom.service.impl;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.TbPrinterDetail;
import com.candao.print.entity.TbPrinterManager;
import com.candao.print.service.*;
import com.candao.www.constant.Constant;
import com.candao.www.constant.Constant.TABLETYPE;
import com.candao.www.data.dao.*;
import com.candao.www.data.model.*;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.permit.service.UserService;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.model.Order;
import com.candao.www.webroom.model.Table;
import com.candao.www.webroom.model.UrgeDish;
import com.candao.www.webroom.service.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;


@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    //	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5000));
    private Log log = LogFactory.getLog(OrderDetailServiceImpl.class.getName());

    @Autowired
    private OrderOpService orderOpService;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String updateorderprice(Order orders) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tableNo", orders.getCurrenttableid());
        List<Map<String, Object>> tableList = tableService.find(params);
        if (tableList != null && tableList.size() > 0) {
            orders.setOrderid(String.valueOf(tableList.get(0).get("orderid")));
        } else {
            return Constant.FAILUREMSG;
        }
        //从传过来的数据中，获取订单详情的所有信息
        List<TorderDetail> listall = getallTorderDetail(orders);
        Map<String, Object> mapStatus = torderMapper.findOne(orders.getOrderid());
        if (!"0".equals(String.valueOf(mapStatus.get("orderstatus")))) {
            return Constant.FAILUREMSG;
        }
        for (TorderDetail torderDetail : listall) {
            torderDetailMapper.updateOrderDetail(torderDetail);
        }

        return Constant.SUCCESSMSG;
    }

    /**
     * 清台接口
     * 包含咖啡模式清台和正常模式清台
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String cleantableSimply(Table table) {
        String tableNo = table.getTableNo();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableNo", tableNo);
        List<Map<String, Object>> resultMapList = tableService.find(map);
        if (resultMapList == null || resultMapList.size() == 0) {
            log.error("-->resultMapList为空(查询table为空)，参数tableNo为：" + tableNo);
            return Constant.FAILUREMSG;
        }
        Map<String, Object> tableMap = resultMapList.get(0);
        String tableId = String.valueOf(tableMap.get("tableid"));
        String orderid = String.valueOf(tableMap.get("orderid"));

        if (StringUtils.isEmpty(orderid)) {
            log.error("-->订单为空(查询orderid为空)，参数tableNo为：" + tableNo);
            return Constant.FAILUREMSG;
        }

        Map<String, Object> order = orderService.findOrderById(orderid);
        if (!MapUtils.isEmpty(order)) {
            String orderstatus = String.valueOf(order.get("orderstatus"));
            orderstatus = orderstatus == null ? "" : orderstatus.trim();
            // 正常模式清台
            switch (orderstatus) {
                case "0": {
                    // 统一判断，验证数据一直性，保证PAD数据与数据库数据的菜单信息一直，通过反查询数据库判断---by liangdong
                    // 2016-5-30
                    // 计算菜单订单下面财大个数如果大与0 表示不能 清台
                    long menuNum = tableService.getMenuInfoByCount(resultMapList.get(0));
                    if (menuNum > 0) {
                        return Constant.UPDATE_MENU;
                    }

                    TbTable tbTable = new TbTable();
                    tbTable.setTableid(tableId);
                    tbTable.setStatus(0);
                    tbTable.setOrderid(orderid);
                    tableService.updateSettleStatus(tbTable);

                    Torder torder = new Torder();
                    torder.setOrderid(String.valueOf(tableMap.get("orderid")));
                    torder.setOrderstatus(2);
                    torder.setEndtime(new Date());
                    torderMapper.update(torder);
                    break;
                }
                case "2": {
                    // 异常结业
                    TbTable tbTable = new TbTable();
                    tbTable.setTableid(tableId);
                    tbTable.setStatus(0);
                    tbTable.setOrderid(orderid);
                    tableService.updateSettleStatus(tbTable);
                    break;
                }
                case "3": {
                    // 咖啡模式清台
                    TbTable tbTable = new TbTable();
                    tbTable.setTableid(tableId);
                    tbTable.setStatus(0);
                    tbTable.setOrderid(orderid);
                    tableService.updateSettleStatus(tbTable);
                    break;
                }
                default:
                    return Constant.FAILUREMSG;
            }
        } else {
            return Constant.FAILUREMSG;
        }

        return Constant.SUCCESSMSG;
    }

    /**
     * 清桌 ，
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String cleantable(Table table) {
        String tableNo = table.getTableNo();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableNo", tableNo);
        List<Map<String, Object>> resultMapList = tableService.find(map);
        if (resultMapList == null || resultMapList.size() == 0) {
            log.error("-->resultMapList为空(查询table为空)，参数tableNo为：" + tableNo);
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap());
        }
        // 统一判断，验证数据一直性，保证PAD数据与数据库数据的菜单信息一直，通过反查询数据库判断---by liangdong 2016-5-30
        //计算菜单订单下面财大个数如果大与0 表示不能 清台
        long menuNum = tableService.getMenuInfoByCount(resultMapList.get(0));
        if (menuNum > 0) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("订单下还有菜品，不能清台"));
        }
        Map<String, Object> tableMap = resultMapList.get(0);
        String tableId = String.valueOf(tableMap.get("tableid"));

        TbTable tbTable = new TbTable();
        tbTable.setTableid(tableId);
        tbTable.setStatus(0);
        tbTable.setOrderid(String.valueOf(tableMap.get("orderid") == null ? "" : tableMap.get("orderid")));
        tableService.updateCleanStatus(tbTable);

        if (tableMap.get("orderid") != null) {
            Torder torder = new Torder();
            torder.setOrderid(String.valueOf(tableMap.get("orderid")));
            torder.setOrderstatus(2);
            torder.setEndtime(new Date());
            torderMapper.update(torder);
        }
        return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap("清台成功"));
    }

    @Override
    public List<TorderDetail> find(Map<String, String> mapDetail) {
        return torderDetailMapper.find(mapDetail);
    }

    @Override
    public TorderDetail findOne(Map<String, String> mapDetail) {
        return torderDetailMapper.findOne(mapDetail);
    }

    @Override
    public List<Map<String, String>> findTemp(Map<String, String> mapDetail) {
        return torderDetailMapper.findTemp(mapDetail);
    }

    //从传过来的数据中，获取订单详情的所有信息

    /**
     * 1 获取所有的订单详情信息   2删除个数为0的菜品
     *
     * @param order
     * @return dishtype
     * 单品 0
     * 组合    组合里面的鱼和锅 1
     * 套餐 2     套餐里面单品4    组合中的鱼和锅3
     */
    public List<TorderDetail> getallTorderDetail(Order order) {
        List<TorderDetail> orderDetails = order.getRows();
        List<TorderDetail> listall = new ArrayList<TorderDetail>();
        if (null == orderDetails || orderDetails.isEmpty()) {
            return listall;
        }
        List<TorderDetail> primaryKeys = torderDetailMapper.getByPrimarykey(orderDetails);
        for (TorderDetail t : orderDetails) {
            /*******处理网络差的情况下，下单出现多个相同的Primarykey导致退菜失败的情况*********/
            boolean isDuplicate = isDuplicate(primaryKeys, t);
            if (isDuplicate) {
                continue;
            }
            /**********end************/
            //忌口、全单备注、口味、赠菜人、赠菜授权人、赠菜原因合并
            StringBuilder sperequire = new StringBuilder();
            sperequire.append(t.getSperequire());
            sperequire.append(Constant.ORDER_REMARK_SEPARATOR);
            sperequire.append(order.getGlobalsperequire());
            sperequire.append(Constant.ORDER_REMARK_SEPARATOR);
            sperequire.append(t.getTaste());
            sperequire.append(Constant.ORDER_REMARK_SEPARATOR);
            sperequire.append(t.getFreeuser());
            sperequire.append(Constant.ORDER_REMARK_SEPARATOR);
            sperequire.append(t.getFreeauthorize());
            sperequire.append(Constant.ORDER_REMARK_SEPARATOR);
            sperequire.append(t.getFreereason());
            t.setSperequire(sperequire.toString());

            t.setOrderid(order.getOrderid());

            if ("0".equals(t.getDishtype())) {
                if (!"0".equals(t.getDishnum())) {
                    t.setOrdertype(0);
                    t.setPrimarykey(t.getPrimarykey());
                    t.setParentkey(t.getPrimarykey());
                    t.setSuperkey(t.getPrimarykey());
                    t.setIsmaster(1);
                    listall.add(t);
                }
            } else if ("1".equals(t.getDishtype())) {
                t.setRelatedishid(t.getDishid());

                t.setParentkey(t.getPrimarykey());
                t.setSuperkey(t.getPrimarykey());
                t.setOrderprice(null);

                t.setOrdertype(1);
                t.setIsmaster(1);
                listall.add(t);

                List<TorderDetail> list1 = t.getDishes();
                for (TorderDetail t1 : list1) {
                    if (!"0".equals(t1.getDishnum())) {
                        t1.setDishtype("1");
                        t1.setRelatedishid(t.getDishid());

                        t1.setOrdertype(1);
                        t1.setPrimarykey(t1.getPrimarykey());
                        t1.setParentkey(t.getPrimarykey());
                        t1.setSuperkey(t.getPrimarykey());
                        listall.add(t1);
                    }
                }

            } else if ("2".equals(t.getDishtype())) {
                List<TorderDetail> list2 = t.getDishes();
                if (!"0".equals(t.getDishnum())) {
                    t.setDishtype("2");
                    t.setOrdertype(2);
                    t.setChilddishtype(2);
                    t.setParentkey(t.getPrimarykey());
                    t.setSuperkey(t.getPrimarykey());
                    t.setIsmaster(1);
                    listall.add(t);
                    for (TorderDetail t2 : list2) {
                        if ("0".equals(t2.getDishtype())) {
                            if (!"0".equals(t2.getDishnum())) {
                                t2.setOrderprice(new BigDecimal(0));
                                t2.setDishtype("2");
                                t2.setRelatedishid(t.getDishid());
//		    	    			 t2.setPrinttype(t.getPrinttype());

                                t2.setOrdertype(2);
                                t2.setPrimarykey(t2.getPrimarykey());
                                t2.setParentkey(t.getPrimarykey());
                                t2.setSuperkey(t.getPrimarykey());
                                t2.setChilddishtype(0);
                                t2.setIsmaster(0);

                                listall.add(t2);
                            }
                        } else if ("1".equals(t2.getDishtype())) {

                            t2.setRelatedishid(t.getDishid());
                            t2.setOrdertype(2);
                            t2.setIsmaster(1);
                            t2.setOrderprice(null);
                            t2.setDishtype("2");
                            t2.setChilddishtype(1);

                            t2.setParentkey(t.getPrimarykey());
                            t2.setSuperkey(t.getPrimarykey());

                            listall.add(t2);

                            List<TorderDetail> list3 = t2.getDishes();
                            for (TorderDetail t3 : list3) {
                                if (!"0".equals(t3.getDishnum())) {
                                    t3.setDishtype("2");
                                    t3.setOrderprice(new BigDecimal(0));
                                    t3.setRelatedishid(t.getDishid());
//		    	    				 t3.setDishtype("2");
                                    t3.setOrdertype(0);
                                    t3.setChilddishtype(1);
                                    t3.setPrimarykey(t3.getPrimarykey());
                                    t3.setParentkey(t2.getPrimarykey());
                                    t3.setSuperkey(t.getPrimarykey());
                                    t3.setIsmaster(0);
                                    listall.add(t3);
                                }
                            }

                        }
                    }
                }
            }
        }

        return listall;
    }

    private boolean isDuplicate(List<TorderDetail> primaryKeys, TorderDetail t) {
        if (null != primaryKeys && !primaryKeys.isEmpty()) {
            for (TorderDetail primaryKey : primaryKeys) {
                if (t.getPrimarykey().equals(primaryKey.getPrimarykey())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 下单service
     */
    @Override
// @Transactional( propagation=Propagation.REQUIRED,rollbackFor=java.net.ConnectException.class)
    public Map<String, Object> setOrderDetailList(Order orders) {
        TransactionStatus status = null;
        try {
//		object = status.createSavepoint();
            String tableNo = orders.getCurrenttableid();
            TbTable table = tableService.findByTableNo(tableNo);
            orders.setCurrenttableid(table.getTableid());
            if (table != null) {
                orders.setOrderid(table.getOrderid());
            } else {
                log.error("-->t_table表中该table为空，tableNo为：" + tableNo);
                return ReturnMap.getFailureMap("查询不到该餐台");
            }

            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def); //获得事务状态
            //锁定订单行
            Torder orderLock = torderMapper.lock(table.getOrderid());
            //从传过来的数据中，获取订单详情的所有信息
            List<TorderDetail> listall = getallTorderDetail(orders);
            //		判断是否重复下单
            if (listall.isEmpty()) {
                transactionManager.rollback(status);
                if (null != orders.getRows() && !orders.getRows().isEmpty()) {
                    log.info("-->重复下单");
//                    return getResult("0", "下单成功", "");
                    return ReturnMap.getSuccessMap("下单成功");
                }
                log.error("-->OrderDetail为空,orders.getRows()值为：" + orders.getRows());
//                return getResult("3", "订单中没有菜品", "");
                return ReturnMap.getFailureMap("订单中没有菜品");
            }
            if (Constant.ORDERSTATUS.ORDER_STATUS != orderLock.getOrderstatus()) {
                log.error("-->orderId为：" + orders.getOrderid());
                transactionManager.rollback(status);
//                return getResult("3", "查询不到该订单", "");
                return ReturnMap.getFailureMap("查询不到该订单");
            }
            orders.setUserid(orderLock.getUserid());
            Map<String, Object> mapParam1 = new HashMap<String, Object>();
            mapParam1.put("orderid", orders.getOrderid());
            TorderDetail detailList = torderDetailMapper.findOne(mapParam1);

            Map<String, String> result = order(orders, listall);

            String code = result.get("code");
            if (!"0".equals(code)) {
                log.error("-->result为：" + 1);
                transactionManager.rollback(status);
//                return getResult(code, result.get("msg"), "");
                return ReturnMap.getFailureMap(result.get("msg"));
            }
            int flag = null == detailList ? 0 : 1;
            printOrderList(orders.getOrderid(), table.getTableid(), flag);
            printweigth(listall, orders.getOrderid());
            transactionManager.commit(status);
            log.info(orders.getOrderid() + "下单成功");
//            return getResult("0", "下单成功", "");
            return ReturnMap.getSuccessMap("下单成功");
        } catch (Exception ex) {
            log.error("-->", ex);
            ex.printStackTrace();
            if (null != status) {
                transactionManager.rollback(status);
            }
//            return getResult("3", "服务器异常 ", "");
            return ReturnMap.getFailureMap("服务器异常");
        }

    }

    /**
     * 下单
     *
     * @param order
     * @return
     */
    private Map<String, String> order(Order order, List<TorderDetail> orderDetails) {
        Map<String, String> result = new HashMap<>();
        //获取printobjId
        Map printParam = new HashMap();
        printParam.put("orderno", order.getOrderid());
        PrintObj printObj = tbPrintObjDao.find(printParam);
        String printobjId;
        if (null == printObj) {
            printobjId = tableService.generatePrintObjId();
        } else {
            printobjId = printObj.getId();
        }

//        检查菜谱是否存在
        Tmenu tmenu = menuDao.checkMenu();
        if (null == tmenu) {
            result.put("code", "1");
            result.put("msg", "找不到可用的菜谱");
            return result;
        }

        Map<String, Object> tableInfo = tableDao.getTableNoById(order.getCurrenttableid());
        String tableNo = StringUtils.isEmpty(tableInfo.get("tableno")) ? "" : tableInfo.get("tableno").toString();
        tableNo = "桌号: " + tableNo;
        List<String> dishIds = getDishIds(orderDetails);
        List<Tdish> dishs = dishService.findAllByIds(dishIds);
        ;
        Map<String, Object> dishUnitParam = new HashMap<>();
        dishUnitParam.put("menuId", tmenu.getMenuid());
        dishUnitParam.put("dishIds", dishIds);
        List<TtemplateDishUnit> dishUnits = templateDishUnitlDao.findAllByDishIds(dishUnitParam);
        List<PrintDish> printDishs = new ArrayList<>();
        for (TorderDetail orderDetail : orderDetails) {
            Tdish existsDish = isExistsDish(dishs, orderDetail.getDishid());
            if (null == existsDish) {
                result.put("code", "1");
                result.put("msg", "部分菜品找不到");
                return result;
            }
            //生成PrintDish
            PrintDish printDish = new PrintDish();

            TtemplateDishUnit dishUnit = getTtemplateDishUnit(dishUnits, orderDetail.getDishid());
            if (null == dishUnit) {
                printDish.setDishName(existsDish.getTitle());
            } else {
                printDish.setDishName(null == dishUnit.getDishname() ? "" : dishUnit.getDishname());
            }
            if (null != orderDetail.getDishtype() &&
                    (orderDetail.getDishtype().equals(String.valueOf(Constant.PRINTTYPE.DISCARD_DISH)) || orderDetail.getDishtype().equals(String.valueOf(Constant.PRINTTYPE.COOKIE_DISH))) &&
                    !orderDetail.getParentkey().equals(orderDetail.getPrimarykey())) {
                printDish.setDishName("-" + printDish.getDishName());
            }
            printDish.setPrintdishid("printdishid");
            printDish.setPrintobjid(printobjId);
            printDish.setDishNum(orderDetail.getDishnum());
            printDish.setDishPrice(orderDetail.getOrderprice());
            printDish.setTotalAmount(new BigDecimal(0));
            printDish.setPayAmount(new BigDecimal(0));
            printDish.setSperequire(orderDetail.getSperequire());
            printDish.setTableNomsg(tableNo);
            printDish.setDishUnit(orderDetail.getDishunit());
            printDish.setPrintport(orderDetail.getPricetype());
            printDish.setPrintnum(0);
            printDish.setDishId(orderDetail.getDishid());
            String printtype = orderDetail.getPrinttype();
            printDish.setPrinttype(StringUtils.isEmpty(printtype) ? "2" : printtype);
            printDish.setRelatedishid(orderDetail.getRelatedishid());
            printDish.setDishtype(Integer.valueOf(orderDetail.getDishtype()));
            printDish.setOrdertype(orderDetail.getOrdertype());
            printDish.setParentkey(orderDetail.getParentkey());
            printDish.setSuperkey(orderDetail.getSuperkey());
            printDish.setIsmaster(orderDetail.getIsmaster());
            printDish.setPrimarykey(orderDetail.getPrimarykey());
            if (StringUtils.isEmpty(printtype)) {
                printDish.setIslatecooke(0);
            } else {
                printDish.setIslatecooke(Integer.valueOf(printtype));
            }
            printDish.setIsadddish(orderDetail.getIsadddish());
            printDish.setChilddishtype(orderDetail.getChilddishtype());
            printDish.setIspot(orderDetail.getIspot());

            printDishs.add(printDish);

            //设置默认值
            if (null == orderDetail.getDiscountamount()) {
                orderDetail.setDiscountamount(0);
            }
            if (StringUtils.isEmpty(orderDetail.getStatus())) {
                orderDetail.setStatus("0");
            }
            orderDetail.setDisuserid(orderDetail.getUserName());
        }
        tbPrintObjDao.insertPrintDishBatch(printDishs);
        torderDetailMapper.insertOnce(orderDetails);
        //下单后立即计算应收金额
        orderOpService.calcOrderAmount(order.getOrderid());

        if (null == printObj) {
            //更新桌台状态
            TbTable table = new TbTable();
            table.setTableid(order.getCurrenttableid());
            table.setStatus(Constant.TABLESTATUS.EAT_STATUS);
            table.setOrderid(order.getOrderid());
            tableDao.updateStatus(table);

            Torder torder = new Torder();
            torder.setOrderid(order.getOrderid());
            torder.setOrderstatus(Constant.ORDERSTATUS.ORDER_STATUS);
            torderMapper.update(torder);

//            插入printobj
            printObj = new PrintObj();
            printObj.setId(printobjId);
            printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
            printObj.setOrderNo(order.getOrderid());
            printObj.setTableNo(tableNo);
            printObj.setTimeMsg(DateUtils.dateToString(new Date()));
            String userName = userDao.getNameByUserNumber(order.getUserid(), Constant.BRANCH.BRANCH_ID);
            printObj.setUserName(userName);
            String areaName = StringUtils.isEmpty(tableInfo.get("areaname")) ? "" : tableInfo.get("areaname").toString();
            printObj.setTableArea(areaName);
            printObj.setTableid(order.getCurrenttableid());
            tbPrintObjDao.insertPrintObj(printObj);
        } else {
            tbPrintObjDao.updateTimemsg(DateUtils.dateToString(new Date()), printobjId);
        }
        result.put("code", "0");
        result.put("msg", "下单成功");
        return result;
    }

    /**
     * 获取多单位
     *
     * @param dishUnits
     * @param dishId
     * @return
     */
    private TtemplateDishUnit getTtemplateDishUnit(List<TtemplateDishUnit> dishUnits, String dishId) {
        if (null != dishUnits) {
            for (TtemplateDishUnit tdu : dishUnits) {
                if (tdu.getDishid().equals(dishId)) {
                    return tdu;
                }
            }
        }
        return null;
    }

    private List<String> getDishIds(List<TorderDetail> orderDetails) {
        List<String> dishIds = new ArrayList<>();
        for (TorderDetail orderDetail : orderDetails) {
            dishIds.add(orderDetail.getDishid());
        }
        return dishIds;
    }

    private Tdish isExistsDish(List<Tdish> dishs, String dishId) {
        for (Tdish dish : dishs) {
            if (dish.getDishid().equals(dishId)) {
                return dish;
            }
        }
        return null;
    }

    /**
     * 咖啡模式下单
     * 1 下单不打单 2不操作餐台(外卖，咖啡外卖)
     */
    @Override
    public Map<String, Object> placeOrder(Order orders) {
        TransactionStatus status = null;
        try {
            if (StringUtils.isEmpty(orders.getOrderid())) {
                log.info("-----------------------");
                log.info("下单失败，参数失败，没有订单id");
                return ReturnMap.getFailureMap("参数错误，没有订单id");
            }

            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def); // 获得事务状态
            //锁定订单行
            Torder orderLock = torderMapper.lock(orders.getOrderid());
            // 从传过来的数据中，获取订单详情的所有信息
            List<TorderDetail> listall = getallTorderDetail(orders);
            //		判断是否重复下单
            if (listall.isEmpty()) {
                transactionManager.rollback(status);
                if (null != orders.getRows() && !orders.getRows().isEmpty()) {
                    log.info("-->重复下单");
                    return ReturnMap.getSuccessMap("下单成功");
                }
                log.error("-->OrderDetail为空,orders.getRows()值为：" + orders.getRows());
                return ReturnMap.getFailureMap("订单中没有菜品");
            }
            //通过餐台判断订单状态
            String tableNo = orders.getCurrenttableid();
            TbTable table = tableService.findByTableNo(tableNo);
            orders.setCurrenttableid(table.getTableid());
            if (table == null) {
                log.error("-->t_table表中该table为空，tableNo为：" + tableNo);
                transactionManager.rollback(status);
                return ReturnMap.getFailureMap("查询不到该餐台");
            }
            if (table.getTabletype() == null) {
                log.error("-->t_table表中该tabletype为空，tableNo为：" + tableNo);
                transactionManager.rollback(status);
                return ReturnMap.getFailureMap("查询不到该餐台");
            }

            if (Constant.ORDERSTATUS.ORDER_STATUS != orderLock.getOrderstatus()) {
                log.error("-->orderId为：" + orders.getOrderid());
                transactionManager.rollback(status);
                return ReturnMap.getFailureMap("查询不到该订单");
            }
            if (!Constant.TABLETYPE.COFFEETABLE.equals(table.getTabletype()) && !TABLETYPE.TAKEOUT.equals(table.getTabletype()) && !TABLETYPE.TAKEOUT_COFFEE.equals(table.getTabletype())) {
                log.error("-->orderId为：" + orders.getOrderid());
                transactionManager.rollback(status);
                return ReturnMap.getFailureMap("查询不到该订单");
            }
            orders.setUserid(orderLock.getUserid());


            Map<String, String> result = order(orders, listall);
            String code = result.get("code");
            if (!"0".equals(code)) {
                log.error("-->result为：" + 1);
                transactionManager.rollback(status);
                return ReturnMap.getFailureMap(result.get("msg"), orders.getOrderid());
            }
            transactionManager.commit(status);
            log.info(orders.getOrderid() + "下单成功");
            return ReturnMap.getSuccessMap("下单成功", orders.getOrderid());
        } catch (Exception ex) {
            log.error("-->", ex);
            ex.printStackTrace();
            if (null != status) {
                transactionManager.rollback(status);
            }
            return ReturnMap.getFailureMap("服务器异常");
        }

    }

    /**
     * 打印订单中的需要称重的数据，打印称重单
     *
     * @author shen
     * @date:2015年6月11日下午8:43:51
     * @Description: TODO
     */
    public void printweigth(List<TorderDetail> list, String orderid) {
        Map<String, Object> printMap = new HashMap<String, Object>();
        printMap.put("orderno", orderid);
        PrintObj printObj = tbPrintObjDao.find(printMap);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "1");
        map.put("printertype", "5");
        //称重单不需要分区域


        List<TbPrinterManager> printerList = tbPrinterManagerDao.findPrintByType(map);
        if (list != null && list.size() > 0) {
            for (TorderDetail orderDetail : list) {
                if ("1".equals(orderDetail.getDishstatus())) {
                    for (TbPrinterManager printer : printerList) {
                        PrintObj object = new PrintObj();

                        object.setCustomerPrinterIp(printer.getIpaddress());
                        object.setBillName("称重单");
                        object.setCustomerPrinterPort(printer.getPort());
                        object.setOrderNo(orderid);
                        object.setTimeMsg(DateUtils.dateToString(new Date()));
                        object.setUserName(printObj.getUserName());
                        object.setTableArea(printObj.getTableArea());
                        object.setTableNo(printObj.getTableNo());
                        object.setPrinterid(printer.getPrinterid());
                        object.setPrintName(printObj.getPrintName());

                        List<PrintDish> printDishList = new ArrayList<>();

                        Map<String, Object> fishMap = new HashMap<String, Object>();
                        fishMap.put("printobjid", printObj.getId());
                        fishMap.put("primarykey", orderDetail.getPrimarykey());
                        List<PrintDish> dishList = tbPrintObjDao.findDish(fishMap);
                        for (PrintDish p : dishList) {
                            PrintDish printDish1 = new PrintDish();
                            printDish1.setDishName(p.getDishName());
                            printDish1.setDishNum(p.getDishNum());
                            printDish1.setDishUnit(p.getDishUnit());
//								printDish1.setSperequire((String)params.get("dishnum"));
                            printDish1.setAbbrname("待称重");
                            printDish1.setOrderseq(p.getOrderseq());
                            printDishList.add(printDish1);
                        }
                        object.setList(printDishList);
                        new Thread(new WeigthThread(object)).run();
//							executor.execute(new WeigthThread(object));
                    }
                }
            }
        }
    }

    /**
     * 单独打印客用单
     *
     * @param orderid
     * @param isRepeat 是否标记为重印客用单
     */
    public void printCust(String orderid, boolean isRepeat) throws Exception {
        Assert.hasText(orderid);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderno", orderid);
        PrintObj printObj = tbPrintObjDao.find(map);

        if (isRepeat) {
            printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.POSCUSTDISHNAME);
        } else {
            printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.NORMALCUSTDISHNAME);
        }
        printCustDish(printObj,isRepeat);
    }

    /**
     * flag 0 初次下菜单 1 加菜单
     *
     * @param orderId
     * @param tableId
     * @param flag
     * @return
     * @author tom_zhao
     */
    private void printOrderList(String orderId, String tableId, int flag) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderno", orderId);
        PrintObj printObj = tbPrintObjDao.find(map);

        //判断是厨打单还是加菜单
        if (flag == 1) {
            printObj.setPrintType(Constant.PRINTTYPE.ADD_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.CUSTADDDISHNAME);
            printObj.setAbbrbillName(Constant.DISHBILLNAME.ADDDISHNAME_ABBR);
        } else if (flag == 0) {
            printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.NORMALDISHNAME);
        } else if (flag == 3) {
            printObj.setPrintType(Constant.PRINTTYPE.DISCARD_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.DISCARDDISHNAME);
            printObj.setAbbrbillName(Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR);
        }
//			else  if(flag == 4){
//		    	 printObj.setPrintType(Constant.PRINTTYPE.COOKIE_DISH);
//		    	 printObj.setBillName(Constant.DISHBILLNAME.READYNAME);
//		    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.READY_ABBR);
//			}
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("PrintType", printObj.getPrintType());
        paramsMap.put("billName", printObj.getBillName());
        if (printObj.getAbbrbillName() != null && !"".equals(printObj.getAbbrbillName())) {
            paramsMap.put("AbbrbillName", printObj.getAbbrbillName());
        }
//		     int PrintType=printObj.getPrintType();
//			  String billName=printObj.getBillName();
//			  String AbbrbillName=printObj.getAbbrbillName();

        //打印单品
        printSingleDish(printObj, paramsMap);
        // 打印锅和鱼
//		     printObj.setPrintType(PrintType);
//	    	 printObj.setBillName(billName);
//	    	 printObj.setAbbrbillName(AbbrbillName);
        printFishAndHot(printObj, paramsMap);
        //打印套餐
//			 printObj.setPrintType(PrintType);
//	    	 printObj.setBillName(billName);
//	    	 printObj.setAbbrbillName(AbbrbillName);
        printDishSet(printObj, paramsMap);

//		     added by caicai 2016-02-19  套餐小票(传菜员专用)
        printDishSetIndividually(printObj, paramsMap);

        if (flag == 1) {
            printObj.setPrintType(Constant.PRINTTYPE.ADD_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.ADDDISHNAME);
            printObj.setAbbrbillName(Constant.DISHBILLNAME.ADDDISHNAME_ABBR);
        } else if (flag == 0) {
            printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.NORMALCUSTDISHNAME);
        } else if (flag == 3) {
            printObj.setPrintType(Constant.PRINTTYPE.DISCARD_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.DISCARDDISHNAME);
            printObj.setAbbrbillName(Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR);
        }
//			else  if(flag == 4){
//				 printObj.setPrintType(Constant.PRINTTYPE.ADD_DISH);
//		    	 printObj.setBillName(Constant.DISHBILLNAME.ADDDISHNAME);
//		    	 printObj.setAbbrbillName(Constant.DISHBILLNAME.ADDDISHNAME_ABBR);
//			}

        printCustDish(printObj);

    }

    /**
     * 打印单品
     *
     * @param printObj
     * @author tom_zhao
     */

    private void printSingleDish(PrintObj printObj, Map<String, Object> paramsMap) {
        //打印还没有打印的单品
        Map<String, Object> map0 = new HashMap<String, Object>();
        map0.put("printobjid", printObj.getId());
        map0.put("dishtype", "0");
        map0.put("printnum", "0");
//			  map0.put("islatecooke",0 );
        printSingleDish(map0, printObj, 0, paramsMap);
    }


    /**
     * 打印套餐
     *
     * @param printObj
     * @author tom_zhao
     */
    private void printDishSet(PrintObj printObj, Map<String, Object> paramsMap) {
        Map<String, Object> map0;
        //打印套餐   套餐中的单品
        map0 = new HashMap<String, Object>();
        map0.put("printobjid", printObj.getId());
        map0.put("dishtype", "2");
        map0.put("printnum", "0");
//				  map0.put("islatecooke",0 );
        map0.put("childdishtype", "0");
        printSingleDish(map0, printObj, 0, paramsMap);

        //打印套餐   套餐中的火锅
        map0 = new HashMap<String, Object>();
        map0.put("printobjid", printObj.getId());
        map0.put("dishtype", "2");
        map0.put("printnum", "0");
//				  map0.put("islatecooke",0 );
        map0.put("childdishtype", "1");
        map0.put("ismaster", "1");
        if ("(备菜)".equals(printObj.getAbbrbillName())) {
            map0.put("islatecooke", 1);
        }
        printMutilDish(map0, printObj, 0, paramsMap);

//				  map0 = new HashMap<String, Object>();
//				  map0.put("printobjid", printObj.getId());
//				  map0.put("printnum", 0);
//				  printCustDish(map0,printObj);

    }

    /**
     * 打印传菜单
     *
     * @param printObj
     */
    private void printDishSetIndividually(PrintObj printObj, Map<String, Object> paramsMap) {
        Map<String, Object> map0 = new HashMap<String, Object>();

        map0.put("printobjid", printObj.getId());
        // dishtype 0 单品 1 鱼锅 2 套餐
        map0.put("dishtype", "2");
        map0.put("printnum", "0");
        // childdishtype 0 单品 1 鱼锅 2 套餐
        map0.put("childdishtype", "2");
        map0.put("ismaster", "1");
        // 查询所有套餐
        List<PrintDish> listPrint = tbPrintObjDao.findDishGroupBySuperKey(map0);
        if (listPrint != null && !listPrint.isEmpty()) {
            // 保持不变
            printObj.setPrintType(Constant.PRINTTYPE.NORMAL_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.DISHSETNAME);

            for (PrintDish pd : listPrint) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("printobjid", printObj.getId());
                params.put("superkey", pd.getSuperkey());
                params.put("dishtype", "2");
                List<PrintDish> dishsetlists = tbPrintObjDao.findDish(params);
                // 去零
                for (PrintDish it : dishsetlists) {
                    formatDishNum(it);
                    try {
                        it.initData();
                    } catch (Exception e) {
                        log.error("------------------菜品解析失败！-------------");
                        log.error("菜品忌口信息解析失败！ :" + it.getDishName(), e);
                        e.printStackTrace();
                    }
                }

                printObj.setList(dishsetlists);

                Map<String, Object> paramMap = new HashMap<String, Object>();
                // 查询火锅打印机
                paramMap.put("status", "1");
                paramMap.put("printertype", "1");
                paramMap.put("tableid", printObj.getTableid());

                // 需要把所有的菜品配置的打印机全部打印
                paramMap.put("dishid", pd.getDishId());
                List<String> IPList = new ArrayList<String>();
                List<TbPrinterManager> printers = tbPrinterManagerDao.findDishPrinter(paramMap);
                TbPrinterManager tbPrinter = new TbPrinterManager();

                if (printers != null && printers.size() > 0) {
                    tbPrinter = printers.get(0);
                } else {
                    log.info("该桌套餐未配备打印机----- 桌号id:" + printObj.getTableid());
                }

                if (!"(退)".equals(printObj.getAbbrbillName())) {
                    int printNum = (tbPrinter.getPrintNum() == null ? 0 : tbPrinter.getPrintNum()) + 1;
                    tbPrinter.setPrintNum(printNum);
                    int flag = tbPrinterManagerDao.update(tbPrinter);
                    if (flag <= 0) {
                        System.out.println("printnum更新失败！");
                        log.info("printnum更新失败！" + printObj.getId() + "打印机:" + tbPrinter.getPrintername());
                    } else {
                        printObj.setOrderseq(printNum);
                    }
                } else {
                    printObj.setOrderseq(pd.getOrderseq());
                }

                if (printers != null) {
                    for (TbPrinterManager pm : printers) {
                        if (IPList != null) {
                            if (IPList.contains(tbPrinter.getIpaddress())) {
                                continue;
                            }
                            IPList.add(tbPrinter.getIpaddress());
                        }
                        printObj.setCustomerPrinterIp(pm.getIpaddress());
                        printObj.setCustomerPrinterPort(pm.getPort());
                        printObj.setPrinterid(pm.getPrinterid());

                        new Thread(new PrintDishSetThread(printObj)).run();
                    }
                }

            }
        }
    }

    /**
     * 打印锅和鱼
     *
     * @param printObj
     * @author tom_zhao
     */
    private void printFishAndHot(PrintObj printObj, Map<String, Object> paramsMap) {
        //打印火锅
        Map<String, Object> map0 = new HashMap<String, Object>();
        map0.put("printobjid", printObj.getId());
        map0.put("dishtype", "1");
        map0.put("printnum", "0");
//		  map0.put("islatecooke",0 );
        map0.put("ismaster", "1");
        printMutilDish(map0, printObj, 0, paramsMap);
    }
    
    private void printCustDish(PrintObj printObj ,boolean flag) {

        Map<String, Object> map0 = new HashMap<String, Object>();
        map0.put("printobjid", printObj.getId());
        if (!flag) {
        	map0.put("printnum", 0);			
		}
        map0.put("dishtype", 0);
        List<PrintDish> listall = new ArrayList<>();
        //单品
        List<PrintDish> sigleList = tbPrintObjDao.findDishBycolumn(map0);
        //餐具
        PrintDish dishes = null;
        if (sigleList != null && sigleList.size() > 0) {
            for (PrintDish printDish : sigleList) {
                //客用单不打印餐具
                if (!"DISHES_98".equals(printDish.getDishId())) {
                    listall.add(printDish);
                } else {
                    dishes = printDish;
                }
            }
        }
        //组合菜
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("printobjid", printObj.getId());
        if (!flag) {
        	map0.put("printnum", 0);			
		}
        map1.put("dishtype", 2);
        // 查询所有套餐
        List<PrintDish> temp = tbPrintObjDao.findDishGroupBySuperKey(map1);
        for (PrintDish pd : temp) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("printobjid", printObj.getId());
            params.put("superkey", pd.getSuperkey());
            params.put("dishtype", "2");
            List<PrintDish> dishsetlists = tbPrintObjDao.findDish(params);
            listall.addAll(dishsetlists);
        }
        //鱼锅
        map1.put("dishtype", 1);
        List<PrintDish> temp1 = tbPrintObjDao.findDishGroupBySuperKey(map1);
        for (PrintDish pd : temp1) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("printobjid", printObj.getId());
            params.put("superkey", pd.getSuperkey());
            params.put("dishtype", "1");
            List<PrintDish> dishsetlists = tbPrintObjDao.findDish(params);
            listall.addAll(dishsetlists);
        }
        if (dishes != null) {
            listall.add(dishes);
        }

        if (listall != null && !listall.isEmpty()) {
            for (PrintDish it : listall) {
                try {
                    it.initData();
                } catch (Exception e) {
                    log.error("------------------菜品解析失败！-------------");
                    log.error("菜品忌口信息解析失败！ :" + it.getDishName(), e);
                    e.printStackTrace();
                }
            }
        }

        printObj.setList(listall);
        //得到区域
        //1. 厨打单
        //2. 客用单
        //3.预结单
        //4. 结账单

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", "1");
        paramMap.put("printertype", "2");
        paramMap.put("tableid", printObj.getTableid());

        List<TbPrinterManager> tps = tbPrinterManagerDao.findNoDishPrinter(paramMap);
        //该餐厅是否设置为不打印
        int result = tbPrintObjDao.findPrintTable(paramMap);
        if (result == 0) {//该餐厅不打印
            listall = null;
        }
        if (listall != null && listall.size() > 0) {
            for (TbPrinterManager tPrinterManager : tps) {
                printObj.setCustomerPrinterIp(tPrinterManager.getIpaddress());
                printObj.setCustomerPrinterPort(tPrinterManager.getPort());
                //added by caicai
                printObj.setPrintName(tPrinterManager.getPrintername());
                printObj.setPrinterid(tPrinterManager.getPrinterid());

                new Thread(new PrintCustThread(printObj)).run();
            }
        }
    }

    private void printCustDish(PrintObj printObj) {

        Map<String, Object> map0 = new HashMap<String, Object>();
        map0.put("printobjid", printObj.getId());
        map0.put("printnum", 0);
        map0.put("dishtype", 0);
        List<PrintDish> listall = new ArrayList<>();
        //单品
        List<PrintDish> sigleList = tbPrintObjDao.findDishBycolumn(map0);
        //餐具
        PrintDish dishes = null;
        if (sigleList != null && sigleList.size() > 0) {
            for (PrintDish printDish : sigleList) {
                //客用单不打印餐具
                if (!"DISHES_98".equals(printDish.getDishId())) {
                    listall.add(printDish);
                } else {
                    dishes = printDish;
                }
            }
        }
        //组合菜
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("printobjid", printObj.getId());
        map1.put("printnum", 0);
        map1.put("dishtype", 2);
        // 查询所有套餐
        List<PrintDish> temp = tbPrintObjDao.findDishGroupBySuperKey(map1);
        for (PrintDish pd : temp) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("printobjid", printObj.getId());
            params.put("superkey", pd.getSuperkey());
            params.put("dishtype", "2");
            List<PrintDish> dishsetlists = tbPrintObjDao.findDish(params);
            listall.addAll(dishsetlists);
        }
        //鱼锅
        map1.put("dishtype", 1);
        List<PrintDish> temp1 = tbPrintObjDao.findDishGroupBySuperKey(map1);
        for (PrintDish pd : temp1) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("printobjid", printObj.getId());
            params.put("superkey", pd.getSuperkey());
            params.put("dishtype", "1");
            List<PrintDish> dishsetlists = tbPrintObjDao.findDish(params);
            listall.addAll(dishsetlists);
        }
        if (dishes != null) {
            listall.add(dishes);
        }

        if (listall != null && !listall.isEmpty()) {
            for (PrintDish it : listall) {
                try {
                    it.initData();
                } catch (Exception e) {
                    log.error("------------------菜品解析失败！-------------");
                    log.error("菜品忌口信息解析失败！ :" + it.getDishName(), e);
                    e.printStackTrace();
                }
            }
        }

        printObj.setList(listall);
        //得到区域
        //1. 厨打单
        //2. 客用单
        //3.预结单
        //4. 结账单

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", "1");
        paramMap.put("printertype", "2");
        paramMap.put("tableid", printObj.getTableid());

        List<TbPrinterManager> tps = tbPrinterManagerDao.findNoDishPrinter(paramMap);
        //该餐厅是否设置为不打印
        int result = tbPrintObjDao.findPrintTable(paramMap);
        if (result == 0) {//该餐厅不打印
            listall = null;
        }
        if (listall != null && listall.size() > 0) {
            for (TbPrinterManager tPrinterManager : tps) {
                printObj.setCustomerPrinterIp(tPrinterManager.getIpaddress());
                printObj.setCustomerPrinterPort(tPrinterManager.getPort());
                //added by caicai
                printObj.setPrintName(tPrinterManager.getPrintername());
                printObj.setPrinterid(tPrinterManager.getPrinterid());

                new Thread(new PrintCustThread(printObj)).run();
//				  executor.execute(new PrintCustThread(printObj));
            }
        }

        map0.clear();
        map0.put("printobjid", printObj.getId());
        map0.put("printnum", 0);
        tbPrintObjDao.updateDishCall(map0);
    }

    LoggerHelper logger = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    private void printSingleDish(Map<String, Object> map0, PrintObj printObj, int refundDish, Map<String, Object> paramsMap) {
        List<PrintDish> listPrint = tbPrintObjDao.findDish(map0);

        if (listPrint != null && !listPrint.isEmpty()) {
            for (PrintDish it : listPrint) {
                try {
                    it.initData();
                } catch (Exception e) {
                    log.error("------------------菜品解析失败！-------------");
                    log.error("菜品忌口信息解析失败！ :" + it.getDishName(), e);
                    e.printStackTrace();
                }
            }
        }

        Collections.sort(listPrint);
        printObj.setList(listPrint);
        logger.error("------------------------", "");
        logger.error("封装数据开始，订单号：" + printObj.getOrderNo() + "*菜品数量：" + listPrint.size(), "");
        // 得到区域
        // 1. 厨打单
        // 2. 客用单
        // 3.预结单
        // 4. 结账单
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", "1");
        paramMap.put("printertype", "1");
        paramMap.put("tableid", printObj.getTableid());
        // 需要把所有的菜品配置的打印机全部打印
        // 查找菜品所有符合的打印机
        List<PrintDish> printedList = new ArrayList<>();
        Map<String, Integer> printedmap = new HashMap<>();
        for (PrintDish pd : printObj.getList()) {
            /*if (printedList.contains(pd)) {//已经合并打印了则跳过
                logger.error("------------------------", "");
                logger.error("组合打印后忽略单品，订单号：" + printObj.getOrderNo() + "*菜品名称：" + pd.getDishName(), "");
                continue;
            }*/
            List<String> IPList = new ArrayList<String>();
            formatDishNum(pd);
            //查询菜品所属套餐,不包括鱼锅
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("printobjid", pd.getPrintobjid());
            map1.put("dishtype", "2");
            map1.put("printnum", "0");
            map1.put("childdishtype", "2");
            map1.put("ismaster", "1");
            map1.put("primarykey", pd.getSuperkey());
            List<PrintDish> superdishes = tbPrintObjDao.findDishGroupBySuperKey(map1);
            //记录单品所属的套餐名
            if (superdishes != null && superdishes.size() == 1) {
                pd.setParentDishName(superdishes.get(0).getDishName());
            }
            if (refundDish != 1) {
                if (pd.getIslatecooke() == 1) {
                    printObj.setPrintType(Constant.PRINTTYPE.COOKIE_DISH);
                    printObj.setBillName(Constant.DISHBILLNAME.READYNAME);
                    printObj.setAbbrbillName(Constant.DISHBILLNAME.READY_ABBR);
                } else if (paramsMap != null) {
                    printObj.setPrintType(Integer.valueOf(String.valueOf(paramsMap.get("PrintType"))));
                    printObj.setBillName(String.valueOf(paramsMap.get("billName")));
                    if (paramsMap.get("AbbrbillName") != null) {
                        printObj.setAbbrbillName(String.valueOf(paramsMap.get("AbbrbillName")));
                    } else {
                        printObj.setAbbrbillName("");
                    }
                }
            }
            pd.setAbbrname(printObj.getAbbrbillName());
            if (("2".equals(pd.getDishtype()) && pd.getPrimarykey().equals(pd.getParentkey())
                    && pd.getPrimarykey().equals(pd.getSuperkey())) || pd.getDishId().equals("DISHES_98")) {
                continue;
            }

            // if(map0.get("discardNum")!=null ){
            // if(map0.get("discardNum")!=null&&!"".equals(String.valueOf(map0.get("discardNum")))&&String.valueOf(map0.get("discardNum")).endsWith(".0")){
            // String discardNum=String.valueOf(map0.get("discardNum"));
            // pd.setDishNum(discardNum.substring(0,
            // discardNum.lastIndexOf(".")));
            // }
            // pd.setDishNum("-"+pd.getDishNum());
            // }
            if (map0.get("discardNum") != null && !"".equals(String.valueOf(map0.get("discardNum")))) {
                String discardNum = String.valueOf(map0.get("discardNum"));
                if (String.valueOf(map0.get("discardNum")).endsWith(".0")) {
                    discardNum = discardNum.substring(0, discardNum.lastIndexOf("."));
                }
                pd.setDishNum("-" + discardNum);
            }
            if (map0.get("discardNum") == null && refundDish == 1) {
                if (pd.getDishNum() != null && !"".equals(pd.getDishNum()) && pd.getDishNum().endsWith(".0")) {
                    String discardNum = pd.getDishNum();
                    pd.setDishNum(discardNum.substring(0, discardNum.lastIndexOf(".")));
                }
                pd.setDishNum("-" + pd.getDishNum());
            }
            if (map0.get("discardReason") != null && refundDish == 1) {
                pd.setGlobalsperequire(String.valueOf(map0.get("discardReason")));
            }
            paramMap.put("dishid", pd.getDishId());

            List<TbPrinterManager> printers = tbPrinterManagerDao.findDishPrinter(paramMap);
            if (printers != null) {
                for (TbPrinterManager tbPrinter : printers) {
                    if (IPList != null) {
                        if (IPList.contains(tbPrinter.getIpaddress())) {
                            continue;
                        }
                        IPList.add(tbPrinter.getIpaddress());
                    }

                    if (!"(退)".equals(printObj.getAbbrbillName())) {
                        int printNum = (tbPrinter.getPrintNum() == null ? 0 : tbPrinter.getPrintNum()) + 1;
                        tbPrinter.setPrintNum(printNum);
                        int flagB = tbPrinterManagerDao.update(tbPrinter);
                        if (flagB <= 0) {
                            System.out.println("printnum更新失败！");
                        } else {
                            updateDishPrintNum(printNum, pd.getPrimarykey());
                            printObj.setOrderseq(printNum);
                        }

                    } else {
                        printObj.setOrderseq(pd.getOrderseq());
                    }

                    // 判断是否合并打印
                    boolean needMerge = false;
                    List<PrintDish> pdList = new ArrayList<>();
                    //退菜不合并打印;稍后上菜不合并打印;送礼的菜不合并打印;
                    boolean isRefund = Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR.equals(printObj.getAbbrbillName());
                    boolean gift = isGiftDish(pd);
                    if (!isRefund && pd.getIslatecooke() != 1 && !gift) {
                        String groupSequence = getDishGroupSequence(pd, tbPrinter);
                        if (groupSequence != null) {
                            List<TbPrinterDetail> findPrintDetail = getSameGroupDishList(tbPrinter, groupSequence);
                            logger.error("------------------------", "");
                            logger.error("进入组合打印的逻辑，订单号：" + printObj.getOrderNo() + "*组合数量：" + findPrintDetail.size(), "");
                            // 有两个及以上的菜才需要合并
                            //modified by caicai
                            if (findPrintDetail.size() > 1) {
                                int i = 0;
                                for (TbPrinterDetail tbPrinterDetail : findPrintDetail) {
                                    for (PrintDish printDish : printObj.getList()) {
                                        if (printDish.getDishId().equals(tbPrinterDetail.getDishid())) {
                                            gift = isGiftDish(printDish);
                                            //退菜情况不会进入
                                            if (printDish.getIslatecooke() != 1 && !gift) {
                                                //buffer
                                                pdList.add(printDish);
                                                i++;
                                            }
                                        }
                                    }
                                }
                                // 有两个以上才打印
                                if (i >= 2) {
                                    needMerge = true;
                                    for (PrintDish it : pdList) {
                                        // 加入已打印列表
                                        formatDishNum(it);//格式化菜品数量，不能省略
                                        printedList.add(it);
                                        //更新打印序号
                                        int printNum = printObj.getOrderseq();
                                        String primarykey = it.getPrimarykey();
                                        updateDishPrintNum(printNum, primarykey);
                                    }
                                } else {
                                    pdList.clear();
                                }
                            }
                        }
                    }
                    if (!needMerge) {
                        pdList.add(pd);
                    }

                    printObj.setCustomerPrinterIp(tbPrinter.getIpaddress());
                    printObj.setCustomerPrinterPort(tbPrinter.getPort());
                    printObj.setpDish(pdList);
                    //added by caicai
                    printObj.setPrintName(tbPrinter.getPrintername());
                    printObj.setPrinterid(tbPrinter.getPrinterid());

                    int temp = 0;//默认表示还没有打印
                    logger.error("------------------------,菜品数量" + pdList.size(), "");

                    for (PrintDish printDish : pdList) {
                        Object obj = printedmap.get(printObj.getCustomerPrinterIp() + printDish.getDishId()+printDish.getPrimarykey()+printDish.getOrderseq());
                        String abbrname=printDish.getAbbrname()==null?"":printDish.getAbbrname();
                        if (obj != null && !abbrname.contains("退")) {//退菜单除外
                            temp = 1;//已经打印过
                            break;
                        }
                        logger.error("封装数据结束，订单号：" + printObj.getOrderNo() + "*菜品名称：" + printDish.getDishName(), "");
                    }
                    if (temp == 1) {
                        continue;//已经打印过了
                    }
                    new Thread(new PrintThread(printObj)).run();

                    for (PrintDish printDish : pdList) {
                        printedmap.put(printObj.getCustomerPrinterIp() + printDish.getDishId()+printDish.getPrimarykey()+printDish.getOrderseq(), 1);//已经打印的菜品
                    }
                }
            }
            if (refundDish == 1) {
                Map<String, Object> printertypeMap = new HashMap<String, Object>();
                printertypeMap.put("printertype", 4);
                List<Map> findPrinterByType = tbPrinterManagerDao.find(printertypeMap);
                IPList.clear();
                if (printers != null && "(退)".equals(printObj.getAbbrbillName())) {
                    for (Map tbPrinter : findPrinterByType) {
                        if (IPList != null) {
                            if (IPList.contains(tbPrinter.get("ipaddress"))) {
                                continue;
                            }
                            IPList.add((String) tbPrinter.get("ipaddress"));
                        }
                        printObj.setOrderseq(pd.getOrderseq());
                        printObj.setCustomerPrinterIp((String) tbPrinter.get("ipaddress"));
                        printObj.setCustomerPrinterPort((String) tbPrinter.get("port"));
                        //added by caicai
                        printObj.setPrintName((String) tbPrinter.get("printername"));
                        printObj.setPrinterid((String) tbPrinter.get("printerid"));

                        List<PrintDish> list = new ArrayList<>();
                        list.add(pd);
                        printObj.setpDish(list);
                        new Thread(new PrintThread(printObj)).run();
                        // executor.execute(new PrintThread(printObj));
                    }
                }
            }
        }
        if (refundDish == 1) {
            printdishware(listPrint, printObj, map0);
        }
    }

    /**
     * 更新菜品的打印序号
     *
     * @param printNum
     * @param primarykey
     */
    private void updateDishPrintNum(int printNum, String primarykey) {
        PrintDish tempPrintDish = new PrintDish();
        tempPrintDish.setOrderseq(printNum);
        tempPrintDish.setPrimarykey(primarykey);
        tbPrintObjDao.updateDish(tempPrintDish);
    }

    /**
     * 判断菜品是否送礼
     *
     * @param pd
     * @return
     */
    private boolean isGiftDish(PrintDish pd) {
        String sperequire = pd.getSperequire();
        return !StringUtils.isEmpty(sperequire) && sperequire.contains("[");
    }

    /**
     * 格式化菜品数量，去掉小数点
     *
     * @param pd
     */
    private void formatDishNum(PrintDish pd) {
        String num = pd.getDishNum();
        if (!"".equals(num) && num.endsWith(".0")) {
            pd.setDishNum(num.substring(0, num.lastIndexOf(".")));
        }
    }

    /**
     * 获取与当前菜品同一分组的所有菜品
     *
     * @param tbPrinter
     * @param groupSequence
     * @return
     */
    private List<TbPrinterDetail> getSameGroupDishList(TbPrinterManager tbPrinter, String groupSequence) {
        Map<String, Object> sameGroupMap = new HashMap<>();
        sameGroupMap.put("printerid", tbPrinter.getPrinterid());
        sameGroupMap.put("groupsequence", groupSequence);
        List<TbPrinterDetail> findPrintDetail = tbPrinterManagerDao.findPrintDetail(sameGroupMap);
        // 去重，添加组合时同一个菜属于两个分类会被同时勾选
        Set<String> dishIds = new HashSet<>();
        for (Iterator<TbPrinterDetail> it = findPrintDetail.iterator(); it.hasNext(); ) {
            TbPrinterDetail tbPrinterDetail = it.next();
            if (dishIds.contains(tbPrinterDetail.getDishid())) {
                it.remove();
                continue;
            }
            dishIds.add(tbPrinterDetail.getDishid());
        }
        return findPrintDetail;
    }

    /**
     * 查询菜品分组的ID
     *
     * @param pd
     * @param tbPrinter
     * @return
     */
    private String getDishGroupSequence(PrintDish pd, TbPrinterManager tbPrinter) {
        Map<String, Object> detailMap = new HashMap<>();
        detailMap.put("printerid", tbPrinter.getPrinterid());
        detailMap.put("dishid", pd.getDishId());
        List<TbPrinterDetail> findPrintDetail = tbPrinterManagerDao.findPrintDetail(detailMap);
        String groupSequence = null;
        if (findPrintDetail != null && !findPrintDetail.isEmpty()) {
            groupSequence = findPrintDetail.get(0).getGroupSequence();
        }
        return groupSequence;
    }

    /**
     * 这个类主要解决打印退餐具
     *
     * @author shen
     * @date:2015年6月26日下午3:31:20
     * @Description: TODO
     */
    private void printdishware(List<PrintDish> listPrint, PrintObj printObj, Map<String, Object> map0) {
        printObj.setPrintType(Constant.PRINTTYPE.DISCARD_DISH);
        printObj.setBillName(Constant.DISHBILLNAME.DISCARDDISHNAME);
        printObj.setAbbrbillName(Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR);
        if (listPrint != null && listPrint.size() > 0) {
            List<String> IPList = new ArrayList<String>();
            for (PrintDish pdish : listPrint) {
                if (pdish.getDishId().equals("DISHES_98")) {
                    Map<String, Object> printertypeMap = new HashMap<String, Object>();
                    printertypeMap.put("printertype", 4);
                    List<Map<String, Object>> findPrinterByType = tbPrinterManagerDao.find(printertypeMap);
                    if (map0.get("discardNum") != null && !"".equals(String.valueOf(map0.get("discardNum")))) {
                        String discardNum = String.valueOf(map0.get("discardNum"));
                        if (String.valueOf(map0.get("discardNum")).endsWith(".0")) {
                            discardNum = discardNum.substring(0, discardNum.lastIndexOf("."));
                        }
                        pdish.setDishNum("-" + discardNum);
                    }
                    IPList.clear();
                    if (findPrinterByType != null && findPrinterByType.size() > 0) {
                        for (Map<String, Object> tbPrinter : findPrinterByType) {
                            if (IPList != null) {
                                if (IPList.contains(tbPrinter.get("ipaddress"))) {
                                    continue;
                                }
                                IPList.add((String) tbPrinter.get("ipaddress"));
                            }
                            printObj.setOrderseq(0);
                            printObj.setCustomerPrinterIp((String) tbPrinter.get("ipaddress"));
                            printObj.setCustomerPrinterPort((String) tbPrinter.get("port"));
                            //added by caicai
                            printObj.setPrintName((String) tbPrinter.get("printername"));
                            printObj.setPrinterid((String) tbPrinter.get("printerid"));

                            List<PrintDish> list = new ArrayList<>();
                            list.add(pdish);
                            printObj.setpDish(list);
                            new Thread(new PrintThread(printObj)).run();
//							  executor.execute(new PrintThread(printObj));
                        }
                    }
                }
            }
        }
    }

    private void printMutilDish(Map<String, Object> map0, PrintObj printObj, int flag, Map<String, Object> paramsMap) {
        List<PrintDish> listPrint = tbPrintObjDao.findDishGroupByParentKey(map0);

        if (listPrint != null && listPrint.size() != 0) {
            for (PrintDish pd : listPrint) {
                if (flag != 1) {
                    if (pd.getIslatecooke() == 1) {
                        printObj.setPrintType(Constant.PRINTTYPE.COOKIE_DISH);
                        printObj.setBillName(Constant.DISHBILLNAME.READYNAME);
                        printObj.setAbbrbillName(Constant.DISHBILLNAME.READY_ABBR);
                    } else if (paramsMap != null) {
                        printObj.setPrintType(Integer.valueOf(String.valueOf(paramsMap.get("PrintType"))));
                        printObj.setBillName(String.valueOf(paramsMap.get("billName")));
                        if (paramsMap.get("AbbrbillName") != null) {
                            printObj.setAbbrbillName(String.valueOf(paramsMap.get("AbbrbillName")));
                        }
                    }
                }
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("printobjid", printObj.getId());
                params.put("primarykey", pd.getPrimarykey());
                params.put("ismaster", "1");
                List<PrintDish> fishesList = tbPrintObjDao.findDish(params);
                params = new HashMap<String, Object>();
                params.put("printobjid", printObj.getId());
                params.put("parentkey", pd.getPrimarykey());
                params.put("ismaster", "0");
                List<PrintDish> fishandpotList = tbPrintObjDao.findDish(params);
                List<PrintDish> alllist = new ArrayList<PrintDish>();
                //直接addall有问题
                if (fishesList != null && fishesList.size() > 0) {
                    for (PrintDish p : fishesList) {
                        alllist.add(p);
                    }
                }
                if (fishandpotList != null && fishandpotList.size() > 0) {
                    for (PrintDish p : fishandpotList) {
                        alllist.add(p);
                    }
                }
                if (fishandpotList != null && fishandpotList.size() > 0) {
                    boolean flagfishpot = false;
                    for (PrintDish printDish : fishandpotList) {
                        if (printDish.getIspot() == 1) {//1代表锅底
                            flagfishpot = true;
                        }
                    }
                    if (flagfishpot) {
                        fishesList.addAll(fishandpotList);
                    } else {
                        fishesList = fishandpotList;
                    }
                }
                //查询菜品所属套餐,不包括鱼锅
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("printobjid", pd.getPrintobjid());
                map1.put("dishtype", "2");
                map1.put("printnum", "0");
                map1.put("childdishtype", "2");
                map1.put("ismaster", "1");
                map1.put("primarykey", pd.getSuperkey());
                List<PrintDish> superdishes = tbPrintObjDao.findDishGroupBySuperKey(map1);
                //设置套餐信息
                if (superdishes != null && superdishes.size() == 1) {
                    pd.setParentDishName(superdishes.get(0).getDishName());
                }

                for (PrintDish pf : fishesList) {
                    pf.setAbbrname(printObj.getAbbrbillName());
                    //added by caicai 增加套餐信息
                    pf.setParentDishName(pd.getParentDishName());
                }
                if (fishesList != null) {
                    if (map0.get("discardReason") != null && flag == 1) {
                        fishesList.get(0).setGlobalsperequire((String.valueOf(map0.get("discardReason"))));
                    }
                    if (flag == 1) {
                        for (PrintDish printDish : fishesList) {
                            if (printDish.getDishNum() != null && !"".equals(printDish.getDishNum())) {
                                String discardNum = printDish.getDishNum();
                                if (printDish.getDishNum().endsWith(".0")) {
                                    printDish.setDishNum(discardNum.substring(0, discardNum.lastIndexOf(".")));
                                }
                                printDish.setDishNum("-" + printDish.getDishNum());

                            }
                        }
                    }
                    printObj.setList(fishesList);
                    //初始化，解析忌口字段
                    if (!fishesList.isEmpty()) {
                        for (PrintDish it : fishesList) {
                            try {
                                it.initData();
                            } catch (Exception e) {
                                log.error("------------------菜品解析失败！-------------");
                                log.error("菜品忌口信息解析失败！ :" + it.getDishName(), e);
                                e.printStackTrace();
                            }
                        }
                    }

                    //查询火锅打印机
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("status", "1");
                    paramMap.put("printertype", "1");
                    paramMap.put("tableid", printObj.getTableid());

                    //需要把所有的菜品配置的打印机全部打印
                    paramMap.put("dishid", pd.getDishId());
                    List<String> IPList = new ArrayList<String>();
                    List<TbPrinterManager> printers = tbPrinterManagerDao.findDishPrinter(paramMap);
                    TbPrinterManager tbPrinter = new TbPrinterManager();
                    if (printers != null && printers.size() > 0) {
                        tbPrinter = printers.get(0);
                    } else {
                        System.out.println("----------------------------------------------------------------------");
                        System.out.println("--------------------------------该桌未配置打印机-----------------------------");
                        System.out.println("----------------------------------------------------------------------");
                    }
                    if (!"(退)".equals(printObj.getAbbrbillName())) {
                        int printNum = (tbPrinter.getPrintNum() == null ? 0 : tbPrinter.getPrintNum()) + 1;
                        tbPrinter.setPrintNum(printNum);
                        int flagB = tbPrinterManagerDao.update(tbPrinter);
                        if (flagB <= 0) {
                            System.out.println("printnum更新失败！");
                        } else {
                            for (PrintDish Pdish : alllist) {
                                PrintDish printDish = new PrintDish();
                                printDish.setOrderseq(printNum);
                                printDish.setPrimarykey(Pdish.getPrimarykey());
                                printDish.setIslatecooke(Pdish.getIslatecooke());
//										  if("(备菜)".equals(printObj.getAbbrbillName())){
//											  printDish.setIslatecooke(1);
//											  printObj.getList().get(0).setIslatecooke(1);
//										  }
                                int flagC = tbPrintObjDao.updateDish(printDish);
                            }

                            printObj.setOrderseq(printNum);

                        }

                    } else {

                        printObj.setOrderseq(pd.getOrderseq());
                    }
                    if (printers != null) {
                        for (TbPrinterManager pm : printers) {
                            if (IPList != null) {
                                if (IPList.contains(tbPrinter.getIpaddress())) {
                                    continue;
                                }
                                IPList.add(tbPrinter.getIpaddress());
                            }
                            printObj.setCustomerPrinterIp(pm.getIpaddress());
                            printObj.setCustomerPrinterPort(pm.getPort());
                            //added by caicai
                            printObj.setPrintName(pm.getPrintername());
                            printObj.setPrinterid(pm.getPrinterid());

                            new Thread(new PrintMutiThread(printObj)).run();

//									  executor.execute(new PrintMutiThread(printObj));
                        }
                    }
                    if (flag == 1) {
                        Map<String, Object> printertypeMap = new HashMap<String, Object>();
                        printertypeMap.put("printertype", 4);
                        List<Map<String, Object>> findPrinterByType = tbPrinterManagerDao.find(printertypeMap);
                        IPList.clear();
                        if (printers != null && "(退)".equals(printObj.getAbbrbillName()) && findPrinterByType != null) {
                            for (Map<String, Object> tbPrinterMap : findPrinterByType) {
                                if (IPList != null) {
                                    if (IPList.contains(tbPrinterMap.get("ipaddress"))) {
                                        continue;
                                    }
                                    IPList.add((String) tbPrinterMap.get("ipaddress"));
                                }
                                printObj.setOrderseq(pd.getOrderseq());
                                printObj.setCustomerPrinterIp((String) tbPrinterMap.get("ipaddress"));
                                printObj.setCustomerPrinterPort((String) tbPrinterMap.get("port"));
                                //added by caicai
                                printObj.setPrintName((String) tbPrinterMap.get("printername"));
                                printObj.setPrinterid((String) tbPrinterMap.get("printerid"));

                                new Thread(new PrintMutiThread(printObj)).run();
//										  executor.execute(new PrintMutiThread(printObj));
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * 判断是否是重复下单
     *
     * @param orderDetails
     * @return
     */
    private boolean isRepetitionOrder(List<TorderDetail> orderDetails) {
        //没有提交任何菜品不能视为重复下单
        if (orderDetails.isEmpty()) {
            return false;
        }
        //有菜品时再判断
        int count = torderDetailMapper.countByPrimarykey(orderDetails);
        return count == orderDetails.size();
    }

    /**
     * 退菜处理
     */
    public String discardDishList(UrgeDish urgeDish, ToperationLog toperationLog) {
        if (urgeDish == null) {
            log.error("-->参数urgeDish为空");
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("参数错误"));
        }
        //是否打印单据
        boolean isPrint = true;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tableNo", urgeDish.getCurrenttableid());
        List<Map<String, Object>> tableList = tableService.find(params);
        if (tableList != null && tableList.size() > 0) {
            urgeDish.setOrderNo(String.valueOf(tableList.get(0).get("orderid")));
        } else {
            log.error("-->tableList为空，参数tableNo为" + urgeDish.getCurrenttableid());
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("没有找到桌台"));
        }
        //咖啡模式，外卖模式反结算以后才打印打印退菜单
        String tableType = (String) tableList.get(0).get("tabletype");
        tableType = tableType == null ? "" : tableType.trim();
        if (!StringUtils.isEmpty(tableType)) {
            if (TABLETYPE.COFFEETABLE.equals(tableType) || TABLETYPE.TAKEOUT.equals(tableType) || TABLETYPE.TAKEOUT_COFFEE.equals(tableType)) {
                Map<String, Object> param = new HashMap<>();
                param.put("orderid", urgeDish.getOrderNo());
                Map<String, Object> res = settlementMapper.fingHistory(param);
                if (MapUtils.isEmpty(res)) {
                    isPrint = false;
                }
            }
        }
        String orderId = urgeDish.getOrderNo();
        String discardUserId = urgeDish.getDiscardUserId();
        String discardReason = urgeDish.getDiscardReason();
        if (discardUserId == null) {
            discardUserId = urgeDish.getUserName();
        }

        Map<String, Object> mapStatus = torderMapper.findOne(orderId);
        if (!"0".equals(String.valueOf(mapStatus.get("orderstatus")))) {
            log.error("-->订单状态为:" + mapStatus.get("orderstatus") + "-->订单Id为：" + orderId);
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("订单状态不正确"));
        }
        String actionType = urgeDish.getActionType();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderno", orderId);
        PrintObj printObj = tbPrintObjDao.find(map);
        printObj.setPrintType(Constant.PRINTTYPE.DISCARD_DISH);
        printObj.setBillName(Constant.DISHBILLNAME.DISCARDDISHNAME);
        printObj.setAbbrbillName(Constant.DISHBILLNAME.DISCARDDISHNAME_ABBR);
        if (urgeDish.getDiscardUserId() != null && !"".equals(urgeDish.getDiscardUserId())) {
            User disUser = userService.getUserByjobNum(urgeDish.getDiscardUserId());
            printObj.setDiscardUserId(disUser.getName());
        }
        //根据桌号配置客户打印机
//			  Map<String,Object> selectmap=new HashMap<String, Object>();

        if ("0".equals(actionType)) {
            //单品退
            //判断退的数量是否和原始点的相同
            //查询当前的菜品是否是和 退订的数量一致
            BigDecimal detailNum = new BigDecimal("0");//下单的数量
            BigDecimal urgeNum = new BigDecimal("0");//退的数量
            TorderDetail orderDetail = torderDetailMapper.getOrderDetailByPrimaryKey(urgeDish.getPrimarykey());
            if (orderDetail == null) {
                log.error("-->orderDetail为空，参数Primarykey值为：" + urgeDish.getPrimarykey());
                return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("订单数据有误"));
            }
            if (orderDetail != null) {
                detailNum = new BigDecimal(orderDetail.getDishnum());
            }
            orderDetail.setDiscardReason(discardReason);
            orderDetail.setDiscardUserId(discardUserId);
            urgeNum = detailNum.subtract(urgeDish.getDishNum());
            String dishType = orderDetail.getDishtype();
            int isMaster = orderDetail.getIsmaster();
            if (urgeNum.compareTo(detailNum) <= 0) {
                //退单品
                if ("0".equals(dishType)) {
                    Map<String, Object> map0 = new HashMap<String, Object>();
                    map0.put("printobjid", printObj.getId());
                    map0.put("dishtype", 0);
                    map0.put("discardNum", urgeDish.getDishNum());
                    map0.put("primarykey", urgeDish.getPrimarykey());
                    map0.put("discardReason", discardReason);
                    if (isPrint) {
                        printSingleDish(map0, printObj, 1, null);
                    }


                    PrintDish pd = new PrintDish();
                    pd.setPrintobjid(printObj.getId());
                    pd.setPrimarykey(urgeDish.getPrimarykey());
                    updateOrderDetail(urgeDish.getPrimarykey(), orderDetail, urgeDish.getDishNum(), pd, "0");
                } else if ("1".equals(dishType)) {
                    //退火锅 如果是主锅
                    if (isMaster == 1) {
                        Map<String, Object> map0 = new HashMap<String, Object>();
                        map0.put("printobjid", printObj.getId());
                        map0.put("primarykey", urgeDish.getPrimarykey());
                        map0.put("discardReason", discardReason);
                        if (isPrint) {
                            printMutilDish(map0, printObj, 1, null);
                        }

                        PrintDish pd = new PrintDish();
                        pd.setPrintobjid(printObj.getId());
                        pd.setPrimarykey(urgeDish.getPrimarykey());
                        updateOrderDetail(urgeDish.getPrimarykey(), orderDetail, urgeDish.getDishNum(), pd, "1");
                    } else {
                        if (orderDetail.getIspot() == 1) {
                            Map<String, Object> map0 = new HashMap<String, Object>();
                            map0.put("printobjid", printObj.getId());
                            map0.put("primarykey", orderDetail.getParentkey());
                            map0.put("discardReason", discardReason);
                            if (isPrint) {
                                printMutilDish(map0, printObj, 1, null);
                            }

                            PrintDish pd = new PrintDish();
                            pd.setPrintobjid(printObj.getId());
                            pd.setPrimarykey(orderDetail.getParentkey());
                            updateOrderDetail(orderDetail.getParentkey(), orderDetail, urgeDish.getDishNum(), pd, "1");
                        }
                        if (orderDetail.getIspot() == 0) {
                            if (urgeDish.getDishNum().compareTo(new BigDecimal(orderDetail.getDishnum())) < 0) {
                                Map<String, Object> map0 = new HashMap<String, Object>();
                                map0.put("printobjid", printObj.getId());
                                map0.put("discardNum", urgeDish.getDishNum());
                                map0.put("primarykey", urgeDish.getPrimarykey());
                                map0.put("discardReason", discardReason);
                                if (isPrint) {
                                    printSingleDish(map0, printObj, 1, null);
                                }

                                PrintDish pd = new PrintDish();
                                pd.setPrintobjid(printObj.getId());
                                pd.setPrimarykey(urgeDish.getPrimarykey());
                                updateOrderDetail(urgeDish.getPrimarykey(), orderDetail, urgeDish.getDishNum(), pd, "0");
                            } else {
                                params = new HashMap<String, Object>();
                                params.put("parentkey", orderDetail.getParentkey());
                                List<TorderDetail> list = torderDetailMapper.find(params);
                                boolean flag = false;
                                if (list != null && list.size() > 0) {
                                    for (TorderDetail t : list) {
                                        if (t.getIspot() == 1) {
                                            flag = true;
                                        }
                                    }
                                }
                                if (flag) {
                                    Map<String, Object> map0 = new HashMap<String, Object>();
                                    map0.put("printobjid", printObj.getId());
                                    map0.put("discardNum", urgeDish.getDishNum());
                                    map0.put("primarykey", urgeDish.getPrimarykey());
                                    map0.put("discardReason", discardReason);
                                    if (isPrint) {
                                        printSingleDish(map0, printObj, 1, null);
                                    }

                                    PrintDish pd = new PrintDish();
                                    pd.setPrintobjid(printObj.getId());
                                    pd.setPrimarykey(urgeDish.getPrimarykey());
                                    updateOrderDetail(urgeDish.getPrimarykey(), orderDetail, urgeDish.getDishNum(), pd, "0");
                                } else {
                                    Map<String, Object> map0 = new HashMap<String, Object>();
                                    map0.put("printobjid", printObj.getId());
                                    map0.put("primarykey", orderDetail.getParentkey());
                                    map0.put("discardReason", discardReason);
                                    if (isPrint) {
                                        printMutilDish(map0, printObj, 1, null);
                                    }

                                    PrintDish pd = new PrintDish();
                                    pd.setPrintobjid(printObj.getId());
                                    pd.setPrimarykey(orderDetail.getParentkey());
                                    updateOrderDetail(orderDetail.getParentkey(), orderDetail, urgeDish.getDishNum(), pd, "1");
                                }
                            }

                        }
                    }

                } else if ("2".equals(dishType)) {
                    //退套餐  如果是主套餐
                    if (isMaster == 1) {
                        //套餐中的单品
                        Map<String, Object> map0 = new HashMap<String, Object>();
                        map0.put("printobjid", printObj.getId());
                        map0.put("dishtype", "2");
                        map0.put("childdishtype", "0");
                        map0.put("parentkey", urgeDish.getPrimarykey());
                        map0.put("discardReason", discardReason);
                        if (isPrint) {
                            printSingleDish(map0, printObj, 1, null);
                        }

                        //套餐中的鱼锅
                        map0 = new HashMap<String, Object>();
                        map0.put("printobjid", printObj.getId());
                        map0.put("dishtype", "2");
                        map0.put("childdishtype", "1");
                        map0.put("ismaster", "1");
                        map0.put("discardReason", discardReason);
                        map0.put("parentkey", urgeDish.getPrimarykey());
                        if (isPrint) {
                            printMutilDish(map0, printObj, 1, null);
                        }


                        Map<String, Object> paramsDish = new HashMap<String, Object>();
                        paramsDish.put("orderid", orderId);
                        paramsDish.put("superkey", urgeDish.getPrimarykey());
                        torderDetailMapper.insertDiscardDishSetOnce(paramsDish);

                        TorderDetail orderDetailDel = new TorderDetail();
                        orderDetailDel.setOrderid(orderId);
                        orderDetailDel.setSuperkey(urgeDish.getPrimarykey());
                        orderDetailDel.setDiscardUserId(discardUserId);
                        orderDetailDel.setDiscardReason(discardReason);
                        torderDetailMapper.updateDiscardDishSetUserId(orderDetailDel);

                        Map<String, Object> deleteMap = new HashMap<String, Object>();
                        deleteMap.put("orderid", orderId);
                        deleteMap.put("superkey", urgeDish.getPrimarykey());
                        torderDetailMapper.deleteDish(deleteMap);

                        deleteMap = new HashMap<String, Object>();
                        deleteMap.put("printobjid", printObj.getId());
                        deleteMap.put("superkey", urgeDish.getPrimarykey());
                        tbPrintObjDao.deleteDish(deleteMap);

                    }
                }
                //TODO 设置也打印到前台一份

            }
        } else if ("1".equals(actionType)) {
            //整单退菜

            Map<String, Object> map0 = new HashMap<String, Object>();
            map0.put("printobjid", printObj.getId());
            map0.put("dishtype", "0");
            map0.put("discardReason", discardReason);
            if (isPrint) {
                printSingleDish(map0, printObj, 1, null);
            }

            //打印火锅
            map0 = new HashMap<String, Object>();
            map0.put("printobjid", printObj.getId());
            map0.put("dishtype", "1");
            map0.put("ismaster", "1");
            map0.put("discardReason", discardReason);
            if (isPrint) {
                printMutilDish(map0, printObj, 1, null);
            }

            //打印套餐   套餐中的单品
            map0 = new HashMap<String, Object>();
            map0.put("printobjid", printObj.getId());
            map0.put("dishtype", "2");
            map0.put("childdishtype", "0");
            map0.put("discardReason", discardReason);
            if (isPrint) {
                printSingleDish(map0, printObj, 1, null);
            }

            //打印套餐   套餐中的火锅
            map0 = new HashMap<String, Object>();
            map0.put("printobjid", printObj.getId());
            map0.put("dishtype", "2");
            map0.put("childdishtype", "1");
            map0.put("ismaster", "1");
            map0.put("discardReason", discardReason);
            if (isPrint) {
                printMutilDish(map0, printObj, 1, null);
            }

            torderDetailMapper.insertDiscardDishOnce(orderId);
            TorderDetail orderDetail = new TorderDetail();
            orderDetail.setOrderid(orderId);
            orderDetail.setDiscardUserId(discardUserId);
            orderDetail.setDiscardReason(discardReason);
            torderDetailMapper.updateDiscardDishUserIdOnce(orderDetail);

            Map<String, Object> deleteMap = new HashMap<String, Object>();
            deleteMap.put("orderid", orderId);
            torderDetailMapper.deleteDish(deleteMap);

            deleteMap = new HashMap<String, Object>();
            deleteMap.put("printobjid", printObj.getId());
            tbPrintObjDao.deleteDish(deleteMap);

        }
        //退菜后重新计算应收金额
        orderOpService.calcOrderAmount(orderId);
        
        if (toperationLogService.save(toperationLog)) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
        } else {
            log.error("-->插入t_operation_log数据出错。参数toperationLog值为：" + toperationLog.getId());
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("获取数据失败"));
        }
    }

    /**
     * 退菜使用的更新退菜的表和下单的明细表
     *
     * @param orderDetail
     * @param discardNum  0 退单品  1退火锅
     * @author tom_zhao
     */
    private void updateOrderDetail(String primarykey, TorderDetail orderDetail, BigDecimal discardNum, PrintDish printDish, String flag) {
        if ("0".equals(flag)) {
            BigDecimal num = new BigDecimal(orderDetail.getDishnum()).subtract(discardNum);
            if (num.compareTo(new BigDecimal("0")) <= 0) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("primarykey", primarykey);
                torderDetailMapper.deleteDish(params);
                params.put("printobjid", printDish.getPrintobjid());
                tbPrintObjDao.deleteDish(params);
            } else {
                orderDetail.setDishnum(String.valueOf(num));
                torderDetailMapper.update(orderDetail);
                printDish.setDishNum(String.valueOf(num));
                tbPrintObjDao.updateDish(printDish);
            }
            orderDetail.setDishnum(String.valueOf(discardNum));
            torderDetailMapper.insertDiscardDish(orderDetail);
        }
        if ("1".equals(flag)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("primarykey", primarykey);
            torderDetailMapper.insertDiscardfishpot(params);
            torderDetailMapper.deletefishpot(params);
            params.put("printobjid", printDish.getPrintobjid());
            tbPrintObjDao.deletefishpot(params);
            TorderDetail uporderDetail = new TorderDetail();
            uporderDetail.setDiscardUserId(orderDetail.getDiscardUserId());
            uporderDetail.setDiscardReason(orderDetail.getDiscardReason());
            uporderDetail.setPrimarykey(primarykey);
            torderDetailMapper.updateFishpotReason(uporderDetail);
        }
    }

    /**
     * 退整个菜
     *
     * @param orderDetail
     * @param discardNum
     * @author tom_zhao
     */
    @Override
    public void discardOrderDetail(TorderDetail orderDetail, String discardUserId, String discardReason) {
        torderDetailMapper.insertDiscardDish(orderDetail);
        orderDetail.setDiscardUserId(discardUserId);
        orderDetail.setDiscardReason(discardReason);
        torderDetailMapper.updateDiscardUserId(orderDetail);
        torderDetailMapper.delete(orderDetail.getOrderdetailid());
    }


    public class StatementThread implements Runnable {

        PrintObj printObj;

        StatementThread(PrintObj printObj) {
            this.printObj = printObj;
        }

        @Override
        public void run() {
            //根据动作打印不同的小票
            statentMentProducerService.sendMessage(printObj);
            //0.正常下单
            //1.加菜下单
        }

    }

    /**
     * 打印结账单
     */
    @Override
    public void printStatement(String orderno) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderno", orderno);
        PrintObj printObj = tbPrintObjDao.find(map);
        printObj.setPrintType(Constant.PRINTTYPE.ADD_DISH);
        printObj.setBillName(Constant.DISHBILLNAME.STATEMENTDISHNAME);
        printObj.setAbbrbillName(Constant.DISHBILLNAME.ADDDISHNAME_ABBR);
        map.put("printobjid", printObj.getId());

        List<Map<String, Object>> dishes = torderDetailMapper.getDishesInfoByOrderId(orderno);
        printObj.setDishes(dishes);
        Map<String, Object> ordermap = orderService.findOrderById(orderno);
        if (ordermap != null) {
            printObj.setOrdermap(ordermap);
        }
        //得到打印机配置
        Map<String, Object> printertypeMap = new HashMap<String, Object>();
        printertypeMap.put("printertype", 4);
        List<Map> ipconfigs = tbPrinterManagerDao.find(printertypeMap);
        if (ipconfigs != null && ipconfigs.size() > 0) {
            for (Map tbPrinter : ipconfigs) {
                Object ipaddress = tbPrinter.get("ipaddress");
                Object port = tbPrinter.get("port");
                if (ipaddress != null) {
                    printObj.setCustomerPrinterIp(ipaddress.toString());
                    printObj.setCustomerPrinterPort(port.toString());
                    printObj.setPrinterid((String) tbPrinter.get("printerid"));
                }
            }
        }
        new Thread(new StatementThread(printObj)).run();

    }


    /**
     * 催菜
     */
    @Override
    public String urgeDishList(UrgeDish urgeDish) {
        if (urgeDish == null) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("参数有误"));
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tableNo", urgeDish.getCurrenttableid());
        List<Map<String, Object>> tableList = tableService.find(params);
        if (tableList != null && tableList.size() > 0) {
            urgeDish.setOrderNo(String.valueOf(tableList.get(0).get("orderid")));
        }
        String orderNo = urgeDish.getOrderNo();

        Map<String, Object> mapStatus = torderMapper.findOne(orderNo);
        if (!"0".equals(String.valueOf(mapStatus.get("orderstatus")))) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("订单状态有误"));
        }

        String actionType = urgeDish.getActionType();
        if ("0".equals(actionType)) {
            // 0 单餐 催菜
            //1 整桌催菜
            commonDishList(urgeDish, "0", "0");

        } else if ("1".equals(actionType)) {
            //1 整桌催菜
            commonDishList(urgeDish, "0", "1");

        }
        return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
    }


    @Override
    public String cookiedishList(UrgeDish urgeDish) {
        if (urgeDish == null) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("参数有误"));
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tableNo", urgeDish.getCurrenttableid());
        List<Map<String, Object>> tableList = tableService.find(params);
        if (tableList != null && tableList.size() > 0) {
            urgeDish.setOrderNo(String.valueOf(tableList.get(0).get("orderid")));
        }
        String orderNo = urgeDish.getOrderNo();

        Map<String, Object> mapStatus = torderMapper.findOne(orderNo);
        if (!"0".equals(String.valueOf(mapStatus.get("orderstatus")))) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("订单状态不正确"));
        }

        String actionType = urgeDish.getActionType();
        if ("0".equals(actionType)) {
            // 0 单餐  叫起
            //1 整桌 叫起
            commonDishList(urgeDish, "1", "0");
            //update t_printdish status
        } else if ("1".equals(actionType)) {
            //1 整桌 叫起
            commonDishList(urgeDish, "1", "1");
            //update t_printdish status
        }
        return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
    }

    /**
     * @param urgeDish
     * @param type     0催菜  1叫起
     * @param batch    0单个 1 整桌
     * @return
     */
    private String commonDishList(UrgeDish urgeDish, String type, String batch) {

        Map<String, Object> mapAll = new HashMap<String, Object>();
        mapAll.put("orderno", urgeDish.getOrderNo());
        PrintObj printObj = tbPrintObjDao.find(mapAll);
        if (printObj == null) {
            return Constant.FAILUREMSG;
        }
        if ("0".equals(type)) {
            printObj.setPrintType(Constant.PRINTTYPE.URGE_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.URGEDISHNAME);
            printObj.setAbbrbillName(Constant.DISHBILLNAME.URGEDISHNAME_ABBR);
        } else {
            printObj.setPrintType(Constant.PRINTTYPE.NOW_DISH);
            printObj.setBillName(Constant.DISHBILLNAME.NOWDISHNAME);
            printObj.setAbbrbillName(Constant.DISHBILLNAME.CALL_ABBR);
        }

        //0 催单个菜品
        if ("0".equals(batch)) {
            singleDishUrgeAndCall(urgeDish.getPrimarykey(), urgeDish.getOrderNo(), type, printObj);

        } else if ("1".equals(batch)) {
            Map<String, Object> map0 = new HashMap<String, Object>();
            map0.put("printobjid", printObj.getId());
            map0.put("orderid", urgeDish.getOrderNo());
            if ("0".equals(type)) {
                //催菜
                map0.put("islatecooke", "0");
            }
            if ("1".equals(type)) {
                //叫菜
                map0.put("islatecooke", "1");
            }

            List<PrintDish> pdList = tbPrintObjDao.findDishByObjId(map0);
            if (pdList != null) {
                for (PrintDish pd : pdList) {
                    singleDishUrgeAndCall(pd.getPrimarykey(), urgeDish.getOrderNo(), type, printObj);
                }
            }
        }
        return Constant.SUCCESSMSG;
    }


    private void singleDishUrgeAndCall(String primaryKey, String orderNo, String type,
                                       PrintObj printObj) {
        Map<String, Object> map0 = new HashMap<String, Object>();

        map0.put("printobjid", printObj.getId());
        map0.put("primarykey", primaryKey);
        map0.put("orderid", orderNo);

        if ("0".equals(type)) {
            //催菜
            map0.put("islatecooke", "0");
        }
        if ("1".equals(type)) {
            //叫菜
            map0.put("islatecooke", "1");
        }

        //根据primarykey 和 parentkey 取单品或者鱼锅的数据
        urgeAndCallDish(printObj, map0, type);
    }


    private void urgeAndCallDish(PrintObj printObj, Map<String, Object> map0, String flag) {
        List<PrintDish> pdList = tbPrintObjDao.findDishByPrimaryKey(map0);
        if (pdList != null) {
            for (PrintDish pf : pdList) {
                try {
                    pf.initData();
                } catch (Exception e) {
                    log.error("------------------菜品解析失败！-------------");
                    log.error("菜品忌口信息解析失败！ :" + pf.getDishName(), e);
                    e.printStackTrace();
                }
                pf.setAbbrname(printObj.getAbbrbillName());
                if (!"".equals(pf.getDishNum()) && pf.getDishNum().endsWith(".0")) {
                    pf.setDishNum(pf.getDishNum().substring(0, pf.getDishNum().lastIndexOf(".")));
                }
            }
            printObj.setList(pdList);
            printObj.setOrderseq(pdList.get(0).getOrderseq());
            List<TbPrinterManager> printers = getPrinter(printObj.getTableid(), pdList.get(0).getDishId(), "1");
            if (printers != null) {
                for (TbPrinterManager printer : printers) {
                    printObj.setCustomerPrinterIp(printer.getIpaddress());
                    printObj.setCustomerPrinterPort(printer.getPort());
                    printObj.setPrintName(printer.getPrintername());
                    printObj.setPrinterid(printer.getPrinterid());
//					executor.execute(new PrintMutiThread(printObj));
                    new Thread(new PrintMutiThread(printObj)).run();
//					executor.execute(new PrintMutiThread(printObj));
                }

            }
        }
        //如果是叫起的单子需要更新状态为0
        if ("1".equals(flag)) {
            tbPrintObjDao.updateDishByPrimaryKey(map0);
            tbPrintObjDao.updateDetailByPrimaryKey(map0);
        }
    }


    private List<TbPrinterManager> getPrinter(String tableId, String dishId, String printertype) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", "1");
        paramMap.put("printertype", printertype);
        paramMap.put("tableid", tableId);
        paramMap.put("dishid", dishId);

        return tbPrinterManagerDao.findDishPrinter(paramMap);
    }

    @SuppressWarnings("unused")
    @Override
    public String getOrderDetailByOrderId(String orderid) {
        List<TorderDetailSimple> torderDetail = torderDetailMapper.getOrderDetailByOrderId(orderid);
//		List<TorderDetail> torderDetail = null;
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("orderid", orderid);
        if (torderDetail != null) {
            //TODO 没有完成
            retMap.put("orderMap", torderDetail);
        }
        return JacksonJsonMapper.objectToJson(retMap);
    }


    /**
     * 获取品项销售明细
     */
    @Override
    public List<Map<String, Object>> getItemSellDetail(Map<String, Object> timeMap) throws Exception {
        return torderDetailMapper.getItemSellDetail(timeMap);
    }

    /**
     * 结账后打印
     *
     * @param order
     * @param flag
     */
    public void afterprint(String orderid) {
        int flag = 0;//
        Map<String, Object> mapParam1 = new HashMap<String, Object>();
        mapParam1.put("orderid", orderid);
        List<TorderDetail> detailList = torderDetailMapper.find(mapParam1);
        TbTable table = tableService.findTableByOrder(orderid);
        printOrderList(orderid, table.getTableid(), flag);
        printweigth(detailList, orderid);
    }

    public class PrintThread implements Runnable {

        PrintObj printObj;

        PrintThread(PrintObj printObj) {
            this.printObj = printObj;
        }

        @Override
        public void run() {
            //根据动作打印不同的小票
            producerService.sendMessage(printObj);
            //0.正常下单
            //1.加菜下单
        }
    }

    public class WeigthThread implements Runnable {

        PrintObj printObj;

        WeigthThread(PrintObj printObj) {
            this.printObj = printObj;
        }

        @Override
        public void run() {
            //根据动作打印不同的小票
//		printCommonService.setDestination(printDishQueue);
            printCommonService.sendMessage(printObj);
            //0.正常下单
            //1.加菜下单
        }
    }

    public class PrintMutiThread implements Runnable {

        PrintObj printObj;

        PrintMutiThread(PrintObj printObj) {
            this.printObj = printObj;
        }

        @Override
        public void run() {
            //根据动作打印不同的小票
            mutilService.sendMessage(printObj);
            //0.正常下单
            //1.加菜下单
        }
    }

    public class PrintDishSetThread implements Runnable {

        PrintObj printObj;

        public PrintDishSetThread(PrintObj printObj) {
            this.printObj = printObj;
        }

        @Override
        public void run() {
            dishSetService.sendMessage(printObj);
        }

    }

    public class PrintCustThread implements Runnable {

        PrintObj printObj;

        PrintCustThread(PrintObj printObj) {
            this.printObj = printObj;
        }

        @Override
        public void run() {
            //根据动作打印不同的小票
            custService.sendMessage(printObj);
            //0.正常下单
            //1.加菜下单
        }
    }


    @Autowired
    TorderDetailMapper torderDetailMapper;

    @Autowired
    TableService tableService;

    @Autowired
    TorderMapper torderMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    DishService dishService;

    @Autowired
    PrinterService printerService;

    @Autowired
    TableAreaService tableAreaService;

    @Autowired
    ToperationLogService toperationLogService;

//	@Autowired
//	OrderDetailService  orderDetailService;

    @Autowired
    private NormalDishProducerService producerService;

    @Autowired
    private MutilDishProducerService mutilService;


    @Autowired
    private CustDishProducerService custService;

    @Autowired
    TbPrintObjDao tbPrintObjDao;

    @Autowired
    TbPrinterManagerDao tbPrinterManagerDao;
    @Autowired
    PrintCommonServiceImpl printCommonService;

//	@Autowired
//	@Qualifier("weightQueue")
//	private Destination printDishQueue;

    @Autowired
    DataSourceTransactionManager transactionManager;
    @Autowired
    @Qualifier("t_userService")
    UserService userService;

    @Autowired
    ToperationLogDao toperationLogDao;

    @Autowired
    private DishSetProducerService dishSetService;

    @Autowired
    StatentMentProducerService statentMentProducerService;

    @Autowired
    TsettlementMapper settlementMapper;
    @Autowired
    private TmenuDao menuDao;
    @Autowired
    private TtemplateDishUnitlDao templateDishUnitlDao;
    @Autowired
    private TbTableDao tableDao;
    @Autowired
    private UserDao userDao;
}
