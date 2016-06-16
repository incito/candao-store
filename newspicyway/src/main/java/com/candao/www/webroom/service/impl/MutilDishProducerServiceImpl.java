package com.candao.www.webroom.service.impl;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.print.entity.PrintObj;
import com.candao.print.service.MutilDishProducerService;

@Component
@Service
public class MutilDishProducerServiceImpl implements MutilDishProducerService {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	private ActiveMQQueue multiDishDishQueue = new ActiveMQQueue();
	
	public void sendMessage( final String message) {
		 
	}

	public void sendMessage(final PrintObj obj) {

		if (obj == null) {
			return;
		}
		obj.setListenerType(Constant.ListenerType.MultiDishListener);
		String ipAddress = obj.getCustomerPrinterIp();
		if (ipAddress.contains(",")) {
			String[] ips = ipAddress.split(",");
			ipAddress = ips[0];
		}
		multiDishDishQueue.setPhysicalName(ipAddress);

		jmsTemplate.convertAndSend(multiDishDishQueue, obj);
	}
}
