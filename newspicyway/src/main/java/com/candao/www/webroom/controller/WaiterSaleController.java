package com.candao.www.webroom.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.security.controller.BaseController;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.WaiterSaleService;

@Controller
@RequestMapping(value="/waiterSale")
public class WaiterSaleController extends BaseController {

	@Autowired
	private WaiterSaleService waiterSaleService;
	
	@Autowired
	private ItemDetailService itemDetailService;
	
	/**
	 * 查询服务员销售统计主表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getWaiterSaleList",method = RequestMethod.POST)
	public JSONObject getWaiterSaleList(@RequestParam Map<String, Object> params, HttpServletRequest request){
		String branchid = PropertiesUtils.getValue("current_branch_id");
		params.put("branchId", branchid);
	    Map<String,Object> map = new HashMap<String,Object>();
	    if(!params.get("waiterName").equals("")){
	    	params.put("waiterName", params.get("waiterName").toString().trim());
	    }
	    if(!params.get("dishName").equals("")){
	    	params.put("dishName", params.get("dishName").toString().trim());
	    }
	    try{
	    	List<Map<String,Object>> list = waiterSaleService.waiterSaleListProcedure(params);
	    	JSONArray data = JSONArray.fromObject(list);
		    map = ReturnMap.getReturnMap(1, "001", "查询服务员销售统计表成功");
		    map.put("data", data);
	    }catch(Exception e){
	    	map = ReturnMap.getReturnMap(0, "002", "查询服务员销售统计表失败");
	    	e.printStackTrace();
	    }
	    return JSONObject.fromObject(map);
	}
	
	/**
	 * 查询服务员销售统计子表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getWaiterSaleDetail",method = RequestMethod.POST)
	public JSONObject getWaiterSaleDetail(@RequestParam Map<String, Object> params, HttpServletRequest request){
		String branchid = PropertiesUtils.getValue("current_branch_id");
		params.put("branchId", branchid);
		String dishunit = (String)params.get("dishunit");
		Map<String,Object> map = new HashMap<String,Object>();
	    try{
			dishunit = URLDecoder.decode(dishunit,"UTF-8");
			dishunit = URLDecoder.decode(dishunit,"UTF-8");
			params.put("dishunit", dishunit);
		}catch(Exception e){
			e.printStackTrace();
		}
	    try{
			List<Map<String,Object>> list = waiterSaleService.getWaiterSaleDetail(params);
			JSONArray data = JSONArray.fromObject(list);
		    map = ReturnMap.getReturnMap(1, "001", "查询服务员销售统计表成功");
		    map.put("data", data);
	    }catch(Exception e){
	    	map = ReturnMap.getReturnMap(0, "002", "查询服务员销售统计表失败");
	    	e.printStackTrace();
	    }
	    return JSONObject.fromObject(map);
	}
	
	/**
	 * 校验查询参数
	 * @author weizhifang
	 * @since 2016-3-25
	 * @param params
	 * @return
	 */
	private Map<String,Object> validateParameter(Map<String,Object> params){
		String errMsg = "";
		if(params.get("begintime") == null || params.get("begintime").equals("")){
			errMsg = "缺少起始时间";
		}
		if(params.get("endtime") == null || params.get("endtime").equals("")){
			errMsg = "缺少结束时间";
		}
		return ReturnMap.getReturnMap(0, "002", errMsg);
	}
	
	/**
	 * 导出服务员销售统计表主表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param request
	 * @param response
	 * @param beginTime
	 * @param endTime
	 * @param waiterName
	 * @param dishName
	 */
	@RequestMapping("/exportWaiterSaleMainReport/{beginTime}/{endTime}/{waiterName}/{dishName}/{searchType}/{dishtype}/{dishunit}")
	@ResponseBody
	public void exportWaiterSaleMainReport(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "waiterName") String waiterName,
			@PathVariable(value = "dishName") String dishName,
			@PathVariable(value = "searchType") String searchType,
			@PathVariable(value = "dishtype") String dishtype,
			@PathVariable(value = "dishunit") String dishunit
			){
		Map<String,Object> params = setParameter(beginTime,endTime,waiterName,dishName,searchType,"","","",dishtype,dishunit,"0","9999999");
		List<Map<String,Object>> list = waiterSaleService.waiterSaleListProcedure(params);
		waiterSaleService.createMainExcel(request, response, list, params);
	}
	
	/**
	 * 导出服务员销售统计表子表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param request
	 * @param response
	 * @param beginTime
	 * @param endTime
	 * @param searchType
	 * @param userId
	 * @param dishid
	 * @param dishtype
	 * @param dishunit
	 */
	@RequestMapping("/exportWaiterSaleChildReport/{beginTime}/{endTime}/{searchType}/{userId}/{dishid}/{dishtype}/{dishunit}")
	@ResponseBody
	public void exportWaiterSaleChildReport(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "searchType") String searchType,
			@PathVariable(value = "userId") String userId,
			@PathVariable(value = "dishid") String dishid,
			@PathVariable(value = "dishtype") String dishtype,
			@PathVariable(value = "dishunit") String dishunit){
		Map<String,Object> params = setParameter(beginTime,endTime,"","",searchType,userId,dishid,"",dishtype,dishunit,"","");
		Map<String,Object> mainList = waiterSaleService.getWaiterDishInfo(params);
		List<Map<String,Object>> childList = waiterSaleService.getWaiterSaleDetail(params);
		waiterSaleService.createChildExcel(request, response, childList, params, mainList);
	}
	
	/**
	 * 设置报表查询参数
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param beginTime
	 * @param endTime
	 * @param waiterName
	 * @param dishName
	 * @param searchType
	 * @param userId
	 * @param dishid
	 * @return
	 */
	private Map<String,Object> setParameter(String beginTime,String endTime,String waiterName,String dishName,
			String searchType,String userid,String dishid,String num,String dishtype,String dishunit,String page,String rows){
		Map<String,Object> params = new HashMap<String,Object>();
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String branchname = itemDetailService.getBranchName(branchid);
		params.put("branchname", branchname);
		params.put("branchId", branchid);
		if(beginTime != null || !"".equals(beginTime)){
			params.put("beginTime", beginTime);
		}
		if(endTime != null || !"".equals(endTime)){
			params.put("endTime", endTime);
		}
		try{
			if(!waiterName.equals("null")){
				waiterName = URLDecoder.decode(waiterName,"UTF-8");
				params.put("waiterName", waiterName.trim());
			}else{
				params.put("waiterName", "");
			}
			if(!dishName.equals("null")){
				dishName = URLDecoder.decode(dishName,"UTF-8");
				params.put("dishName", dishName.trim());
			}else{
				params.put("dishName", "");
			}
			if(!dishunit.equals("null")){
				dishunit = URLDecoder.decode(dishunit,"UTF-8");
				params.put("dishunit", dishunit);
			}else{
				params.put("dishunit", "");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!searchType.equals("null")){
			params.put("searchType", searchType);
		}
		if(!userid.equals("null")){
			params.put("userid", userid);
		}
		if(!dishid.equals("null")){
			params.put("dishid", dishid);
		}
		if(!num.equals("null")){
			params.put("num", num);
		}else{
			params.put("num", "");
		}
		if(!num.equals("dishtype")){
			params.put("dishtype", dishtype);
		}else{
			params.put("dishtype", "");
		}
		if(!page.equals("page")){
			params.put("page", page);
		}else{
			params.put("page", "");
		}
		if(!rows.equals("rows")){
			params.put("rows", rows);
		}else{
			params.put("rows", "");
		}
		return params;
	}
	
}
