package com.candao.www.webroom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.data.model.AreaManager;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.ManagerWatchService;
import com.candao.www.webroom.service.TableService;

@Controller
@RequestMapping(value="/managerWatch")
public class ManagerWatchController {

	private LoggerHelper logger = LoggerFactory.getLogger(ManagerWatchController.class);
	
	@Autowired
	private ManagerWatchService managerWatchService;
	
	@Autowired
	private TableService tableService;
	
	/**
	 * 保存经理手环区域
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/saveAreaManager", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveAreaManager(@RequestBody String json){
		logger.debug("start method saveAreaManager");
		System.out.println("start method saveAreaManager："+json);
		try{
			logger.debug("save complain  data for params : {} ", json);
			Map<String, String> map = JacksonJsonMapper.jsonToObject(json, Map.class);
			if (StringUtils.isEmpty(json)) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			if (json == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			//获取json数组
			JSONArray ja = JSONArray.fromObject(map.get("areaid").split(";"));
			String userid = map.get("userid");
			managerWatchService.deleteAreaManager(userid);
			List<AreaManager> list = new ArrayList<AreaManager>();
			for(int i=0;i<ja.size();i++){
				String areaid = ja.getString(i);
				AreaManager am = new AreaManager();
				am.setId(IdentifierUtils.getId().generate().toString());
				am.setTableareaId(areaid);
				am.setJobNumber(userid);
				list.add(am);
			}
			int f = managerWatchService.batchInsertAreaManager(list);
			if(f <= 0){
				return ReturnMap.getReturnMap(0, "002", "保存失败");
			}
			return ReturnMap.getReturnMap(1, "001", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("method saveAreaManager is wrong :{}", e.getMessage());
			return ReturnMap.getReturnMap(0, "002", "服务异常请联系管理员");
		}
	}
	
	/**
	 * 推送经理手环消息
	 * @author weizhifang
	 * @since 2016-3-21
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/sendMsgToManagerWatch", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> sendMsgToManagerWatch(@RequestBody String json){
		logger.debug("start method sendMsgToManagerWatch");
		System.out.println("start method sendMsgToManagerWatch："+json);
		try{
			logger.debug("save complain  data for params : {} ", json);
			Map<String, String> map = JacksonJsonMapper.jsonToObject(json, Map.class);
			if (StringUtils.isEmpty(json)) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			JSONObject messageInfo = JSONObject.fromObject(json);
			if (json == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			if (!messageInfo.containsKey("tableno")||messageInfo.getString("tableno")==null||messageInfo.getString("tableno").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数桌号");
			}
			if (!messageInfo.containsKey("userid")||messageInfo.getString("userid")==null||messageInfo.getString("userid").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数服务员信息");
			}
			if (!messageInfo.containsKey("status")||messageInfo.getString("status")==null||messageInfo.getString("status").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少参数状态信息");
			}
			if (!messageInfo.containsKey("orderid")||messageInfo.getString("orderid")==null||messageInfo.getString("orderid").equals("")) {
				return ReturnMap.getReturnMap(0, "002", "缺少订单信息");
			}
			int num = managerWatchService.sendManagerWatchMsg(map);
			if (num <= 0) {
				return ReturnMap.getReturnMap(0, "002", "保存失败");
			}
			return ReturnMap.getReturnMap(1, "001", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("method sendMsgToManagerWatch is wrong :{}", e.getMessage());
			return ReturnMap.getReturnMap(0, "002", "服务异常请联系管理员");
		}
	}
	
	/**
	 * 获取所有区域名和区域id
     * @author weizhifang
	 * @since 2016-3-21
	 * @return
	 */
	@RequestMapping(value="/getTableTag", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getTableTag() {
		logger.debug("start method getTableTag");
		System.out.println("start method getTableTag");
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			List<Map<String, Object>> list = tableService.getTableTag();
	    	JSONArray data = JSONArray.fromObject(list);
		    map = ReturnMap.getReturnMap(1, "001", "查询餐厅区域成功");
		    map.put("data", data);
	    }catch(Exception e){
	    	map = ReturnMap.getReturnMap(0, "002", "查询餐厅区域失败");
	    	e.printStackTrace();
	    }
	    return JSONObject.fromObject(map);
	}
}
