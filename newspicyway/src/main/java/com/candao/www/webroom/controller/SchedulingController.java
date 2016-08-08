package com.candao.www.webroom.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.PropertiesUtils;
import com.candao.www.webroom.service.ItemDetailService;
import com.candao.www.webroom.service.SchedulingService;

/**
 * 排班报表分析
 * @author zhouyao
 * @serialData 2015-12-29
 */
@Controller
@RequestMapping(value="/scheduling")
public class SchedulingController {
	@Autowired
	private SchedulingService SchedulingService;
	
	@Autowired
	private ItemDetailService itemDetailService;
	
	@RequestMapping("/schedulingReport")
	public ModelAndView schedulingReport(@RequestParam Map<String, Object> params) {
		ModelAndView mod = new ModelAndView();
		try {
			List<Map<String,Object>> schedulingReptList = SchedulingService.schedulingReport(params);
			List<Object> resultObject = getResultObject(schedulingReptList);
			mod.addObject("schedulingReptList", resultObject);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return mod;
	}
	
	/**
	 * 构建排班表返回值数据
	 * @param schedulingReptList
	 * @return
	 */
	public List<Object> getResultObject(List<Map<String,Object>> schedulingReptList){
		//获取所有的dateinterval去重
		List<String> listDateinterval = new ArrayList<String>();
		
		for (int i = 0; i < schedulingReptList.size(); i++) {
			listDateinterval.add(schedulingReptList.get(i).get("dateinterval").toString());
		}
		
		Iterator<String> it=listDateinterval.iterator();
		List<String> toHeavyResult = new ArrayList<String>();
		
		//去重dateinterval
		while(it.hasNext()){  
			  String ateintervalStr=it.next();  
			  if(toHeavyResult.contains(ateintervalStr)){  
			   it.remove();  
			  }  
			  else{  
				  toHeavyResult.add(ateintervalStr);  
			  }  
		}  
		
		List<Object> resultObject = new ArrayList<Object>();
		//循环dateinterval拼数据
		for (int i = 0; i < toHeavyResult.size(); i++) {
			Map<String,Object> schedulingMap = new HashMap<String,Object>();
			List<Object> resultMap = new ArrayList<Object>();
			for (int t = 0; t < schedulingReptList.size(); t++) {
				Map<String,Object> schedulingResultMap = new HashMap<String,Object>();
				 if(schedulingReptList.get(t).get("dateinterval").equals(toHeavyResult.get(i))){
					 Map<String,Object> map = new HashMap<String, Object>();
					 map.put("datetime", schedulingReptList.get(t).get("datetimeStr"));
					 map.put("dateinterval", schedulingReptList.get(t).get("dateinterval"));
					 map.put("openNum", schedulingReptList.get(t).get("openNum"));
					 map.put("guestNum", schedulingReptList.get(t).get("guestNum"));
					 map.put("orderamount", schedulingReptList.get(t).get("orderamount"));
					 map.put("alreadycheckNum", schedulingReptList.get(t).get("alreadycheckNum"));
					 map.put("checkamount", schedulingReptList.get(t).get("checkamount"));
					 map.put("notcheckNum", schedulingReptList.get(t).get("notcheckNum"));
					 map.put("intheNum", schedulingReptList.get(t).get("IntheNum"));
                     schedulingResultMap.put("date", schedulingReptList.get(t).get("datetimeStr"));
                     schedulingResultMap.put("values", map);
                     resultMap.add(schedulingResultMap);
                     
				 }
			}
			schedulingMap.put("time",toHeavyResult.get(i));
			schedulingMap.put("stores", resultMap);
			resultObject.add(schedulingMap);
		}
		return resultObject;
	}

	/**
	 * 导出排班参考表
	 * @author weizhifang
	 * @since 2015-12-3
	 * @param request
	 * @param response
	 * @param beginTime
	 * @param endTime
	 * @param shiftid
	 * @param week
	 * @param dateinterval
	 * @param searchType
	 * @throws Exception
	 */
	@RequestMapping(value="/exportSchedulingData/{beginTime}/{endTime}/{shiftid}/{week}/{dateinterval}")
	@ResponseBody
	public void exportSchedulingData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "beginTime") String beginTime,
			@PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "shiftid") String shiftid,
			@PathVariable(value = "week") String week,
			@PathVariable(value = "dateinterval") String dateinterval) throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		params.put("shiftid", shiftid);
		params.put("week", week);
		params.put("dateinterval", dateinterval);
		//查询门店名称
		String branchid = PropertiesUtils.getValue("current_branch_id");
        String branchname = itemDetailService.getBranchName(branchid);
        params.put("branchname", branchname);
        List<Map<String,Object>> schedulingReptList = SchedulingService.schedulingReport(params);
        SchedulingService.createExcel(request, response, schedulingReptList,params);
	}
	
}
