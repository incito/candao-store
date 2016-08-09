package com.candao.common.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerHelper {

	
private Logger logger;
	
	/**
	 * 内部私有构造器
	 * @param logger
	 */
	protected LoggerHelper(String name){
		logger = LoggerFactory.getLogger(name);
	}
 
	public static LoggerHelper getLogger(String name) {
		return  new LoggerHelper(name);
	}
	 
	public void debug(String logMessage, String logType) {
		logger.debug(logMessage);
	}
	 
	public void info(String logMessage, String logType) {
		logger.info(logMessage);
	}

	 
	public void error(String logMessage, String logType) {
		logger.error(logMessage);
	}


	public void error(Exception exception, String logType) {
		logger.error("错误信息：[",exception.getMessage() +" ]");
	}

 
	public void error(String logMessage, Exception exception, String logType) {
		logger.error(logMessage,exception);
	}
	public boolean isDebugEnabled() {
		return  logger.isDebugEnabled();
	}
	public void debug(String msg) {
		 
		logger.debug(msg);
	}
	
	
	
}