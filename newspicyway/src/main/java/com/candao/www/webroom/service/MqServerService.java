package com.candao.www.webroom.service;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;


public interface MqServerService {

	  public Destination  createDestination();
	  
	  public JmsTemplate  createJmsTemplate();
	  
	  public ActiveMQQueue  createMQQueue();
	  
	  public String sendMessage(TextMessage message);
	  
	  public String sendMessage(ObjectMessage message);
	  
	  public String sendMessage();
	  
	  
	  public void onMessage(Message msg);
		   
      
}
