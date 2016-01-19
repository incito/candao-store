package com.candao.common.log;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
 
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
			Logger logger =  org.slf4j.LoggerFactory.getLogger(caller);
			mapbomClass.put(caller.getName(), new LoggerHelper(logger));
			return mapbomClass.get(caller.getName());
		}
	}
	
}