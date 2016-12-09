package com.candao.www.webroom.controller;

import com.alibaba.fastjson.JSON;
import com.candao.common.dto.ResultDto;
import com.candao.common.enums.ResultMessage;
import com.candao.common.exception.AuthException;
import com.candao.common.exception.SysException;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.file.fastdfs.service.FileService;
import com.candao.www.constant.Constant;
import com.candao.www.constant.SystemConstant;
import com.candao.www.data.dao.*;
import com.candao.www.data.model.*;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.dataserver.service.order.OrderOpService;
import com.candao.www.dataserver.util.StringUtil;
import com.candao.www.permit.common.Constants;
import com.candao.www.permit.service.EmployeeUserService;
import com.candao.www.permit.service.FunctionService;
import com.candao.www.permit.service.UserService;
import com.candao.www.security.controller.BaseController;
import com.candao.www.security.service.LoginService;
import com.candao.www.timedtask.BranchDataSyn;
import com.candao.www.utils.*;
import com.candao.www.webroom.model.*;
import com.candao.www.webroom.service.*;
import com.candao.www.webroom.service.impl.SystemServiceImpl;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 所有pad 端处理的接口
 * <p/>
 * <p/>
 * <pre>
 * 所有pad 端处理的接口
 * Copyright : Copyright  Pandoranews 2014 ,Inc. All right
 * Company : 凯盈资讯科技有限公司
 * </pre>
 *
 * @author zhao
 * @version 1.0
 * @date 2014年12月2日 上午9:55:41
 * @history
 */

