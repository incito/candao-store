package com.candao.print.service;

import javax.jms.JMSException;

public interface ConsumerService {

	public void receiveMessage(String message) throws JMSException;
	
}
