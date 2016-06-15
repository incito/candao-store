package com.candao.www.webroom.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.candao.www.utils.DataServerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
import com.candao.www.data.dao.TbBranchDao;
import com.candao.www.constant.SystemConstant;
import com.candao.www.data.dao.TbUserInstrumentDao;
import com.candao.www.data.dao.TorderMapper;
import com.candao.www.data.dao.TtellerCashDao;
import com.candao.www.data.model.EmployeeUser;
import com.candao.www.data.model.TJsonRecord;
import com.candao.www.data.model.TbMessageInstrument;
import com.candao.www.data.model.TbOpenBizLog;
import com.candao.www.data.model.TbTable;
import com.candao.www.data.model.TbUserInstrument;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.Tinvoice;
import com.candao.www.data.model.ToperationLog;
import com.candao.www.data.model.Torder;
import com.candao.www.data.model.TorderDetail;
import com.candao.www.data.model.TtellerCash;
import com.candao.www.data.model.User;
import com.candao.www.permit.common.Constants;
import com.candao.www.permit.service.EmployeeUserService;
import com.candao.www.permit.service.FunctionService;
import com.candao.www.permit.service.UserService;
import com.candao.www.security.service.LoginService;
import com.candao.www.timedtask.BranchDataSyn;
import com.candao.www.utils.HttpRequestor;
import com.candao.www.utils.ReturnMap;
import com.candao.www.utils.TsThread;
import com.candao.www.webroom.model.LoginInfo;
import com.candao.www.webroom.model.OperPreferentialResult;
import com.candao.www.webroom.model.Order;
import com.candao.www.webroom.model.SettlementInfo;
import com.candao.www.webroom.model.SqlData;
import com.candao.www.webroom.model.Table;
import com.candao.www.webroom.model.TableStatus;
import com.candao.www.webroom.model.UrgeDish;
import com.candao.www.webroom.service.CallWaiterService;
import com.candao.www.webroom.service.ComboDishService;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.DishTypeService;
import com.candao.www.webroom.service.GiftLogService;
import com.candao.www.webroom.service.InstrumentService;
import com.candao.www.webroom.service.InvoiceService;
import com.candao.www.webroom.service.JsonRecordService;
import com.candao.www.webroom.service.MemeberService;
import com.candao.www.webroom.service.MenuService;
import com.candao.www.webroom.service.MessageInstrumentService;
import com.candao.www.webroom.service.OpenBizService;
import com.candao.www.webroom.service.OrderDetailService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.OrderSettleService;
import com.candao.www.webroom.service.PicturesService;
import com.candao.www.webroom.service.PreferentialActivityService;
import com.candao.www.webroom.service.TableService;
import com.candao.www.webroom.service.ToperationLogService;
import com.candao.www.webroom.service.UserInstrumentService;
import com.candao.www.webroom.service.impl.SystemServiceImpl;

import ch.qos.logback.core.net.SyslogConstants;
import net.sf.json.JSONObject;
/**
 * 所有pad 端处理的接口
 *  <pre>
 * 所有pad 端处理的接口
 * Copyright : Copyright  Pandoranews 2014 ,Inc. All right
 * Company : 凯盈资讯科技有限公司
 * </pre>
 * @author  zhao
 * @version 1.0
 * @date 2014年12月2日 上午9:55:41
 * @history
 *
 */

@Controller
@RequestMapping("/padinterface")
public class PadInterfaceController {
	
	
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5000));
	
	
	/**ti
	 * 菜品分类接口，全部页菜品数据获取
	 * @author zhao
	 * @return  json 数据，包括分类的菜品
	 */
	@RequestMapping("/getalldishtype")
	public void   getAllDishType(HttpServletRequest request,HttpServletResponse response){
		List<Map<String, Object>> list = dishTypeService.getAllDishAndDishType("0");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 菜品分类接口，全部页菜品数据获取
	 * @author zhao
	 * @return  json 数据，包括分类的菜品
	 */
	@RequestMapping("/getcategory")
	public void   findcategory(HttpServletRequest request,HttpServletResponse response){
		List<Map<String, Object>> list = dishTypeService.findAllCategory("0");
		if(list == null || list.size() == 0 ){
			return ;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 	PAD 查询规定菜品的优惠信息 
	 *  桌号 ，人数 
	 * @author zhao
	 */
	@RequestMapping("/querycoupons")
	@ResponseBody
	public String querycoupons(@RequestBody Tdish dish){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(dish));
		record.setPadpath("querycoupons");
		jsonRecordService.insertJsonRecord(record);

		String dishId = dish.getDishid();
		return orderService.querycoupons(dishId);
	}

	/**
	 * 	PAD 查询所有菜品的优惠信息 
	 *  桌号 ，人数 
	 * @author zhao
	 */
	@RequestMapping("/queryallcoupons")
	@ResponseBody
	public String queryallcoupons(){

		return orderService.queryallcoupons();
	}

	/**
	 * 	PAD 开台 
	 *  桌号 ，人数 
	 * @author zhao
	 */
	@RequestMapping("/setorder")
	@ResponseBody
	public String startOrderInfo(@RequestBody Torder order){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(order));
		record.setPadpath("setorder");
		jsonRecordService.insertJsonRecord(record);

		String returnStr =  orderService.startOrder(order);
		
		JSONObject returnobject = JSONObject.fromObject(returnStr);
		
		
		if(!StringUtils.isBlank(order.getIsShield())&&order.getIsShield().equals("0")&&returnobject.containsKey("result")&&!StringUtils.isBlank(returnobject.getString("result"))&&returnobject.getString("result").equals("0")){
			try{
				String orderid = returnobject.containsKey("orderid")?returnobject.getString("orderid"):"";
				if(!StringUtils.isBlank(orderid)){
					giftService.updateOrderStatus(orderid);
				}
				
			}catch(Exception ex){
				logger.error("--->开台失败！",ex);
			}
		}
		logger.info("开台返回结果：" + returnStr);
		return returnStr;
	}


	/**
	 * 	PAD 登陆会员后 更新后台菜品价格
	 *  orderid ，dishid,price ,vipprice
	 * @author zhao
	 */
	@RequestMapping("/updateorderprice")
	@ResponseBody
	public String updateorderprice(@RequestBody String jsonString,HttpServletRequest reqeust){


		TJsonRecord record = new TJsonRecord();
		record.setJson(jsonString);
		record.setPadpath("updateorderprice");
		jsonRecordService.insertJsonRecord(record);

		Order order = JacksonJsonMapper.jsonToObject(jsonString, Order.class);

		return   orderDetailService.updateorderprice(order);
	}



	/**
	 * 	PAD 开台 
	 *  更新 桌号 人数 
	 * @author zhao
	 */
	@RequestMapping("/updateorder")
	@ResponseBody
	public String updateorder(@RequestBody Torder order){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(order));
		record.setPadpath("updateorder");
		jsonRecordService.insertJsonRecord(record);

		return orderService.updateOrder(order);
	}

	/**
	 * 	PAD下单接口(台号，标志，菜品ID，份数，口味）返回成功失败
	 * @author zhao
	 */
