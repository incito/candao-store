package com.candao.print.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.print.entity.PrintObj;
import com.candao.print.service.TableOptionService;
@Component
@Service
public class TableOptionServiceImpl implements TableOptionService {

	@Autowired
	private JmsTemplate jmsTemplate;
//	@Autowired
//	@Qualifier("tableSwitchQueue")
//	private Destination tableSwitchQueue;
	private ActiveMQQueue tableSwitchQueue = new ActiveMQQueue();
	
	public void sendMessage( final String message) {
		System.out.println("--------------- 开始打印-----------------"+message);
		jmsTemplate.send(tableSwitchQueue, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}

	public void sendMessage(final PrintObj obj) {

		if (obj == null) {
			return;
		}

		obj.setListenerType(Constant.ListenerType.TableChangeListener);
		String ipAddress = obj.getCustomerPrinterIp();
		if (ipAddress.contains(",")) {
			String[] ips = ipAddress.split(",");
			ipAddress = ips[0];
		}
		tableSwitchQueue.setPhysicalName(ipAddress);

		jmsTemplate.convertAndSend(tableSwitchQueue, obj);
	}
}
