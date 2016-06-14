package com.candao.www.webroom.service.impl;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.print.entity.PrintObj;
import com.candao.print.service.StatentMentProducerService;

@Component
@Service
public class StatentMentProducerServiceImpl implements StatentMentProducerService{

	
	@Autowired
	private JmsTemplate jmsTemplate;
	
//	@Autowired
//	@Qualifier("statementQueue")
//	private Destination statementQueue;

	private ActiveMQQueue statementQueue = new ActiveMQQueue();
	
	@Override
	public void sendMessage(String message) {
		
	}

	@Override
	public void sendMessage(PrintObj obj) {
		if (obj == null) {
			return;
		}

		obj.setListenerType(Constant.ListenerType.StatementDishListener);
		String ipAddress = obj.getCustomerPrinterIp();
		if (ipAddress.contains(",")) {
			String[] ips = ipAddress.split(",");
			ipAddress = ips[0];
		}
		statementQueue.setPhysicalName(ipAddress);

		jmsTemplate.convertAndSend(statementQueue, obj);

	}

}
