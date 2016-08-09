package com.candao.www.webroom.service.impl;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.print.entity.PrintObj;
import com.candao.print.service.CustDishProducerService;

@Component
@Service
public class CustDishProducerServiceImpl implements CustDishProducerService {

	@Autowired
	private JmsTemplate jmsTemplate;
//	@Autowired
//	@Qualifier("custDishQueue")
//	private Destination normalDishQueue;
	private ActiveMQQueue normalDishQueue = new ActiveMQQueue();
	
	public void sendMessage( final String message) {
		 
	}
//	
	public void sendMessage(final PrintObj obj) {

		if (obj == null) {
			return;
		}

		obj.setListenerType(Constant.ListenerType.CustDishListener);
		String ipAddress = obj.getCustomerPrinterIp();
		if (ipAddress.contains(",")) {
			String[] ips = ipAddress.split(",");
			ipAddress = ips[0];
		}
		normalDishQueue.setPhysicalName(ipAddress);

		jmsTemplate.convertAndSend(normalDishQueue, obj);
	}
}
