package com.candao.www.weixin.controller;

import java.io.BufferedOutputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.InputSource;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.constant.Constant;
import com.candao.www.utils.TsThread;
import com.candao.www.weixin.dto.WeixinRequestParam;
import com.candao.www.weixin.dto.WxPayDto;
import com.candao.www.weixin.dto.WxPayResult;
import com.candao.www.weixin.utils.GetWxOrderno;
import com.candao.www.weixin.utils.RequestHandler;
import com.candao.www.weixin.utils.TenpayUtil;

/**
 * 微信控制器
 * 
 * @author snail
 *
 */
@Controller
@RequestMapping("/weixin")
public class WeixinController {

	public static final String ERRORCODE = "1";

	public static final String SUCCESSCODE = "0";

	// 微信支付商户开通后 微信会提供appid和appsecret和商户号partner
	private static String appid = "wxbb9b73edc3aac8fb";
	private static String appsecret = "candao2015beijingxiangmuzukaifaa";
	private static String partner = "1260836301";
	// 这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	private static String partnerkey = "candao2015beijingxiangmuzukaifaa";
	// 微信支付成功后通知地址 必须要求80端口并且地址不能带参数
	//private static String notifyurl = "http://1.appnewspi.applinzi.com/index/hello.do"; // Key
	//private static String notifyurl = "http://1.appnewspi.applinzi.com/index.jsp"; // Key
	private static String notifyurl = "http://123.71.192.133:10000/newspicyway/weixin/notify";
	/**
	 * 生成二维码url
	 * 
	 * @param body
	 * @param orderid
	 * @param spbillCreateIp
	 * @param totalFee
	 * @return
	 */
	@RequestMapping(value = "/createQRcodeurl", produces = { "application/json;charset=UTF-8" })
	public Map<String, Object> createQRcodeurl(@RequestBody String jsonString) {
		WeixinRequestParam  weixinRequestParam=JacksonJsonMapper.jsonToObject(jsonString, WeixinRequestParam.class);
		System.out.println(weixinRequestParam);
		if (isNull(weixinRequestParam.getBody())) {
			return renderErrorJSONString(ERRORCODE, "商品信息不能为空");
		}
		if (isNull(weixinRequestParam.getOrderid())) {
			return renderErrorJSONString(ERRORCODE, "订单id不能为空");
		}
		if (isNull(weixinRequestParam.getSpbillCreateIp())) {
			return renderErrorJSONString(ERRORCODE, "ip地址不能为空");
		}
		if (isNull(weixinRequestParam.getTotalFee())) {
			return renderErrorJSONString(ERRORCODE, "商品总价不能为空");
		}
		
		WxPayDto tpWxPay1 = new WxPayDto();
		tpWxPay1.setBody(weixinRequestParam.getBody());
		tpWxPay1.setOrderId(weixinRequestParam.getOrderid());
		tpWxPay1.setSpbillCreateIp(weixinRequestParam.getSpbillCreateIp());
		tpWxPay1.setTotalFee(weixinRequestParam.getTotalFee());
		String codeurl = getCodeurl(tpWxPay1);
		return renderSuccessJSONString(SUCCESSCODE, codeurl);
		
	}

	/**
	 * 扫码支付回调新浪云回调
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/sinanotify", produces = { "application/json;charset=UTF-8" })
	public void sinanotify(String isSuccess, HttpServletResponse response) throws Exception {
		System.out.println("--------"+isSuccess);
		String resXml="";
		if ("0".equals(isSuccess)) {
			// 支付成功
			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
					+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		} else {
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		System.out.println("微信支付回调数据结束");

		/*BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		sendmessage2Android(resXml);
		out.write(resXml.getBytes());
		out.flush();
		out.close();*/

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
		System.out.print("微信支付回调数据开始");
		// String xml =
		// "<xml><appid><![CDATA[wxb4dc385f953b356e]]></appid><bank_type><![CDATA[CCB_CREDIT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1228442802]]></mch_id><nonce_str><![CDATA[1002477130]]></nonce_str><openid><![CDATA[o-HREuJzRr3moMvv990VdfnQ8x4k]]></openid><out_trade_no><![CDATA[1000000000051249]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[1269E03E43F2B8C388A414EDAE185CEE]]></sign><time_end><![CDATA[20150324100405]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1009530574201503240036299496]]></transaction_id></xml>";
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

		String isSuucess="1";//失败
		if ("SUCCESS".equals(wpr.getResultCode())) {
			// 支付成功
			isSuucess="0";
			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
					+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		} else {
			isSuucess="1";
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}

		sendmessage2Android(isSuucess);
		System.out.println("微信支付回调数据结束");

		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();

	}

	/**
	 * 消息推送
	 * @param str
	 */
	private void sendmessage2Android(String str) {
		StringBuilder messageinfo=new StringBuilder(Constant.TS_URL+Constant.MessageType.msg_5555+""+"/");
		messageinfo.append(str);
		System.out.println("微信支付推送");
		new TsThread(messageinfo.toString()).run();
	}

	/**
	 * description: 解析微信通知xml
	 * 
	 * @param xml
	 * @return
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
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

	private boolean isNull(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
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
	private static String getCodeurl(WxPayDto tpWxPayDto) {

		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = "";
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

	public static void main(String args[]){
		
		String dd=String.valueOf(new Date().getTime());
		System.out.println(dd);
		String str="{\"body\":\"测试"+dd+"\",\"orderid\":\""+dd+"\",\"spbillCreateIp\":\"192.168.0.1\",\"totalFee\":\"0.01\"}";
		WeixinRequestParam  weixinRequestParam=JacksonJsonMapper.jsonToObject(str, WeixinRequestParam.class);
		System.out.println(weixinRequestParam);
		
		WxPayDto tpWxPay1 = new WxPayDto();
		tpWxPay1.setBody(weixinRequestParam.getBody());
		tpWxPay1.setOrderId(weixinRequestParam.getOrderid());
		tpWxPay1.setSpbillCreateIp(weixinRequestParam.getSpbillCreateIp());
		tpWxPay1.setTotalFee(weixinRequestParam.getTotalFee());
		String codeurl = getCodeurl(tpWxPay1);
		
		
	}
	
}
