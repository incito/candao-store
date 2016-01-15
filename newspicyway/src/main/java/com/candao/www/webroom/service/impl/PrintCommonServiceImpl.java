package com.candao.www.webroom.service.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.print.entity.PrintObj;
import com.candao.www.webroom.service.PrintCommonService;

@Service
public class PrintCommonServiceImpl implements PrintCommonService{

	@Autowired
	private JmsTemplate jmsTemplate;
 
	private Destination destination;
	
 
	public void sendMessage(final PrintObj obj) {
	 
		 if(obj == null){
			 return ;
		 }
		jmsTemplate.convertAndSend(destination, obj);
	}

	public void setDestination(Destination destination) {
	     
		  this.destination = destination;
	}
 

}
