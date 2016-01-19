package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * 服务员考核
 * 
 * 
 * @author YANGZHONGLI
 *
 */
public interface WaiterShiftService {
	
	/**
	 * 
	 * 查询服务员信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,String>> getWaiterShiftInfo(Map<String,Object> paramMap); 
	
	
	
	/**
	 * 
	 * 查询服务员订单信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,String>> getWaiterShiftOrderInfo(Map<String,Object> paramMap); 

	
}
