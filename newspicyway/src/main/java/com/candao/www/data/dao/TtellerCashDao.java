package com.candao.www.data.dao;

import java.util.List;

import com.candao.www.data.model.TtellerCash;

public interface TtellerCashDao {

	public final static String PREFIX = TtellerCashDao.class.getName();
	
	/**
	 * 获取未清机的POS列表
	 * @return
	 */
	public List<TtellerCash> findUncleanPosList();
	
}
