package com.candao.www.webroom.service;

import java.util.Map;

import com.candao.www.data.model.ToperationLog;

public interface ToperationLogService {
	/**
	 * 新增
	 * @param toperationLog
	 */
	public boolean save(ToperationLog toperationLog); 
	
	/**
	 * 查询
	 * @param map
	 * @return
	 */
	public ToperationLog findByparams(Map<String,Object> map);
	/**
	 * 删除操作
	 * @param map
	 * @return
	 */
	public int deleteToperationLog(Map<String,Object> map);
	
	public int deleteToperationLogByTableNo(String tableNO);

}
