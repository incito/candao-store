package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.candao.www.data.model.TBranchBusinessInfo;

public interface BranchBusinessService {
	/**
	 * 
	 * 查询所有指定门店，指定日期的所有的数据信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<TBranchBusinessInfo> getBuinessInfos(Map<String,Object> paramMap);
	
	/**
	 * 
	 * 按天查询门店的数据信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<TBranchBusinessInfo> getBuinessInfosByDay(Map<String,Object> paramMap);
	/**
	 * 
	 * 查询门店每天所有订单信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,String>> getBranchDayOrders(Map<String,String> paramMap);
	
	/**
	 * 导出查询所有指定门店，指定日期的所有的数据信息/按天查询门店的数据信息
	 * @param params
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	public void exportReportInfos(Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp) throws Exception;
	/**
	 * 查询门店每天所有订单信息
	 * @param params
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	public void exportReportDayOrders(Map<String, String> params, HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
