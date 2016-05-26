package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.candao.www.webroom.model.BusinessReport;

/**
 * 营业分析图表
 * @author Administrator
 *
 */
public interface BusinessAnalysisChartsService {


	public List<BusinessReport> isfindBusinessReport(Map<String, Object> params);

	/**
	 * 导出营业分析报表
	 * @author weizhifang
	 * @since 2016-5-18
	 * @param request
	 * @param response
	 * @param BusinessList
	 * @param params
	 */
	public void createBusinessReportExcel(HttpServletRequest request, HttpServletResponse response,List<BusinessReport> BusinessList,Map<String, Object> params);


	
}
