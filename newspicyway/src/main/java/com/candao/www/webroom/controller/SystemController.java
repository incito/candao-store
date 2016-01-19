package com.candao.www.webroom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.www.data.dao.TbDataDictionaryDao;
import com.candao.www.utils.ReturnObj;
import com.candao.www.webroom.service.impl.SystemServiceImpl;


@Controller
@RequestMapping("/system")
public class SystemController {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);
	
	

	@Autowired
	private SystemServiceImpl systemService;


	@Autowired
	private TbDataDictionaryDao tbDataDictionaryDao;

	/**
	 * 系统设置页面跳转
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/systemSet")
	public String daliyChart(HttpServletRequest request) {
//        request.setAttribute("showView", "true");		
		return "/system/systemSet";
	}

	@RequestMapping("/loadDishSelect")
	public ModelAndView loadDishSelect(){
	   ModelAndView mav = new ModelAndView("system/dishSelect");
	    return mav;
	}

	/**
	 * 修改项目字典表中的数据
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping("/insertDate")
	@ResponseBody
	public ReturnObj insertDate(@RequestBody String body) {
		ReturnObj returnObj = new ReturnObj();
		try {
			//获取修改数据的字段
			if (StringUtils.isNotBlank(body)) {
				//type":"DISHES
				JSONObject jsonObjectDish = JSONObject.fromObject(body);
				String type=jsonObjectDish.getString("type");
				if(type.equals("DISHES")){
					systemService.insertDish(JSONObject.fromObject(body));
					systemService.insertDishUnit(JSONObject.fromObject(body));
				}
				systemService.insertTdictionary(JSONObject.fromObject(body));
				return 	ReturnObj.createSuccess(systemService.getTdictionaryList());

			} else {
				return ReturnObj.createError();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnObj.createError();
		}
	}

	/**
	 * 接口列表数据
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping("/getalllist")
	@ResponseBody
	public ReturnObj getalllist(@RequestBody String body) {
		ReturnObj returnObj = new ReturnObj();
		try {
			//获取修改数据的字段
			return ReturnObj.createSuccess(systemService.getTdictionaryList());
		} catch (Exception e) {
			return ReturnObj.createError();
		}
	}
	/**
	 * 按照字典类型获取字典数据
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/getComplainDicList",method = RequestMethod.GET)
	@ResponseBody
	public ReturnObj getDectoyrByType() {
		logger.debug("start method getDicListByType");
		try {
			String type ="COMPLAINT";
			//获取修改数据的字段
			return ReturnObj.createSuccess(systemService.getDicListByType(type));
		} catch (Exception e) {
			logger.error("method getDicListByType is wrong :{}",e.getMessage());
			return ReturnObj.createError();
		}
	}


	/**
	 * 删除字典表的数据
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping("/deleteDate")
	@ResponseBody
	public ReturnObj deleteDate(@RequestParam String dictid) {
		ReturnObj returnObj = new ReturnObj();

		try {
			int statue = 0;
			if (StringUtils.isNotBlank(dictid)) {//id不为空
				statue = tbDataDictionaryDao.delete(dictid);
			}else {
				statue=0;
			}
			if (statue == 1) {		//删除成功
				return ReturnObj.createSuccess(systemService.getTdictionaryList());
			}else {
				return ReturnObj.createError();
			}

		} catch (Exception e) {
			return ReturnObj.createError();
		}
	}

	/**
	 * 按照字典类型获取字典数据
	 *
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/updateComplainDicData",method = RequestMethod.POST)
	@ResponseBody
	public ReturnObj updateDicData(@RequestBody String body) {
		logger.debug("start method updateDicData");
		try {
			logger.debug("update dic data for params : {} ",body);
			if(StringUtils.isEmpty(body)){
				logger.error("update dic data is null");
				return ReturnObj.createError("传入参数数据为空");
			}
			
			JSONObject dishInfo = JSONObject.fromObject(body);
			
			if(!dishInfo.containsKey("type")&&!dishInfo.containsKey("data")){
				logger.error("update dic data param is error");
				return ReturnObj.createError("参数格式不正确");
			}
			
			JSONArray dishInfoList = (JSONArray)dishInfo.get("data");
			
			if(dishInfoList==null||dishInfoList.size()<=0){
				logger.error("update dic data param is null");
				return ReturnObj.createError("至少需要传入一个需要修改的目标");
			}
			
			List<Map<String,Object>> updataList = new ArrayList<Map<String,Object>>();
			
			for(Object data : dishInfoList){
				if(data==null){
					continue;
				}
				JSONObject info = JSONObject.fromObject(data);
				if(info==null||!info.containsKey("dictid")||!info.containsKey("itemDesc")){
					continue;
				}
				String itemDesc =  info.get("itemDesc")==null?"":info.get("itemDesc").toString();
				String dictid =  info.get("dictid")==null?"":info.get("dictid").toString();
				
				if(dictid==null||dictid.length()<=0){
					continue;
				}
				
				if(itemDesc==null){
					continue;
				}
				
				Map<String,Object> infoMap =new HashMap<String,Object>(2);
				infoMap.put("dictid", dictid);
				infoMap.put("itemDesc", itemDesc);
				updataList.add(infoMap);
			}
			if(updataList.size()<=0){
				return ReturnObj.createError("至少需要传入一个需要修改的目标");
			}
			logger.debug("method updateDicData ro change data : {} ",updataList);
			String type= dishInfo.getString("type");
			int num = 0;
			if(type.equals("RESPONSETIME")){
				num = systemService.updataTimeset(updataList);
			}else if (type.equals("COMPLAINT")){
				num = systemService.updataDicData(updataList);
			}
			logger.debug("change dic data success ,change rows {}",num);
		} catch (Exception e) {
			logger.error("method updateDicData is wrong :{}",e.getMessage());
			return ReturnObj.createError();
		}
		return ReturnObj.createSuccess();
	}
	
	
}
