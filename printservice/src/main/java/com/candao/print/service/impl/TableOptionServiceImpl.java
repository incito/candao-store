package com.candao.print.service.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.print.entity.PrintObj;
import com.candao.print.service.TableOptionService;
@Component
@Service
public class TableOptionServiceImpl implements TableOptionService {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("tableSwitchQueue")
	private Destination tableSwitchQueue;
	
	public void sendMessage( final String message) {
		System.out.println("--------------- 开始打印-----------------"+message);
		jmsTemplate.send(tableSwitchQueue, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}
	public void sendMessage(final PrintObj obj) {
	 
		 if(obj == null){
			 return ;
		 }
		
		 
		jmsTemplate.convertAndSend(tableSwitchQueue, obj);
	}
}