//	@REQUESTMAPPING("/BOOKORDER")
//	@RESPONSEBODY
//	PUBLIC STRING SAVEORDERINFO(@REQUESTBODY STRING JSONSTRING,HTTPSERVLETREQUEST REQEUST){
//		 
//		TJSONRECORD RECORD = NEW TJSONRECORD();
//		RECORD.SETJSON(JSONSTRING);
//		RECORD.SETPADPATH("BOOKORDER");
//		JSONRECORDSERVICE.INSERTJSONRECORD(RECORD);
//		
//		ORDER ORDER = JACKSONJSONMAPPER.JSONTOOBJECT(JSONSTRING, ORDER.CLASS);
//		LIST<TORDERDETAIL> TDS = ORDER.GETROWS();
//		LIST<TORDERDETAIL> DELS = NEW ARRAYLIST<TORDERDETAIL>();
//		FOR(TORDERDETAIL TD : TDS){
//			INT DISHNUM = TD.GETDISHNUM() == NULL?0:INTEGER.PARSEINT(TD.GETDISHNUM());
//			IF(DISHNUM == 0){
//				DELS.ADD(TD);
//			}
//		}
//		TDS.REMOVEALL(DELS);		
//		RETURN ORDERDETAILSERVICE.SAVEORDERDETAILS(ORDER);
//	}
	/**add by sgy 2015.1.14
	 * 修改下单接口
	 * 	PAD下单接口(台号，标志，菜品ID，份数，口味）返回成功失败
	 */
	@RequestMapping("/bookorderList")
	@ResponseBody
	public String saveOrderInfoList(@RequestBody String jsonString,HttpServletRequest reqeust){
		long start = System.currentTimeMillis();
		TJsonRecord record = new TJsonRecord();
		record.setJson(jsonString);
		record.setPadpath("bookorderList");
		jsonRecordService.insertJsonRecord(record);
		Order order = JacksonJsonMapper.jsonToObject(jsonString, Order.class);
		logger.error(order.getOrderid() + "-下单开始：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "");
		//判断重复下单
		ToperationLog toperationLog=new ToperationLog();
		toperationLog.setId(IdentifierUtils.getId().generate().toString());
		toperationLog.setTableno(order.getCurrenttableid());
		toperationLog.setOperationtype(Constant.operationType.SAVEORDERINFOLIST);
		toperationLog.setSequence(order.getSequence());
		int flag= judgeRepeatData(toperationLog);
		
		Map<String, String> mapDetail = new HashMap<String,String>();
		mapDetail.put("orderid", order.getOrderid());
		List<Map<String,String>> orderDetileTempList = orderDetailService.findTemp(mapDetail);
		
		List<TorderDetail> orderDetileList = orderDetailService.find(mapDetail);
		
		if(flag==0){
			Map<String, Object> res = orderDetailService.setOrderDetailList(order,toperationLog);
			try{
				String type = "12";
				if((orderDetileList!=null&&orderDetileList.size()>0)||(orderDetileTempList!=null&&orderDetileTempList.size()>0)){
					type = "13";
				}
				executor.execute(new PadThread(order.getCurrenttableid(),type));
			}catch(Exception ex){
				logger.error("--->",ex);
				ex.printStackTrace();
			}
			logger.error(order.getOrderid() + "-下单结束：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "");
			logger.error(order.getOrderid() + "-下单业务耗时：" + (System.currentTimeMillis() - start), "");
			return JacksonJsonMapper.objectToJson(res);
		}else if(flag==1){
			return Constant.FAILUREMSG;
		}else{
			return Constant.SUCCESSMSG;
		}
	}

	/**
	 * 	PAD 催菜接口
	 * @author zhao
	 */
	@RequestMapping("/urgedish")
	@ResponseBody
	public String urgedish(@RequestBody UrgeDish urgeDish,HttpServletRequest reqeust){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(urgeDish));
		record.setPadpath("urgedish");
		jsonRecordService.insertJsonRecord(record);
//		urgeDish.setDishtype("1");
		return orderDetailService.urgeDishList(urgeDish);
	}

	/**
	 * 	PAD 叫起接口
	 * @author zhao
	 */
	@RequestMapping("/cookiedish")
	@ResponseBody
	public String cookiedish(@RequestBody UrgeDish urgeDish,HttpServletRequest reqeust){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(urgeDish));
		record.setPadpath("cookiedish");
		jsonRecordService.insertJsonRecord(record);

		return orderDetailService.cookiedishList(urgeDish);
	}
	
	/**
	 * 	PAD开发票
	 * @author zhao
	 */
	@RequestMapping("/InsertTinvoice")
	@ResponseBody
	public String Tinvoice(@RequestBody String jsonString){
	  Map<String,String> map = new HashMap<String, String>();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
			String cardno= (String) params.get("memberNo");  //会员卡号
			String tableno= (String) params.get("tableno");  //桌号
			String device_no= (String) params.get("deviceId");  //设备编号
			String invoice_title= (String) params.get("invoiceTitle");//发票的名称

			Tinvoice tinvoice = new Tinvoice();
			//节省 空间 和 去除 使用 - 符号的一些问题
			tinvoice.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			tinvoice.setCardno(cardno);
			tinvoice.setDevice_no(device_no);
			tinvoice.setInvoice_title(invoice_title);
			tinvoice.setOrderid((String) params.get("orderid"));
			invoiceService.insertInvoice(tinvoice);
			
			map.put("flag","1");
			map.put("code","001");
			map.put("desc","操作成功");
			map.put("data","[]");
			try{
				if(StringUtils.isNotBlank((String) params.get("orderid"))){
					TbTable table = tableService.findTableByOrder((String) params.get("orderid"));
					if(table!=null&&StringUtils.isNotBlank(table.getTableNo())){
						executor.execute(new PadThread(table.getTableNo(),"11"));
					}
				}
			}catch(Exception ex){
				logger.error("--->",ex);
				ex.printStackTrace();
			}
			
			
			
		} catch (Exception e) {
			logger.error("--->",e);
		    e.printStackTrace();
		    map.put("flag","0");
		    map.put("code","000");
		    map.put("desc","操作失败");
		    map.put("data","[]");
		}
		JSONObject object = JSONObject.fromObject(map);
		return object.toString();

	}
	
	/**
	 * PAD根据会员号查询发票信息
	 * @author zhao
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/FindTinvoice")
	@ResponseBody
	public String FindTinvoice(@RequestBody String jsonString){
	  Map<String,Object> map = new HashMap<String, Object>();
	  List<Map<String,String>> dataMapList= new ArrayList< Map<String,String>>();
		try {
			Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
			
			List<Tinvoice> TinvoiceList = invoiceService.findTinvoice(params);
			if(TinvoiceList.size()>0){
				for (int i = 0; i < TinvoiceList.size(); i++) {
					 Map<String,String> dataMap = new HashMap<String, String>();
					 dataMap.put("invoiceTitle",TinvoiceList.get(i).getInvoice_title());
					 dataMapList.add(dataMap);
				}
			}
			map.put("flag","1");
			map.put("code","001");
			map.put("desc","操作成功");
			map.put("data",dataMapList);
		} catch (Exception e) {
			logger.error("--->",e);
		    e.printStackTrace();
		    map.put("flag","0");
		    map.put("code","000");
		    map.put("desc","操作失败");
		    map.put("data","[]");
		}
		JSONObject object = JSONObject.fromObject(map);
		return object.toString();

	}
	

	/**
	 * PAD根据订单号查询发票信息
	 * @author zhao
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/findInvoiceByOrderid")
	@ResponseBody
	public String findInvoiceByOrderid(@RequestBody String jsonString){
	  Map<String,Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
			List<Tinvoice> invoiceList = invoiceService.findInvoiceByOrderid(params);
			
			map.put("data",invoiceList);
			map.put("result", "0");
		} catch (Exception e) {
			logger.error("--->",e);
		    e.printStackTrace();
		    return Constant.FAILUREMSG;
		}
		JSONObject object = JSONObject.fromObject(map);
		return object.toString();

	}
	
	
	/**
	 * pos 打印发票后更新发票状态
	 * @author zhao
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updateInvoice")
	@ResponseBody
	public String updateInvoice(@RequestBody String jsonString){
		try {
			Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
			 invoiceService.updateInvoice(params);
			 
			return Constant.SUCCESSMSG;
		} catch (Exception e) {
			logger.error("--->",e);
		    e.printStackTrace();
		    return Constant.FAILUREMSG;
		}

	}


	/**
	 * 	退菜
	 * @author zhao
	 */
	@RequestMapping("/discarddish")
	@ResponseBody
	public String discardDish(@RequestBody String  jsonString ,HttpServletRequest reqeust){
//		  UrgeDish urgeDish = null;

		UrgeDish urgeDish = JacksonJsonMapper.jsonToObject(jsonString, UrgeDish.class);
		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(urgeDish));
		record.setPadpath("discarddish");
		jsonRecordService.insertJsonRecord(record);

		ToperationLog toperationLog=new ToperationLog();
		toperationLog.setId(IdentifierUtils.getId().generate().toString());
		toperationLog.setTableno(urgeDish.getCurrenttableid());
		toperationLog.setOperationtype(Constant.operationType.DISCARDDISH);
		toperationLog.setSequence(urgeDish.getSequence());
		int flag=judgeRepeatData(toperationLog);
		if(flag==0){
			String a = orderDetailService.discardDishList(urgeDish,toperationLog);
			return a ;
		}else if(flag==1){
			return Constant.FAILUREMSG;
		}else{
			return Constant.SUCCESSMSG;
		}
	}


	/**
	 * 换台 接口
	 */
	@RequestMapping("/switchtable")
	@ResponseBody
	public String switchTable(@RequestBody Table table,HttpServletRequest reqeust){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(table));
		record.setPadpath("switchtable");
		jsonRecordService.insertJsonRecord(record);


		ToperationLog toperationLog=new ToperationLog();
		toperationLog.setId(IdentifierUtils.getId().generate().toString());
		toperationLog.setTableno(table.getOrignalTableNo());
		toperationLog.setOperationtype(Constant.operationType.SWITCHTABLE);
		toperationLog.setSequence(table.getSequence());
		int flag=judgeRepeatData(toperationLog);
		if(flag==0){
			return tableService.switchTable(table,toperationLog);
		}else if(flag==1){
			return Constant.FAILUREMSG;
		}else{
			return Constant.SUCCESSMSG;
		}


	}
	
	/**
	 * 拼凑台 接口
	 */
	@RequestMapping("/mergetable")
	@ResponseBody
	public String mergetable(@RequestBody Table table,HttpServletRequest reqeust){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(table));
		record.setPadpath("mergetable");
		jsonRecordService.insertJsonRecord(record);
		ToperationLog toperationLog=new ToperationLog();
		toperationLog.setId(IdentifierUtils.getId().generate().toString());
		toperationLog.setTableno(table.getOrignalTableNo());
		toperationLog.setOperationtype(Constant.operationType.MERGETABLE);
		toperationLog.setSequence(table.getSequence());
		int flag=judgeRepeatData(toperationLog);
		if(flag==0){
			return tableService.mergetable(table,toperationLog);
		}else if(flag==1){
			return Constant.FAILUREMSG;
		}else{
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
		record.setPadpath("mergetable");
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
			map.put("msg", e.getMessage());
			map.put("result", "1");
			JSONObject object = JSONObject.fromObject(map);
			return object.toString();
		}
	}

	/**
	 * 清台 接口
	 */
	@RequestMapping("/cleantable")
	@ResponseBody
	public String cleantable(@RequestBody Table table,HttpServletRequest reqeust){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(table));
		record.setPadpath("cleantable");
		jsonRecordService.insertJsonRecord(record);

		return orderDetailService.cleantable(table);
	}

