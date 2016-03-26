package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WaiterSaleService {

	/**
	 * 查询服务员销售列表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> waiterSaleListProcedure(Map<String,Object> params);
	
	/**
	 * 按订单查询服务员销售列表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getWaiterSaleDetail(Map<String,Object> params);
	
	/**
	 * 导出Excel主表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param request
	 * @param response
	 * @param list
	 * @param params
	 */
	public void createMainExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> list,Map<String,Object> params);
	
	/**
	 * 导出Excel子表
	 * @author weizhifang
	 * @since 2016-3-16
	 * @param request
	 * @param response
	 * @param list
	 * @param params
	 */
	public void createChildExcel(HttpServletRequest request,HttpServletResponse response,List<Map<String,Object>> childList,Map<String,Object> params,Map<String,Object> mainList);
	
	/**
	 * 查询服务员菜品信息
	 * @author weizhifang
	 * @sice 2016-3-26
	 * @param params
	 * @return
	 */
	public Map<String,Object> getWaiterDishInfo(Map<String,Object> params);
}
