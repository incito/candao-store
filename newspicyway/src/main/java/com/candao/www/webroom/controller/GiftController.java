package com.candao.www.webroom.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.IdentifierUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.model.TGiftLog;
import com.candao.www.utils.ReturnMap;
import com.candao.www.utils.TsThread;
import com.candao.www.webroom.service.GiftLogService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/clientgift")
public class GiftController {

	@Autowired
	private GiftLogService giftService;

	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);

	/**
	 * 
	 * 赠送礼物到邻桌
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping("/send")
	@ResponseBody
	public Map<String, Object> sendGiftToAnthorTable(@RequestBody String body) {
		logger.debug("start method sendGiftInfo  waiter");
		try {
			logger.debug("save sendGiftInfo  data for params : {} ", body);
			System.out.println("send gift param body "+body);
			if (StringUtils.isEmpty(body)) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			JSONObject giftInfo = JSONObject.fromObject(body);
			if (giftInfo == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}

			if (!giftInfo.containsKey("giftId") || giftInfo.getString("giftId") == null|| giftInfo.getString("giftId").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "礼物ID");
			}
			if (!giftInfo.containsKey("giveTableNo") || giftInfo.getString("giveTableNo") == null|| giftInfo.getString("giveTableNo").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少赠送礼物桌号");
			}
			if (!giftInfo.containsKey("receiveTableNo") || giftInfo.getString("receiveTableNo") == null|| giftInfo.getString("receiveTableNo").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少接受礼物桌号");
			}
			if (!giftInfo.containsKey("giftNo") || giftInfo.getString("giftNo") == null|| giftInfo.getString("giftNo").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少礼物编号");
			}
			if (!giftInfo.containsKey("giftNum") || giftInfo.getString("giftNum") == null|| giftInfo.getString("giftNum").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少礼物数量");
			}
			if (!giftInfo.containsKey("gprice") || giftInfo.getString("gprice") == null|| giftInfo.getString("gprice").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少礼物价格");
			}
			if (!giftInfo.containsKey("orderId") || giftInfo.getString("orderId") == null|| giftInfo.getString("orderId").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少订单编号");
			}
			if (!giftInfo.containsKey("receiveOrderId") || giftInfo.getString("receiveOrderId") == null|| giftInfo.getString("receiveOrderId").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少目标桌订单号");
			}
			
			String receiveOrderId = giftInfo.getString("receiveOrderId");
			
			String isAnonymous = giftInfo.containsKey("isAnonymous")?giftInfo.getString("isAnonymous"):"1";
			
			String msg = giftInfo.containsKey("msg")?giftInfo.getString("msg"):"无";//留言
			
			if(StringUtils.isBlank(msg)){
				msg = "无";
			}
			
			
			int gnum = parseInt(giftInfo.getString("giftNum"));
			
			float gprice = parseFloat(giftInfo.getString("gprice"));
			
			if(gnum<=0){
				return ReturnMap.getReturnMap(0, "002", "礼物数量必须大于0");
			}
			if(gprice<0){
				return ReturnMap.getReturnMap(0, "002", "礼物价格必须大于0");
			}
			Map<String,String> giftMap = giftService.getGiftInfo(giftInfo.getString("giftId"));
			if(giftMap==null||giftMap.size()!=8){
				return ReturnMap.getReturnMap(0, "002", "没有查询到对应的礼物信息");
			}
			//判断是否屏蔽送礼
			Map<String,String> orderMap = giftService.getOrderStatus(receiveOrderId);
			if(orderMap!=null&&orderMap.containsKey("giftStatus")&&orderMap.get("giftStatus").equals("-1")){
				return ReturnMap.getReturnMap(0, "003", "目标拒绝接受礼物");
			}
			//判断送礼个数
			List<TGiftLog> reclist = giftService.getGiftLogByRecOrder(receiveOrderId);
			if(reclist!=null&&reclist.size()>=3){
				return ReturnMap.getReturnMap(0, "004", "目标桌台已经接受超过三个赠送");
			}
			
			String typeId = giftMap.get("typeId")==null?"":String.valueOf(giftMap.get("typeId"));
			TGiftLog log  = TGiftLog.jsonToObject(giftInfo);
			log.setGiftTypeId(typeId);
			log.setGiftStatus("1");
			log.setIsAnonymous(isAnonymous);
			log.setInsertTime(new Date());
			log.setGiftName(giftMap.containsKey("gname")?giftMap.get("gname"):"");
			log.setGiftNum(gnum);
			log.setGiftUnit(giftMap.containsKey("gunit")?giftMap.get("gunit"):"");
			log.setGiftPrice(gprice);
			log.setGiftAmount(gnum*gprice);
			
			log = giftService.saveGiftLogInfo(log);
			
			if (log == null) {
				return ReturnMap.getReturnMap(0, "002", "发起赠送失败");
			}
			try{
				final String giveTableNo = isAnonymous.equals("0")?"":giftInfo.getString("giveTableNo");
				final String receiveTableNo = giftInfo.getString("receiveTableNo");
				final String giftNo = giftInfo.getString("giftNo");
				final String messageid = log.getId();
				final String receiveOrderIdstr = receiveOrderId;
				final String finalmsg = msg;
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						StringBuilder messageinfo=new StringBuilder(Constant.TS_URL+Constant.MessageType.msg_2101+"/");
						try {
							messageinfo.append(giveTableNo).append("|").append(receiveTableNo).append("|").append(giftNo).append("|").append(messageid).append("|").append(receiveOrderIdstr).append("|").append(java.net.URLEncoder.encode(finalmsg,"UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						new TsThread(messageinfo.toString()).run();
						
					}
				}).start();
			}catch(Exception ex){
				
			}
			List<Map<String,String>> datalist = new ArrayList<Map<String,String>>();

			return ReturnMap.getReturnMap(1, "001", "发起赠送成功成功",datalist);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("method sendGiftToAnthorTable is wrong :{}", e.getMessage());
			return ReturnMap.getReturnMap(0, "002", "服务异常请联系管理员");
		}
	}
	
	/**
	 * 
	 * 赠送礼物到邻桌
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping("/recvice")
	@ResponseBody
	public Map<String, Object> recviceGiftToAnthorTable(@RequestBody String body,HttpServletRequest reqeust) {
		logger.debug("start method recvice sendGiftInfo  waiter");
		try {
			logger.debug("update recviceGiftInfo  data for params : {} ", body);
			if (StringUtils.isEmpty(body)) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			JSONObject giftInfo = JSONObject.fromObject(body);
			if (giftInfo == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			System.out.println(">>>>>>>>>>>"+body);
			if (!giftInfo.containsKey("giftlogId") || giftInfo.getString("giftlogId") == null|| giftInfo.getString("giftlogId").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少礼物记录礼物ID");
			}
			if (!giftInfo.containsKey("giftStatus") || giftInfo.getString("giftStatus") == null|| giftInfo.getString("giftStatus").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少礼物记录状态");
			}
			if (!giftInfo.containsKey("giftPriceType") || giftInfo.getString("giftPriceType") == null|| giftInfo.getString("giftPriceType").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少礼物大小标示");
			}
			
			
			String giftLogId =  giftInfo.getString("giftlogId");
			String giftStatusstr = giftInfo.getString("giftStatus");
			String giftPriceType = giftInfo.getString("giftPriceType");
			
			String msg = giftInfo.containsKey("msg")?giftInfo.getString("msg"):"无";//留言
			
			if(StringUtils.isBlank(msg)){
				msg = "无";
			}
			
			TGiftLog log  = giftService.getGiftLogInfo(giftLogId);
			
			if (log == null){
				return ReturnMap.getReturnMap(0, "002", "没有查询到对应的送礼信息");
			}
			
			if(log.getGiftStatus()==null||!log.getGiftStatus().equals("1")){
				return ReturnMap.getReturnMap(0, "002", "礼物已经处理");
			}
			
			String primarykey = IdentifierUtils.getId().generate().toString()+"-"+giftPriceType;
			log.setGiftStatus(giftStatusstr);
			
			int tempnum = giftService.updateGiftLogInfo(log,primarykey,reqeust);
			
			if(tempnum==1){
				return ReturnMap.getReturnMap(0, "002", "礼物已经处理");
			}else if(tempnum==2){
				return ReturnMap.getReturnMap(0, "002", "数据信息不完整，请联系服务员");
			}else if(tempnum==3){
				return ReturnMap.getReturnMap(0, "003", "订单已结账");
			}else if(tempnum==4){
				return ReturnMap.getReturnMap(0, "002", "没有查询到对应的送礼信息");
			}else if(tempnum==5){
				return ReturnMap.getReturnMap(0, "002", "系统内部错误");
			}
			//推送消息到目的桌pad显示
			try{
				final String giveTableNo = String.valueOf(log.getGiveTableNo());
				final String receiveTableNo = String.valueOf(log.getReceiveTableNo());
				final String giftNo = log.getGiftNo();
				final String giftStatus = giftStatusstr;
				final String orderId = log.getOrderId();
				final String finalprimarykey = primarykey;
				final String finalmsg = msg;
				new Thread(new Runnable() {
					@Override
					public void run() {
						StringBuilder messageinfo=new StringBuilder(Constant.TS_URL+Constant.MessageType.msg_2102+"/");
						try {
							messageinfo.append(giveTableNo).append("|").append(receiveTableNo).append("|").append(giftNo).append("|").append(giftStatus).append("|").append(orderId).append("|").append(finalprimarykey).append("|").append(java.net.URLEncoder.encode(finalmsg,"UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						new TsThread(messageinfo.toString()).run();
					}
				}).start();
			}catch(Exception ex){
			    ex.printStackTrace();	
			}
			return ReturnMap.getReturnMap(1, "001", "接受礼物接口处理成功");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("method sendGiftToAnthorTable is wrong :{}", e.getMessage());
			return ReturnMap.getReturnMap(0, "002", "服务异常请联系管理员");
		}
	}
	
	
	/**
	 * 
	 * 赠送礼物到邻桌
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping("/msg")
	@ResponseBody
	public Map<String, Object> sendMsg(@RequestBody String body,HttpServletRequest reqeust) {
		logger.debug("start method recvice sendGiftInfo  waiter");
		try {
			logger.debug("update recviceGiftInfo  data for params : {} ", body);
			if (StringUtils.isEmpty(body)) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			JSONObject giftInfo = JSONObject.fromObject(body);
			if (giftInfo == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			System.out.println(">>>>>>>>>>>"+body);
			if (!giftInfo.containsKey("sendOrderId") || giftInfo.getString("sendOrderId") == null|| giftInfo.getString("sendOrderId").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少发送订单ID");
			}
			if (!giftInfo.containsKey("recOrderId") || giftInfo.getString("recOrderId") == null|| giftInfo.getString("recOrderId").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少收礼订单ID");
			}
			if (!giftInfo.containsKey("msg") || giftInfo.getString("msg") == null|| giftInfo.getString("msg").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少消息内容");
			}
			
			final String sendOrderId =  giftInfo.getString("sendOrderId");
			final String recOrderId = giftInfo.getString("recOrderId");
			final String msg = giftInfo.getString("msg");
			
			Map<String,String> params = new HashMap<String,String>();
			params.put("recOrderId", recOrderId);
			params.put("sendOrderId", sendOrderId);
			
			List<TGiftLog> logs  = giftService.getGiftLogInfo(params);
			
			if (logs == null||logs.size()<=0){
				return ReturnMap.getReturnMap(0, "002", "没有查询到对应的送礼信息");
			}
			
			//推送消息到目的桌pad显示
			try{
				new Thread(new Runnable() {
					@Override
					public void run() {
						StringBuilder messageinfo=new StringBuilder(Constant.TS_URL+Constant.MessageType.msg_2103+"/");
						messageinfo.append(sendOrderId).append("|").append(recOrderId).append("|").append(msg);
						new TsThread(messageinfo.toString()).run();
					}
				}).start();
			}catch(Exception ex){
			    ex.printStackTrace();	
			}
			return ReturnMap.getReturnMap(1, "001", "消息发送成功");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("method sendMsg is wrong :{}", e.getMessage());
			return ReturnMap.getReturnMap(0, "002", "服务异常请联系管理员");
		}
	}
	
	private Integer parseInt(String value){
		try{
			return Integer.parseInt(value);
		}catch(Exception ex){
			
		}
		return 0;
	}
	
	
	private Float parseFloat(String value){
		try{
			return Float.parseFloat(value);
		}catch(Exception ex){
			
		}
		return -1f;
	}
}
