package com.candao.www.data.dao;

import java.util.Map;

import com.candao.www.data.model.ToperationLog;

public interface ToperationLogDao {
	public final static String PREFIX = ToperationLogDao.class.getName();
	/**
	 * 新增
	 * @param toperationLog
	 */
	public int save(ToperationLog toperationLog);
	
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