//	/**
//	 * 结业 接口
//	 */
//	@RequestMapping("/accountcash")
//	@ResponseBody
//	public String accountcash(@RequestBody AccountCash accountCash,HttpServletRequest reqeust){
//		 
//		
//		TJsonRecord record = new TJsonRecord();
//		record.setJson(JacksonJsonMapper.objectToJson(accountCash));
//		record.setPadpath("accountcash");
//		jsonRecordService.insertJsonRecord(record);
//		
//		return tableService.accountcash(accountCash);
//	}


	/**
	 *
	 *  获取套餐信息
	 * @author zhao
	 */
	@RequestMapping("/getdishset")
	@ResponseBody
	public String getDishSet(@RequestBody Tdish dish){
		return dishService.getDishSet(dish);
	}


	/**
	 *
	 *  获取所有套餐信息
	 * @author zhao
	 */
	@RequestMapping("/getalldishset")
	@ResponseBody
	public String getAllDishSet(){
		return dishService.getAllDishSet();
	}

	/**
	 *  结账
	 */
	@RequestMapping("/settleorder")
	@ResponseBody
	public String settleOrder(@RequestBody String settlementStrInfo){
		// SettlementInfo settlementInfo
		
		TJsonRecord record = new TJsonRecord();
		record.setJson(settlementStrInfo);
		record.setPadpath("settleorder");
		jsonRecordService.insertJsonRecord(record);

		SettlementInfo  settlementInfo =  JacksonJsonMapper.jsonToObject(settlementStrInfo, SettlementInfo.class);
		String result = orderSettleService.settleOrder(settlementInfo);

		final String orderid = settlementInfo.getOrderNo();
		//修改投诉表信息
		new Thread(new Runnable(){
			public void run(){
				callWaiterService.updateCallStatus(orderid);
			}
		}).start();
		
		if("0".equals(result)){
			logger.info("结算成功，调用进销存接口");
			return psicallback(settlementInfo,0);
		}else {
			logger.error("结算失败，result :" + result);
			return Constant.FAILUREMSG;
		}
	}
	
	
	//进销存回调
	/**
	 * 
	 * @param settlementInfo
	 * @param isweixin  0 not   1  yes
	 * @return
	 */
	private String psicallback(SettlementInfo settlementInfo,Integer isweixin){
		String psishow=PropertiesUtils.getValue("PSI_SHOW");
		if("Y".equals(psishow)){//显示进销存
			  //调用进销存接口 返回数据给 进销存 管理
			 String retString = orderDetailService.getOrderDetailByOrderId(settlementInfo.getOrderNo());
			String url="http://"+PropertiesUtils.getValue("PSI_URL") + PropertiesUtils.getValue("PSI_SUFFIX_ORDER");
			Map<String, String> dataMap = new HashMap<String, String>();
			 dataMap.put("data", retString);
			String retPSI = null;
			try {
				retPSI = new HttpRequestor().doPost(url, dataMap);
			} catch (Exception e) {
				logger.error("--->",e);
				e.printStackTrace();
			}
			 @SuppressWarnings("unchecked")
			  Map<String,String> retMap = JacksonJsonMapper.jsonToObject(retPSI, Map.class);
			  if(retMap == null || "1".equals(retMap.get("code"))){		
				  SettlementInfo info = new SettlementInfo();
				  info.setOrderNo(settlementInfo.getOrderNo());
				  orderSettleService.rebackSettleOrder(settlementInfo);
				  return Constant.FAILUREMSG;
	        }
		}
		
		if(1==isweixin){
			  return Constant.WEIXINSUCCESSMSG;
		  }
		     return Constant.SUCCESSMSG;
	}
	
	/**
	 * 处理实收字段的问题 ，pos 端调用
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/debitamout")
	@ResponseBody
	public String debitamout(@RequestBody final String orderId){
		new Thread(new Runnable(){
			public void run(){
				@SuppressWarnings({"unchecked" })
				Map<String,String>  map =  JacksonJsonMapper.jsonToObject(orderId, Map.class);
				try {
					orderSettleService.calDebitAmount(map.get("orderNo"));
				} catch (Exception e) {
					logger.error("计算实收失败，订单号：" + orderId, e, "");
				}
			}
		}).start();
		return Constant.SUCCESSMSG;
	}
	

	/**
	 * 反结账
	 */

	@RequestMapping("/rebacksettleorder")
	@ResponseBody
	public String rebacksettleOrder(@RequestBody String settlementStrInfo){
		// SettlementInfo settlementInfo

		TJsonRecord record = new TJsonRecord();
		record.setJson(settlementStrInfo);
		record.setPadpath("rebacksettleorder");
		jsonRecordService.insertJsonRecord(record);

		SettlementInfo  settlementInfo =  JacksonJsonMapper.jsonToObject(settlementStrInfo, SettlementInfo.class);
		String result = orderSettleService.rebackSettleOrder(settlementInfo);
		 
		if("0".equals(result)){
			logger.info("反结算成功，调用进销存反结算接口，是否使用微信支付： "+ result);
			return psicallback(settlementInfo,0);
		}else if("2".equals(result)){
			logger.info("反结算成功，调用进销存反结算接口，是否使用微信支付： "+ result);
			return psicallback(settlementInfo,1);
		}else {
			logger.error("反结算失败！"+result);
			return Constant.FAILUREMSG;
		}
	}

	/**
	 *  经理保存零找金
	 */

	@RequestMapping("/saveposcash")
	@ResponseBody
	public String saveposcash(@RequestBody String settlementStrInfo){
		// SettlementInfo settlementInfo
		SettlementInfo  settlementInfo =  JacksonJsonMapper.jsonToObject(settlementStrInfo, SettlementInfo.class);
		String result = orderSettleService.saveposcash(settlementInfo);
		if("0".equals(result)){
			return Constant.SUCCESSMSG;
		}else {
			return Constant.FAILUREMSG;
		}
	}

	/**
	 *  校验服务员接口
	 */

	@RequestMapping("/verifyuser")
	@ResponseBody
	public String verifyuser(@RequestBody  LoginInfo  loginInfo){
		int result = loginService.existUser(loginInfo);
		if(result == 0){
			return Constant.SUCCESSMSG;
		}else {
			logger.error("-->用户不存在");
			return Constant.FAILUREMSG;
		}
	}



	/**
	 * 登录接口，验证成功后 返回信息需要有PAD IP和房号绑定状态，并且房号是否是在用餐状态
	 * 0 成功 1 失败
	 */
	@RequestMapping(value = "/openbiz" )
	@ResponseBody
	public String openBiz(@RequestBody  LoginInfo  loginInfo){
		String jsonString  = "";

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(loginInfo));
		record.setPadpath("openbiz");
		jsonRecordService.insertJsonRecord(record);

		try {
			//TODO 查询今天是否开业
			TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
			if(tbOpenBizLog != null){
				jsonString = Constant.FAILUREMSG;
				return jsonString;
			}
			User user = loginService.authPadUser(loginInfo, 0,"2");
			if(user == null || !user.getUserType().equals( Constants.SUPER_ADMIN)){
				jsonString = Constant.FAILUREMSG;
			}else {
				List<Map<String, Object>> list = tableService.find(null);
				jsonString = wrapJson(user,list);
			}
		} catch (AuthException e) {
			logger.error("--->",e);
			jsonString = Constant.FAILUREMSG;
		}
		return jsonString;
	}


	/**
	 * 登录接口，验证成功后 返回信息需要有PAD IP和房号绑定状态，并且房号是否是在用餐状态
	 * 0 成功 1 失败
	 */
	@RequestMapping(value = "/login" )
	@ResponseBody
	public String userLogin(@RequestBody  LoginInfo  loginInfo){
		String jsonString  = "";

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(loginInfo));
		record.setPadpath("login");
		jsonRecordService.insertJsonRecord(record);


		try {
			//TODO 查询今天是否开业
			String loginType = loginInfo.getLoginType();
			if("0".equals(loginInfo.getLoginType())){


				TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
				if(tbOpenBizLog == null){
					jsonString = Constant.FAILUREMSG;
					return jsonString;
				}
				String pwd = dataDictionaryService.find("SECRETKEY");
//				String pwd = dataDictionaryService.find("PASSWORD");
				if(! pwd.equals(loginInfo.getPassword())){
					jsonString = Constant.FAILUREMSG;
				}else {
					userService.updateLoginTime(loginInfo.getUsername());
					jsonString = Constant.SUCCESSMSG;
				}
			}else {
				User user = loginService.authPadUser(loginInfo, 0,loginType);
				if(user == null ){
					jsonString = Constant.FAILUREMSG;
				}else {
					List<Map<String, Object>> list = tableService.find(null);
					jsonString = wrapJson(user,list);
				}
			}

		} catch (AuthException e) {
			logger.error("--->",e);
			jsonString = Constant.FAILUREMSG;
		}
		return jsonString;
	}

	/**
	 * 登录接口，验证成功后 返回信息需要有PAD IP和房号绑定状态，并且房号是否是在用餐状态
	 * 0 成功 1 失败
	 */
	@RequestMapping(value = "/querytables" )
	@ResponseBody
	public String queryAllTable(){
		Map<String, Object>  map=new HashMap<>();
		String defaultsort="0";//默认
		if(null!=Constant.DEFAULT_TABLE_SORT){
			defaultsort=Constant.DEFAULT_TABLE_SORT;
		}
		map.put("defaultsort", Integer.parseInt(defaultsort));
		String jsonString  = "";
		try {
			List<Map<String, Object>> list = tableService.find(map);
			return JacksonJsonMapper.objectToJson(list);

		} catch (Exception e) {
			jsonString = "";
			logger.error("查询所有桌台异常！", e);
		}
		return jsonString;
	}

	/**
	 * 根据桌号查询桌子使用情况
	 */
	@RequestMapping(value = "/queryonetable" )
	@ResponseBody
	public String queryOneTable(@RequestBody Table  tbTable){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(tbTable));
		record.setPadpath("queryonetable");
		jsonRecordService.insertJsonRecord(record);



		String jsonString  = "";
