package com.candao.www.bossstore.dao;

import java.util.Map;

public interface TUserDao {
	public static final String PREFIX = TUserDao.class.getName();
	/**
	 * 取当天值班经理信息
	 * @return 值班经理信息
	 */
	public Map<String,String> getOpenUser(Map<String,String> params);
}
