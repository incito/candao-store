package com.candao.www.webroom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.MemberService;

@Controller
@RequestMapping("/memberReport")
public class MemberReportController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberReportController.class);
	
	@Autowired
	private MemberService memberService;

	@RequestMapping(value = "/total", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAllMemberInfos(@RequestParam Map<String, String> params) {
		logger.debug("start method getAllMemberInfos");
		try{
			logger.debug("getAllMemberInfos params : {} ", params);
			
			if (params == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			
			String branchId = params.containsKey("branchId")?params.get("branchId"):"";
			String beginTime = params.containsKey("beginTime")?params.get("beginTime"):"";
			String endTime = params.containsKey("endTime")?params.get("endTime"):"";
			String shiftid = params.containsKey("shiftid")?params.get("shiftid"):"-1";
			String type = params.containsKey("type")?params.get("type"):"";
			String cardno = params.containsKey("cardno")?params.get("cardno"):"";
			String day = params.containsKey("day")?params.get("day"):"0";

			if(branchId.equals("")){
				branchId = PropertiesUtils.getValue("current_branch_id");
			}
			
			if (branchId.equals("")) {
				return ReturnMap.getReturnMap(0, "002", "门店信息获取失败");
			}
			if (beginTime.equals("")) {
				return ReturnMap.getReturnMap(0, "002", "开始时间参数不正确");
			}
			if (endTime.equals("")) {
				return ReturnMap.getReturnMap(0, "002", "结束时间参数不正确");
			}
			List<Integer> typeList = new ArrayList<Integer>();
			if(type.equals("")){
				typeList.add(5);//现金
				typeList.add(11);//银行卡
			}else{
				typeList.add(Integer.parseInt(type));
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			beginTime = beginTime.length()==10?beginTime+" 00:00:00":beginTime;
			endTime = endTime.length()==10?endTime+" 23:59:59":endTime;
			paramMap.put("branchId",branchId );
			paramMap.put("beginTime",beginTime);
			paramMap.put("endTime",endTime );
			paramMap.put("shiftid",shiftid );
			paramMap.put("typeList",typeList );
			paramMap.put("cardno",cardno);
			List<Map<String,String>>returnList = new ArrayList<Map<String,String>>();
			
			if(day.equals("0")){
				Map<String, String>  infoMap = memberService.queryMemberDealInfos(branchId, typeList, beginTime, endTime, shiftid,cardno);
				returnList.add(infoMap);
			}else if(day.equals("1")){
				returnList = memberService.queryMemberDealInfosByDay(branchId, typeList, beginTime, endTime, shiftid,cardno);
			}
			
			return ReturnMap.getReturnMap(1, "001", "操作成功",returnList);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ReturnMap.getReturnMap(0, "002", "操作失败，请联系管理员");
	} 
	
	@RequestMapping(value = "/queryMemberDealInfosToTime", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryMemberDealInfosToTime(@RequestParam Map<String, String> params) {
		logger.debug("start method getAllMemberInfos");
		try{
			logger.debug("getAllMemberInfos params : {} ", params);
			
			if (params == null) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			
			String branchId = params.containsKey("branchId")?params.get("branchId"):"";
			String beginTime = params.containsKey("beginTime")?params.get("beginTime"):"";
			String endTime = params.containsKey("endTime")?params.get("endTime"):"";
			String shiftid = params.containsKey("shiftid")?params.get("shiftid"):"-1";
			String type = params.containsKey("type")?params.get("type"):"";
			String cardno = params.containsKey("cardno")?params.get("cardno"):"";

			if(branchId.equals("")){
				branchId = PropertiesUtils.getValue("current_branch_id");
			}
			
			if (branchId.equals("")) {
				return ReturnMap.getReturnMap(0, "002", "门店信息获取失败");
			}
			if (beginTime.equals("")) {
				return ReturnMap.getReturnMap(0, "002", "开始时间参数不正确");
			}
			if (endTime.equals("")) {
				return ReturnMap.getReturnMap(0, "002", "结束时间参数不正确");
			}
			List<Integer> typeList = new ArrayList<Integer>();
			if(type.equals("")){
				typeList.add(5);//现金
				typeList.add(11);//银行卡
			}else{
				typeList.add(Integer.parseInt(type));
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			beginTime = beginTime.length()==10?beginTime+" 00:00:00":beginTime;
			endTime = endTime.length()==10?endTime+" 23:59:59":endTime;
			paramMap.put("branchId",branchId );
			paramMap.put("beginTime",beginTime);
			paramMap.put("endTime",endTime );
			paramMap.put("shiftid",shiftid );
			paramMap.put("typeList",typeList );
			paramMap.put("cardno",cardno);
//			List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
				
			@SuppressWarnings("unchecked")
			List<Map<String,String>> returnList =  memberService.queryMemberDealInfosToTime(branchId, typeList, beginTime, endTime, shiftid,cardno);
//		    returnList.add(infoMap);
			
			return ReturnMap.getReturnMap(1, "001", "操作成功",returnList);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ReturnMap.getReturnMap(0, "002", "操作失败，请联系管理员");
	} 
}
