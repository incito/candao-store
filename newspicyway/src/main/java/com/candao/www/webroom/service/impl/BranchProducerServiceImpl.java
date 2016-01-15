package com.candao.www.webroom.service.impl;


import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.www.webroom.model.SynSqlObject;
import com.candao.www.webroom.service.BranchProducerService;

@Service
public class BranchProducerServiceImpl implements BranchProducerService{

	@Autowired
	@Qualifier("jmsTemplateCenter")
	private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("centerQueue")
	private Destination destination;
	
	
//	@Autowired
//	@Qualifier("branchDataTopic")
//	private Destination destination;
	
	
 
	public void sendMessage(final SynSqlObject obj) {
	 
		 if(obj == null){
			 return ;
		 }
	   try{
		   jmsTemplate.convertAndSend(destination, obj);
		 }catch(Exception e){
			 jmsTemplate.convertAndSend(destination, obj);
		 }
	}


	@Override
	public void sendMessage(String obj,Destination branchQueue) {
		
	}

}
