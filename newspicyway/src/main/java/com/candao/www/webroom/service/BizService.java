package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

public interface BizService {
	/**
	 * 
	 * 查询指定门店开业结业信息
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> getBizInfos(String branchId,String beginTime,String endTime);
	
	/**
	 * 
	 * 查询指定门店开业结业信息
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> getBizNodeClassInfos(String branchId,String beginTime,String endTime);
	

}