//		Map<String, String> mapRet = new HashMap<String, String>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("tableNo", tbTable.getTableNo());
//		map.put("status", "1");
		try {
			List<Map<String, Object>> list = tableService.find(map);
			if(list == null || list.size() > 1){
				jsonString = Constant.FAILUREMSG;
			}else {
//					mapRet.put("result", "0");
				Map<String, Object> retMap = list.get(0);
				TableStatus resultTable = new TableStatus();
				resultTable.setStatus(String.valueOf(retMap.get("status")));
				resultTable.setResult("0");
				resultTable.setOrderid(String.valueOf(retMap.get("orderid")));
				jsonString = JacksonJsonMapper.objectToJson(resultTable);
			}

		} catch (Exception e) {
			logger.error("--->",e);
			jsonString = "";
		}
		return jsonString;
	}

	/**
	 * 查询年龄段数据字典
	 */
	@RequestMapping(value = "/queryagepriod" )
	@ResponseBody
	public String queryAgePriod(){

		String jsonString  = "";
		try {
			List<Map<String, Object>> list = dataDictionaryService.getDatasByType("AGEPERIOD");
			if(list == null || list.size() > 1){
				jsonString = Constant.FAILUREMSG;
			}else {
				Map<String, Object> retMap = list.get(0);
				TableStatus resultTable = new TableStatus();
				resultTable.setStatus(String.valueOf(retMap.get("status")));
				resultTable.setResult("0");
				resultTable.setOrderid(String.valueOf(retMap.get("orderid")));
				jsonString = JacksonJsonMapper.objectToJson(resultTable);
			}

		} catch (Exception e) {
			logger.error("--->",e);
			jsonString = "";
		}
		return jsonString;
	}

	/**
	 *
	 *  返回所有的数据
	 */
	@RequestMapping(value = "/queryusers" )
	@ResponseBody
	public String queryAllUser(){
		String jsonString  = "";
		try {
			List<EmployeeUser> list = this.employeeUserService.findAllServiceUserForCurrentStore();
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("result", "0");
			retMap.put("detail", list);
			return JacksonJsonMapper.objectToJson(retMap);
		} catch (Exception e) {
			logger.error("--->",e);
			e.printStackTrace();
			jsonString = "";
		}
		return jsonString;
	}


	/**
	 *  获取所有的分类的套餐
	 * @author zhao
	 * @return  json 数据，包括获取所有的分类的套餐
	 */
	@RequestMapping("/getdishgroup")
	public String   getdishgroup(HttpServletRequest request,HttpServletResponse response){

		return null;
//		dishService.getDishList();
//		String dishId = dish.getDishid();
//		return orderService.quercoupons(dishId);
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("rows", list);
//		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
//		try{
//        	response.reset();
//        	response.setHeader("Content-Type", "application/json");
//        	response.setContentType("text/json;charset=UTF-8");  
//        	OutputStream stream = response.getOutputStream();
//        	stream.write(wholeJsonStr.getBytes("UTF-8"));
//        	stream.flush();
//        	stream.close();
// 
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
	}

	/**
	 * 催菜接口
	 */
	public String quickCookie(){
		return "/dishtype/show";
	}

	/**
	 * 退菜接口
	 */
	public String discardDish(){
		return "/dishtype/show";
	}

	/**
	 * 换台接口
	 * 修改人数，会员等开台信息
	 */
	public String changeTableOrder(){
		return "/dishtype/show";
	}

	private String wrapJson(User user, List<Map<String, Object>> list){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", "0");
		if(user != null){
			map.put("fullname", user.getName() +"("+ user.getAccount()+")");
		}
		map.put("loginTime", DateFormat.getDateTimeInstance().format(new Date()));

		map.put("tables", list);
		return JacksonJsonMapper.objectToJson(map);

	}
	/**
	 * 下单和退菜的  重复下单的判断
	 * @return
	 */
	private int judgeRepeatData(ToperationLog toperationLog){
		//因为 (newtoperationLog.getSequence()==toperationLog.getSequence()) 这个条件永远不成立，应该使用equals方法；
		//如果加上这个判断条件，会导致PAD、POS同时给一个餐台下单时，可能误认为是重复下单，sequence没有同步，所以调用该方法的逻辑以后要去掉，暂时先返回0
		return 0;
		
		/*Map<String,Object> map=new HashMap<String,Object>();
		map.put("tableno", toperationLog.getTableno());
		map.put("operationType", toperationLog.getOperationtype());
		ToperationLog newtoperationLog=toperationLogService.findByparams(map);
		if(newtoperationLog==null){
			return 0;//第一次操作
		}else if(newtoperationLog.getSequence()==toperationLog.getSequence()){
			//本次操作的序号和上次操作的序号相等，返回操作成功
			return 2;
		}
//		else if(newtoperationLog.getSequence()+1!=toperationLog.getSequence()){
//			//本次操作的序号，必须是上次操作序号+1,不等就返回失败
//			return 1;
//		}
		else{
			return 0;
		}*/
	}

	/**
	 * 获取屏保图片
	 *
	 */
	@RequestMapping("/getPictures")
	@ResponseBody

	public void   getPictures(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> params){

		List<Map<String, Object>> list = picturesService.find(params);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 获取套餐数据
	 *
	 */
	@RequestMapping("/getComboDish")
	@ResponseBody
	public void   getComboDish(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){
		@SuppressWarnings("unchecked")
		Map<String,Object> map=JacksonJsonMapper.jsonToObject(params, Map.class);
		String wholeJsonStr = comboDishService.getComboDishJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}


	/**
	 * 传入typeid 获取对应优惠列表
	 *
	 */
	@RequestMapping(value="/getPreferentialList",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView getPreferentialList( @RequestBody String body){
		@SuppressWarnings("unchecked")
		Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
		ModelAndView mav = new ModelAndView();
		String typeid=(String) params.get("typeid"); //优惠分类
		if( !StringUtils.isBlank(typeid)){
			List<Map<String,Object>> l= this.preferentialActivityService.findCouponsByType4Pad(typeid);
			mav.addObject("list", l);
		}
		return mav;
	}
	
	@RequestMapping(value="/setPreferentialFavor",method = RequestMethod.POST)
	@ResponseBody
	public String setPreferentialFavor(@RequestBody String body){
		Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
		
		int num = preferentialActivityService.updateBySelective(params);
		num =(num == 0?1:0);
		
		Map<String, Object> res = new HashMap<>();
		res.put("result", num);
		res.put("msg", "");
		
		return JacksonJsonMapper.objectToJson(res);
	}
	
	/**
	 * 查询所有的可挂账的合作单位
	 *
	 */
	@RequestMapping(value="/getCooperationUnit",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView getCooperationUnit(@RequestBody String body){
		/*@SuppressWarnings("unchecked")
		Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
		ModelAndView mav = new ModelAndView();
		String typeid=(String) params.get("typeid"); //优惠分类
		if( !StringUtils.isBlank(typeid)){*/
		    ModelAndView mav = new ModelAndView();
		    Map<String, Object> params = new HashMap<String, Object>();
			List<Map<String,Object>> l= this.preferentialActivityService.findCooperationUnit(params);
			mav.addObject("list", l);
		//}
		return mav;
	}
	
	/**
	 *  使用特价菜品类和单品折扣类优惠
	 */
	@RequestMapping(value="/usePreferentialItem",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView usePreferentialItem( @RequestBody String body ){
		ModelAndView mav = new ModelAndView();
		@SuppressWarnings("unchecked")
		Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
		String orderid= (String) params.get("orderid");  //账单号
		String preferentialid= (String) params.get("preferentialid"); //优惠活动id
		String disrate = (String) params.get("disrate") ; // 手工折扣类会上传一个>0的折扣
		String type=(String) params.get("type");
		String subtype=(String) params.get("sub_type");
        String preferentialAmt = (String) params.get("preferentialAmt");
		OperPreferentialResult result = this.preferentialActivityService.updateOrderDetailWithPreferential(type,subtype,orderid, 
				preferentialid , disrate,preferentialAmt);

		mav.addObject(result);
		return mav;
	}

	/**
	 *  取消账单使用的优惠
	 */
	@RequestMapping(value="/cancelPreferentialItem",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView cancelPreferentialItem(@RequestBody String body ){
		ModelAndView mav = new ModelAndView();
		Map<String, Object> params = JacksonJsonMapper.jsonToObject(body, Map.class);
		String orderid= (String) params.get("orderid");  //账单号
		String preferentialid= (String) params.get("preferentialid"); //优惠活动id
		OperPreferentialResult result;
// 		try{
		result = this.preferentialActivityService.cancelPreferentialItemInOrder(orderid, preferentialid);
// 		}catch(Exception e){
// 			result=new OperPreferentialResult();
// 			result.setResult(0);
// 			result.setMsg("数据操作抛出异常！");
// 		}
		mav.addObject(result);
		return mav;
	}
	/**
	 * 获取菜谱数据，新版
	 * @author shen
	 * @date:2015年5月6日下午1:33:13
	 * @Description: TODO
	 * @param @return
	 * @throws
	 */
	@RequestMapping(value="/getMenu",method = RequestMethod.POST)
	@ResponseBody
	public void getMenu(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = menuService.getMenuData();
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			logger.info(wholeJsonStr);
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 获取当前菜谱的分类
	 * @author shen
	 * @date:2015年5月6日下午11:53:36
	 * @Description: TODO
	 */
	@RequestMapping(value="/getMenuColumn",method = RequestMethod.POST)
	@ResponseBody
	public void getMenuColumn(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = menuService.getMenuColumn();
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			logger.info(wholeJsonStr);
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 获取当前菜谱的鱼锅
	 * @author shen
	 * @date:2015年5月6日下午11:54:28
	 * @Description: TODO
	 */
	@RequestMapping(value="/getMenuFishPot",method = RequestMethod.POST)
	@ResponseBody
	public void getMenuFishPot(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){
		List<Map<String, Object>> map = menuService.getMenuFishPot(jsonString);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			logger.info(wholeJsonStr);
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 获取菜谱中套餐的数据
	 * @author shen
	 * @date:2015年5月7日上午10:26:56
	 * @Description: TODO
	 */
	@RequestMapping(value="/getMenuCombodish",method = RequestMethod.POST)
	@ResponseBody
	public void getMenuCombodish(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = menuService.getMenuCombodish(jsonString);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			logger.info(wholeJsonStr);
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 获取菜谱中双拼鱼锅的数据
	 * @author shen
	 * @date:2015年5月7日上午10:26:56
	 * @Description: TODO
	 */
	@RequestMapping(value="/getMenuSpfishpot",method = RequestMethod.POST)
	@ResponseBody
	public void getMenuSpfishpot(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){
		List<Map<String, Object>> map = menuService.getMenuSpfishpot(jsonString);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			logger.info(wholeJsonStr);
			stream.flush();
			stream.close();
			
		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 更换pad的时候调用，获取已经开台的桌子的信息，返回一个list
	 * @author shen
	 * @date:2015年5月14日下午3:25:42
	 * @Description: TODO
	 */
	@RequestMapping(value="/getOrderedTableList",method = RequestMethod.POST)
	@ResponseBody
	public void getOrderedTableList(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("status", "1");
		List<Map<String, Object>> list = tableService.find(params);
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String,Object>();
		//tableNum tableName contentNum
		if(list!=null&&list.size()>0){
			for(Map<String,Object> ma:list){
				Map<String,Object> param=new HashMap<String,Object>();
				param.put("tableNum", ma.get("tableNo"));
				param.put("tableName", ma.get("tableName"));
				param.put("contentNum", ma.get("personNum"));
				datalist.add(param);
			}
		}
		map.put("data", datalist);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 获取更换pad的信息
	 * @author shen
	 * @date:2015年5月14日下午3:45:47
	 * @Description: TODO
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getOrderInfo",method = RequestMethod.POST)
	@ResponseBody
	public void getOrderInfo(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> params=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		Map<String, Object> map = orderService.switchPadOrderInfo(params);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 获取人气推荐的菜
	 * @author shen
	 * @date:2015年5月26日下午3:47:20
	 * @Description: TODO
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getHeatDishList",method = RequestMethod.POST)
	@ResponseBody
	public void getHeatDishList(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> params=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		Map<String, Object> map = menuService.getHeatDishList(params);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			logger.info(wholeJsonStr);
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 更新订单中菜品的重量
	 * {"tableNo":"3","dishid":"XXX","primarykey":"XXXX","dishnum":"XX"}
	 * @author shen
	 * @date:2015年5月27日下午5:20:27
	 * @Description: TODO
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/updateDishWeight",method = RequestMethod.POST)
	@ResponseBody
	public void updateDishWeight(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> params=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		Map<String, Object> map = orderService.updateDishWeight(params);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		logger.info("更新菜品称重结果： "+wholeJsonStr);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("更新菜品重量失败！", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 登录接口
	 * @param username,登录帐号
	 * @param password,登录密码，MD5加密后
	 * @param loginType,登录方式			POS登录    1    POS
	开业        	2    POS
	反结算      3    POS
	清机        	4    POS
	结业        	5    POS
	收银        	6    POS
	退菜       	7    PAD
	开台        	8    PAD
	 @return
	 {
	 "result": 0, //0成功 1 失败
	 "loginTime":  "",//服务器当前时间
	 "fullname": "收银员1(003)",  //员工姓名
	 "msg": "密码错误、没有开台权限"
	 }
	 */
	@RequestMapping(value = "/login.json" )
	@ResponseBody
	public String loginJson(@RequestBody String body){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			Map<String, String> params = JacksonJsonMapper.jsonToObject(body, Map.class);
			String username = params.get("username");
			String password = params.get("password");
			String loginType = params.get("loginType");
			Map<String,Object> userMap = userService.validatePasswordLoginTypeByAccount(username, password,loginType);
			if(Boolean.valueOf(String.valueOf(userMap.get("success")))){
				SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String   date   =   sDateFormat.format(new java.util.Date());
				resultMap.put("result",0);
				resultMap.put("msg","验证成功");
				resultMap.put("loginTime",date);
				resultMap.put("fullname",String.valueOf(userMap.get("name")));
			}else {
				//userService.updateLoginTime(account);
				SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String   date   =   sDateFormat.format(new java.util.Date());
				resultMap.put("result",1);
				resultMap.put("loginTime",date);
				resultMap.put("msg",String.valueOf(userMap.get("msg")));
			}

		} catch (Exception e) {
			logger.error("--->",e);
			resultMap.put("result",1);
			resultMap.put("msg",e.getMessage());
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	/**
	 * 获取员工权限列表
	 * @param username,登录帐号
	 * @param password,登录密码，MD5加密后
	 @return
	 {
	 "result": 0, //0成功 1 失败
	 "loginTime":  "",//服务器当前时间
	 "fullname": "收银员1(003)",  //员工姓名
	 "msg": "密码错误、没有开台权限"
	 "rights": [{  //所有POS和PAD权限放在里面
	 "1": 1,
	 "2": 0,
	 "3": 1,
	 "4": 1,
	 "5": 0
	 "6": 1,
	 "7": 1,
	 "8": 0

	 }]
	 }
	 */
	@RequestMapping(value = "/userrights.json" )
	@ResponseBody
	public String userrights(@RequestBody String body){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			Map<String, String> params = JacksonJsonMapper.jsonToObject(body, Map.class);
			String username = params.get("username");
			String password = params.get("password");
			User user = userService.validatePasswordByAccount(username, password);
			if(user!=null){
				SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String   date   =   sDateFormat.format(new java.util.Date());
				resultMap.put("result",0);
				resultMap.put("msg","验证成功");
				resultMap.put("loginTime",date);
				resultMap.put("fullname",user.getName());

				Map<String,Object> authMap = functionService.getPosPadAuthByAccount(user.getAccount());
				resultMap.put("rights", authMap);
			}else {
				//userService.updateLoginTime(account);
				resultMap.put("result",1);
				resultMap.put("msg","密码错误");
			}

		} catch (Exception e) {
			logger.error("查询用户权限失败！" ,e);
			resultMap.put("result",1);
			resultMap.put("msg",e.getMessage());
		}
		logger.info("获取用户权限结果,resultMap:" + resultMap);
		return JacksonJsonMapper.objectToJson(resultMap);
	}


	@Autowired
	JmsTemplate jmsTemplate;

	/**
	 * 登录接口，验证成功后 返回信息需要有PAD IP和房号绑定状态，并且房号是否是在用餐状态
	 * 0 成功 1 失败
	 */
	@RequestMapping(value = "/padlogin" )
	@ResponseBody
	public String userPadLogin(@RequestBody  LoginInfo  loginInfo){
		String jsonString  = "";
		try {

			TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
			if(tbOpenBizLog == null){
				logger.info("操作失败");
				jsonString = Constant.FAILUREMSG;
				return jsonString;
			}

			String pwd = dataDictionaryService.find("SECRETKEY");
			if(! pwd.equals(loginInfo.getPassword())){
				logger.info("密码错误");
				jsonString = Constant.FAILUREMSG;
			}else {
				logger.info("登录成功");
//				userService.updateLoginTime(loginInfo.getUsername());
				jsonString = Constant.SUCCESSMSG;
			}
//			 
		} catch (Exception e) {
			logger.error("--->",e);
			jsonString = Constant.FAILUREMSG;
		}
		return jsonString;
	}

	/**
	 * 系统设置接口
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getSystemSetData")
	@ResponseBody
	public void   getSystemSetData(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		Map<String, Object> map=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		List<Map<String, Object>> listFind = dataDictionaryService.findByParams(map);
		if("ROUNDING".equals(map.get("type"))){
			map.clear();
			map.put("type", "ACCURACY");
			List<Map<String, Object>> listFind2 = dataDictionaryService.findByParams(map);
			listFind.addAll(listFind2);
		}
		map.clear();
		map.put("rows", listFind);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 手环登陆接口
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/BraceletLoginIn")
	@ResponseBody
	public void   BraceletLoginIn(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		Map<String, Object> map=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
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
		
		Map<String, Object> resultmap= new HashMap<String, Object>();
		boolean flag = false;
		boolean loginflag = false;
		
		if(employeeUser!=null){
			employeeUserAll=employeeUserService.get(employeeUser.getId());
		}
		
		if(employeeUserAll==null){//服务员不存在
			result = 2;
			resultmap.put("result", result);
		}else{
			List<TbUserInstrument> tbUserInstrumentList = new ArrayList<TbUserInstrument>();
			Map<String, Object> userInstrumentMap = new HashMap<String, Object>();
			userInstrumentMap.put("userid", (String) map.get("userid"));
			userInstrumentMap.put("status", "0");
			tbUserInstrumentList =userInstrumentService.findByParams(userInstrumentMap);
			
			if(tbUserInstrumentList!=null&&tbUserInstrumentList.size()>0){//之前已经登录,将之前的登录数据修改，重新登录
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
			
			if(loginflag){//登录保存成功
				Map<String, Object> mapV = new HashMap<String,Object>();
				mapV.put("type", "VIPADDRESS");
				List<Map<String, Object>> listFind = dataDictionaryService.findByParams(mapV);
				resultmap.put("result", 1);
				resultmap.put("VIPADDRESS", listFind!=null&&listFind.size()>0?listFind.get(0).get("itemid"):"");
				resultmap.put("name", employeeUserAll.getUser().getName());
				resultmap.put("sex", employeeUser.getSex());	
			}else{
				resultmap.put("result", 0);
			}
		}
		String wholeJsonStr = JacksonJsonMapper.objectToJson(resultmap);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();
			if(flag){
				String info = meidset.toString().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
				if(!info.equals(meid)){
					StringBuilder messageinfo=new StringBuilder(Constant.TS_URL+Constant.MessageType.msg_2003+"/");
					messageinfo.append(userid+"|"+info);
					new TsThread(messageinfo.toString()).start();
				}
			}
		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 手环退出接口
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/BraceletLoginOut")
	@ResponseBody

	public void   BraceletLoginOut(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		Map<String, Object> map=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		TbUserInstrument tbUserInstrument = new TbUserInstrument();
		tbUserInstrument.setUserid((String) map.get("userid"));
		tbUserInstrument.setStatus(1);
//		tbUserInstrument.setInstrumentid((String) map.get("menid"));
		tbUserInstrument.setLogouttime(new Date());// new Date()为获取当前系统时间
		int result = 0;
		boolean a = userInstrumentService.update(tbUserInstrument);
		if(a==true){
			result = 1;
		}
		map.clear();
		map.put("result", result);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 手环端回应
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/BraceletReply")
	@ResponseBody
	public void   BraceletReply(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		Map<String, Object> map=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		TbMessageInstrument tbMessageInstrument = new TbMessageInstrument();
		tbMessageInstrument.setId((String) map.get("id"));
		tbMessageInstrument.setReply_time(new Date());// new Date()为获取当前系统时间
		int result = 0;
		boolean a = messageInstrumentService.update(tbMessageInstrument);
		TbMessageInstrument messageMap=messageInstrumentService.get((String) map.get("id"));
		if(messageMap!=null){
			
		}
		if(a==true){
			result = 1;
		}
		map.clear();
		map.put("result", result);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	/**
	 * pad端呼叫服务员
	 *
	 */
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/BraceletSent")
	@ResponseBody

	public void BraceletSent(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonString){
		Map<String, Object> map=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		int result=userInstrumentService.insertByParams(map);
		map.clear();
		map.put("result", result);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	@RequestMapping("/uploadImage")
	@ResponseBody
	public void   uploadImage(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map=new HashMap<String,Object>();
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String extName = fileName.substring(fileName.lastIndexOf(".")+1);
			try {
				//使用 fast dfs 上传文件
				 String fileUrlpath = fileService.uploadFile(file.getInputStream(),extName);
				 map.put("imagePath", fileUrlpath);
				 map.put("flag", "0");
			} catch (IllegalStateException e) {
				logger.error("--->",e);
				e.printStackTrace();
				map.put("imagePath", "");
				map.put("flag", "1");
			} catch (IOException e) {
				logger.error("--->",e);
				e.printStackTrace();
				map.put("imagePath", "");
				map.put("flag", "1");
			}catch (Exception e) {
				logger.error("--->",e);
				e.printStackTrace();
				map.put("imagePath", "");
				map.put("flag", "1");
			}
		}
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();
		}catch(Exception ex){
			logger.error("--->",ex);
			ex.printStackTrace();
		}
	}
	@RequestMapping("/importdata")
	@ResponseBody
	public String   importdata(@RequestBody SqlData sqlData){
		if(sqlData == null || sqlData.getTenantId() == null || sqlData.getSql() == null ||
				"".equals(sqlData.getSql().trim())){
			return Constant.FAILUREMSG;
		}
		orderService.executeSql(sqlData.getSql());
		return Constant.SUCCESSMSG;
	}
	
	/**
	 * After pos end work ,begin synchronize data 
	 * @param json
	 * @return
	 */
	@RequestMapping("/jdesyndata")
	@ResponseBody
	public String jdeSynData(@RequestBody String json) {
		logger.info("jdeSynData-start:"+json, "");
		@SuppressWarnings("unchecked")
		//Map<String, String> map = JacksonJsonMapper.jsonToObject(json, Map.class);
		Map<String, String> map = JSON.parseObject(json, Map.class);
		String key = map.get("synkey");
		String synKey = PropertiesUtils.getValue("SYNKEY");
		if (!synKey.equalsIgnoreCase(key)) {
			return Constant.FAILUREMSG;
		}
		ResultDto dto = new ResultDto();
		//获取同步数据的传送方式
		String type = PropertiesUtils.getValue("SYN_DATA_TYPE");
		try {
			//MQ
			if(type.equals("1")){
				if (branchDataSyn.synBranchData()) {
					dto.setInfo(ResultMessage.SUCCESS);
				}else{
					dto.setCode("1");
					dto.setMessage("修改数据同步的状态失败");
				}
			//HTTP
			}else if(type.equals("2")){
				dto = executeSyn();
			}
		}catch (SysException e) {
			loggers.error("门店上传到总店数据失败",e);
			//后面执行是否成功的标志
			boolean afterStatus = false;
			//如果异常,则重新执行三次,直到成功或者3次执行完(后期建议通过任务处理器优化)
			for(int i=0;i<3;i++){
				int j = i + 1;
				try{
					dto = executeSyn();
					afterStatus = true;
					loggers.info("第"+ j +"次执行重传成功");
					break;
				}catch(SysException sysEx){
					loggers.error("第"+ j +"次执行重传失败",sysEx);
				}
			}
			//连续3次执行失败
			if(afterStatus == false){
				dto.setInfo(ResultMessage.INTERNET_EXE);
				//resultMap.put("result", ResultMessage.INTERNET_EXE.getCode());
				//resultMap.put("msg", ResultMessage.INTERNET_EXE.getMsg());
			}
		//使用原有代码MQ机制时出现的异常处理
		}catch(Exception e){
			loggers.error("门店上传到总店数据失败",e);
			dto = null;
		}
		if(dto == null){
			dto = new ResultDto();
			dto.setInfo(ResultMessage.NO_RETURN_MESSAGE);
		}
		logger.info("jdeSynData-end:"+dto, "");
		//return JacksonJsonMapper.objectToJson(resultMap);
		return JSON.toJSONString(dto);
	}
	
	//门店同步数据方法执行
	private ResultDto executeSyn() throws SysException{
		return branchDataSyn.synLocalData();
	}
	
	/**
	 * Excute by pos client
	 * @param json
	 * @return
	 */
	@RequestMapping("/endwork")
	@ResponseBody
	public String endwork(@RequestBody String json){
	  
	  @SuppressWarnings("unchecked")
	 Map<String, String> map = JacksonJsonMapper.jsonToObject(json, Map.class);
	  String userName = map.get("userName");
	  Map<String,String> retMap = new HashMap<String,String>();
	  
	  List<Torder> orderList = orderService.verifyAllOrder();
 
	  if(orderList != null && orderList.size() > 0){
		   for(Torder order : orderList){
			   if("order".equalsIgnoreCase(order.getOrderid())){
				   //1.判断是否所有的订单已经结算
				      retMap.put("result", "1");
			   }else if("teller".equalsIgnoreCase(order.getOrderid())){
				   //2.判断是否全部清机
				   retMap.put("result", "2");
			   }else if("table".equalsIgnoreCase(order.getOrderid())){
				   //3.所有的桌台已经处理
				   retMap.put("result", "3");
			   }
			   return JacksonJsonMapper.objectToJson(retMap);
		   }
	  }
 
	  String isSucess = "0";
	  isSucess = orderService.callEndWork(userName,isSucess);
	  if("1".equals(isSucess) ){
		  retMap.put("result", "4");
		  return JacksonJsonMapper.objectToJson(retMap);
		} else {
			try {
				branchDataSyn.synBranchData();
				retMap.put("result", "0");
			} catch (Exception e) {
				logger.error("--->",e);
				retMap.put("result", "1");
				retMap.put("msg", e.getMessage());
			}
			return JacksonJsonMapper.objectToJson(retMap);
		}
	 
	}
	
	/**
	 * 消息中心插入信息
	 * @param json
	 * @return
	 */
	@RequestMapping("/insertmsg")
	@ResponseBody
	public String   insertMsg(@RequestBody String json){
	  
	 Map<String, String> map = JacksonJsonMapper.jsonToObject(json, Map.class);
	 String key = map.get("synkey");
 
	 return Constant.SUCCESSMSG;
	}
	
	/**
	 * 消息推送
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
			StringBuilder messageinfo = new StringBuilder(Constant.TS_URL + Constant.MessageType.msg_2011 + "/");
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
						//服务员还在线
						//服务员编号|消息类型|区号|台号|消息id
					} else {
						//服务员退出了,找到同一区的服务员 进行推送
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
					String areaname = null;
					String tableNo = null;
					try {
						areaname = java.net.URLEncoder.encode(String.valueOf(tableList.get(0).get("areaname")), "utf-8");
						tableNo = java.net.URLEncoder.encode(finaltableno, "utf-8");
					} catch (UnsupportedEncodingException e) {
						logger.error("--->", e);
						e.printStackTrace();
					}
					if (!userid.equals("")) {
						messageinfo.append(userid + "|" + finalmsgType + "|" + finalcallStatus + "|" + areaname + "|" + tableNo + "|" + IdentifierUtils.getId().generate().toString());
						//new TsThread(messageinfo.toString()).start();
						URL urlobj;
						try {
							urlobj = new URL(messageinfo.toString());
							URLConnection urlconn = urlobj.openConnection();
							urlconn.connect();
							InputStream myin = urlconn.getInputStream();
							BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
							String content = reader.readLine();
							/**
							 * 如果DataServer推送异常(特征值判断)，则重启Dataserver后重新推送
							 */
							if (null != content && content.contains("Access violation")) {
								restartAndRetry();
							}
							JSONObject object = JSONObject.fromObject(content.trim());
							@SuppressWarnings("unchecked")
							List<Map<String, Object>> resultList = (List<Map<String, Object>>) object.get("result");
							if ("1".equals(String.valueOf(resultList.get(0).get("Data")))) {
								System.out.println("推送成功");
							} else {
								System.out.println("推送失败");
							}
						} catch (IOException e) {
							logger.error("--->", e);
							e.printStackTrace();
							restartAndRetry();
						}
					}
				}
			}
			//根据动作打印不同的小票
		}

		private void restartAndRetry() {
			//已重启过则放弃
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
	 * @return
	 */
	@RequestMapping("/findUncleanPosList")
	@ResponseBody
	public String findUncleanPosList() {
		
		Map<String,Object> retMap = new HashMap<>();
		try{
			List<TtellerCash> list = tellerCashService.findUncleanPosList();
			retMap.put("result", "0");
			retMap.put("detail", list);
		}catch(Exception e){
			retMap.put("result", "1");
			retMap.put("msg", e.getMessage());
			logger.error("--->",e);
		}
		return JacksonJsonMapper.objectToJson(retMap);
	}
	
	/**
	 * 获取开业结业时间
	 * @return
	 */
	@RequestMapping("/getOpenEndTime")
	@ResponseBody
	public String getOpenEndTime(){
		Map<String,Object> retMap = new HashMap<>();
		try{
			Map<String, Object> timeMap = dataDictionaryService.getOpenEndTime("BIZPERIODDATE");
			timeMap.remove("datetype");
			retMap.put("result", "0");
			retMap.put("detail", timeMap);
		}catch(Exception e){
			retMap.put("result", "1");
			retMap.put("msg", e.getMessage());
			logger.error("--->",e);
		}
		return JacksonJsonMapper.objectToJson(retMap);
	}
	
	/**
	 * 查询是否结业
	 * @return
	 */
	@RequestMapping("/isEndWork")
	@ResponseBody
	public String isEndWork(){
		Map<String,Object> retMap = new HashMap<>();
		try{
			TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
			if(tbOpenBizLog == null){
				retMap.put("result", "0");
				retMap.put("detail", true);
			}else{
				retMap.put("result", "0");
				retMap.put("detail", false);
			}
		}catch(Exception e){
			retMap.put("result", "1");
			retMap.put("msg", e.getMessage());
			logger.error("--->",e);
		}
		return JacksonJsonMapper.objectToJson(retMap);
	}
	
	
	/**
	 * 查询昨天是否结业
	 * @return
	 */
	@RequestMapping("/isYesterdayEndWork")
	@ResponseBody
	public String isYesterdayEndWork(){
		Map<String,Object> retMap = new HashMap<>();
		try{
			TbOpenBizLog tbOpenBizLog = openBizService.getOpenBizLog();
			if(tbOpenBizLog == null){
				retMap.put("result", "0");
				retMap.put("detail", true);
			}else{
				Date date = tbOpenBizLog.getOpendate();
				if(DateUtils.isToday(date)){
//					如果是今天，则已经结业
					retMap.put("result", "0");
					retMap.put("detail", true);
				}else{
//					如果不是今天
					Map<String, Object> timeMap = dataDictionaryService.getOpenEndTime("BIZPERIODDATE");
					if("T".equals(timeMap.get("datetype"))){ //"T"代表当日
						retMap.put("result", "0");
						retMap.put("detail", false);
					}else{
						retMap.put("result", "0");
						retMap.put("detail", true);
					}
				}
//				retMap.put("result", "0");
//				retMap.put("detail", false);
			}
		}catch(Exception e){
			retMap.put("result", "1");
			retMap.put("msg", e.getMessage());
			logger.error("--->",e);
		}
		return JacksonJsonMapper.objectToJson(retMap);
	}
	
	/**
	 * 获取手环通知消息的类型
	 * @return
	 */
	@RequestMapping("/getNotification")
	@ResponseBody
	public Map<String,Object> getNotification(){
		List<Map<String, Object>> maps = new ArrayList<>();
		try{
			maps = dataDictionaryService.getNotificationDate("NOTIFICATION");
		}catch(Exception e){
			logger.error("--->",e);
			e.printStackTrace();
			return ReturnMap.getReturnMap(0, "003", "数据异常，请联系管理员");
		}
		if(maps == null || maps.size() <= 0){
			return ReturnMap.getReturnMap(0, "002", "没有查询到相应的数据");
		}
		Map<String,Object> returnMap = ReturnMap.getReturnMap(1, "001", "查询成功");
		returnMap.put("data", maps);
		return returnMap;
	}
	
	
	/**
	 * Pad端获取Logo图和背景图
	 * @return
	 */
	@RequestMapping("/getPadImg.json")
	@ResponseBody
	public String getPadImg(){
		Map<String,Object> retMap = new HashMap<>();
		try{
			List<Map<String, Object>> maps = systemServiceImpl.getImgByType("PADIMG");
			retMap.put("ImgIp", PropertiesUtils.getValue("fastdfs.url"));
			retMap.put("result", "0");
			retMap.put("msg", "成功");
			retMap.put("detail", maps);
		}catch(Exception e){
			retMap.put("result", "1");
			retMap.put("msg", e.getMessage());
			logger.error("-->",e);
		}
		return JacksonJsonMapper.objectToJson(retMap);
	}
	
	/**
	 * 查询一页菜谱配置信息
	 * @return
	 */
	@RequestMapping("/onepageinfo.json")
	@ResponseBody
	public String onepageinfo(){
		Map<String, Object> resultMap = new HashMap<>();
		Map  map=new HashMap<>();
		map.put("type", SystemConstant.ONEPAGETYPE.type());
		List<Map<String, Object>>  list= dataDictionaryService.findByParams(map);
		if(list==null || list.size()==0){//没有数据
			resultMap.put("code", 1);
			resultMap.put("msg","后台没有配置信息");
			resultMap.put("data","");
		}else{
			resultMap.put("code", 0);
			resultMap.put("data",list.get(0).get("status").toString());
		}
		
		return  JacksonJsonMapper.objectToJson(resultMap);
	}
	
	
	/**
	 * 获取品项销售明细的打印数据
	 * @return
	 */
	@RequestMapping("/getItemSellDetail.json")
	@ResponseBody
	public String getItemSellDetail(String flag){
		Map<String, Object> timeMap = getTime(flag);
		Map<String, Object> resultMap = new HashMap<>();
		try {
			List<Map<String, Object>> result = orderDetailService.getItemSellDetail(timeMap);
			resultMap.put("result", 0);
			resultMap.put("mag","");
			resultMap.put("data",result);
			resultMap.put("time", timeMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), "");
			resultMap.put("result", 1);
			resultMap.put("mag","获取数据失败");
			resultMap.put("data","");
			resultMap.put("time", timeMap);
			e.printStackTrace();
		}
		return JacksonJsonMapper.objectToJson(resultMap);
	}
	
	/**
	 * 获取开始结束时间
	 * @param falg
	 * @return
	 */
	private Map<String, Object> getTime(String falg){
		Map<String, Object> map = new HashMap<>();
		String startTime = null;
		String endTime = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		endTime = df.format(new Date());
		
		if(falg.equals("1")){  //今日
			startTime = DateUtils.today() + " 00:00:00";
		}else if(falg.equals("2")){  //本周
			startTime = DateUtils.weekOfFirstDay() + " 00:00:00";
		}else if(falg.equals("3")){  //本月
			startTime = DateUtils.monthOfFirstDay() + " 00:00:00";
		}else if(falg.equals("4")){   //上月
			startTime = DateUtils.beforeMonthOfFirstDay() + " 00:00:00";
			endTime = DateUtils.beforeMonthOfLastDay() + " 23:59:59";
		}
		map.put("startTime",startTime);
		map.put("endTime", endTime);
		return map;
	}
	
	//服务员pad
	/**
	 * 服务员pad获取信息
	 * @param jsonString
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getOrderInfowaiter",method = RequestMethod.POST)
	@ResponseBody
	public void getOrderInfowaiter(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> params=JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		Map<String, Object> map = orderService.switchPadOrderInfowaiter(params);
		String wholeJsonStr = JacksonJsonMapper.objectToJson(map);
		try{
			response.reset();
			response.setHeader("Content-Type", "application/json");
			response.setContentType("text/json;charset=UTF-8");
			OutputStream stream = response.getOutputStream();
			stream.write(wholeJsonStr.getBytes("UTF-8"));
			stream.flush();
			stream.close();

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 	PAD 开台 
	 *  桌号 ，人数 
	 * @author zhao
	 */
	@RequestMapping("/setorderwaiter")
	@ResponseBody
	public String startOrderInfowaiter(@RequestBody Torder order){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(order));
		record.setPadpath("setorder");
		jsonRecordService.insertJsonRecord(record);

		String returnStr =  orderService.startOrderwaiter(order);
		
		JSONObject returnobject = JSONObject.fromObject(returnStr);
		
		
		if(!StringUtils.isBlank(order.getIsShield())&&order.getIsShield().equals("0")&&returnobject.containsKey("result")&&!StringUtils.isBlank(returnobject.getString("result"))&&returnobject.getString("result").equals("0")){
			try{
				String orderid = returnobject.containsKey("orderid")?returnobject.getString("orderid"):"";
				if(!StringUtils.isBlank(orderid)){
					giftService.updateOrderStatus(orderid);
				}
				
			}catch(Exception ex){
				
			}
		}
		return returnStr;
	}
	
	
	/**
	 * 	PAD 开台 
	 *  更新 桌号 人数 
	 * @author zhao
	 */
	@RequestMapping("/updateorderwaiter")
	@ResponseBody
	public String updateorderwaiter(@RequestBody Torder order){

		TJsonRecord record = new TJsonRecord();
		record.setJson(JacksonJsonMapper.objectToJson(order));
		record.setPadpath("updateorder");
		jsonRecordService.insertJsonRecord(record);

		return orderService.updateOrderwaiter(order);
	}	
	
	
	//end
	
	
	/**
	 * 消息中心查询信息
	 * @param json
	 * @return
	 */
	@RequestMapping("/querymsg")
	@ResponseBody
	public String   queryMsg(@RequestBody String json){
	  
 
	 return Constant.SUCCESSMSG;
	}
	
	/**
	 * 消息中心更新信息
	 * @param json
	 * @return
	 */
	@RequestMapping("/updatemsg")
	@ResponseBody
	public String   updateMsg(@RequestBody String json){
	  
 
	 return Constant.SUCCESSMSG;
	}
	
	@RequestMapping("/getbranchinfo")
	@ResponseBody
	public String getBranchInfo(){
		Map<String, Object> branchInfo = tbBranchDao.getBranchInfo();
		HashMap<String, Object> res = new HashMap<>();
		if(branchInfo != null && !branchInfo.isEmpty()){
			res.put("result", 1);
			res.put("msg", "success");
			res.put("data", branchInfo);
			return JacksonJsonMapper.objectToJson(res);
		}
		res.put("result", 0);
		res.put("msg", "查询门店信息失败");
		return JacksonJsonMapper.objectToJson(res);
	}
	
	@Autowired
	BranchDataSyn  branchDataSyn;
	
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
	private  OrderService   orderService;


	@Autowired
	OrderDetailService   orderDetailService;

	@Autowired
	private  MemeberService   memeberService;

	@Autowired
	private LoginService  loginService;

	@Autowired
	private TableService  tableService;

	@Autowired
	OrderSettleService  orderSettleService ;

	@Autowired
	DishService    dishService;

	@Autowired
	OpenBizService  openBizService;

	@Autowired
	DataDictionaryService dataDictionaryService;

	@Autowired
	JsonRecordService  jsonRecordService;

	@Autowired
	@Qualifier("t_userService")
	UserService   userService;//新用户service对象

	@Autowired
	FunctionService   functionService;//新方法service对象

	@Autowired
	EmployeeUserService employeeUserService;

	@Autowired
	private ComboDishService comboDishService;

	@Autowired
	private PreferentialActivityService preferentialActivityService;
	@Autowired
	private InstrumentService instrumentService;
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
	TorderMapper  torderMapper;
	@Autowired
	private CallWaiterService callService;
	@Autowired
	private TtellerCashDao tellerCashService;
	@Autowired
	private TbBranchDao tbBranchDao;
	@Autowired
	private SystemServiceImpl systemServiceImpl;
	
	private Logger logger = LoggerFactory.getLogger(PadInterfaceController.class);
	
	private Logger loggers = org.slf4j.LoggerFactory.getLogger(PadInterfaceController.class);
	
	
}
