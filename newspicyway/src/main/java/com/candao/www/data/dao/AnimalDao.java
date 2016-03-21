package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

public interface AnimalDao {

	public final static String PREFIX = AnimalDao.class.getName();
	
	List<Map<String, Object>> allanimals();

}
