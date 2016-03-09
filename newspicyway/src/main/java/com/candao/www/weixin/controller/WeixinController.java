package com.candao.www.weixin.controller;

import java.io.BufferedOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.InputSource;

import com.candao.common.page.Page;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.json.base.BaseJsonController;
import com.candao.www.data.model.TJsonRecord;
import com.candao.www.data.model.TOrderMember;
import com.candao.www.utils.HttpRequestor;
import com.candao.www.utils.TsThread;
import com.candao.www.webroom.model.SettlementInfo;
import com.candao.www.webroom.service.CallWaiterService;
import com.candao.www.webroom.service.JsonRecordService;
import com.candao.www.webroom.service.OrderDetailService;
import com.candao.www.webroom.service.OrderMemberService;
import com.candao.www.webroom.service.OrderSettleService;
import com.candao.www.weixin.dto.SettlementStrInfoDto;
import com.candao.www.weixin.dto.VipInfoDto;
import com.candao.www.weixin.dto.WeixinRequestParam;
import com.candao.www.weixin.dto.WeixinStatus;
import com.candao.www.weixin.dto.WxPayDto;
import com.candao.www.weixin.dto.WxPayResult;
import com.candao.www.weixin.service.WeixinService;
import com.candao.www.weixin.utils.GetWxOrderno;
import com.candao.www.weixin.utils.JsonPostQuest;
import com.candao.www.weixin.utils.Refund;
import com.candao.www.weixin.utils.RequestHandler;
import com.candao.www.weixin.utils.TenpayUtil;

import net.sf.json.JSONObject;

/**
 * 微信控制器
 * 
 * @author snail
 *
 */
@Controller
@RequestMapping("/weixin")
public class WeixinController extends BaseJsonController {

	@Autowired
	private WeixinService weixinService;
	@Autowired
	private JsonRecordService  jsonRecordService;
	@Autowired
	private OrderSettleService  orderSettleService ;
	@Autowired
	private CallWaiterService callWaiterService;
	@Autowired
	OrderDetailService   orderDetailService;
	@Autowired
	private OrderMemberService orderMemberService ;
	
	public static final String ERRORCODE = "1";

	public static final String SUCCESSCODE = "0";

	// 微信支付商户开通后 微信会提供appid和appsecret和商户号partner
	private static String notifyurl = PropertiesUtils.getValue("NOTIFYURL");
	private static String appid = null;
	private static String appsecret = null;
	private static String partner = null;
	// 这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	private static String partnerkey = null;
	
	
	
	@RequestMapping(value = "/createurl", produces = { "application/json;charset=UTF-8" })
	public Map<String, Object> createurl(HttpServletRequest request) {
		
		WeixinRequestParam weixinRequestParam = new WeixinRequestParam();
		weixinRequestParam.setInfos("125.50;100.05");
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String orderid="H"+simpleDateFormat.format(new Date());
		System.out.println(orderid);
		weixinRequestParam.setAttach(orderid);
		weixinRequestParam.setOrderid(UUID.randomUUID().toString().replaceAll("-", ""));
		weixinRequestParam.setBody("北京微信测试001");
		weixinRequestParam.setSpbillCreateIp("192.168.0.1");
		weixinRequestParam.setTotalFee("0.01");
		System.out.println(weixinRequestParam);
		
		if (isNull(weixinRequestParam.getBody())) {
			return renderErrorJSONString(ERRORCODE, "商品信息不能为空");
		}
		if (isNull(weixinRequestParam.getAttach())) {
			return renderErrorJSONString(ERRORCODE, "订单id不能为空");
		}
		if (isNull(weixinRequestParam.getSpbillCreateIp())) {
			return renderErrorJSONString(ERRORCODE, "ip地址不能为空");
		}
		if (isNull(weixinRequestParam.getTotalFee())) {
			return renderErrorJSONString(ERRORCODE, "商品总价不能为空");
		}
		//
		String branchid = PropertiesUtils.getValue("current_branch_id");// 当前门店id
		Map<String, Object> map = weixinService.queryWeixinInfoBybranchid(branchid);
		if (map != null) {
			this.appid = map.get("appid").toString();
			this.appsecret = map.get("appsecret").toString();
			this.partner = map.get("partner").toString();
			this.partnerkey = map.get("appsecret").toString();
		}
		//
		WxPayDto tpWxPay1 = new WxPayDto();
		tpWxPay1.setBody(weixinRequestParam.getBody());
		tpWxPay1.setOrderId(weixinRequestParam.getOrderid());
		tpWxPay1.setSpbillCreateIp(weixinRequestParam.getSpbillCreateIp());
		tpWxPay1.setTotalFee(weixinRequestParam.getTotalFee());
		tpWxPay1.setAttach(weixinRequestParam.getAttach());
		String codeurl = getCodeurl(tpWxPay1);
		if(codeurl!=null && !"".equals(codeurl)){
			return renderSuccessJSONString(SUCCESSCODE, codeurl);
		}
		return renderSuccessJSONString(ERRORCODE, "生成二维码失败");
	}

	
	
