package com.candao.www.webroom.service.impl;


import javax.jms.Destination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.JacksonObjectMapper;
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
	
	private Log log = LogFactory.getLog(BranchProducerServiceImpl.class.getName());
	
	
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
			 e.printStackTrace();
			 log.error("---------上传数据失败！--------"+JacksonJsonMapper.objectToJson(obj), e);
		 }
	}


	@Override
	public void sendMessage(String obj,Destination branchQueue) {
		
	}

}
