package com.candao.www.bossstore.consts;

/**
 * Created by IntelliJ IDEA.
 * User: johnson
 * Date: 6/30/15
 * Time: 10:24 上午
 */
public class Constants {

	//公共请求信息
	public final static String command = "command"; //请求命令
	public final static String imei = "imei"; //手机标识
	public final static String imsi = "imsi"; //手机标识
	public final static String softwareVersion = "softwareversion"; //版本号
	public final static String coopId = "coopid"; //渠道号
	public final static String machineId = "machineid"; //机型
	public final static String platform = "platform"; //平台(android|iPhone|塞班)
	public final static String isemulator =  "isemulator"; //是否是模拟器
	public final static String isCompress = "isCompress"; //是否压缩
	public final static String phoneSIM = "phoneSIM"; //SIM卡手机号
	public final static String mac = "mac"; //网卡地址
	public final static String email = "email"; //邮箱
	public final static String phonenum = "phonenum"; //手机号
	public final static String username = "username"; //用户名
	public final static String pwd = "pwd"; //密码
	public final static String oldPwd = "oldPwd"; //旧密码
	public final static String branchId = "branchId"; //门店id
	public final static String codeKey = "codeKey"; //验证码key
	public final static String token = "token"; // token
	public final static String data = "data";// 当天营业明细

	//类型
	public final static String requestType = "requestType"; //请求类型
	public final static String type = "type"; //查询类型



	//返回
	public final static String error_code = "error_code"; //错误码
	public final static String message = "message"; //错误信息


	//其他
	public final static String key = "<>hj12@#$$%^~~ff"; //加密串


	public static String getKey() {
		return key;
	}

	public static String getError_code() {
		return error_code;
	}

	public static String getMessage() {
		return message;
	}

	public static String getCommand() {
		return command;
	}

	public static String getImei() {
		return imei;
	}

	public static String getImsi() {
		return imsi;
	}

	public static String getSoftwareVersion() {
		return softwareVersion;
	}

	public static String getCoopId() {
		return coopId;
	}

	public static String getMachineId() {
		return machineId;
	}

	public static String getPlatform() {
		return platform;
	}

	public static String getIsemulator() {
		return isemulator;
	}

	public static String getCompress() {
		return isCompress;
	}

	public static String getPhoneSIM() {
		return phoneSIM;
	}

	public static String getMac() {
		return mac;
	}

	public static String getRequestType() {
		return requestType;
	}

	public static String getType() {
		return type;
	}
}
