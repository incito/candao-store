package com.candao.www.webroom.service.impl;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.print.entity.PrintObj;
import com.candao.print.service.DishSetProducerService;

@Component
@Service
public class DishSetProducerServiceImpl implements DishSetProducerService {

	@Autowired
	private JmsTemplate jmsTemplate;
//	@Autowired
//	@Qualifier("dishSetQueue")
//	private Destination dishSetQueue;
	private ActiveMQQueue dishSetQueue = new ActiveMQQueue();
	
	public void sendMessage( final String message) {
		 
	}

	public void sendMessage(final PrintObj obj) {

		if (obj == null) {
			return;
		}

		obj.setListenerType(Constant.ListenerType.DishSetListener);
		String ipAddress = obj.getCustomerPrinterIp();
		if (ipAddress.contains(",")) {
			String[] ips = ipAddress.split(",");
			ipAddress = ips[0];
		}
		dishSetQueue.setPhysicalName(ipAddress);

		jmsTemplate.convertAndSend(dishSetQueue, obj);
	}
}
