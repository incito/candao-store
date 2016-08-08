package com.candao.www.webroom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.model.TBranchBusinessInfo;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.BranchBusinessService;
import com.candao.www.webroom.service.ItemDetailService;

/**
 * 门店营业数据
 * 
 * @author YANGZHONGLI
 *
 */
@Controller
@RequestMapping("/branchbusiness")
public class BranchBusinessController {
	
	private static final Logger logger = LoggerFactory.getLogger(BranchBusinessController.class);
	
	@Autowired
	private BranchBusinessService branchBusinessService; 
	
	@Autowired
	private ItemDetailService itemDetailService;
	
	/**
	 * 查询指定时间段内，门店的信息
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/infos", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBranchBusinessInfos(@RequestParam Map<String, String> params) {
		logger.debug("start method getBranchBusinessInfos");
		try{
			logger.debug("getBranchBusinessInfos params : {} ", params);
			
			if (params == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			
			String branchId = PropertiesUtils.getValue("current_branch_id");
			String beginTime = params.containsKey("beginTime")?params.get("beginTime"):"";
			String endTime = params.containsKey("endTime")?params.get("endTime"):"";
			String type = params.containsKey("type")?params.get("type"):"";
			String day = params.containsKey("day")?params.get("day"):"0";//0 查询汇总,每个门店一条   1按天显示
			
			if (StringUtils.isBlank(beginTime)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询时间段开始时间信息");
			}
			if (StringUtils.isBlank(endTime)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询时间段结束时间信息");
			}
			if (StringUtils.isBlank(type)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询类型");
			}
			
			List<String> branchidsList = new ArrayList<String>();
			
			branchidsList.add(branchId);
			
			if(branchidsList.size()<=0){
				return ReturnMap.getReturnMap(0, "002", "当前用户没有关联的门店");
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ids", branchidsList);
			paramMap.put("beginTime", beginTime);
			paramMap.put("endTime", endTime);
			paramMap.put("type", type);
			List<TBranchBusinessInfo> returnList= null;
			if(day.equals("0")){
				 returnList = branchBusinessService.getBuinessInfos(paramMap);
			}else if(day.equals("1")){
				 returnList = branchBusinessService.getBuinessInfosByDay(paramMap);
			}
			if(returnList==null){
				return ReturnMap.getReturnMap(0, "002", "数据查询失败");
			}
			List<Map<String,String>> returnMapList = new ArrayList<Map<String,String>>();
			
			for(TBranchBusinessInfo info : returnList){
				Map<String,String> infoMap = new HashMap<String,String>();
				infoMap.put("branchName", info.getBranchName());
				infoMap.put("jdeNo", info.getJdeNo());
				infoMap.put("branchId", info.getBranchId());
				infoMap.put("date", info.getDate());
				infoMap.put("shouldamount", info.getShouldamount().toString());
				infoMap.put("paidinamount", info.getPaidinamount().toString());
				infoMap.put("discountamount", info.getDiscountamount().toString());
				infoMap.put("cash", info.getCash().toString());
				infoMap.put("credit", info.getCredit().toString());
				infoMap.put("card", info.getCard().toString());
				infoMap.put("othercard", info.getOthercard().toString());
				infoMap.put("weixin", info.getWeixin().toString());
				infoMap.put("zhifubao", info.getZhifubao().toString());
				infoMap.put("merbervaluenet", info.getMerbervaluenet().toString());
				infoMap.put("mebervalueadd", info.getMebervalueadd().toString());
				infoMap.put("integralconsum", info.getIntegralconsum().toString());
				infoMap.put("meberTicket", info.getMeberTicket().toString());
				returnMapList.add(infoMap);
			}
			return ReturnMap.getReturnMap(1, "001", "操作成功",returnMapList);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询指定时间段内，门店的信息
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/dayorders", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBranchDayOrders(@RequestParam Map<String, String> params) {
		logger.debug("start method getBranchDayOrders");
		try{
			logger.debug("getBranchDayOrders params : {} ", params);
			
			if (params == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			
			String branchId = PropertiesUtils.getValue("current_branch_id");
			String type = params.containsKey("type")?params.get("type"):"";
			String date = params.containsKey("date")?params.get("date"):"";
			
			if (StringUtils.isBlank(branchId)) {
				return ReturnMap.getReturnMap(0, "002", "缺少门店信息");
			}
			if (StringUtils.isBlank(date)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询订单日期");
			}
			if (StringUtils.isBlank(type)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询类型");
			}

			String beginTime = date+" 00:00:00";
			String engTime = date+" 23:59:59";
			params.put("beginTime", beginTime);
			params.put("endTime", engTime);
			params.put("branchId", branchId);
			params.put("type", type);
			List<Map<String,String>> returnList = branchBusinessService.getBranchDayOrders(params);
			
			if(returnList==null){
				return ReturnMap.getReturnMap(0, "002", "数据查询失败");
			}
			return ReturnMap.getReturnMap(1, "001", "操作成功",returnList);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	/**
	 * 营业报表（门店总数据、明细）
	 *
	 * @param settlementWay
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param bankcardno
	 * @param type
	 * @param payway
	 * @param ptype
	 * @param pname
	 * @param searchType
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/exportReportInfos//{beginTime}/{endTime}/{type}/{day}")
	@ResponseBody
	public ModelAndView exportReportInfos(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "type") String type,
			@PathVariable(value = "day") String day) {
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView();
		try {
			List<String> branchidsList = new ArrayList<String>();
			String branchId = PropertiesUtils.getValue("current_branch_id");
			branchidsList.add(branchId);
			
			map.put("ids", branchidsList);
			map.put("branchId", branchId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("endTime", endTime);
			map.put("type", type);
			map.put("day", day);
	
			map.put("branchId", branchId);
			String branchName = itemDetailService.getBranchName(branchId);
			map.put("branchName", branchName);
			mav.addObject("message", "导出成功！");
			
			branchBusinessService.exportReportInfos(map, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败！");
		}
		return mav;
	}
	
	/**
	 * 查询门店每天所有订单信息
	 * @param request
	 * @param response
	 * @param branchId
	 * @param date
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/exportReportDayOrders/{date}/{type}")
	@ResponseBody
	public ModelAndView exportReportDayOrders(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "date") String date,
			@PathVariable(value = "type") String type) {
		Map<String, String> map = new HashMap<String, String>();
		ModelAndView mav = new ModelAndView();
		try {
			String branchId = PropertiesUtils.getValue("current_branch_id");
			String beginTime = date+" 00:00:00";
			String engTime = date+" 23:59:59";
			map.put("beginTime", beginTime);
			map.put("endTime", engTime);
			map.put("branchId", branchId);
			map.put("type", type);
	
			map.put("branchId", branchId);
			String branchName = itemDetailService.getBranchName(branchId);
			map.put("branchName", branchName);
			mav.addObject("message", "导出成功！");
			
			branchBusinessService.exportReportDayOrders(map, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("message", "导出失败！");
		}
		return mav;
	}
}