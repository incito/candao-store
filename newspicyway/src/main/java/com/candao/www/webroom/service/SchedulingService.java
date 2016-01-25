package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 排班报表分析
 * @author zhouyao
 * @serialData 2015-12-29
 */
public interface SchedulingService {
	public List<Map<String,Object>> schedulingReport(Map<String, Object> params);
	
	/**
	 * 排班表导出
	 * @author weizhifang
	 * @since 2015-12-3
	 * @param request
	 * @param response
	 * @param schedulingReptList
	 */
	public void createExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> schedulingReptList,Map<String,Object> params);

    
}
