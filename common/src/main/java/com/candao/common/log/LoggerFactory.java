package com.candao.common.log;

import java.util.HashMap;
import java.util.Map;
 
public class LoggerFactory {

//	private static Logger logger;

	/**
	 * 用于对象缓存
	 */
	private static Map<String,LoggerHelper> mapbomClass = new HashMap<String,LoggerHelper>();
	
	public static LoggerHelper getLogger(final Class<?> caller) {
		if (mapbomClass.containsKey(caller.getName()))
			return (LoggerHelper) mapbomClass.get(caller.getName());
		else {
			LoggerHelper loger = new LoggerHelper(caller.getName());
			mapbomClass.put(caller.getName(), loger);
			return loger;
		}
	}
}