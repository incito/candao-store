package com.candao.www.webroom.controller;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.CallWaiterService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/clientcallwaiter")
public class CallWaiterController {

	private static final Logger logger = LoggerFactory.getLogger(CallWaiterController.class);

	@Autowired
	private CallWaiterService callWaiterService;

	/**
	 * 保存呼叫信息
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/savemessage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveComplain(@RequestBody String body) {
		logger.debug("start method savecall message waiter");
		try {
			logger.debug("save message  data for params : {} ", body);
			if (StringUtils.isEmpty(body)) {
				return ReturnMap.getFailureMap("传入参数不正确");
			}
			JSONObject messageInfo = JSONObject.fromObject(body);
			if (messageInfo == null) {
				return ReturnMap.getFailureMap("传入参数不正确");
			}

			if (!messageInfo.containsKey("msgType")||messageInfo.getString("msgType")==null||messageInfo.getString("msgType").equals("")) {
				return ReturnMap.getFailureMap("缺少参数呼叫类型");
			}
			if (!messageInfo.containsKey("tableno")||messageInfo.getString("tableno")==null||messageInfo.getString("tableno").equals("")) {
				return ReturnMap.getFailureMap("缺少参数桌号");
			}
			if (!messageInfo.containsKey("userid")||messageInfo.getString("userid")==null||messageInfo.getString("userid").equals("")) {
				return ReturnMap.getFailureMap("缺少参数服务员信息");
			}
			if (!messageInfo.containsKey("orderid")||messageInfo.getString("orderid")==null||messageInfo.getString("orderid").equals("")) {
				return ReturnMap.getFailureMap("缺少参数订单信息");
			}
			if (!messageInfo.containsKey("deviceType")||messageInfo.getString("deviceType")==null||messageInfo.getString("deviceType").equals("")) {
				return ReturnMap.getFailureMap("缺少参数设备类型信息");
			}
			if (!messageInfo.containsKey("deviceNo")||messageInfo.getString("deviceNo")==null||messageInfo.getString("deviceNo").equals("")) {
				return ReturnMap.getFailureMap("缺少参数设备号");
			}
			if (!messageInfo.containsKey("callStatus")||messageInfo.getString("callStatus")==null||messageInfo.getString("callStatus").equals("")) {
				return ReturnMap.getFailureMap("缺少参数呼叫状态");
			}
			int num = callWaiterService.saveCallInfo(messageInfo);
			if (num <= 0) {
				return ReturnMap.getFailureMap("保存失败");
			}else if(num==99){
				return ReturnMap.getFailureMap("请求类型的状态与数据库已有的状态不能继续，请联系服务员");
			}else if(num==3){
				return ReturnMap.getFailureMap("请求类型为投诉，传入的数据需要投诉类型信息");
			}
			return ReturnMap.getSuccessMap("保存成功");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("method getDicListByType is wrong :{}", e.getMessage());
			return ReturnMap.getFailureMap("服务异常请联系管理员");
		}
	}
}
