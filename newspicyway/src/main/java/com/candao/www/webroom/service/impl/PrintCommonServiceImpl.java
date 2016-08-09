package com.candao.www.webroom.service.impl;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.common.utils.Constant;
import com.candao.print.entity.PrintObj;
import com.candao.www.webroom.service.PrintCommonService;

@Service
public class PrintCommonServiceImpl implements PrintCommonService{

	@Autowired
	private JmsTemplate jmsTemplate;
	
	private ActiveMQQueue PrintCommon = new ActiveMQQueue();
	
 
	public void sendMessage(final PrintObj obj) {

		if (obj == null) {
			return;
		}

		obj.setListenerType(Constant.ListenerType.WeighDishListener);
		String ipAddress = obj.getCustomerPrinterIp();
		if (ipAddress.contains(",")) {
			String[] ips = ipAddress.split(",");
			ipAddress = ips[0];
		}
		PrintCommon.setPhysicalName(ipAddress);
		
		jmsTemplate.convertAndSend(PrintCommon, obj);
	}
 
}
