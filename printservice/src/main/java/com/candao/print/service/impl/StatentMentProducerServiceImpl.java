package com.candao.print.service.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.print.entity.PrintObj;
import com.candao.print.service.StatentMentProducerService;

@Component
@Service
public class StatentMentProducerServiceImpl implements StatentMentProducerService{

	
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("statementQueue")
	private Destination statementQueue;
	
	
	@Override
	public void sendMessage(String message) {
		
	}

	@Override
	public void sendMessage(PrintObj obj) {
		 if(obj == null){
			 return ;
		 }
		jmsTemplate.convertAndSend(statementQueue, obj);
		
	}

}
