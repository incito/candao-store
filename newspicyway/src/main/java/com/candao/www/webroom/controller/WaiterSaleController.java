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
	 * 导出服务员销售统计表主表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/exportWaiterSaleMainReport")
	@ResponseBody
	public void exportWaiterSaleMainReport(@RequestParam Map<String, Object> params,HttpServletRequest request, HttpServletResponse response){
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String branchname = itemDetailService.getBranchName(branchid);
		params.put("branchname", branchname);
		params.put("branchId", branchid);
		params.put("page", 0);
		params.put("rows", 9999999);
		List<Map<String,Object>> list = waiterSaleService.waiterSaleListProcedure(params);
		waiterSaleService.createMainExcel(request, response, list, params);
	}
	
	/**
	 * 导出服务员销售统计表子表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/exportWaiterSaleChildReport")
	@ResponseBody
	public void exportWaiterSaleChildReport(@RequestParam Map<String, Object> params,HttpServletRequest request, HttpServletResponse response){
		String branchid = PropertiesUtils.getValue("current_branch_id");
		String branchname = itemDetailService.getBranchName(branchid);
		params.put("branchname", branchname);
		params.put("branchId", branchid);
		Map<String,Object> mainList = waiterSaleService.getWaiterDishInfo(params);
		List<Map<String,Object>> childList = waiterSaleService.getWaiterSaleDetail(params);
		waiterSaleService.createChildExcel(request, response, childList, params, mainList);
	}
	
}
