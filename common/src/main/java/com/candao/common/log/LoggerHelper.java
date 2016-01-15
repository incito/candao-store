package com.candao.common.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerHelper {

	
private static Logger logger;
	
	/**
	 * 内部私有构造器
	 * @param logger
	 */
	protected LoggerHelper(Logger logger){
		this.logger = logger;
	}
	private void setLogger(String logType){
		this.logger = LoggerFactory.getLogger(logType);
	}
 
	public static LoggerHelper getLogger(String name) {
		logger = LoggerFactory.getLogger(name);
		return  new LoggerHelper(logger);
	}
	 
	public void debug(String logMessage, String logType) {
		setLogger(logType);
		logger.debug(logMessage);
	}
	 
	public void info(String logMessage, String logType) {
		setLogger(logType);
		logger.info(logMessage);
	}

	 
	public void error(String logMessage, String logType) {
		setLogger(logType);
		logger.error(logMessage);
	}


	public void error(Exception exception, String logType) {
		setLogger(logType);
		logger.error("错误信息：[",exception.getMessage() +" ]");
	}

 
	public void error(String logMessage, Exception exception, String logType) {
		setLogger(logType);
		logger.error(logMessage,exception);
	}
	public boolean isDebugEnabled() {
		return  logger.isDebugEnabled();
	}
	public void debug(String msg) {
		 
		logger.debug(msg);
	}
	
	
	
}