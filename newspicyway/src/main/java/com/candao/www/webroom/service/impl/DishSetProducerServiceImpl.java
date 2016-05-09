package com.candao.www.webroom.service.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.print.entity.PrintObj;
import com.candao.print.service.DishSetProducerService;

@Component
@Service
public class DishSetProducerServiceImpl implements DishSetProducerService {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("dishSetQueue")
	private Destination dishSetQueue;
	
	public void sendMessage( final String message) {
		 
	}
//	
	public void sendMessage(final PrintObj obj) {
	 
		 if(obj == null){
			 return ;
		 }
		jmsTemplate.convertAndSend(dishSetQueue, obj);
	}
}