@Controller
@RequestMapping("/padinterface")
public class PadInterfaceController extends BaseController{

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(5000));
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private MsgForwardService msgForwardService;

    // 屏蔽礼物
    private final String NO_GIFT = "0";


	@RequestMapping("/consumInfo")
	@ResponseBody
	public String consumInfo(){
		try {
			return orderService.consumInfo();
		}catch (Exception e){
			logger.error("--->",e);
			return JSON.toJSONString(ReturnMap.getFailureMap());
		}
	}
	/**
	 * ti 菜品分类接口，全部页菜品数据获取
	 *
	 * @return json 数据，包括分类的菜品
	 * @author zhao
	 */
	@RequestMapping("/getalldishtype")
	public void getAllDishType(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = dishTypeService.getAllDishAndDishType("0");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try {
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 菜品分类接口，全部页菜品数据获取
     *
     * @return json 数据，包括分类的菜品
     * @author zhao
     */
    @RequestMapping("/getcategory")
    public void findcategory(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> list = dishTypeService.findAllCategory("0");
        if (list == null || list.size() == 0) {
            return;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * PAD 查询规定菜品的优惠信息 桌号 ，人数
     *
     * @author zhao
     */
    @RequestMapping("/querycoupons")
    @ResponseBody
    public String querycoupons(@RequestBody Tdish dish) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(dish));
        record.setPadpath("querycoupons");
        jsonRecordService.insertJsonRecord(record);

        String dishId = dish.getDishid();
        return orderService.querycoupons(dishId);
    }

    /**
     * PAD 查询所有菜品的优惠信息 桌号 ，人数
     *
     * @author zhao
     */
    @RequestMapping("/queryallcoupons")
    @ResponseBody
    public String queryallcoupons() {

        return orderService.queryallcoupons();
    }

    /**
     * PAD 开台 桌号 ，人数
     *
     * @author zhao
     */
    @RequestMapping("/setorder")
    @ResponseBody
    public String startOrderInfo(@RequestBody Torder order) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(order));
        record.setPadpath("setorder");
        jsonRecordService.insertJsonRecord(record);
        //判断是否是pos开台还是pad开台
        if (order.getMeid() == null || order.getMeid().trim().isEmpty()) {
            order.setNumOfMeals((order.getManNum() == null ? 0 : order.getManNum())
                    + (order.getWomanNum() == null ? 0 : order.getWomanNum()));
        }


        String returnStr = "";
        try {
        	returnStr = orderService.startOrder(order);
        	 JSONObject returnobject = JSONObject.fromObject(returnStr);
             if (!StringUtils.isBlank(order.getIsShield()) && order.getIsShield().equals("0")
                     && returnobject.containsKey("code") && !StringUtils.isBlank(returnobject.getString("code"))
                     && returnobject.getString("code").equals("0")) {
                     Map<String, Object> map = returnobject.getJSONObject("data");
                     String orderid = map.get("orderid") == null ? "" : map.get("orderid").toString();
                     if (!StringUtils.isBlank(orderid)) {
                         giftService.updateOrderStatus(orderid);
                     }
             }
		} catch (Exception e) {
			logger.error("开台失败（内部系统错误）：",e.fillInStackTrace());
			returnStr=JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("开台系统内部错误，请您联系餐道管理员为你检查！"));
		}

        logger.info("开台返回结果：" + returnStr);
        return returnStr;
    }

    /**
     * PAD 登陆会员后 更新后台菜品价格 orderid ，dishid,price ,vipprice
     *
     * @author zhao
     */
    @RequestMapping("/updateorderprice")
    @ResponseBody
    public String updateorderprice(@RequestBody String jsonString, HttpServletRequest reqeust) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(jsonString);
        record.setPadpath("updateorderprice");
        jsonRecordService.insertJsonRecord(record);

        Order order = JacksonJsonMapper.jsonToObject(jsonString, Order.class);

        return orderDetailService.updateorderprice(order);
    }

    /**
     * PAD 开台 更新 桌号 人数
     *
     * @author zhao
     */
    @RequestMapping("/updateorder")
    @ResponseBody
    public String updateorder(@RequestBody Torder order) {
        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(order));
        record.setPadpath("updateorder");
        jsonRecordService.insertJsonRecord(record);
        return orderService.updateOrder(order);
    }

    /**
     * PAD下单接口(台号，标志，菜品ID，份数，口味）返回成功失败
     *
     * @author zhao
     */
    // @REQUESTMAPPING("/BOOKORDER")
    // @RESPONSEBODY
    // PUBLIC STRING SAVEORDERINFO(@REQUESTBODY STRING
    // JSONSTRING,HTTPSERVLETREQUEST REQEUST){
    //
    // TJSONRECORD RECORD = NEW TJSONRECORD();
    // RECORD.SETJSON(JSONSTRING);
    // RECORD.SETPADPATH("BOOKORDER");
    // JSONRECORDSERVICE.INSERTJSONRECORD(RECORD);
    //
    // ORDER ORDER = JACKSONJSONMAPPER.JSONTOOBJECT(JSONSTRING, ORDER.CLASS);
    // LIST<TORDERDETAIL> TDS = ORDER.GETROWS();
    // LIST<TORDERDETAIL> DELS = NEW ARRAYLIST<TORDERDETAIL>();
    // FOR(TORDERDETAIL TD : TDS){
    // INT DISHNUM = TD.GETDISHNUM() ==
    // NULL?0:INTEGER.PARSEINT(TD.GETDISHNUM());
    // IF(DISHNUM == 0){
    // DELS.ADD(TD);
    // }
    // }
    // TDS.REMOVEALL(DELS);
    // RETURN ORDERDETAILSERVICE.SAVEORDERDETAILS(ORDER);
    // }

    /**
     * add by sgy 2015.1.14 修改下单接口 PAD下单接口(台号，标志，菜品ID，份数，口味）返回成功失败
     */
    @RequestMapping("/bookorderList")
    @ResponseBody
    public String saveOrderInfoList(@RequestBody String jsonString) {
        logger.error("saveOrderInfoList-start:" + jsonString, "");
        long start = System.currentTimeMillis();
        TJsonRecord record = new TJsonRecord();
        record.setJson(jsonString);
        record.setPadpath("bookorderList");
        jsonRecordService.insertJsonRecord(record);
        final Order order = JacksonJsonMapper.jsonToObject(jsonString, Order.class);
        logger.error(order.getOrderid() + "-下单开始：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                "");

        Map<String, String> mapDetail = new HashMap<>();
        mapDetail.put("orderid", order.getOrderid());

        String tableNo = order.getCurrenttableid();
        Map<String, Object> res = orderDetailService.setOrderDetailList(order);
        if ("0".equals(res.get("code"))) {
            // POS下单通知PAD订单改变
            if (Constant.SOURCE.POS.equals(order.getSource())) {
                notifyService.notifyOrderChange(order.getOrderid());
            }
            String type = "12";
            if ("true".equals(res.get("data"))) {
                type = "13";
            }
            executor.execute(new PadThread(tableNo, type));
        }
        logger.error(order.getOrderid() + "-下单结束：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                "");
        logger.error(order.getOrderid() + "-下单业务耗时：" + (System.currentTimeMillis() - start), "");
        String result = JacksonJsonMapper.objectToJson(res);

        logger.error("saveOrderInfoList-end:" + result, "");
        return result;
    }

    /**
     * 咖啡餐台，虚拟外卖餐台下单接口 隔离订单和餐台的关联关系
     *
     * @param jsonString
     * @return
     */
    @ResponseBody
    @RequestMapping("/placeOrder")
    public String placeOrder(@RequestBody String jsonString) {
        logger.error("saveOrderInfoList-start:" + jsonString, "");
        long start = System.currentTimeMillis();
        TJsonRecord record = new TJsonRecord();
        record.setJson(jsonString);
        record.setPadpath("placeOrder");
        jsonRecordService.insertJsonRecord(record);
        Order order = JacksonJsonMapper.jsonToObject(jsonString, Order.class);
        logger.error(order.getOrderid() + "-下单开始：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                "");

        // 下单业务中会修改该字段，先获取
        String currenttableid = order.getCurrenttableid();
        String childrenOrderid = null;
        // 判断是否是咖啡模式加菜
        try {
            childrenOrderid = orderService.createChildOrderid(order.getOrderid());
        } catch (Exception e) {
            logger.error("咖啡模式加菜失败！创建子订单失败", e);
            e.printStackTrace();
            return JSON.toJSONString(ReturnMap.getFailureMap("咖啡模式加菜失败！创建子订单失败"));
        }
        order.setOrderid(childrenOrderid);

        Map<String, String> mapDetail = new HashMap<String, String>();
        mapDetail.put("orderid", order.getOrderid());

        TorderDetail orderDetail = orderDetailService.findOne(mapDetail);
        String result = "";
        Map<String, Object> res = orderDetailService.placeOrder(order);
        try {
            String type = "12";
            if (null != orderDetail) {
                type = "13";
            }
            executor.execute(new PadThread(currenttableid, type));
        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
        logger.error(order.getOrderid() + "-下单结束：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                "");
        logger.error(order.getOrderid() + "-下单业务耗时：" + (System.currentTimeMillis() - start), "");
        result = JacksonJsonMapper.objectToJson(res);
        logger.error("saveOrderInfoList-end:" + result, "");
        return result;
    }

    /**
     * PAD 催菜接口
     *
     * @author zhao
     */
    @RequestMapping("/urgedish")
    @ResponseBody
    public String urgedish(@RequestBody UrgeDish urgeDish, HttpServletRequest reqeust) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(urgeDish));
        record.setPadpath("urgedish");
        jsonRecordService.insertJsonRecord(record);
        // urgeDish.setDishtype("1");
        return orderDetailService.urgeDishList(urgeDish);
    }

    /**
     * PAD 叫起接口
     *
     * @author zhao
     */
    @RequestMapping("/cookiedish")
    @ResponseBody
    public String cookiedish(@RequestBody UrgeDish urgeDish, HttpServletRequest reqeust) {
        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(urgeDish));
        record.setPadpath("cookiedish");
        jsonRecordService.insertJsonRecord(record);
        return orderDetailService.cookiedishList(urgeDish);
    }

    /**
     * PAD开发票
     *
     * @author zhao
     */
    @RequestMapping("/InsertTinvoice")
    @ResponseBody
    public String Tinvoice(@RequestBody String jsonString) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
            String cardno = (String) params.get("memberNo"); // 会员卡号
            String tableno = (String) params.get("tableno"); // 桌号
            String device_no = (String) params.get("deviceId"); // 设备编号
            String invoice_title = (String) params.get("invoiceTitle");// 发票的名称

            Tinvoice tinvoice = new Tinvoice();
            // 节省 空间 和 去除 使用 - 符号的一些问题
            tinvoice.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            tinvoice.setCardno(cardno);
            tinvoice.setDevice_no(device_no);
            tinvoice.setInvoice_title(invoice_title);
            tinvoice.setOrderid((String) params.get("orderid"));
            invoiceService.insertInvoice(tinvoice);

            map = ReturnMap.getSuccessMap("操作成功");
            try {
                if (StringUtils.isNotBlank((String) params.get("orderid"))) {
                    TbTable table = tableService.findTableByOrder((String) params.get("orderid"));
                    if (table != null && StringUtils.isNotBlank(table.getTableNo())) {
                        executor.execute(new PadThread(table.getTableNo(), "11"));
                    }
                }
            } catch (Exception ex) {
                logger.error("--->", ex);
                ex.printStackTrace();
            }
        } catch (Exception e) {
            logger.error("--->", e);
            e.printStackTrace();
            map = ReturnMap.getFailureMap("操作失败，服务器内部错误");
        }
        JSONObject object = JSONObject.fromObject(map);
        return object.toString();
    }

    /**
     * PAD根据会员号查询发票信息
     *
     * @author zhao
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/FindTinvoice")
    @ResponseBody
    public String FindTinvoice(@RequestBody String jsonString) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> dataMapList = new ArrayList<Map<String, String>>();
        try {
            Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);

            List<Tinvoice> TinvoiceList = invoiceService.findTinvoice(params);
            if (TinvoiceList.size() > 0) {
                for (int i = 0; i < TinvoiceList.size(); i++) {
                    Map<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("invoiceTitle", TinvoiceList.get(i).getInvoice_title());
                    dataMapList.add(dataMap);
                }
            }
            map = ReturnMap.getSuccessMap("操作成功", dataMapList);
        } catch (Exception e) {
            logger.error("--->", e);
            e.printStackTrace();
            map = ReturnMap.getFailureMap("操作失败,服务器内部错误");
        }
        JSONObject object = JSONObject.fromObject(map);
        return object.toString();
    }

    /**
     * PAD根据订单号查询发票信息
     *
     * @author zhao
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/findInvoiceByOrderid")
    @ResponseBody
    public String findInvoiceByOrderid(@RequestBody String jsonString) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
            List<Tinvoice> invoiceList = invoiceService.findInvoiceByOrderid(params);

            map.put("data", invoiceList);
            map.put("result", "0");
        } catch (Exception e) {
            logger.error("--->", e);
            e.printStackTrace();
            return Constant.FAILUREMSG;
        }
        JSONObject object = JSONObject.fromObject(map);
        return object.toString();

    }

    /**
     * pos 打印发票后更新发票状态
     *
     * @author zhao
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/updateInvoice")
    @ResponseBody
    public String updateInvoice(@RequestBody String jsonString) {
        try {
            Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
            invoiceService.updateInvoice(params);

            return Constant.SUCCESSMSG;
        } catch (Exception e) {
            logger.error("--->", e);
            e.printStackTrace();
            return Constant.FAILUREMSG;
        }

    }

    /**
     * 退菜
     *
     * @author zhao
     */
    @RequestMapping("/discarddish")
    @ResponseBody
    public String discardDish(@RequestBody String jsonString, HttpServletRequest reqeust) {
        // UrgeDish urgeDish = null;

        UrgeDish urgeDish = JacksonJsonMapper.jsonToObject(jsonString, UrgeDish.class);
        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(urgeDish));
        record.setPadpath("discarddish");
        jsonRecordService.insertJsonRecord(record);

        ToperationLog toperationLog = new ToperationLog();
        toperationLog.setId(IdentifierUtils.getId().generate().toString());
        toperationLog.setTableno(urgeDish.getCurrenttableid());
        toperationLog.setOperationtype(Constant.operationType.DISCARDDISH);
        toperationLog.setSequence(urgeDish.getSequence());
        int flag = judgeRepeatData(toperationLog);
        if (flag == 0) {
            String a = orderDetailService.discardDishList(urgeDish, toperationLog);
            if (Constant.SOURCE.POS.equals(urgeDish.getSource())) {
                notifyService.notifyOrderChange(urgeDish.getOrderNo());
            }
            return a;
        } else if (flag == 1) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap());
        } else {
            return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
        }
    }

    /**
     * 换台 接口
     */
    @RequestMapping("/switchtable")
    @ResponseBody
    public String switchTable(@RequestBody Table table, HttpServletRequest reqeust) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(table));
        record.setPadpath("switchtable");
        jsonRecordService.insertJsonRecord(record);

        ToperationLog toperationLog = new ToperationLog();
        toperationLog.setId(IdentifierUtils.getId().generate().toString());
        toperationLog.setTableno(table.getOrignalTableNo());
        toperationLog.setOperationtype(Constant.operationType.SWITCHTABLE);
        toperationLog.setSequence(table.getSequence());
        int flag = judgeRepeatData(toperationLog);
        if (flag == 0) {
            return tableService.switchTable(table, toperationLog);
        } else if (flag == 1) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap());
        } else {
            return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
        }

    }

    /**
     * 拼凑台 接口
     */
    @RequestMapping("/mergetable")
    @ResponseBody
    public String mergetable(@RequestBody Table table, HttpServletRequest reqeust) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(table));
        record.setPadpath("mergetable");
        jsonRecordService.insertJsonRecord(record);
        ToperationLog toperationLog = new ToperationLog();
        toperationLog.setId(IdentifierUtils.getId().generate().toString());
        toperationLog.setTableno(table.getOrignalTableNo());
        toperationLog.setOperationtype(Constant.operationType.MERGETABLE);
        toperationLog.setSequence(table.getSequence());
        int flag = judgeRepeatData(toperationLog);
        if (flag == 0) {
            return tableService.mergetable(table, toperationLog);
        } else if (flag == 1) {
            return Constant.FAILUREMSG;
        } else {
            return Constant.SUCCESSMSG;
        }
    }

    /**
     * 新的并台接口，支持释放目标餐台的模式，对应的PAD版本1.7.6以上
     */
    @RequestMapping("/mergetableMultiMode")
    @ResponseBody
    public String mergetableMultiMode(@RequestBody Table table, HttpServletRequest reqeust) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(table));
        record.setPadpath("mergetableMultiMode");
        jsonRecordService.insertJsonRecord(record);
        ToperationLog toperationLog = new ToperationLog();
        toperationLog.setId(IdentifierUtils.getId().generate().toString());
        toperationLog.setTableno(table.getOrignalTableNo());
        toperationLog.setOperationtype(Constant.operationType.MERGETABLE);
        toperationLog.setSequence(table.getSequence());
        try {
            return tableService.mergetableMultiMode(table, toperationLog);
        } catch (Exception e) {
            logger.error("并台失败：" + e.getMessage(), e);
            Map<String, Object> map = new HashMap<String, Object>();
            map = ReturnMap.getFailureMap(e.getMessage());
            JSONObject object = JSONObject.fromObject(map);
            return object.toString();
        }
    }

    /**
     * 清台 接口
     */
    @RequestMapping("/cleantable")
    @ResponseBody
    public Map<String, Object> cleantable(@RequestBody Table table, HttpServletRequest reqeust) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(table));
        record.setPadpath("cleantable");
        jsonRecordService.insertJsonRecord(record);

        boolean flag = true;
        String msg = "清台成功";
        try {
            orderDetailService.cleantable(table);
        } catch (Exception e) {
            flag = false;
            msg = e.getMessage();
            e.printStackTrace();
            loggers.error("-------------------->");
            loggers.error("清台失败", e);
        }

        return getResponseStr(null, msg, flag);
    }

    @RequestMapping("/inoderCleanTable")
	@ResponseBody
	public String inoderCleanTable(@RequestBody Table table, HttpServletRequest reqeust){
		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(table));
		record.setPadpath("cleantable");
		jsonRecordService.insertJsonRecord(record);


		//清台
		TbTable tbTable = tableService.findByTableNo(table.getTableNo());

		//整单退菜
		orderDetailService.deleteordreDetailByOrderid(tbTable.getOrderid());
		if (tbTable != null) {
			Map<String, Object> params = new HashMap<>();
			params.put("orderid", tbTable.getOrderid());
			params.put("clear", "1");
			torderDetailPreferentialService.deleteDetilPreFerInfo(params);
		}

        String msg = "清台成功";
        boolean flag = true;
        try {
            orderDetailService.cleantable(table);
        } catch (Exception e) {
            flag = false;
            msg = e.getMessage();
            e.printStackTrace();
            loggers.error("------------------------->");
            loggers.error("清台失败", e);
        }

        return JSON.toJSONString(getResponseStr(null, msg, flag));
	}

    /**
     * 咖啡模式清台
     *
     * @return
     */
    @RequestMapping("/cleanTableSimply")
    @ResponseBody
    public String cleanTable(@RequestBody Table table, HttpServletRequest reqeust) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(table));
        record.setPadpath("cleanTableSimply");
        jsonRecordService.insertJsonRecord(record);
        TbTable tbTable = tableService.findByTableNo(table.getTableNo());
        if (tbTable != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("orderid", tbTable.getOrderid());
            params.put("clear", "1");
            torderDetailPreferentialService.deleteDetilPreFerInfo(params);
        }
        String result = orderDetailService.cleantableSimply(table);

        return result;
    }

    // /**
    // * 结业 接口
    // */
    // @RequestMapping("/accountcash")
    // @ResponseBody
    // public String accountcash(@RequestBody AccountCash
    // accountCash,HttpServletRequest reqeust){
    //
    //
    // TJsonRecord record = new TJsonRecord();
    // record.setJson(JacksonJsonMapper.objectToJson(accountCash));
    // record.setPadpath("accountcash");
    // jsonRecordService.insertJsonRecord(record);
    //
    // return tableService.accountcash(accountCash);
    // }

    /**
     * 获取套餐信息
     *
     * @author zhao
     */
    @RequestMapping("/getdishset")
    @ResponseBody
    public String getDishSet(@RequestBody Tdish dish) {
        return dishService.getDishSet(dish);
    }

    /**
     * 获取所有套餐信息
     *
     * @author zhao
     */
    @RequestMapping("/getalldishset")
    @ResponseBody
    public String getAllDishSet() {
        return dishService.getAllDishSet();
    }

    /**
     * 结账
     */
    @RequestMapping("/settleorder")
    @ResponseBody
    public String settleOrder(@RequestBody String settlementStrInfo) {
        // SettlementInfo settlementInfo

        TJsonRecord record = new TJsonRecord();
        record.setJson(settlementStrInfo);
        record.setPadpath("settleorder");
        jsonRecordService.insertJsonRecord(record);
        SettlementInfo settlementInfo = JacksonJsonMapper.jsonToObject(settlementStrInfo, SettlementInfo.class);
        logger.info("结账开始=====》"+settlementInfo.getOrderNo());
        try {
        	String result = orderSettleService.settleOrder(settlementInfo);

            final String orderid = settlementInfo.getOrderNo();
            // 计算订单的实收、优免等
            orderOpService.calcOrderAmount(orderid);
            // 内部直接调用计算实收，POS不再调用
            orderSettleService.calDebitAmount(orderid);
            // 修改投诉表信息
            new Thread(new Runnable() {
                public void run() {
                    callWaiterService.updateCallStatus(orderid);
                }
            }).start();

            if ("0".equals(result)) {
                logger.info("结算成功，调用进销存接口");
                String psicallback = psicallback(settlementInfo, 0);
                  //此段代码为了解决老的返回格式转换新的返回格式 不影响其他业务规格所以
                 //在此处转换
            	Map<String, Object> map=JacksonJsonMapper.jsonToObject(psicallback, Map.class);
            	String msg=map.containsKey("msg")?String.valueOf(map.get("msg")):"";
            	  logger.info("结账结束成功=====》"+settlementInfo.getOrderNo());
                return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(msg));
            } else {
            	Map<String, Object> map=JacksonJsonMapper.jsonToObject(result, Map.class);
                logger.error("结算结束异常:" + map.get("mes"));
                return  JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap( String.valueOf(map.get("mes"))));
            }
		} catch (Exception e) {
			logger.error("结算结束异常（运行时异常）：",e);
			 return  JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("结账发现未知异常，请联系餐道管理员帮您解决！"));
		}

    }

    // 进销存回调

    /**
     * @param settlementInfo
     * @param isweixin       0 not 1 yes
     * @return
     */
    private String psicallback(SettlementInfo settlementInfo, Integer isweixin) {
        String psishow = PropertiesUtils.getValue("PSI_SHOW");
        if ("Y".equals(psishow)) {// 显示进销存
            // 调用进销存接口 返回数据给 进销存 管理
            String retString = orderDetailService.getOrderDetailByOrderId(settlementInfo.getOrderNo());
            String url = "http://" + PropertiesUtils.getValue("PSI_URL") + PropertiesUtils.getValue("PSI_SUFFIX_ORDER");
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("data", retString);
            String retPSI = null;
            try {
                retPSI = new HttpRequestor().doPost(url, dataMap);
            } catch (Exception e) {
                logger.error("--->", e);
                return Constant.PSIERROR;
            }
            @SuppressWarnings("unchecked")
            Map<String, String> retMap = JacksonJsonMapper.jsonToObject(retPSI, Map.class);
            if (retMap == null || "1".equals(retMap.get("code"))) {// 调用进销存失败,给用户提示
				/*
				 * SettlementInfo info = new SettlementInfo();
				 * info.setOrderNo(settlementInfo.getOrderNo());
				 * orderSettleService.rebackSettleOrder(settlementInfo); return
				 * Constant.FAILUREMSG;
				 */
                return Constant.PSIERROR;
            }
        }

        if (1 == isweixin) {
            return Constant.WEIXINSUCCESSMSG;
        }
        return Constant.SUCCESSMSG;
    }

    /**
     * 咖啡模式结账
     *
     * @return
     */
    @RequestMapping("/checkout")
    @ResponseBody
    public String checkout(@RequestBody String settlementStrInfo) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(settlementStrInfo);
        record.setPadpath("checkout");
        jsonRecordService.insertJsonRecord(record);

        SettlementInfo settlementInfo = JacksonJsonMapper.jsonToObject(settlementStrInfo, SettlementInfo.class);
        String result = orderSettleService.saveOrder(settlementInfo);

        final String orderid = settlementInfo.getOrderNo();
        // 计算订单的实收、优免等
        orderOpService.calcOrderAmount(orderid);
        // 内部直接调用计算实收，POS不再调用
        orderSettleService.calDebitAmount(orderid);
        // 修改投诉表信息
        new Thread(new Runnable() {
            public void run() {
                callWaiterService.updateCallStatus(orderid);
            }
        }).start();

        if ("0".equals(result)) {
            logger.info("结算成功，调用进销存接口");
            String psicallback = psicallback(settlementInfo, 0);
            //此段代码为了解决老的返回格式转换新的返回格式 不影响其他业务规格所以在此处转换
        	Map<String, Object> map=JacksonJsonMapper.jsonToObject(psicallback, Map.class);
        	String msg=map.containsKey("msg")?String.valueOf(map.get("msg")):"";
        	 return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(msg));
        } else {
            Map<String, Object> map=JacksonJsonMapper.jsonToObject(result, Map.class);
            logger.error("结算失败，result :" + map.get("mes"));
            return  JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap( String.valueOf(map.get("mes"))));
        }
    }

    /**
     * 反结账
     */

    @RequestMapping("/rebacksettleorder")
    @ResponseBody
    public String rebacksettleOrder(@RequestBody String settlementStrInfo) {
        // SettlementInfo settlementInfo

        TJsonRecord record = new TJsonRecord();
        record.setJson(settlementStrInfo);
        record.setPadpath("rebacksettleorder");
        jsonRecordService.insertJsonRecord(record);

        SettlementInfo settlementInfo = JacksonJsonMapper.jsonToObject(settlementStrInfo, SettlementInfo.class);
        String result = "";
        try {
        	result=orderSettleService.rebackSettleOrder(settlementInfo);
		} catch (Exception e) {
			logger.error("反结算失败(系统内部错误):",e.fillInStackTrace());
			return Constant.FAILUREMSG;
		}

        if ("0".equals(result)) {
            logger.info("反结算成功，调用进销存反结算接口，是否使用微信支付： " + result);
            return psicallback(settlementInfo, 0);
        } else if ("2".equals(result)) {
            logger.info("反结算成功，调用进销存反结算接口，是否使用微信支付： " + result);
            return psicallback(settlementInfo, 1);
        } else {
            logger.error("反结算失败！" + result);
            return Constant.FAILUREMSG;
        }
    }

    /**
     * 经理保存零找金
     */

    @RequestMapping("/saveposcash")
    @ResponseBody
    public String saveposcash(@RequestBody String settlementStrInfo) {
        // SettlementInfo settlementInfo
        SettlementInfo settlementInfo = JacksonJsonMapper.jsonToObject(settlementStrInfo, SettlementInfo.class);
        String result = orderSettleService.saveposcash(settlementInfo);
        if ("0".equals(result)) {
            return Constant.SUCCESSMSG;
        } else {
            return Constant.FAILUREMSG;
        }
    }

    /**
     * 校验服务员接口
     */

    @RequestMapping("/verifyuser")
    @ResponseBody
    public String verifyuser(@RequestBody LoginInfo loginInfo) {
        int result = loginService.existUser(loginInfo);
        if (result == 0) {
            return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
        } else {
            logger.error("-->用户不存在");
            return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("用户不存在"));
        }
    }

    /**
     * 登录接口，验证成功后 返回信息需要有PAD IP和房号绑定状态，并且房号是否是在用餐状态 0 成功 1 失败
     */
    @RequestMapping(value = "/openbiz")
    @ResponseBody
    public String openBiz(@RequestBody LoginInfo loginInfo) {
        String jsonString = "";

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(loginInfo));
        record.setPadpath("openbiz");
        jsonRecordService.insertJsonRecord(record);

        try {
            // TODO 查询今天是否开业
            TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
            if (tbOpenBizLog != null) {
                jsonString = Constant.FAILUREMSG;
                return jsonString;
            }
            User user = loginService.authPadUser(loginInfo, 0, "2");
            if (user == null || !user.getUserType().equals(Constants.SUPER_ADMIN)) {
                jsonString = Constant.FAILUREMSG;
            } else {
                List<Map<String, Object>> list = tableService.find(null);
                jsonString = wrapJson(user, list);
            }
        } catch (AuthException e) {
            logger.error("--->", e);
            jsonString = Constant.FAILUREMSG;
        }
        return jsonString;
    }

    /**
     * 登录接口，验证成功后 返回信息需要有PAD IP和房号绑定状态，并且房号是否是在用餐状态 0 成功 1 失败
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public String userLogin(@RequestBody LoginInfo loginInfo) {
        String jsonString = "";

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(loginInfo));
        record.setPadpath("login");
        jsonRecordService.insertJsonRecord(record);

        try {
            // TODO 查询今天是否开业
            String loginType = loginInfo.getLoginType();
            if ("0".equals(loginInfo.getLoginType())) {
                TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
                if (tbOpenBizLog == null) {
                    return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("未开业，不能登录"));
                }
                String pwd = dataDictionaryService.find("SECRETKEY");
                if (!pwd.equals(loginInfo.getPassword())) {
                    return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("密码错误"));
                }
                userService.updateLoginTime(loginInfo.getUsername());
                jsonString = JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap());
            } else {
                User user = loginService.authPadUser(loginInfo, 0, loginType);
                if (user == null) {
                    jsonString = JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("没找到该用户"));
                } else {
                    List<Map<String, Object>> list = tableService.find(null);
                    jsonString = wrapJson(user, list);
                }
            }
        } catch (AuthException e) {
            logger.error("--->", e);
            jsonString = JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("服务器内部错误，请联系管理员"));
        }
        return jsonString;
    }

    /**
     * 登录接口，验证成功后 返回信息需要有PAD IP和房号绑定状态，并且房号是否是在用餐状态 0 成功 1 失败
     */
    @RequestMapping(value = "/querytables")
    @ResponseBody
    public String queryAllTable(String orderid) {
        Map<String, Object> map = new HashMap<>();
        String defaultsort = "0";// 默认
        if (null != Constant.DEFAULT_TABLE_SORT) {
            defaultsort = Constant.DEFAULT_TABLE_SORT;
        }
        map.put("defaultsort", Integer.parseInt(defaultsort));
        map.put("exceptorder", orderid);// 排除再外的餐台
        String jsonString = "";
        try {
            int[] tabletypefilter = {2, 3, 4};
            map.put("tabletypefilter", tabletypefilter);// 过滤掉餐台类型为外卖,咖啡的餐台
            List<Map<String, Object>> list = tableService.find(map);
            ServiceChargeDescUnit.handleServiceCharge(list);

            return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(list));
        } catch (Exception e) {
            jsonString = JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("查询所有桌台异常"));
            logger.error("查询所有桌台异常！", e);
        }
        return jsonString;
    }



    /**
     * 根据餐台类型查询餐台
     *
     * @return
     */
    @RequestMapping("getTableByType")
    @ResponseBody
    public String getTableByType(@RequestBody String json) {
        Map<String, Object> map = new HashMap<>();
        com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json);
        String defaultsort = "1";// 默认
