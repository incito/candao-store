package com.candao.www.webroom.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.utils.ExcelUtils;
import com.candao.www.utils.ReturnMap;
import com.candao.www.webroom.service.BizService;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.impl.PoiExcleTest;

/**
 * 交接班  报表处理
 * 
 * @author YANGZHONGLI
 *
 */
@Controller
@RequestMapping("/biz")
public class BizReportController {
	
	private static final Logger logger = LoggerFactory.getLogger(BizReportController.class);
	
	@Autowired
	private BizService bizService; 
	
	@Autowired
	private ItemDetailService itemDetailService;
	
	/**
	 * 查询所有开业结业信息的信息
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/infos", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBizInfos(@RequestParam Map<String, String> params,HttpServletResponse response) {
		logger.debug("start method getBizInfos");
		try{
			logger.debug("getBizInfos params : {} ", params);
			
			if (params == null||params.size()<2) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			String branchId = params.containsKey("branchId")?params.get("branchId"):"";
			String beginTime = params.containsKey("beginTime")?params.get("beginTime"):"";
			String endTime = params.containsKey("endTime")?params.get("endTime"):"";
			if(StringUtils.isBlank(branchId)){
				branchId = PropertiesUtils.getValue("current_branch_id");
			}
			if (StringUtils.isBlank(branchId)) {
				return ReturnMap.getReturnMap(0, "002", "缺少门店编号");
			}
			if (StringUtils.isBlank(beginTime)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询开始信息");
			}
			if (StringUtils.isBlank(endTime)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询结束信息");
			}
			List<Map<String, String>> returnList = bizService.getBizInfos(branchId, beginTime, endTime);
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
	 * 查询所有开业结业信息的信息
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/exportinfos/{branchId}/{beginTime}/{endTime}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> exportBizInfos(@PathVariable(value = "branchId") String branchId,@PathVariable(value = "beginTime") String beginTime,@PathVariable(value = "endTime") String endTime,HttpServletResponse response) {
		logger.debug("start method exportinfos");
		try{
			
			if(StringUtils.isBlank(branchId)){
				branchId = PropertiesUtils.getValue("current_branch_id");
			}
			if (StringUtils.isBlank(beginTime)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询开始信息");
			}
			if (StringUtils.isBlank(endTime)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询结束信息");
			}
			List<Map<String, String>> returnList = bizService.getBizInfos(branchId, beginTime, endTime);
			if(returnList==null){
				return ReturnMap.getReturnMap(0, "002", "数据查询失败");
			}
			String filedisplay = "交接班统计表";
			response.setContentType("application/vnd.ms-excel");
			String filedi = new String(filedisplay.getBytes("GBK"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + filedi + ".xls");
			String branchName = itemDetailService.getBranchName(branchId);
			Map<String, String> params = new HashMap<String,String>();
			params.put("branchName", branchName);
			params.put("type", "-1");
			params.put("beginTime", beginTime);
			params.put("endTime", endTime);
			String title = ExcelUtils.setTabTitleToBusiness("交接班统计表", params);
			String[] clounNames = {"开业时间","开业授权人","结业时间","结业授权人"};
			String[] keys = {"opentime","openauthorized","completiontime","completionauthorized"};
			HSSFWorkbook hssfwof = PoiExcleTest.createExcel(returnList, params, filedisplay, title, clounNames, keys);
			OutputStream fout = response.getOutputStream();
			hssfwof.write(fout);
			fout.flush();
			fout.close();
			hssfwof.close();
			return null;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询所有开业结业信息的信息
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/infosDetail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBizNodeClassInfos(@RequestParam Map<String, String> params) {
		logger.debug("start method getBizNodeClassInfos");
		try{
			logger.debug("getBizNodeClassInfos params : {} ", params);
			
			if (params == null||params.size()<2) {
				return ReturnMap.getReturnMap(0, "002", "传入参数不正确");
			}
			String branchId = params.containsKey("branchId")?params.get("branchId"):"";
			String beginTime = params.containsKey("beginTime")?params.get("beginTime"):"";
			String endTime = params.containsKey("endTime")?params.get("endTime"):"";
			
			if(StringUtils.isBlank(branchId)){
				branchId = PropertiesUtils.getValue("current_branch_id");
			}
			
			if (StringUtils.isBlank(beginTime)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询开始信息");
			}
			if (StringUtils.isBlank(endTime)) {
				return ReturnMap.getReturnMap(0, "002", "缺少查询结束信息");
			}
			List<Map<String, String>> returnList = bizService.getBizNodeClassInfos(branchId, beginTime, endTime);
			if(returnList==null){
				return ReturnMap.getReturnMap(0, "002", "数据查询失败");
			}
			return ReturnMap.getReturnMap(1, "001", "操作成功",returnList);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
