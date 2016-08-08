package com.candao.print.listener;

import javax.jms.Destination;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.candao.print.entity.PrintObjException;

/**
 * 异常队列消息监听器
 * @author zhangjijun
 *
 */
public class PrintExceptionListener implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Autowired
	@Qualifier("exceptionQueue")
	private Destination destination;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	
	public String receiveMessage(String message) {
		return null;
	}

	public String receiveMessage(PrintObjException exception) {
		PrintListener listener = (PrintListener) applicationContext.getBean(exception.getListenerId());
		return listener.receiveMessage(exception.getPrintObj());
	}
	
	
}