//        if (null != Constant.DEFAULT_TABLE_SORT) {
//            defaultsort = Constant.DEFAULT_TABLE_SORT;
//        }
        map.put("defaultsort", Integer.parseInt(defaultsort));
        String jsonString = "";
        try {
            map.put("tabletype", obj.getJSONArray("tableType").toArray());
            List<Map<String, Object>> list = tableService.find(map);
            return JacksonJsonMapper.objectToJson(list);
        } catch (Exception e) {
            jsonString = "";
            logger.error("根据类型查询桌台异常！", e);
        }
        return jsonString;
    }

    @RequestMapping("/getTableAndType")
    @ResponseBody
    public Map<String,Object> getTableAndType( @RequestBody String json) {
        Map<String, Object> param = null;

        if (!org.springframework.util.StringUtils.isEmpty(json)) {
            param = JSON.parseObject(json, Map.class);
        }
        List<Map<String, Object>> listTableArea;
        try {
            listTableArea = tableAreaService.getTableAreaTag();
            Map<String, Object> mapAreaid = new HashMap<String, Object>();
            for (Map<String, Object> map : listTableArea) {
                mapAreaid.clear();
                mapAreaid.put("areaid", map.get("areaid"));
                mapAreaid.put("defaultsort", 1);
                if (!MapUtils.isEmpty(param)) {
                    mapAreaid.put("exceptorder", StringUtil.isEmpty(param.get("orderid")) ? null : param.get("orderid"));// 排除再外的餐台
                    mapAreaid.put("tabletypefilter", CollectionUtils.isEmpty((Collection) param.get("tableTypeFilter")) ?
                            null : param.get("tableTypeFilter"));// 排除再外的餐台类型
                }
                List<Map<String, Object>> tablelist = tableService.find(mapAreaid);
                if (!CollectionUtils.isEmpty(tablelist)) {
                    ServiceChargeDescUnit.handleServiceCharge(tablelist);
                    map.put("tables", tablelist);
                }
            }
        } catch (Throwable e) {
            loggers.error(e.getMessage(), e);
            return getResponseStr(null, e.getMessage(), false);
        }
        return getResponseStr(listTableArea, "查询成功", true);
    }

    /**
     * 根据桌号查询桌子使用情况
     */
    @RequestMapping(value = "/queryonetable")
    @ResponseBody
    public String queryOneTable(@RequestBody Table tbTable) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(tbTable));
        record.setPadpath("queryonetable");
        jsonRecordService.insertJsonRecord(record);

        String jsonString = "";
        // Map<String, String> mapRet = new HashMap<String, String>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableNo", tbTable.getTableNo());
        // map.put("status", "1");
        try {
            List<Map<String, Object>> list = tableService.find(map);
            if (list == null || list.size() > 1) {
                jsonString = JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("获取数据失败"));
            } else {
                // mapRet.put("result", "0");
                Map<String, Object> retMap = list.get(0);
                TableStatus resultTable = new TableStatus();
                resultTable.setStatus(String.valueOf(retMap.get("status")));
                resultTable.setResult("0");
                resultTable.setOrderid(String.valueOf(retMap.get("orderid")));
                jsonString = JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(resultTable));
            }

        } catch (Exception e) {
            logger.error("--->", e);
            jsonString = JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("操作失败，服务器内部错误"));
        }
        return jsonString;
    }

    /**
     * 查询年龄段数据字典
     */
    @RequestMapping(value = "/queryagepriod")
    @ResponseBody
    public String queryAgePriod() {

        String jsonString = "";
        try {
            List<Map<String, Object>> list = dataDictionaryService.getDatasByType("AGEPERIOD");
            if (list == null || list.size() > 1) {
                jsonString = Constant.FAILUREMSG;
            } else {
                Map<String, Object> retMap = list.get(0);
                TableStatus resultTable = new TableStatus();
                resultTable.setStatus(String.valueOf(retMap.get("status")));
                resultTable.setResult("0");
                resultTable.setOrderid(String.valueOf(retMap.get("orderid")));
                jsonString = JacksonJsonMapper.objectToJson(resultTable);
            }

        } catch (Exception e) {
            logger.error("--->", e);
            jsonString = "";
        }
        return jsonString;
    }

    /**
     * 返回所有的数据
     */
    @RequestMapping(value = "/queryusers")
    @ResponseBody
    public String queryAllUser() {
        String jsonString = "";
        try {
            List<EmployeeUser> list = this.employeeUserService.findAllServiceUserForCurrentStore();
            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap = ReturnMap.getSuccessMap(list);
            return JacksonJsonMapper.objectToJson(retMap);
        } catch (Exception e) {
            logger.error("--->", e);
            e.printStackTrace();
            jsonString = JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("操作失败，服务器内部错误"));
        }
        return jsonString;
    }

    /**
     * 获取所有的分类的套餐
     *
     * @return json 数据，包括获取所有的分类的套餐
     * @author zhao
     */
    @RequestMapping("/getdishgroup")
    public String getdishgroup(HttpServletRequest request, HttpServletResponse response) {

        return null;
        // dishService.getDishList();
        // String dishId = dish.getDishid();
        // return orderService.quercoupons(dishId);
        //
        // Map<String, Object> map = new HashMap<String, Object>();
        // map.put("rows", list);
        // String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        // try{
        // response.reset();
        // response.setHeader("Content-Type", "application/json");
        // response.setContentType("text/json;charset=UTF-8");
        // OutputStream stream = response.getOutputStream();
        // stream.write(wholeJsonStr.getBytes("UTF-8"));
        // stream.flush();
        // stream.close();
        //
        // }catch(Exception ex){
        // ex.printStackTrace();
        // }
    }

    /**
     * 催菜接口
     */
    public String quickCookie() {
        return "/dishtype/show";
    }

    /**
     * 换台接口 修改人数，会员等开台信息
     */
    public String changeTableOrder() {
        return "/dishtype/show";
    }

    private String wrapJson(User user, List<Map<String, Object>> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (user != null) {
            map.put("fullname", user.getName() + "(" + user.getAccount() + ")");
        }
        map.put("loginTime", DateFormat.getDateTimeInstance().format(new Date()));
        map.put("tables", list);
        return JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(map));
    }

    /**
     * 下单和退菜的 重复下单的判断
     *
     * @return
     */
    private int judgeRepeatData(ToperationLog toperationLog) {
        // 因为 (newtoperationLog.getSequence()==toperationLog.getSequence())
        // 这个条件永远不成立，应该使用equals方法；
        // 如果加上这个判断条件，会导致PAD、POS同时给一个餐台下单时，可能误认为是重复下单，sequence没有同步，所以调用该方法的逻辑以后要去掉，暂时先返回0
        return 0;

		/*
		 * Map<String,Object> map=new HashMap<String,Object>();
		 * map.put("tableno", toperationLog.getTableno());
		 * map.put("operationType", toperationLog.getOperationtype());
		 * ToperationLog
		 * newtoperationLog=toperationLogService.findByparams(map);
		 * if(newtoperationLog==null){ return 0;//第一次操作 }else
		 * if(newtoperationLog.getSequence()==toperationLog.getSequence()){
		 * //本次操作的序号和上次操作的序号相等，返回操作成功 return 2; } // else
		 * if(newtoperationLog.getSequence()+1!=toperationLog.getSequence()){ //
		 * //本次操作的序号，必须是上次操作序号+1,不等就返回失败 // return 1; // } else{ return 0; }
		 */
    }

    /**
     * 获取屏保图片
     */
    @RequestMapping("/getPictures")
    @ResponseBody

    public void getPictures(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam Map<String, Object> params) {

        List<Map<String, Object>> list = picturesService.find(params);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 获取套餐数据
     */
    @RequestMapping("/getComboDish")
    @ResponseBody
    public void getComboDish(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = JacksonJsonMapper.jsonToObject(params, Map.class);
        String wholeJsonStr = comboDishService.getComboDishJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 传入typeid 获取对应优惠列表
     */
    @RequestMapping(value = "/getPreferentialList", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView getPreferentialList(@RequestBody String body) {
        @SuppressWarnings("unchecked")
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
        ModelAndView mav = new ModelAndView();
        String typeid = (String) params.get("typeid"); // 优惠分类
        if (!StringUtils.isBlank(typeid)) {
            List<Map<String, Object>> l = this.preferentialActivityService.findCouponsByType4Pad(typeid);
            mav.addObject("list", l);
        }
        return mav;
    }

    @RequestMapping(value = "/setPreferentialFavor", method = RequestMethod.POST)
    @ResponseBody
    public String setPreferentialFavor(@RequestBody String body) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);

        int num = preferentialActivityService.updateBySelective(params);
        num = (num == 0 ? 1 : 0);

        Map<String, Object> res = new HashMap<>();
        res.put("result", num);
        res.put("msg", "");

        return JacksonJsonMapper.objectToJson(res);
    }

    /**
     * 查询所有的可挂账的合作单位
     */
    @RequestMapping(value = "/getCooperationUnit", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView getCooperationUnit(@RequestBody String body) {
		/*
		 * @SuppressWarnings("unchecked") Map<String, Object> params =
		 * JacksonJsonMapper.jsonToObject(body, Map.class); ModelAndView mav =
		 * new ModelAndView(); String typeid=(String) params.get("typeid");
		 * //优惠分类 if( !StringUtils.isBlank(typeid)){
		 */
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = new HashMap<String, Object>();
        List<Map<String, Object>> l = this.preferentialActivityService.findCooperationUnit(params);
        mav.addObject("list", l);
        // }
        return mav;
    }

    /**
     * 使用特价菜品类和单品折扣类优惠
     */
    @RequestMapping(value = "/usePreferentialItem", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView usePreferentialItem(@RequestBody String body) {
    	Map<String, Object> result=new HashMap<>();
        ModelAndView mav = new ModelAndView();
        @SuppressWarnings("unchecked")
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
        OperPreferentialResult operPreferentialResult = this.preferentialActivityService.updateOrderDetailWithPreferential(params);
        result.put("preferentialInfo", operPreferentialResult);
        Map<String, Object> serParams = new HashMap<>();
		serParams.put("orderId", String.valueOf(params.get("orderid")));
    	  TServiceCharge charageService = chargeService.getChargeInfo(serParams);
    	  if(charageService!=null){
    		  result.put("serviceCharge", charageService);
    	  }
        if (operPreferentialResult.isFalg()) {
            mav.addObject(ReturnMap.getSuccessMap(result));
        } else {
            mav.addObject(ReturnMap.getFailureMap(operPreferentialResult.getMes(), result));
        }

        return mav;
    }

    @RequestMapping(value = "/givePrefer", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView queryGiveprefer(@RequestBody String body) {
        ModelAndView mav = new ModelAndView();
        @SuppressWarnings("unchecked")
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
        List<Map<String, Object>> retuMap = torderDetailPreferentialService.queryGiveprefer((String) params.get("orderId"));
        mav.addObject(ReturnMap.getSuccessMap(retuMap));
        return mav;
    }

    /**
     * 删除使用优惠（根据订单号以及消费的ID删除使用优惠）
     *
     * @param body 传入参数
     * @return
     */
    @RequestMapping(value = "delPreferentialItem", method = RequestMethod.POST)
    @ResponseBody
    public void delPreferentialItem(@RequestBody String body, HttpServletRequest request,
                                    HttpServletResponse response) {
        @SuppressWarnings("unchecked")
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
        torderDetailPreferentialService.deleteDetilPreFerInfo(params);
        params.put("clean", "clean");
        Map<String, Object> map = orderService.calGetOrderInfo(params);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();
        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /***
     * @param jsonString
     * @param request
     * @param response   更换pad
     */
    @RequestMapping(value = "/switchPadInfo", method = RequestMethod.POST)
    @ResponseBody
    public void switchPadInfo(@RequestBody String jsonString, HttpServletRequest request,
                              HttpServletResponse response) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        Map<String, Object> map = orderService.switchPadOrderInfo(params);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 取消账单使用的优惠
     */
    @RequestMapping(value = "/cancelPreferentialItem", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView cancelPreferentialItem(@RequestBody String body) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
        String orderid = (String) params.get("orderid"); // 账单号
        String preferentialid = (String) params.get("preferentialid"); // 优惠活动id
        OperPreferentialResult result;
        // try{
        result = this.preferentialActivityService.cancelPreferentialItemInOrder(orderid, preferentialid);
        // }catch(Exception e){
        // result=new OperPreferentialResult();
        // result.setResult(0);
        // result.setMsg("数据操作抛出异常！");
        // }
        mav.addObject(result);
        return mav;
    }

    /**
     * 获取菜谱数据，新版
     *
     * @param @return
     * @throws @author shen
     * @date:2015年5月6日下午1:33:13
     * @Description: TODO
     */
    @RequestMapping(value = "/getMenu", method = RequestMethod.POST)
    @ResponseBody
    public void getMenu(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = menuService.getMenuData();
        String wholeJsonStr = JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(map));
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            logger.info(wholeJsonStr);
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 获取当前菜谱的分类
     *
     * @author shen
     * @date:2015年5月6日下午11:53:36
     * @Description: TODO
     */
    @RequestMapping(value = "/getMenuColumn", method = RequestMethod.POST)
    @ResponseBody
    public void getMenuColumn(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = menuService.getMenuColumn();
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            logger.info(wholeJsonStr);
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 获取当前菜谱的鱼锅
     *
     * @author shen
     * @date:2015年5月6日下午11:54:28
     * @Description: TODO
     */
    @RequestMapping(value = "/getMenuFishPot", method = RequestMethod.POST)
    @ResponseBody
    public void getMenuFishPot(@RequestBody String jsonString, HttpServletRequest request,
                               HttpServletResponse response) {
        List<Map<String, Object>> map = menuService.getMenuFishPot(jsonString);
        String wholeJsonStr = "";
        if (map != null && map.size() > 0) {
            wholeJsonStr = JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(map));
        } else {
            wholeJsonStr = JacksonJsonMapper.objectToJson("获取数据失败");
        }
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            logger.info(wholeJsonStr);
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 获取菜谱中套餐的数据
     *
     * @author shen
     * @date:2015年5月7日上午10:26:56
     * @Description: TODO
     */
    @RequestMapping(value = "/getMenuCombodish", method = RequestMethod.POST)
    @ResponseBody
    public void getMenuCombodish(@RequestBody String jsonString, HttpServletRequest request,
                                 HttpServletResponse response) {
        Map<String, Object> map = menuService.getMenuCombodish(jsonString);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(map));
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            logger.info(wholeJsonStr);
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 获取菜谱中双拼鱼锅的数据
     *
     * @author shen
     * @date:2015年5月7日上午10:26:56
     * @Description: TODO
     */
    @RequestMapping(value = "/getMenuSpfishpot", method = RequestMethod.POST)
    @ResponseBody
    public void getMenuSpfishpot(@RequestBody String jsonString, HttpServletRequest request,
                                 HttpServletResponse response) {
        List<Map<String, Object>> map = menuService.getMenuSpfishpot(jsonString);
        String wholeJsonStr = "";
        if (map != null && map.size() > 0) {
            wholeJsonStr = JacksonJsonMapper.objectToJson(ReturnMap.getSuccessMap(map));
        } else {
            wholeJsonStr = JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("获取数据失败"));
        }
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            logger.info(wholeJsonStr);
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 更换pad的时候调用，获取已经开台的桌子的信息，返回一个list
     *
     * @author shen
     * @date:2015年5月14日下午3:25:42
     * @Description: TODO
     */
    @RequestMapping(value = "/getOrderedTableList", method = RequestMethod.POST)
    @ResponseBody
    public void getOrderedTableList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", "1");
        // 排除外卖，咖啡外卖，咖啡台
        params.put("tabletypefilter", new String[]{"2", "3", "4"});
        List<Map<String, Object>> list = tableService.find(params);
        List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        // tableNum tableName contentNum
        if (list != null && list.size() > 0) {
            for (Map<String, Object> ma : list) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("tableNum", ma.get("tableNo"));
                param.put("tableName", ma.get("tableName"));
                param.put("contentNum", ma.get("personNum"));
                datalist.add(param);
            }
            map = ReturnMap.getSuccessMap(datalist);
        } else {
            map = ReturnMap.getSuccessMap("没有获取到数据", datalist); // pad端需要状态为成功
        }
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 更换pad的时候调用，获取已经开台的桌子的信息，返回一个list
     *
     * @author shen
     * @date:2015年5月14日下午3:25:42
     * @Description: TODO
     */
    @RequestMapping(value = "/getOrderedTableListForCoffee", method = RequestMethod.POST)
    @ResponseBody
    public void getOrderedTableListForCoffee(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", "1");
        // 排除外卖，咖啡外卖，咖啡台
        params.put("tabletype", new String[]{"4"});
        List<Map<String, Object>> list = tableService.find(params);
        List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        // tableNum tableName contentNum
        if (list != null && list.size() > 0) {
            for (Map<String, Object> ma : list) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("tableNum", ma.get("tableNo"));
                param.put("tableName", ma.get("tableName"));
                param.put("contentNum", ma.get("personNum"));
                datalist.add(param);
            }
        }
        map.put("data", datalist);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
	 * 服务费修改
	 *
	 * @param jsonString
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/serviceChange", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView updateServiceCharge(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		TServiceCharge charge = new TServiceCharge();
		charge.setOrderid((String) params.get("orderId"));
		charge.setAutho((String) params.get("autho"));
		charge.setChargeOn(Integer.valueOf(String.valueOf(params.get("chargeOn"))));
		charge.setChargeAmount(new BigDecimal(String.valueOf(params.get("chargeAmount"))));
		charge.setIsCustom(Integer.valueOf(String.valueOf(params.get("custom"))));
		int i = chargeService.changChargeInfo(charge);
		if (i > 0) {
			mav.addObject(ReturnMap.getSuccessMap("修改成功！"));
		} else {
			mav.addObject(ReturnMap.getFailureMap("修改失败！"));
		}
		return mav;
	}

    /**
     * 获取更换pad的信息
     *
     * @author shen
     * @date:2015年5月14日下午3:45:47
     * @Description: TODO
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getOrderInfo", method = RequestMethod.POST)
    @ResponseBody
    public void getOrderInfo(@RequestBody String jsonString, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        String tableNo = (String) params.get("tableNo");
        if (tableNo != null && !tableNo.trim().isEmpty()) {
            TbTable tbTable = tableService.findByTableNo(tableNo);
            params.put("orderid", tbTable.getOrderid());
        }
        Map<String, Object> map = orderService.calGetOrderInfo(params);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 获取人气推荐的菜
     *
     * @author shen
     * @date:2015年5月26日下午3:47:20
     * @Description: TODO
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getHeatDishList", method = RequestMethod.POST)
    @ResponseBody
    public void getHeatDishList(@RequestBody String jsonString, HttpServletRequest request,
                                HttpServletResponse response) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        Map<String, Object> map = menuService.getHeatDishList(params);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            logger.info(wholeJsonStr);
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 更新订单中菜品的重量
     * {"tableNo":"3","dishid":"XXX","primarykey":"XXXX","dishnum":"XX"}
     *
     * @author shen
     * @date:2015年5月27日下午5:20:27
     * @Description: TODO
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateDishWeight", method = RequestMethod.POST)
    @ResponseBody
    public void updateDishWeight(@RequestBody String jsonString, HttpServletRequest request,
                                 HttpServletResponse response) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        Map<String, Object> map = orderService.updateDishWeight(params);
        Object source = params.get("source");
        if (map.get("code").equals("0") && (null == source || Constant.SOURCE.POS.equals(source))) {
            String orderid = (String) params.get("orderid");
            notifyService.notifyOrderChange(orderid);
        }
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        logger.info("更新菜品称重结果： " + wholeJsonStr);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();
        } catch (Exception ex) {
            logger.error("更新菜品重量失败！", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 登录接口
     *
     * @param username,登录帐号
     * @param password,登录密码，MD5加密后
     * @param loginType,登录方式       POS登录 1 POS 开业 2 POS 反结算 3 POS 清机 4 POS 结业 5 POS 收银 6 POS 退菜 7
     *                             PAD 开台 8 PAD
     * @return { "result": 0, //0成功 1 失败 "loginTime": "",//服务器当前时间 "fullname":
     * "收银员1(003)", //员工姓名 "msg": "密码错误、没有开台权限" }
     */
    @RequestMapping(value = "/login.json")
    @ResponseBody
    public String loginJson(@RequestBody String body) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Map<String, String> params = JacksonJsonMapper.jsonToObject(body, Map.class);
            String username = params.get("username");
            String password = params.get("password");
            String loginType = params.get("loginType");
            if (!"030202".equals(loginType)) {
                TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
                if (tbOpenBizLog == null) {
                    return JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("未开业，不能登录"));
                }
            }
            Map<String, Object> userMap = userService.validatePasswordLoginTypeByAccount(username, password, loginType);
            Map<String, Object> map = new HashMap<>();
            if (Boolean.valueOf(String.valueOf(userMap.get("success")))) {
                // 登录验证重复登录
                String code = PropertiesUtils.getValue("logintype.030201");
                if (loginType.equals(code)) {
                    String macAddress = params.get("macAddress");
                    // TtellerCash ttellerCashs =
                    // tellerCashService.selectLastUser(username,macAddress);
                    // if( null!=ttellerCashs){
                    // return
                    // JacksonJsonMapper.objectToJson(ReturnMap.getFailureMap("收银员["+ttellerCashs.getUsername()+"]已经登录，请先清机"));
                    // }
                    TtellerCash ttellerCashs = tellerCashService.selectNotClearByUserId(username, macAddress);
                    if (null != ttellerCashs) {
                        return JacksonJsonMapper
                                .objectToJson(ReturnMap.getFailureMap("您已在其他POS上登录，请在已登录POS上进行清机。清机完成后即可在本机登录。"));
                    }
                }
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sDateFormat.format(new java.util.Date());
                map.put("loginTime", date);
                map.put("fullname", String.valueOf(userMap.get("name")));
                resultMap = ReturnMap.getSuccessMap("验证成功", map);
            } else {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sDateFormat.format(new java.util.Date());
                map.put("loginTime", date);
                resultMap = ReturnMap.getFailureMap(String.valueOf(userMap.get("msg")), map);
            }
        } catch (Exception e) {
            logger.error("--->", e);
            resultMap = ReturnMap.getFailureMap("服务器内部错误，请联系管理员");
        }
        return JacksonJsonMapper.objectToJson(resultMap);
    }

    /**
     * 获取员工权限列表
     *
     * @param username,登录帐号
     * @param password,登录密码，MD5加密后
     * @return { "result": 0, //0成功 1 失败 "loginTime": "",//服务器当前时间 "fullname":
     * "收银员1(003)", //员工姓名 "msg": "密码错误、没有开台权限" "rights": [{
     * //所有POS和PAD权限放在里面 "1": 1, "2": 0, "3": 1, "4": 1, "5": 0 "6": 1,
     * "7": 1, "8": 0
     * <p/>
     * }] }
     */
    @RequestMapping(value = "/userrights.json")
    @ResponseBody
    public String userrights(@RequestBody String body) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Map<String, String> params = JacksonJsonMapper.jsonToObject(body, Map.class);
            String username = params.get("username");
            String password = params.get("password");
            User user = userService.validatePasswordByAccount(username, password);
            if (user != null) {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sDateFormat.format(new java.util.Date());
                resultMap.put("result", 0);
                resultMap.put("msg", "验证成功");
                resultMap.put("loginTime", date);
                resultMap.put("fullname", user.getName());

                Map<String, Object> authMap = functionService.getPosPadAuthByAccount(user.getAccount());
                resultMap.put("rights", authMap);
            } else {
                // userService.updateLoginTime(account);
                resultMap.put("result", 1);
                resultMap.put("msg", "密码错误");
            }

        } catch (Exception e) {
            logger.error("查询用户权限失败！", e);
            resultMap.put("result", 1);
            resultMap.put("msg", e.getMessage());
        }
        logger.info("获取用户权限结果,resultMap:" + resultMap);
        return JacksonJsonMapper.objectToJson(resultMap);
    }

    @Autowired
    JmsTemplate jmsTemplate;

    /**
     * 登录接口，验证成功后 返回信息需要有PAD IP和房号绑定状态，并且房号是否是在用餐状态 0 成功 1 失败
     */
    @RequestMapping(value = "/padlogin")
    @ResponseBody
    public String userPadLogin(@RequestBody LoginInfo loginInfo) {
        Map<String, Object> mapRet = new HashMap<String, Object>();
        try {
            TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
            if (tbOpenBizLog == null) {
                logger.error("未开业，不能登录");
                mapRet = ReturnMap.getFailureMap("未开业，不能登录");
                return JacksonJsonMapper.objectToJson(mapRet);
            }

            String pwd = dataDictionaryService.find("SECRETKEY");
            if (!pwd.equals(loginInfo.getPassword())) {
                logger.info("密码错误");
                mapRet = ReturnMap.getFailureMap("密码错误");
            } else {
                logger.info("登录成功");
                mapRet = ReturnMap.getSuccessMap("登录成功");
            }
        } catch (Exception e) {
            logger.error("--->", e);
            mapRet = ReturnMap.getFailureMap("操作失败，服务器内部错误");
        }
        return JacksonJsonMapper.objectToJson(mapRet);
    }

    /**
     * 系统设置接口
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/getSystemSetData")
    @ResponseBody
    public void getSystemSetData(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody String jsonString) {
        Map<String, Object> map = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        List<Map<String, Object>> listFind = dataDictionaryService.findByParams(map);
        if ("ROUNDING".equals(map.get("type"))) {
            map.clear();
            map.put("type", "ACCURACY");
            List<Map<String, Object>> listFind2 = dataDictionaryService.findByParams(map);
            listFind.addAll(listFind2);
        }
        map.clear();
        map.put("rows", listFind);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 手环登陆接口
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/BraceletLoginIn")
    @ResponseBody
    public void BraceletLoginIn(HttpServletRequest request, HttpServletResponse response,
                                @RequestBody String jsonString) {
        Map<String, Object> map = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        int result = 0;
        EmployeeUser employeeUser = new EmployeeUser();
        EmployeeUser employeeUserAll = new EmployeeUser();
        employeeUserAll = null;

        String meid = map.get("meid").toString();
        String userid = map.get("userid").toString();

        Map<String, Object> mapUser = new HashMap<String, Object>();
        mapUser.put("job_number", (String) map.get("userid"));
        mapUser.put("branch_id", PropertiesUtils.getValue("current_branch_id"));
        employeeUser = employeeUserService.getByParams(mapUser);

        Set<String> meidset = new HashSet<String>();

        Map<String, Object> resultmap = new HashMap<String, Object>();
        boolean flag = false;
        boolean loginflag = false;

        if (employeeUser != null) {
            employeeUserAll = employeeUserService.get(employeeUser.getId());
        }

        if (employeeUserAll == null) {// 服务员不存在
            result = 2;
            resultmap.put("result", result);
        } else {
            List<TbUserInstrument> tbUserInstrumentList = new ArrayList<TbUserInstrument>();
            Map<String, Object> userInstrumentMap = new HashMap<String, Object>();
            userInstrumentMap.put("userid", (String) map.get("userid"));
            userInstrumentMap.put("status", "0");
            tbUserInstrumentList = userInstrumentService.findByParams(userInstrumentMap);

            if (tbUserInstrumentList != null && tbUserInstrumentList.size() > 0) {// 之前已经登录,将之前的登录数据修改，重新登录
                TbUserInstrument logoutInfo = tbUserInstrumentList.get(0);
                logoutInfo.setStatus(1);
                logoutInfo.setLogouttime(new Date());
                meidset.add(logoutInfo.getInstrumentid());
                flag = userInstrumentService.updateByid(logoutInfo);
            }

            TbUserInstrument tbUserInstrument = new TbUserInstrument();
            tbUserInstrument.setId(IdentifierUtils.getId().generate().toString());
            tbUserInstrument.setUserid((String) map.get("userid"));
            tbUserInstrument.setInstrumentid((String) map.get("meid"));
            tbUserInstrument.setStatus(0);
            loginflag = userInstrumentService.save(tbUserInstrument);

            if (loginflag) {// 登录保存成功
                Map<String, Object> mapV = new HashMap<String, Object>();
                mapV.put("type", "VIPADDRESS");
                List<Map<String, Object>> listFind = dataDictionaryService.findByParams(mapV);
                resultmap.put("result", 1);
                resultmap.put("VIPADDRESS",
                        listFind != null && listFind.size() > 0 ? listFind.get(0).get("itemid") : "");
                resultmap.put("name", employeeUserAll.getUser().getName());
                resultmap.put("sex", employeeUser.getSex());
            } else {
                resultmap.put("result", 0);
            }
        }
        String wholeJsonStr = JacksonJsonMapper.objectToJson(resultmap);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();
            if (flag) {
                String info = meidset.toString().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
                if (!info.equals(meid)) {
                    StringBuilder messageinfo = new StringBuilder(
                            Constant.TS_URL + Constant.MessageType.msg_2003 + "/");
                    messageinfo.append(userid + "|" + info);
                    new TsThread(messageinfo.toString()).start();
                }
            }
        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 手环退出接口
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/BraceletLoginOut")
    @ResponseBody

    public void BraceletLoginOut(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody String jsonString) {
        Map<String, Object> map = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        TbUserInstrument tbUserInstrument = new TbUserInstrument();
        tbUserInstrument.setUserid((String) map.get("userid"));
        tbUserInstrument.setStatus(1);
        // tbUserInstrument.setInstrumentid((String) map.get("menid"));
        tbUserInstrument.setLogouttime(new Date());// new Date()为获取当前系统时间
        int result = 0;
        boolean a = userInstrumentService.update(tbUserInstrument);
        if (a == true) {
            result = 1;
        }
        map.clear();
        map.put("result", result);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * 手环端回应
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/BraceletReply")
    @ResponseBody
    public void BraceletReply(HttpServletRequest request, HttpServletResponse response,
                              @RequestBody String jsonString) {
        Map<String, Object> map = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        TbMessageInstrument tbMessageInstrument = new TbMessageInstrument();
        tbMessageInstrument.setId((String) map.get("id"));
        tbMessageInstrument.setReply_time(new Date());// new Date()为获取当前系统时间
        int result = 0;
        boolean a = messageInstrumentService.update(tbMessageInstrument);
        TbMessageInstrument messageMap = messageInstrumentService.get((String) map.get("id"));
        if (messageMap != null) {

        }
        if (a == true) {
            result = 1;
        }
        map.clear();
        map.put("result", result);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    /**
     * pad端呼叫服务员
     */

    @SuppressWarnings("unchecked")
    @RequestMapping("/BraceletSent")
    @ResponseBody

    public void BraceletSent(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsonString) {
        Map<String, Object> map = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        int result = userInstrumentService.insertByParams(map);
        map.clear();
        map.put("result", result);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public void uploadImage(@RequestParam(value = "file", required = false) MultipartFile file,
                            HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
            try {
                // 使用 fast dfs 上传文件
                String fileUrlpath = fileService.uploadFile(file.getInputStream(), extName);
                map.put("imagePath", fileUrlpath);
                map.put("flag", "0");
            } catch (IllegalStateException e) {
                logger.error("--->", e);
                e.printStackTrace();
                map.put("imagePath", "");
                map.put("flag", "1");
            } catch (IOException e) {
                logger.error("--->", e);
                e.printStackTrace();
                map.put("imagePath", "");
                map.put("flag", "1");
            } catch (Exception e) {
                logger.error("--->", e);
                e.printStackTrace();
                map.put("imagePath", "");
                map.put("flag", "1");
            }
        }
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();
        } catch (Exception ex) {
            logger.error("--->", ex);
            ex.printStackTrace();
        }
    }

    @RequestMapping("/importdata")
    @ResponseBody
    public String importdata(@RequestBody SqlData sqlData) {
        if (sqlData == null || sqlData.getTenantId() == null || sqlData.getSql() == null
                || "".equals(sqlData.getSql().trim())) {
            return Constant.FAILUREMSG;
        }
        orderService.executeSql(sqlData.getSql());
        return Constant.SUCCESSMSG;
    }

    /**
     * After pos end work ,begin synchronize data
     *
     * @param json
     * @return
     */
    @RequestMapping("/jdesyndata")
    @ResponseBody
    public String jdeSynData(@RequestBody String json) {
        loggers.info("jdeSynData-start:" + json, "");
        @SuppressWarnings("unchecked")
        // Map<String, String> map = JacksonJsonMapper.jsonToObject(json,
                // Map.class);
                Map<String, String> map = JSON.parseObject(json, Map.class);
        String key = map.get("synkey");
        String synKey = PropertiesUtils.getValue("SYNKEY");
        if (!synKey.equalsIgnoreCase(key)) {
            return Constant.FAILUREMSG;
        }
        ResultDto dto = new ResultDto();
        // 获取同步数据的传送方式
        String type = PropertiesUtils.getValue("SYN_DATA_TYPE");
        try {
            // 选择处理方式
            dto = selectMethod(type);

        } catch (SysException e) {
            loggers.error("门店上传到总店数据失败", e);
            if (e.getCode().equals("") || "null".equals(e.getCode()) || null == e.getCode()) {
                // 异常处理机制
                dto = exceptionDeal(type);
                // 使用原有代码MQ机制时出现的异常处理
            } else {
                dto = new ResultDto();
                dto.setInfo(ResultMessage.OTHER_EXEC_ERROR);
            }
        } catch (Exception e) {
            loggers.error("门店上传到总店数据失败2", e);
            dto = null;
        }
        if (dto == null) {
            dto = new ResultDto();
            dto.setInfo(ResultMessage.NO_RETURN_MESSAGE);
        }
        loggers.info("jdeSynData-end:" + dto, "");
        // return JacksonJsonMapper.objectToJson(resultMap);
        return JSON.toJSONString(dto);
    }

    @RequestMapping("/synDataByDate.do")
    @ResponseBody
    public String synDataByDate(String json, String startDate, String endDate) {

        loggers.info("synDataByDate-start:" + json + "," + startDate + "," + endDate, "");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("opendate", startDate);
        map.put("enddate", endDate);
        BranchDataSyn.TL.set(map);

        return jdeSynData(json);
    }

    // 出现异常处理机制,重新执行直到成功,最多执行3次
    private ResultDto exceptionDeal(String type) {
        ResultDto dto = new ResultDto();
        // 后面执行是否成功的标志
        boolean afterStatus = false;
        // 如果异常,则重新执行三次,直到成功或者3次执行完(后期建议通过任务处理器优化)
        for (int i = 0; i < 3; i++) {
            int j = i + 1;
            try {
                dto = selectMethod(type);
                afterStatus = true;
                loggers.info("第" + j + "次执行重传成功");
                break;
            } catch (SysException sysEx) {
                loggers.error("第" + j + "次执行重传失败", sysEx);
            } catch (Exception exc) {
                loggers.error("门店上传到总店数据失败", exc);
                dto = null;
            }
        }
        // 连续3次执行失败
        if (afterStatus == false) {
            dto.setInfo(ResultMessage.INTERNET_EXE);
        }
        return dto;
    }

    /**
     * @return ResultDto 结果集对象
     * @throws SysException
     * @Description:选择要执行的方法路径
     * @create: 余城序
     * @Modification:
     */
    private ResultDto selectMethod(String type) throws Exception {
        ResultDto dto = new ResultDto();
        // MQ传输sql方式,http接口化方式稳定后将被舍弃
        if (type.equals("1")) {
            if (branchDataSyn.synBranchData()) {
                dto.setInfo(ResultMessage.SUCCESS);
            } else {
                dto.setCode("1");
                dto.setMessage("修改数据同步的状态失败");
            }
            // HTTP传输sql方式,http接口化传输方式稳定后将被舍弃
        } else if (type.equals("2")) {
            dto = executeSyn();
            // HTTP接口化传输方式
        } else if (type.equals("3")) {
            dto = branchDataSyn.synData();
        }
        return dto;
    }

    // 门店同步数据方法执行
    private ResultDto executeSyn() throws SysException {
        return branchDataSyn.synLocalData();
    }

    /**
     * Excute by pos client
     *
     * @param json
     * @return
     */
    @RequestMapping("/endwork")
    @ResponseBody
    public String endwork(@RequestBody String json) {

        @SuppressWarnings("unchecked")
        Map<String, String> map = JacksonJsonMapper.jsonToObject(json, Map.class);
        String userName = map.get("userName");
        Map<String, String> retMap = new HashMap<String, String>();

        List<Torder> orderList = orderService.verifyAllOrder();

        if (orderList != null && orderList.size() > 0) {
            for (Torder order : orderList) {
                if ("order".equalsIgnoreCase(order.getOrderid())) {
                    // 1.判断是否所有的订单已经结算
                    retMap.put("result", "1");
                } else if ("teller".equalsIgnoreCase(order.getOrderid())) {
                    // 2.判断是否全部清机
                    retMap.put("result", "2");
                } else if ("table".equalsIgnoreCase(order.getOrderid())) {
                    // 3.所有的桌台已经处理
                    retMap.put("result", "3");
                }
                return JacksonJsonMapper.objectToJson(retMap);
            }
        }

        String isSucess = "0";
        isSucess = orderService.callEndWork(userName, isSucess);
        if ("1".equals(isSucess)) {
            retMap.put("result", "4");
            return JacksonJsonMapper.objectToJson(retMap);
        } else {
            try {
                branchDataSyn.synBranchData();
                retMap.put("result", "0");
            } catch (Exception e) {
                logger.error("--->", e);
                retMap.put("result", "1");
                retMap.put("msg", e.getMessage());
            }
            return JacksonJsonMapper.objectToJson(retMap);
        }

    }

    /**
     * 消息中心插入信息
     *
     * @param json
     * @return
     */
    @RequestMapping("/insertmsg")
    @ResponseBody
    public String insertMsg(@RequestBody String json) {

        Map<String, String> map = JacksonJsonMapper.jsonToObject(json, Map.class);
        String key = map.get("synkey");

        return Constant.SUCCESSMSG;
    }

    /**
     * 消息推送
     *
     * @param json
     * @return
     */
    class PadThread implements Runnable {
        private Logger logger = LoggerFactory.getLogger(PadThread.class);
        private boolean restarted = false;
        String finaltableno;
        String finalmsgType;
        String finalcallStatus;
        String backno;

        public PadThread(String finaltableno, String msgType) {
            this.finaltableno = finaltableno;
            this.finalmsgType = msgType;
            this.finalcallStatus = "0";
        }

        @Override
        public void run() {
            Map<String, Object> params = new HashMap<String, Object>(1);
            params.put("tableNo", finaltableno);
            List<Map<String, Object>> tableList = tableService.find(params);
            if (tableList != null && tableList.size() > 0) {
                String orderinfoid = String.valueOf(tableList.get(0).get("orderid"));
                Torder torder = torderMapper.get(orderinfoid);
                if (torder != null && torder.getUserid() != "") {
                    String userid = torder.getUserid();
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("status", "0");
                    map1.put("userid", userid);
                    List<TbUserInstrument> listuser = tbUserInstrumentDao.findByParams(map1);
                    if (listuser != null && listuser.size() > 0) {
                        // 服务员还在线
                        // 服务员编号|消息类型|区号|台号|消息id
                    } else {
                        // 服务员退出了,找到同一区的服务员 进行推送
                        String areaid = String.valueOf(tableList.get(0).get("areaid"));
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("areaid", areaid);
                        map.put("status", "1");
                        List<Map<String, Object>> retableList = tableService.find(map);
                        String useridstr = callService.findrelateUserid(retableList, finaltableno);
                        if (useridstr != null && !useridstr.equals("")) {
                            userid = useridstr;
                        }
                    }

                    if (!userid.equals("")) {
                        StringBuilder messageinfo = new StringBuilder();
                        messageinfo.append(userid + "|" + finalmsgType + "|" + finalcallStatus + "|"
                                + tableList.get(0).get("areaname") + "|" + finaltableno + "|"
                                + IdentifierUtils.getId().generate().toString());
                        msgForwardService.broadCastMsg4Netty(Constant.MessageType.msg_2011, 10 * 60,
                                messageinfo.toString());
                        System.out.println("推送成功");
                    }
                }
            }
            // 根据动作打印不同的小票
        }

        private void restartAndRetry() {
            // 已重启过则放弃
            if (restarted) {
                return;
            }
            restarted = true;
            if (DataServerUtil.restart()) {
                run();
            } else {
                logger.error("尝试重启DataServer失败");
            }
        }
    }

    /**
     * 查询所有未清机的POS列表
     *
     * @return
     */
    @RequestMapping("/findUncleanPosList")
    @ResponseBody
    public String findUncleanPosList() {

        Map<String, Object> retMap = new HashMap<>();
        try {
            List<TtellerCash> list = tellerCashService.findUncleanPosList();
            retMap.put("result", "0");
            retMap.put("detail", list);
        } catch (Exception e) {
            retMap.put("result", "1");
            retMap.put("msg", e.getMessage());
            logger.error("--->", e);
        }
        return JacksonJsonMapper.objectToJson(retMap);
    }

    /**
     * 获取开业结业时间
     *
     * @return
     */
    @RequestMapping("/getOpenEndTime")
    @ResponseBody
    public String getOpenEndTime() {
        Map<String, Object> retMap = new HashMap<>();
        try {
            Map<String, Object> timeMap = dataDictionaryService.getOpenEndTime("BIZPERIODDATE");
            // timeMap.remove("datetype");
            retMap.put("result", "0");
            retMap.put("detail", timeMap);
        } catch (Exception e) {
            retMap.put("result", "1");
            retMap.put("msg", e.getMessage());
            logger.error("--->", e);
        }
        return JacksonJsonMapper.objectToJson(retMap);
    }

    /**
     * 查询是否结业
     *
     * @return
     */
    @RequestMapping("/isEndWork")
    @ResponseBody
    public String isEndWork() {
        Map<String, Object> retMap = new HashMap<>();
        try {
            TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
            if (tbOpenBizLog == null) {
                retMap.put("result", "0");
                retMap.put("detail", true);
            } else {
                retMap.put("result", "0");
                retMap.put("detail", false);
            }
        } catch (Exception e) {
            retMap.put("result", "1");
            retMap.put("msg", e.getMessage());
            logger.error("--->", e);
        }
        return JacksonJsonMapper.objectToJson(retMap);
    }

    /**
     * 查询昨天是否结业
     *
     * @return
     */
    @RequestMapping("/isYesterdayEndWork")
    @ResponseBody
    public String isYesterdayEndWork() {
        Map<String, Object> retMap = new HashMap<>();
        try {
            TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
            if (tbOpenBizLog == null) {
                retMap.put("result", "0");
                retMap.put("detail", true);
            } else {
                Date date = tbOpenBizLog.getOpendate();
                if (DateUtils.isToday(date)) {
                    // 如果是今天，则已经结业
                    retMap.put("result", "0");
                    retMap.put("detail", true);
                } else {
                    // 如果不是今天
                    Map<String, Object> timeMap = dataDictionaryService.getOpenEndTime("BIZPERIODDATE");
                    if ("T".equals(timeMap.get("datetype"))) { // "T"代表当日
                        retMap.put("result", "0");
                        retMap.put("detail", false);
                    } else {
                        retMap.put("result", "0");
                        retMap.put("detail", !isTimeOver(date, 1, timeMap.get("begintime").toString()));
                    }
                }
                // retMap.put("result", "0");
                // retMap.put("detail", false);
            }
        } catch (Exception e) {
            retMap.put("result", "1");
            retMap.put("msg", e.getMessage());
            logger.error("--->", e);
        }
        return JacksonJsonMapper.objectToJson(retMap);
    }

    private static boolean isTimeOver(Date openDate, int dayCount, String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(openDate);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + dayCount);
        try {
            String[] times = time.split(":");
            int hour = Integer.valueOf(times[0]);
            int minute = 0;
            if (times.length > 1) {
                minute = Integer.valueOf(times[1]);
            }
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            return calendar.getTime().getTime() <= System.currentTimeMillis();
        } catch (Exception e) {
//			logger.error("时间格式有误："+time);
            return false;
        }
    }

    /**
     * 获取手环通知消息的类型
     *
     * @return
     */
    @RequestMapping("/getNotification")
    @ResponseBody
    public Map<String, Object> getNotification() {
        List<Map<String, Object>> maps = new ArrayList<>();
        try {
            maps = dataDictionaryService.getNotificationDate("NOTIFICATION");
        } catch (Exception e) {
            logger.error("--->", e);
            e.printStackTrace();
            return ReturnMap.getFailureMap("数据异常，请联系管理员");
        }
//		//后台没配置的情况，给PAD返回成功
//		if (maps == null || maps.size() <= 0) {
//			return ReturnMap.getFailureMap("没有查询到相应的数据");
//		}
        return ReturnMap.getSuccessMap("查询成功", maps);
    }

    /**
     * Pad端获取Logo图和背景图
     *
     * @return
     */
    @RequestMapping("/getPadImg.json")
    @ResponseBody
    public String getPadImg() {
        Map<String, Object> retMap = new HashMap<>();
        try {
            List<Map<String, Object>> maps = systemServiceImpl.getImgByType("PADIMG");
            retMap.put("ImgIp", PropertiesUtils.getValue("fastdfs.url"));
            retMap.put("result", "0");
            retMap.put("msg", "成功");
            retMap.put("detail", maps);
        } catch (Exception e) {
            retMap.put("result", "1");
            retMap.put("msg", e.getMessage());
            logger.error("-->", e);
        }
        return JacksonJsonMapper.objectToJson(retMap);
    }

    /**
     * 查询一页菜谱配置信息
     *
     * @return
     */
    @RequestMapping("/onepageinfo.json")
    @ResponseBody
    public String onepageinfo() {
        Map<String, Object> resultMap = new HashMap<>();
        Map map = new HashMap<>();
        map.put("type", SystemConstant.ONEPAGETYPE.type());
        List<Map<String, Object>> list = dataDictionaryService.findByParams(map);
        if (list == null || list.size() == 0) {// 没有数据
            resultMap.put("code", 1);
            resultMap.put("msg", "后台没有配置信息");
            resultMap.put("data", "");
        } else {
            resultMap.put("code", 0);
            resultMap.put("data", list.get(0).get("status").toString());
        }

        return JacksonJsonMapper.objectToJson(resultMap);
    }

    /**
     * 获取品项销售明细的打印数据
     *
     * @return
     */
    @RequestMapping("/getItemSellDetail.json")
    @ResponseBody
    public String getItemSellDetail(String flag) {
        Map<String, Object> timeMap = getTime(flag);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Map<String, Object>> result = orderDetailService.itemSellDetail(timeMap);
            resultMap.put("result", 0);
            resultMap.put("mag", "");
            resultMap.put("data", result);
            resultMap.put("time", timeMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), "");
            resultMap.put("result", 1);
            resultMap.put("mag", "获取数据失败");
            resultMap.put("data", "");
            resultMap.put("time", timeMap);
            e.printStackTrace();
        }
        return JacksonJsonMapper.objectToJson(resultMap);
    }

    /**
     * 获取开始结束时间
     *
     * @param falg
     * @return
     */
    private Map<String, Object> getTime(String falg) {
        Map<String, Object> map = new HashMap<>();
        String startTime = null;
        String endTime = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = df.format(new Date());

        if (falg.equals("1")) { // 今日
            startTime = DateUtils.today() + " 00:00:00";
        } else if (falg.equals("2")) { // 本周
            startTime = DateUtils.weekOfFirstDay() + " 00:00:00";
        } else if (falg.equals("3")) { // 本月
            startTime = DateUtils.monthOfFirstDay() + " 00:00:00";
        } else if (falg.equals("4")) { // 上月
            startTime = DateUtils.beforeMonthOfFirstDay() + " 00:00:00";
            endTime = DateUtils.beforeMonthOfLastDay() + " 23:59:59";
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return map;
    }

    // 服务员pad

    /**
     * 服务员pad获取信息
     *
     * @param jsonString
     * @param request
     * @param response
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getOrderInfowaiter", method = RequestMethod.POST)
    @ResponseBody
    public void getOrderInfowaiter(@RequestBody String jsonString, HttpServletRequest request,
                                   HttpServletResponse response) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        Map<String, Object> map = orderService.switchPadOrderInfowaiter(params);
        String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
        try {
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setContentType("text/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(wholeJsonStr.getBytes("UTF-8"));
            stream.flush();
            stream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * PAD 开台 桌号 ，人数
     *
     * @author zhao
     */
    @RequestMapping("/setorderwaiter")
    @ResponseBody
    public String startOrderInfowaiter(@RequestBody Torder order) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(order));
        record.setPadpath("setorder");
        jsonRecordService.insertJsonRecord(record);

        String returnStr = orderService.startOrderwaiter(order);

        JSONObject returnobject = JSONObject.fromObject(returnStr);

        if (!StringUtils.isBlank(order.getIsShield()) && order.getIsShield().equals("0")
                && returnobject.containsKey("result") && !StringUtils.isBlank(returnobject.getString("result"))
                && returnobject.getString("result").equals("0")) {
            try {
                String orderid = returnobject.containsKey("orderid") ? returnobject.getString("orderid") : "";
                if (!StringUtils.isBlank(orderid)) {
                    giftService.updateOrderStatus(orderid);
                }

            } catch (Exception ex) {

            }
        }
        return returnStr;
    }

    /**
     * PAD 开台 更新 桌号 人数
     *
     * @author zhao
     */
    @RequestMapping("/updateorderwaiter")
    @ResponseBody
    public String updateorderwaiter(@RequestBody Torder order) {

        TJsonRecord record = new TJsonRecord();
        record.setJson(JacksonJsonMapper.objectToJson(order));
        record.setPadpath("updateorder");
        jsonRecordService.insertJsonRecord(record);

        return orderService.updateOrderwaiter(order);
    }

    // end

    // conifg

    /**
     * 保存修改配置信息
     *
     * @param padConfig
     * @return
     */
    @RequestMapping("/saveorupdate")
    @ResponseBody
    public String saveorupdate(PadConfig padConfig) {
        int result = padConfigService.saveorupdate(padConfig);
        Map<String, Object> map = new HashMap<>();
        if (result == 0) {
            map.put("code", 1);
            map.put("msg", "操作失败");
        } else {
            map.put("code", 0);
        }
        return JacksonJsonMapper.objectToJson(map);
    }

    private String getValue(String str) {
        if (str == null || str.length() <= 0 || "".equals(str)) {
            return null;
        }
        return str;
    }

    /**
     * 删除文件
     *
     * @param request
     * @param seatImagename
     * @param fileurl0
     * @param fileurl1
     * @return
     */
    @RequestMapping("/deletefile")
    @ResponseBody
    public String deletefile(HttpServletRequest request, String seatImagename0, String seatImagename1, String fileurl0,
                             String fileurl1) {
        Map<String, Object> map = new HashMap<>();
        fileurl0 = getValue(fileurl0);
        fileurl1 = getValue(fileurl1);
        seatImagename0 = getValue(seatImagename0);
        seatImagename1 = getValue(seatImagename1);
        String imagenames = "";
        String imageurls = "";
        if (fileurl0 != null && fileurl1 == null) {
            imagenames = seatImagename0;
            imageurls = fileurl0;
        }
        if (fileurl0 == null && fileurl1 != null) {
            imagenames = seatImagename1;
            imageurls = fileurl1;
        }
        if (fileurl0 != null && fileurl1 != null) {
            imagenames = seatImagename0 + ";" + seatImagename1;
            imageurls = fileurl0 + ";" + fileurl1;
        }
        PadConfig config = new PadConfig();
        config.setSeatimagenames(imagenames);
        config.setSeatimageurls(imageurls);
        int result = padConfigService.saveorupdate(config);
        if (result == 0) {
            map.put("code", 1);
            map.put("msg", "操作失败");
        } else {
            map.put("code", 0);
        }
        return JacksonJsonMapper.objectToJson(map);
    }

    /**
     * 座位图上传 必须有file对象才会进入该方法
     *
     * @param seatImagefiles
     * @param padConfig
     * @return
     */
    @RequestMapping("/importfile")
    @ResponseBody
    public String importfile(HttpServletRequest request, String[] seatImagename, String fileurl0, String fileurl1) {
        fileurl0 = getValue(fileurl0);
        fileurl1 = getValue(fileurl1);
        String realpath = request.getSession().getServletContext().getRealPath("");
        System.out.println(realpath);
        MultipartHttpServletRequest multipartRq = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRq.getFileMap();
        List<CommonsMultipartFile> seatImagefiles = new ArrayList<>();
        if (fileMap.get("seatImgIpt0") != null) {
            MultipartFile file = fileMap.get("seatImgIpt0");
            seatImagefiles.add((CommonsMultipartFile) file);
            fileurl0 = null;
        }
        if (fileMap.get("seatImgIpt1") != null) {
            MultipartFile file = fileMap.get("seatImgIpt1");
            seatImagefiles.add((CommonsMultipartFile) file);
            fileurl1 = null;
        }
        if (fileurl0 == null && fileurl1 == null) {
            return filetwonewFile(seatImagefiles, realpath, seatImagename);// 上传2个新文件
        }

        Map<String, Object> map = new HashMap<>();
        PadConfig padConfig = padConfigService.getconfiginfos();
        String names = padConfig.getSeatimagenames();
        if (fileurl0 != null && fileurl1 == null) {// 文件0为旧文件，file1为新文件
            String newfilename = seatImagefiles.get(0).getOriginalFilename();
            if (newfilename.indexOf(".") != -1) {
                newfilename = UUID.randomUUID().toString().replaceAll("-", "")
                        + newfilename.substring(newfilename.indexOf(".")).toLowerCase();
            }
            int result = fileupload(seatImagefiles.get(0),
                    realpath + File.separator + "upload" + File.separator + newfilename);
            if (result == 0) {// 成功
                PadConfig config = new PadConfig();
                config.setSeatimagenames(names.split(";")[0] + ";" + seatImagename[0]);
                config.setSeatimageurls(fileurl0 + ";" + "upload" + File.separator + newfilename);
                padConfigService.saveorupdate(config);
                map.put("code", 0);
                map.put("fileurl0", fileurl0);
                map.put("fileurl1", "upload" + File.separator + newfilename);
                return JacksonJsonMapper.objectToJson(map);
            } else {
                map.put("code", 1);
                map.put("msg", "上传文件失败");
                return JacksonJsonMapper.objectToJson(map);
            }
        }

        if (fileurl0 == null && fileurl1 != null) {// 文件0为新文件，file1为旧文件
            String newfilename = seatImagefiles.get(0).getOriginalFilename();
            if (newfilename.indexOf(".") != -1) {
                newfilename = UUID.randomUUID().toString().replaceAll("-", "")
                        + newfilename.substring(newfilename.indexOf(".")).toLowerCase();
            }
            int result = fileupload(seatImagefiles.get(0),
                    realpath + File.separator + "upload" + File.separator + newfilename);
            if (result == 0) {// 成功
                PadConfig config = new PadConfig();
                config.setSeatimagenames(seatImagename[0] + ";" + names.split(";")[0]);
                config.setSeatimageurls("upload" + File.separator + newfilename + ";" + fileurl1);
                padConfigService.saveorupdate(config);
                map.put("code", 0);
                map.put("fileurl0", "upload" + File.separator + newfilename);
                map.put("fileurl1", fileurl1);
                return JacksonJsonMapper.objectToJson(map);
            } else {
                map.put("code", 1);
                map.put("msg", "上传文件失败");
                return JacksonJsonMapper.objectToJson(map);
            }
        }

        map.put("code", 0);
        return JacksonJsonMapper.objectToJson(map);
    }

    private String filetwonewFile(List<CommonsMultipartFile> seatImagefiles, String realpath, String[] seatImagename) {
        String fileurl0 = "";
        String fileurl1 = "";
        String seatimagenames = "";
        String seatimageurls = "";
        int temp = 0;
        Map<String, Object> map = new HashMap<>();
        for (CommonsMultipartFile commonsMultipartFile : seatImagefiles) {
            if (!commonsMultipartFile.isEmpty()) {
                String newfilename = commonsMultipartFile.getOriginalFilename();
                if (newfilename.indexOf(".") != -1) {
                    newfilename = UUID.randomUUID().toString().replaceAll("-", "")
                            + newfilename.substring(newfilename.indexOf(".")).toLowerCase();
                }
                String imagelocation = realpath + File.separator + "upload" + File.separator + newfilename;
                File file = new File(imagelocation);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (temp == 0) {
                    fileurl0 = "upload" + File.separator + newfilename;
                } else {
                    fileurl1 = "upload" + File.separator + newfilename;
                }
                // 文件上传
                fileupload(commonsMultipartFile, imagelocation);
                seatimagenames = seatimagenames + seatImagename[temp++] + ";";
                seatimageurls = seatimageurls + "upload" + File.separator + newfilename + ";";
                map.put("fileurl0", fileurl0);
                map.put("fileurl1", fileurl1);
            }

            PadConfig config = new PadConfig();
            if (seatimagenames.length() > 1 && seatimageurls.length() > 1) {
                config.setSeatimagenames(seatimagenames.substring(0, seatimagenames.length() - 1));
                config.setSeatimageurls(seatimageurls.substring(0, seatimageurls.length() - 1));
                int result = padConfigService.saveorupdate(config);

                if (result == 0) {
                    map.put("code", 1);
                    map.put("msg", "操作失败");
                } else {
                    map.put("code", 0);
                }
            }
        }
        return JacksonJsonMapper.objectToJson(map);

	}

	/**
	 * 上传log背景
	 * pad背景
	 * @param request
	 * @param x
	 * @param y
	 * @param h
	 * @param w
	 * @return
	 * @throws FileNotFoundException
	 *
	 */
	@RequestMapping("/catImg")
	@ResponseBody
	public String catImg(HttpServletRequest request,@RequestParam("x") String x,@RequestParam("y") String y,
			@RequestParam("h") String h,@RequestParam("w") String w) throws FileNotFoundException{

		//上传文件跟路径
		String realpath = request.getSession().getServletContext().getRealPath("");
		//实际文件路径
		String imagelocation = realpath+File.separator+ "upload" + File.separator;
		createDir(imagelocation);
		//
		Map<String, Object> map = new HashMap<String, Object>();
		MultipartHttpServletRequest multipartRq = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRq.getFileMap();
		MultipartFile file ;
		if(fileMap.get("logoimg") == null){
			file = fileMap.get("backgroundimg");
			map.put("type", "bg");
		}else{
			file = fileMap.get("logoimg");
			map.put("type", "logo");
		}
		String fileName = null;
		if (!file.isEmpty()) {
			 fileName = file.getOriginalFilename();
			imagelocation= imagelocation+fileName;
			try {
				//存储数据到字典
				  this.fileupload(file.getInputStream(),imagelocation);
			} catch (Exception e) {
				logger.error("图片上传失败"+e.getMessage(), "");
				e.printStackTrace();
				return JacksonJsonMapper.objectToJson(map);
			}
		}

		int imageX = Math.round(Float.valueOf(x == null || x == "" ? "0" : x));
		int imageY = Math.round(Float.valueOf(y == null || y == "" ? "0" : y));
		int imageH = Math.round(Float.valueOf(h == null || h == "" ? "0" : h));
		int imageW = Math.round(Float.valueOf(w == null || w == "" ? "0" : w));


		String fileupload=File.separator+ "upload" + File.separator;

		String inputDir = request.getRealPath("") +fileupload;
		ImageCompress imageCompress = new ImageCompress();
		String afterCatImgUrl = "";
		String imageurl="" ;
		if(imageH<=0||imageW<0){
			imageurl=fileName;
		}else{
			imageurl=imageCompress.imgCut(inputDir, fileName, imageX, imageY, imageW, imageH);
		}
		if (!"".equals(imageurl)) {
			  afterCatImgUrl="upload" + File.separator+imageurl;
			  if(!imageurl.equals(fileName)){
				  this.delFile(imagelocation);
			  }
		}
		map.put("image", afterCatImgUrl);
		return JacksonJsonMapper.objectToJson(map);
	}

	/**
	 * 设置logo图或背景图
	 * @return
	 */
	@RequestMapping("/setImg")
	@ResponseBody
	public String setImg(TbDataDictionary dictionary){
//		  String delPathName=map.get("type").equals("bg")?padcon.getBackgroudurl():padcon.getLogourl();
//		  this.delFile(inputDir+delPathName);
//		PadConfig padcon = padConfigService.saveorupdateToDic(map.get("type").equals("bg")?"2":"1", afterCatImgUrl);
        padConfigService.saveorupdateToDic(dictionary);
        return "system/systemSet";
    }

    /**
     * 获取支付方式
     *
     * @return
     */
    @RequestMapping("/getPayways")
    @ResponseBody
    public String getPayways() {
        return JSON.toJSONString(ReturnMap.getSuccessMap(paywayService.getPayways()));
    }

    /**
     * 保存自定义支付方式
     *
     * @return
     */
    @RequestMapping("/savePayway")
    @ResponseBody
    public String savePayway(@RequestBody String body) {
        List<Map<String, Object>> payways = JSON.parseObject(body, List.class);
        Map result;
        try {
            paywayService.savePayways(payways);
            result = ReturnMap.getSuccessMap();
        } catch (Exception e) {
            logger.error("->", e);
            result = ReturnMap.getFailureMap("服务器异常");
        }
        return JSON.toJSONString(result);
    }

    private void delFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    private int fileupload(InputStream inuStream, String imagelocation) {
        try {
            // 拿到输出流，同时重命名上传的文件
            FileOutputStream os = new FileOutputStream(imagelocation);
            // 拿到上传文件的输入流
            InputStream in = inuStream;

			// 以写字节的方式写文件
			int b = 0;
			while ((b = in.read()) != -1) {
				os.write(b);
			}
			os.flush();
			os.close();
			in.close();
			// 保存成功
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}

		return 0;
	}
	private void createDir(String path){
		File file =new File(path);
		//如果文件夹不存在则创建
		if  (!file .exists()  && !file .isDirectory())
		{
		    file .mkdirs();
		}
	}

	/**
	 * 0成功 1失败
	 *
	 * @param commonsMultipartFile
	 * @param imagelocation
	 * @return
	 */
	private int fileupload(CommonsMultipartFile commonsMultipartFile, String imagelocation) {
		try {
			// 拿到输出流，同时重命名上传的文件
			FileOutputStream os = new FileOutputStream(imagelocation);
			// 拿到上传文件的输入流
			FileInputStream in = (FileInputStream) commonsMultipartFile.getInputStream();

            // 以写字节的方式写文件
            int b = 0;
            while ((b = in.read()) != -1) {
                os.write(b);
            }
            os.flush();
            os.close();
            in.close();
            // 保存成功
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    /**
     * web获取所有可配置项
     */
    @RequestMapping("/getconfiginfos")
    @ResponseBody
    public String getconfiginfos() {
        PadConfig padConfig = padConfigService.getconfiginfos();
        Map<String, Object> map = new HashMap<>();
        if (padConfig == null) {
            map = ReturnMap.getFailureMap("门店没有配置相关信息");
        } else {
            map = ReturnMap.getSuccessMap(padConfig);
        }
        return JacksonJsonMapper.objectToJson(map);
    }

    /**
     * pad获取配置信息
     *
     * @return
     */
    @RequestMapping("/padconfiginfos")
    @ResponseBody
    public String padconfiginfos() {
        BasePadResponse<PadConfig> basePadResponse = new BasePadResponse();
        PadConfig padConfig = padConfigService.getconfiginfos();
        if (padConfig == null) {
            basePadResponse.setCode(1);
            basePadResponse.setMsg("门店没有配置相关信息");
        } else {
            basePadResponse.setCode(0);
            basePadResponse.setMsg("");
            basePadResponse.setData(padConfig);
        }
        return JacksonJsonMapper.objectToJson(basePadResponse);
    }

    // end config

    /**
     * 消息中心查询信息
     *
     * @param json
     * @return
     */
    @RequestMapping("/querymsg")
    @ResponseBody
    public String queryMsg(@RequestBody String json) {

        return Constant.SUCCESSMSG;
    }

    /**
     * 消息中心更新信息
     *
     * @param json
     * @return
     */
    @RequestMapping("/updatemsg")
    @ResponseBody
    public String updateMsg(@RequestBody String json) {

        return Constant.SUCCESSMSG;
    }

    @RequestMapping("/getbranchinfo")
    @ResponseBody
    public String getBranchInfo() {
        Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
        Map<String, Object> res = new HashMap<>();
        if (branchInfo != null && !branchInfo.isEmpty()) {
            res = ReturnMap.getSuccessMap(branchInfo);
            return JacksonJsonMapper.objectToJson(res);
        }
        res = ReturnMap.getFailureMap("查询门店信息失败");
        return JacksonJsonMapper.objectToJson(res);
    }

    @Autowired
    BranchDataSyn branchDataSyn;

    @Autowired
    private FileService fileService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PicturesService picturesService;
    @Autowired
    private ToperationLogService toperationLogService;
    @Autowired
    private DishTypeService dishTypeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TableService tableService;

    @Autowired
    OrderSettleService orderSettleService;

    @Autowired
    DishService dishService;

    @Autowired
    OpenBizService openBizService;

    @Autowired
    DataDictionaryService dataDictionaryService;

    @Autowired
    JsonRecordService jsonRecordService;

    @Autowired
    @Qualifier("t_userService")
    UserService userService;// 新用户service对象

    @Autowired
    FunctionService functionService;// 新方法service对象

    @Autowired
    EmployeeUserService employeeUserService;

    @Autowired
    private ComboDishService comboDishService;

    @Autowired
    private PreferentialActivityService preferentialActivityService;
    @Autowired
    private UserInstrumentService userInstrumentService;
    @Autowired
    private MessageInstrumentService messageInstrumentService;

    @Autowired
    private CallWaiterService callWaiterService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private GiftLogService giftService;
    @Autowired
    private TbUserInstrumentDao tbUserInstrumentDao;
    @Autowired
    TorderMapper torderMapper;
    @Autowired
    TsettlementMapper tsettlementMapper;
    @Autowired
    private CallWaiterService callService;
    @Autowired
    private TtellerCashDao tellerCashService;
    @Autowired
    private TbBranchDao tbBranchDao;
    @Autowired
    private SystemServiceImpl systemServiceImpl;
    @Autowired
    private PadConfigService padConfigService;
    @Autowired
    private TorderDetailPreferentialService torderDetailPreferentialService;
    @Autowired
    private PaywayService paywayService;
    @Autowired
    private OrderOpService orderOpService;
    private Logger logger = LoggerFactory.getLogger(PadInterfaceController.class);

	private Logger loggers = org.slf4j.LoggerFactory.getLogger(PadInterfaceController.class);

    @Autowired
    private TableAreaService tableAreaService;
	@Autowired
	private TServiceChargeService chargeService;

}
