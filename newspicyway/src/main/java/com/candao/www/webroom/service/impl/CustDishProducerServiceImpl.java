package com.candao.www.webroom.service.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.print.entity.PrintObj;
import com.candao.print.service.CustDishProducerService;

@Component
@Service
public class CustDishProducerServiceImpl implements CustDishProducerService {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("custDishQueue")
	private Destination normalDishQueue;
	
	public void sendMessage( final String message) {
		 
	}
//	
	public void sendMessage(final PrintObj obj) {
	 
		 if(obj == null){
			 return ;
		 }
		jmsTemplate.convertAndSend(normalDishQueue, obj);
	}
}