	/**
	 * android接口 生成二维码url
	 * 
	 * @param body
	 * @param orderid
	 * @param spbillCreateIp
	 * @param totalFee
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/createQRcodeurl", produces = { "application/json;charset=UTF-8" })
	public Map<String, Object> createQRcodeurl(@RequestBody String jsonString) {
		System.out.println(jsonString);
		WeixinRequestParam weixinRequestParam = JacksonJsonMapper.jsonToObject(jsonString, WeixinRequestParam.class);
		//weixinRequestParam.setInfos("125.50;15.05");
		String orderNo=weixinRequestParam.getOrderid();
		weixinRequestParam.setAttach(orderNo+";"+weixinRequestParam.getInfos());
		weixinRequestParam.setOrderid(UUID.randomUUID().toString().replaceAll("-", ""));
		
		System.out.println(weixinRequestParam);
		
		if (isNull(weixinRequestParam.getBody())) {
			return renderErrorJSONString(ERRORCODE, "商品信息不能为空");
		}
		if (isNull(weixinRequestParam.getAttach())) {
			return renderErrorJSONString(ERRORCODE, "订单id不能为空");
		}
		if (isNull(weixinRequestParam.getSpbillCreateIp())) {
			return renderErrorJSONString(ERRORCODE, "ip地址不能为空");
		}
		if (isNull(weixinRequestParam.getTotalFee())) {
			return renderErrorJSONString(ERRORCODE, "商品总价不能为空");
		}
		//
		String branchid = PropertiesUtils.getValue("current_branch_id");// 当前门店id
		Map<String, Object> map = weixinService.queryWeixinInfoBybranchid(branchid);
		if (map != null) {
			this.appid = map.get("appid").toString();
			this.appsecret = map.get("appsecret").toString();
			this.partner = map.get("partner").toString();
			this.partnerkey = map.get("appsecret").toString();
		}
		//
		WxPayDto tpWxPay1 = new WxPayDto();
		tpWxPay1.setBody(weixinRequestParam.getBody());
		tpWxPay1.setOrderId(weixinRequestParam.getOrderid());
		tpWxPay1.setSpbillCreateIp(weixinRequestParam.getSpbillCreateIp());
		tpWxPay1.setTotalFee(weixinRequestParam.getTotalFee());
		tpWxPay1.setAttach(weixinRequestParam.getAttach());
		String codeurl = getCodeurl(tpWxPay1);
		if(codeurl!=null && !"".equals(codeurl)){
			return renderSuccessJSONString(SUCCESSCODE, codeurl);
		}
		return renderSuccessJSONString(ERRORCODE, "生成二维码失败");
	}

	
	/**
	 * 查询是否存在微信配置信息
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/getweixinstatus", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> getweixinstatus(@RequestBody String jsonString) {
		System.out.println(jsonString);
		WeixinStatus  weixinStatus=JacksonJsonMapper.jsonToObject(jsonString, WeixinStatus.class);
		int result=weixinService.getweixinstatus(weixinStatus.getBranchid());
		if(result>0){
			System.out.println(renderSuccessJSONString(SUCCESSCODE,null));
			return renderSuccessJSONString(SUCCESSCODE,null);
		}
		return renderErrorJSONString(null, "门店没有配置微信相关信息");
	}
	
	@RequestMapping(value = "/testweixin")
	@ResponseBody
	public Map<String, Object> testweixin() {
		int result=weixinService.getweixinstatus("579744");
		if(result>0){
			System.out.println(renderSuccessJSONString(SUCCESSCODE,null));
			return renderSuccessJSONString(SUCCESSCODE,null);
		}
		return renderErrorJSONString(null, "门店没有配置微信相关信息");
	}
	/**
	 * 微信退款
	 * @param orderno
	 * @return
	 */
	@RequestMapping(value = "/turnback", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String turnback(String orderno,HttpServletRequest request){
		String branchid = PropertiesUtils.getValue("current_branch_id");// 当前门店id
		Map<String, Object> map = weixinService.queryWeixinInfoBybranchid(branchid);
		if (map != null) {
			this.appid = map.get("appid").toString();
			this.appsecret = map.get("appsecret").toString();
			this.partner = map.get("partner").toString();
			this.partnerkey = map.get("appsecret").toString();
		}
		Map<String, Object>  weixininfos=weixinService.selectinfos(orderno);
		String realpath=request.getSession().getServletContext().getRealPath("");
		if(weixininfos!=null){
				String outorderno=getStringFromMap(weixininfos, "outorderno");
				String totalmoney=getStringFromMap(weixininfos, "dueamount");
				Refund refund=new Refund(this.appid, this.appsecret, this.partner, outorderno, "1");
				int result=refund.wechatRefund(realpath);
				if(result==1){
					weixinService.deletetemp(orderno);
					return JacksonJsonMapper.objectToJson(getSuccessInstance(null));
				}
		}
		return JacksonJsonMapper.objectToJson(getFailInstance(null, "操作失败"));
	}
	
	/**
	 * 扫码支付回调新浪云回调
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Deprecated
	@RequestMapping(value = "/sinanotify", produces = { "application/json;charset=UTF-8" })
	public void sinanotify(String isSuccess, HttpServletResponse response) throws Exception {
		System.out.println("--------" + isSuccess);
		@SuppressWarnings("unused")
		String resXml = "";
		if ("0".equals(isSuccess)) {
			// 支付成功
			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
					+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		} else {
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		System.out.println("微信支付回调数据结束");
	}

	/**
	 * 扫码支付回调
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify", produces = { "application/json;charset=UTF-8" })
	public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("微信支付回调数据开始");
	
		String inputLine;
		String notityXml = "";
		String resXml = "";

		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("接收到的报文：" + notityXml);

		@SuppressWarnings("rawtypes")
		Map m = parseXmlToList2(notityXml);
		WxPayResult wpr = new WxPayResult();
		wpr.setAppid(m.get("appid").toString());
		wpr.setBankType(m.get("bank_type").toString());
		wpr.setCashFee(m.get("cash_fee").toString());
		wpr.setFeeType(m.get("fee_type").toString());
		wpr.setIsSubscribe(m.get("is_subscribe").toString());
		wpr.setMchId(m.get("mch_id").toString());
		wpr.setNonceStr(m.get("nonce_str").toString());
		wpr.setOpenid(m.get("openid").toString());
		wpr.setOutTradeNo(m.get("out_trade_no").toString());
		wpr.setResultCode(m.get("result_code").toString());
		wpr.setReturnCode(m.get("return_code").toString());
		wpr.setSign(m.get("sign").toString());
		wpr.setTimeEnd(m.get("time_end").toString());
		wpr.setTotalFee(m.get("total_fee").toString());
		wpr.setTradeType(m.get("trade_type").toString());
		wpr.setTransactionId(m.get("transaction_id").toString());
		wpr.setAttach(m.get("attach").toString());
		String isSuucess = "1";// 失败
		
		//先查询是否已经回调过了
		int isSave=weixinService.queryIsSave(wpr.getAttach().split(";")[0]);
		if(isSave==0){
			if ("SUCCESS".equals(wpr.getResultCode())) {
				// 支付成功
				try{
					String[] attchresults=wpr.getAttach().split(";");
					//1结账和清台
					SettlementStrInfoDto settlementStrInfoDto=new SettlementStrInfoDto();
					orderSettleService.updatePadData(wpr.getAttach());
					settlementStrInfoDto=orderSettleService.setInitData(settlementStrInfoDto,wpr);
					String settlementStrInfo=JacksonJsonMapper.objectToJson(settlementStrInfoDto);
					settleOrder(settlementStrInfo);
					vipinterface(attchresults,settlementStrInfoDto.getUserName());
					//打印结账单
					orderDetailService.printStatement(attchresults[0]);
					//保存到orderid和随机订单id到数据表
					
				}catch(Exception e){
					isSuucess = "2";//支付成功，清台或者打印结账单出错
				}
				isSuucess = "0";
				
				System.out.println("当前订单id"+wpr.getAttach());
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
				isSuucess = "1";
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}
			sendmessage2Android(isSuucess,wpr.getAttach());
			sendmessage2Handler(isSuucess, wpr.getAttach());
			System.out.println("微信支付回调数据结束");
	
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		}
	}

	private void vipinterface(String[] args,String username){
		if(args!=null){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("branch_id", PropertiesUtils.getValue("current_branch_id"));
			if(args!=null && args.length>0 && args.length>4){
				jsonObject.put("Serial", args[0]);
				jsonObject.put("FCash", "0.0");
				jsonObject.put("FWeChat", args[1]);
				jsonObject.put("cardno", args[3]);
				//jsonObject.put("cardno", "100016000001");
				jsonObject.put("password", "0");
				//
				jsonObject.put("securityCode", "");
				jsonObject.put("FIntegral", "0.0");
				jsonObject.put("FStore", "0.0");
				jsonObject.put("FTicketList", "");
				String url=PropertiesUtils.getValue("VIP_URL");
				if(args[3].length()>2){
							JSONObject vipresult= JsonPostQuest.httpPost(url, jsonObject);
							if(vipresult!=null){
									String     recode=   (String) vipresult.get("Retcode");
									if("0".equals(recode)){
										//成功
										TOrderMember member=new TOrderMember();
										member.setOrderid(args[0]);
										member.setCardno("100016000001");
										member.setBusiness(PropertiesUtils.getValue("current_branch_id"));
										member.setBusinessname("上海餐道");
										member.setUserid(username);
										member.setSerial(vipresult.get("TraceCode").toString());
										orderMemberService.saveOrderMember(member);
									}
							}
				
				}
			}else{
				System.out.println("pad端参数传递错误");
			}
		}
	}
	
	/**
	 * 消息推送
	 * 
	 * @param str
	 */
	private void sendmessage2Android(String str,String orderno) {
		StringBuilder messageinfo = new StringBuilder(Constant.TS_URL + Constant.MessageType.msg_2104 + "" + "/");
		messageinfo.append(str).append("|").append(orderno);
		System.out.println("微信支付推送");
		new TsThread(messageinfo.toString()).run();
	}

	private void sendmessage2Handler(String str,String orderno) {
		StringBuilder messageinfo = new StringBuilder(Constant.TS_URL + Constant.MessageType.msg_2011 + "" + "/");
		String[] handlers= orderno.split("\\|");
		String orderid=handlers[0];
		System.out.println(orderid);
		Map<String, Object>  result= orderSettleService.selectorderinfos(orderid);
		String msgtype="14";
		if(ERRORCODE.equals(str)){
			msgtype="15";
		}
		String areaname = null;
		try {
			 areaname = java.net.URLEncoder.encode(getStringFromMap(result, "areaname"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		messageinfo.append(getStringFromMap(result, "userid")).append("|")
							.append(msgtype).append("|").append("0").append("|")
							.append(areaname).append("|")
							.append(getStringFromMap(result, "tableNo")).append("|")
							.append(UUID.randomUUID().toString().replaceAll("-", ""));
		System.out.println("手环推送成功");
		new TsThread(messageinfo.toString()).run();
	}
	/**
	 * description: 解析微信通知xml
	 * 
	 * @param xml
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}

	private Map<String, Object> renderErrorJSONString(String result, String errorMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		map.put("msg", errorMsg);
		return map;
	}

	private Map<String, Object> renderSuccessJSONString(String result, String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		map.put("data", data);
		return map;
	}

	/**
	 * 获取微信扫码支付二维码连接
	 */
	@SuppressWarnings("static-access")
	private static String getCodeurl(WxPayDto tpWxPayDto) {

		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach =tpWxPayDto.getAttach();
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());

		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = notifyurl;
		String trade_type = "NATIVE";

		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>" + mch_id + "</mch_id>" + "<nonce_str>"
				+ nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + body + "]]></body>"
				+ "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url + "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		code_url = new GetWxOrderno().getCodeUrl(createOrderURL, xml);
		System.out.println("code_url----------------" + code_url);

		return code_url;
	}

	/**
	 * 元转换成分
	 * 
	 * @param money
	 * @return
	 */
	private static String getMoney(String amount) {
		if (amount == null) {
			return "";
		}
		// 金额转化为分为单位
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
																// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
		}
		return amLong.toString();
	}

	/**
	 * 获取随机字符串
	 * 
	 * @return
	 */
	private static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}

	/********************************************* web接口开始 *************************************************************/

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param rows
	 * @param param
	 * @return
	 */
	@RequestMapping("/page")
	@ResponseBody
	public String page(Integer page, Integer rows,String branchid,String branchname,String partner) {
		if(isNull(branchid)){branchid=null;}
		if(isNull(branchname)){branchname=null;}
		if(isNull(partner)){partner=null;}
		 Map<String, Object> param=new HashMap<>();
		 param.put("branchid", branchid);
		 param.put("branchname", branchname);
		 param.put("partner", partner);
		 param.put("page", page);
		 param.put("rows", rows);
		Page<Map<String, Object>> pageMap = weixinService.queryinfos(param, page, rows);
		 return JacksonJsonMapper.objectToJson(pageMap);
	}

	/**
	 * 根据id查询分店名称
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/queryNameByBranchid")
	@ResponseBody
	public String queryNameByBranchid(String branchid) {
		List<Map<String, Object>> results = weixinService.queryNameByBranchid(branchid);

		return JacksonJsonMapper.objectToJson(results);

	}
	/**
	 * 根据分店名称查询分店id
	 * @param branchname
	 * @return
	 */
	@RequestMapping("/queryBranchidByName")
	@ResponseBody
	public String queryBranchidByName(String branchname) {
		List<Map<String, Object>> results = weixinService.queryBranchidByName(branchname);

		return JacksonJsonMapper.objectToJson(results);

	}
	
	/**
	 * 添加微信配置信息
	 * @param param
	 * @return
	 */
	@RequestMapping("/addweixinInfo")
	@ResponseBody
	public String  addweixinInfo(@RequestParam Map<String, Object> param){
		//1先查询是否已经添加过
		Map<String, Object>   count= weixinService.queryisExsit(param);
		if(count!=null){
			String con=count.get("con").toString();
			if(Integer.parseInt(con)>0){
				return JacksonJsonMapper.objectToJson(getFailInstance(null, "该门店已经配置了微信支付配置信息"));
			}
		}
		//保存
		weixinService.addweixinInfo(param);
		return JacksonJsonMapper.objectToJson(getSuccessInstance(null));
	}
	
	/**
	 * 更新微信配置信息
	 * @param param
	 * @return
	 */
	@RequestMapping("/updateweixinInfo")
	@ResponseBody
	public String  updateweixinInfo(@RequestParam Map<String, Object> param){
		//1先查询是否已经添加过
		Map<String, Object>   count= weixinService.queryisExsit(param);
		if(count!=null){
			String con=count.get("con").toString();
			if(Integer.parseInt(con)>0){
				return JacksonJsonMapper.objectToJson(getFailInstance(null, "该门店已经配置了微信支付配置信息"));
			}
		}
		//保存
		weixinService.updateweixinInfo(param);
		return JacksonJsonMapper.objectToJson(getSuccessInstance(null));
	}
	
	
	
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(String id){
		weixinService.delete(id);
		return JacksonJsonMapper.objectToJson(getSuccessInstance(null));
	}
	/**
	 * 根据id查询微信配置信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryweixinInfo")
	@ResponseBody
	public String queryweixinInfo(String id){
		if(isNull(id)){
			return JacksonJsonMapper.objectToJson(getFailInstance(null, "id不能为空"));
		}
		Map<String, Object>  result=weixinService.queryweixinInfo(id);
		return JacksonJsonMapper.objectToJson(getSuccessInstance(result));
	}

	/**
	 * 查询优惠活动
	 * @return
	 */
	@RequestMapping("/queryActivity")
	@ResponseBody
	public String queryActivity(){
		String activityCode="09";
		Map<String, Object>  result=weixinService.queryActivity(activityCode);
		return JacksonJsonMapper.objectToJson(getSuccessInstance(result));
	}
	
	/**
	 * 查询用户填入信息是否有效
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/queryiseffective")
	@ResponseBody
	public String queryiseffective(String appid, String appsecret, String partner) {
		if (isNull(appid)) {
			return JacksonJsonMapper.objectToJson(getFailInstance(null, "appid不能为空"));
		}
		if (isNull(appsecret)) {
			return JacksonJsonMapper.objectToJson(getFailInstance(null, "appsecret不能为空"));
		}
		if (isNull(partner)) {
			return JacksonJsonMapper.objectToJson(getFailInstance(null, "partner不能为空"));
		}
		String dd = String.valueOf(new Date().getTime());
		String jsonString = "{\"body\":\"测试" + dd + "\",\"orderid\":\"" + dd
				+ "\",\"spbillCreateIp\":\"192.168.0.1\",\"totalFee\":\"0.01\"}";
		WeixinRequestParam weixinRequestParam = JacksonJsonMapper.jsonToObject(jsonString, WeixinRequestParam.class);
		this.appid = appid;
		this.appsecret = appsecret;
		this.partner = partner;
		this.partnerkey = appsecret;
		//
		WxPayDto tpWxPay1 = new WxPayDto();
		tpWxPay1.setBody(weixinRequestParam.getBody());
		tpWxPay1.setOrderId(weixinRequestParam.getOrderid());
		tpWxPay1.setSpbillCreateIp(weixinRequestParam.getSpbillCreateIp());
		tpWxPay1.setTotalFee(weixinRequestParam.getTotalFee());
		String codeurl = getCodeurl(tpWxPay1);
		if (codeurl == null || "".equals(codeurl)) {
			return JacksonJsonMapper.objectToJson(getFailInstance(null, "商户id与appid与appsecret不匹配"));
		}
		return JacksonJsonMapper.objectToJson(getSuccessInstance(null));
	}
	
	
	/**
	 *  结账
	 */
	private String settleOrder( String settlementStrInfo){
		
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
			  /*//调用进销存接口 返回数据给 进销存 管理
			  *//**
			  {

				    "orderid": "H20151111152314000068",
				    "orderMap": {
				        "0d19a1a8-d257-4f1c-95b1-7d48c874c71b": "1",
				        "0f7a4a91-a7ba-459a-b41a-a9e917207097": "1",
				        "1a96ae20-1a89-4d86-9d73-de668d20feb7": "2",
				        "1bf992a5-f7ff-4484-9381-1f456d9f53ac": "1",
				        "31d4aaf7-b7d3-43df-8b3c-c512f8d91eea": "1",
				        "6efea012-c10d-4428-b9f4-eacd305664b6": "1",
				        "72e063c3-afdd-41ec-a3a3-a77d4879d65f": "1",
				        "DISHES_98": "2",
				        "a93c4248-b76a-4dbf-a27e-198b17a9b280": "1",
				        "c24adc46-c37f-466c-86f5-1f6547315d8e": "1",
				        "f1ae150c-7a2a-43e7-863e-f86534ac6a29": "1"
				    }
				}
				**//*
		 String retString = orderDetailService.getOrderDetailByOrderId(orderid);
        //String retPSI = HttpUtils.httpPostBookorderArray(PropertiesUtils.getValue("PSI_URL") + PropertiesUtils.getValue("PSI_SUFFIX_ORDER"), retString);
		String url="http://"+PropertiesUtils.getValue("PSI_URL") + PropertiesUtils.getValue("PSI_SUFFIX_ORDER");
		Map<String, String> dataMap = new HashMap<String, String>();
		 dataMap.put("data", retString);
		String retPSI = null;
		try {
			retPSI = new HttpRequestor().doPost(url, dataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 @SuppressWarnings("unchecked")
		  Map<String,String> retMap = JacksonJsonMapper.jsonToObject(retPSI, Map.class);
		  if(retMap == null || "1".equals(retMap.get("code"))){		
			  SettlementInfo info = new SettlementInfo();
			  info.setOrderNo(orderid);
			  orderSettleService.rebackSettleOrder(settlementInfo);
			  return Constant.FAILUREMSG;
           }*/
		    return Constant.SUCCESSMSG;
		}else {
			return Constant.FAILUREMSG;
		}
	}
	
	
}
