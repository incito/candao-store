package com.candao.www.webroom.controller;

import java.util.List;
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

import com.candao.www.data.model.Torder;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.ComplainService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.impl.SystemServiceImpl;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/clientcomplain")
public class ComplainController {

	private static final Logger logger = LoggerFactory.getLogger(ComplainController.class);
	
	
	@Autowired
	private SystemServiceImpl systemService;

	@Autowired
	private ComplainService complainService;
	
	@Autowired
	private OrderService orderService;

	/**
	 * 按照字典类型获取字典数据
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/getComplainType",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getDectoyrByType() {
		logger.info("start method getDicListByType");
		try {
			String type ="COMPLAINT";
			List<Map<String,String>> list = systemService.getDicListByTypeForClient(type);
			if(list==null||list.size()<=0){
				return ReturnMap.getReturnMap(0, "002", "没有查询到相应的数据");
			}
			Map<String,Object> returnMap = ReturnMap.getReturnMap(1, "001", "查询成功");
			returnMap.put("data", list);
			return returnMap;
			
		} catch (Exception e) {
			logger.error("method getDicListByType is wrong :{}",e.getMessage());
			return ReturnMap.getReturnMap(0, "003", "数据异常，请联系管理员");
		}
	}
	
	/**
	 * 按照字典类型获取字典数据
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/getTimeSet",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getTimeSet() {
		logger.info("start method getTimeSet");
		try {
			String type ="RESPONSETIME";
			List<Map<String,String>> list = systemService.getTimeValueByTypeForClient(type);
			if(list==null||list.size()<=0){
				logger.info("没有查询到相应的数据");
				return ReturnMap.getReturnMap(0, "002", "没有查询到相应的数据");
			}
			Map<String,Object> returnMap = ReturnMap.getReturnMap(1, "001", "查询成功");
			returnMap.put("data", list);
			return returnMap;
			
		} catch (Exception e) {
			logger.error("method getTimeSet is wrong :{}",e.getMessage());
			return ReturnMap.getReturnMap(0, "003", "数据异常，请联系管理员");
		}
	}
	
	/**
	 * 保存投诉信息
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/saveComplain", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveComplain(@RequestBody String body) {
		logger.debug("start method saveComplain");
		try {
			logger.debug("save complain  data for params : {} ", body);
			if (StringUtils.isEmpty(body)) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			JSONObject dishInfo = JSONObject.fromObject(body);
			if (dishInfo == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}

			if (!dishInfo.containsKey("orderid")||dishInfo.getString("orderid")==null||dishInfo.getString("orderid").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数订单信息");
			}
			//验证订单信息是否存在
			Torder order = orderService.get(dishInfo.getString("orderid"));
			if(order==null){
				return ReturnMap.getReturnMap(0, "002", "订单信息不存在");
			}
			if (!dishInfo.containsKey("userid")||dishInfo.getString("userid")==null||dishInfo.getString("userid").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数服务员信息");
			}
			if (!dishInfo.containsKey("complaintType")||dishInfo.getString("complaintType")==null||dishInfo.getString("complaintType").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数投诉类型信息");
			}
			if (!dishInfo.containsKey("complaintOpinion")||dishInfo.getString("complaintOpinion")==null||dishInfo.getString("complaintOpinion").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数投诉意见信息");
			}
			if (!dishInfo.containsKey("deviceType")||dishInfo.getString("deviceType")==null||dishInfo.getString("deviceType").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数设备类型信息");
			}
			if (!dishInfo.containsKey("deviceId")||dishInfo.getString("deviceId")==null||dishInfo.getString("deviceId").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数设备号");
			}
			int num = complainService.saveComplain(dishInfo);
			if (num <= 0) {
				return ReturnMap.getReturnMap(0, "002", "保存失败");
			}
			return ReturnMap.getReturnMap(1, "001", "保存保存成功");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("method getDicListByType is wrong :{}", e.getMessage());
			return ReturnMap.getReturnMap(0, "002", "服务异常请联系管理员");
		}
	}
}
