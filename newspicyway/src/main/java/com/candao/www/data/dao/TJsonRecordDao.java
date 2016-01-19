package com.candao.www.data.dao;

import com.candao.www.data.model.TJsonRecord;

public interface TJsonRecordDao {

	public final static String PREFIX = TJsonRecordDao.class.getName();
	/**
	 * 
	 *  插入每次pad 请求的json数据
	 *  
	 */
	public int insertJsonRecord(TJsonRecord jsonRecord);

	
}
