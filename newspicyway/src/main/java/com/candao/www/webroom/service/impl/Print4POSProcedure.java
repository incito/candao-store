package com.candao.www.webroom.service.impl;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.print.entity.PrintObj;

@Service
public class Print4POSProcedure implements Runnable{

	@Autowired
	private JmsTemplate jmsTemplate;

	private ActiveMQQueue PrintCommon = new ActiveMQQueue();

	private PrintObj obj;

	public void setSource(PrintObj obj){
		this.obj = obj;
	}

	public void sendMessage(final PrintObj obj) {

		if (obj == null) {
			return;
		}

		// String ipAddress = obj.getCustomerPrinterIp();
		// if (ipAddress.contains(",")) {
		// String[] ips = ipAddress.split(",");
		// ipAddress = ips[0];
		// }
		PrintCommon.setPhysicalName("10.66.18.3");

		jmsTemplate.convertAndSend(PrintCommon, obj);
	}

	@Override
	public void run() {
		this.sendMessage(obj);
	}
}
