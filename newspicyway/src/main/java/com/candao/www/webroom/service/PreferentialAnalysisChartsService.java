package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 优惠分析图表
 * @author Administrator
 *
 */
public interface PreferentialAnalysisChartsService {
	public List<Map<String,Object>> insertPreferential(Map<String, Object> params);
	public List<Map<String,Object>>  insertPreferentialDetail(Map<String,Object> params);
	public List<Map<String,Object>> insertPreferentialView(Map<String, Object> params);
	public List<Map<String,Object>>  findBranchPreferential(Map<String, Object>params);
	public void exportxlsB(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
