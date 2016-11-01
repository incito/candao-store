package com.candao.www.bossstore.util;

import javax.servlet.http.HttpServletRequest;

import com.candao.www.bossstore.consts.Constants;
import com.candao.www.bossstore.protocol.ClientInfo;

import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: johnson
 * Date: 6/30/15
 * Time: 10:42 上午
 */
public class RequestParamUtil {

	/**
	 * 解析参数
	 *
	 * @param request
	 * @param requestObject
	 * @param clientInfo
	 */
	public static void parseRequestParam(HttpServletRequest request, JSONObject requestObject, ClientInfo clientInfo) {
		//基本参数
		clientInfo.setCommand(requestObject.getString(Constants.command)); //请求命令
		clientInfo.setSysSessionid(request.getSession().getId()); //SessionId

		if (requestObject.has(Constants.imei)) { //手机标识
			clientInfo.setImei(requestObject.getString(Constants.imei));
		}
		if (requestObject.has(Constants.imsi)) { //SIM卡标识
			clientInfo.setImsi(requestObject.getString(Constants.imsi));
		}
		if (requestObject.has(Constants.softwareVersion)) {
			clientInfo.setSoftwareVersion(requestObject.getString(Constants.softwareVersion)); //版本号
		}
		if (requestObject.has(Constants.machineId)) {
			clientInfo.setMachineId(requestObject.getString(Constants.machineId)); //机型
		}
		if (requestObject.has(Constants.coopId)) {
			clientInfo.setCoopId(requestObject.getString(Constants.coopId)); //渠道号
		}
		if (requestObject.has(Constants.platform)) {
			clientInfo.setPlatform(requestObject.getString(Constants.platform)); //平台
		}
		if (requestObject.has(Constants.isCompress)) { //是否压缩
			clientInfo.setIsCompress(requestObject.getString(Constants.isCompress));
		}
		if (requestObject.has(Constants.isemulator)) { //是否是模拟器联网
			clientInfo.setIsemulator(requestObject.getString(Constants.isemulator));
		}
		if (requestObject.has(Constants.phoneSIM)) { //SIM卡手机号
			clientInfo.setPhoneSIM(requestObject.getString(Constants.phoneSIM));
		}
		if (requestObject.has(Constants.mac)) { //网卡地址
			clientInfo.setMac(requestObject.getString(Constants.mac));
		}

		//类型
		if (requestObject.has(Constants.requestType)) { //请求类型
			clientInfo.setRequestType(requestObject.getString(Constants.requestType));
		}
		if (requestObject.has(Constants.type)) { //查询类型
			clientInfo.setType(requestObject.getString(Constants.type));
		}

		if (requestObject.has(Constants.username)) { // 用户名
			clientInfo.setUsername(requestObject.getString(Constants.username));
		}
		if (requestObject.has(Constants.phonenum)) { // 手机号
			clientInfo.setPhonenum(requestObject.getString(Constants.phonenum));
		}
		if (requestObject.has(Constants.email)) { // 邮箱
			clientInfo.setEmail(requestObject.getString(Constants.email));
		}
		if (requestObject.has(Constants.pwd)) { // 密码
			clientInfo.setPwd(requestObject.getString(Constants.pwd));
		}
		if (requestObject.has(Constants.oldPwd)) { // 旧密码
			clientInfo.setOldPwd(requestObject.getString(Constants.oldPwd));
		}
		if (requestObject.has(Constants.branchId)) { // 门店id
			clientInfo.setBranchId(requestObject.getString(Constants.branchId));
		}
		if (requestObject.has(Constants.token)) { // token
			clientInfo.setToken(requestObject.getString(Constants.token));
		}
		if (requestObject.has(Constants.codeKey)) { // codeKey
			clientInfo.setCodeKey(requestObject.getString(Constants.codeKey));
		}
		if (requestObject.has(Constants.data)) {// 当天营业明细
			clientInfo.setData(requestObject.getString(Constants.data));
		}

	}
}
