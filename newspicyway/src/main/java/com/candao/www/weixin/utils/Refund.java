package com.candao.www.weixin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

public class Refund {
	
	private String appid;
	private String  appsecret;
	private String  mch_id;
	private String  out_trade_no;
	private String total_fee;
	
	public Refund(){
		
	}
	
	
	public Refund(String appid, String appsecret, String mch_id, String out_trade_no, String total_fee) {
		super();
		this.appid = appid;
		this.appsecret = appsecret;
		this.mch_id = mch_id;
		this.out_trade_no = out_trade_no;
		if(total_fee!=null){
			total_fee=total_fee.substring(0, total_fee.indexOf("."));
		}
		this.total_fee = total_fee;
	}


	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	
	public int wechatRefund(String realpath) {
		//api地址：http://mch.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
		SimpleDateFormat  dateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String out_refund_no ="t"+dateFormat.format(new Date())+RandomString.getRondom(8);// 退款单号
		String out_trade_no = this.out_trade_no;//"fd89759242c548278f470d1ea413e50b";// 订单号
		String total_fee = this.total_fee;//"1";// 总金额
		String refund_fee = this.total_fee;//"1";// 退款金额
		String nonce_str = RandomString.getRondom(10);// 随机字符串
		String appid = this.appid;//"wxbb9b73edc3aac8fb"; //微信公众号apid
		String appsecret = this.appsecret;//"candao2015beijingxiangmuzukaifaa"; //微信公众号appsecret
		String mch_id = this.mch_id;//"1260836301";  //微信商户id
		String op_user_id = this.mch_id;//"1260836301";//就是MCHID
		String partnerkey = this.appsecret;//"candao2015beijingxiangmuzukaifaa";//商户平台上的那个KEY
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("out_refund_no", out_refund_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("refund_fee", refund_fee);
		packageParams.put("op_user_id", op_user_id);

		RequestHandler reqHandler = new RequestHandler(
				null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>"
				+ "<out_trade_no>" + out_trade_no + "</out_trade_no>"
				+ "<out_refund_no>" + out_refund_no + "</out_refund_no>"
				+ "<total_fee>" + total_fee + "</total_fee>"
				+ "<refund_fee>" + refund_fee + "</refund_fee>"
				+ "<op_user_id>" + op_user_id + "</op_user_id>" + "</xml>";
		String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		try {
			String s= ClientCustomSSL.doRefund(createOrderURL, xml,mch_id,realpath);
			System.out.println(s);
			 if(s.indexOf("FAIL")!=-1){
				 return 0;
			 }
			 return 1;//申请退款成功
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static void main(String[] args) {
		Refund refund=new Refund();
		//refund.wechatRefund();
	}
}
