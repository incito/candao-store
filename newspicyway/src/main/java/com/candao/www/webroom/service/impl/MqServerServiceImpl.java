package com.candao.www.webroom.service.impl;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.SingleConnectionFactory ;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.candao.www.webroom.service.MqServerService;
/**
 *
 *  <pre>
 * 
 * Copyright : Copyright  Pandoranews 2015 ,Inc. All right
 * Company : 餐道科技
 * </pre>
 * @author  tom_zhao
 * @version 1.0
 * @date 2015年5月7日 下午7:06:36
 * @history
 * 发送到分店队列的服务处理
 */

@Service
public class MqServerServiceImpl  implements MqServerService{

	@Autowired
	SingleConnectionFactory connectionFactory;
	

	@Autowired
	private JmsTemplate jmsTemplate; 
	
	@Override
	public Destination createDestination() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JmsTemplate createJmsTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActiveMQQueue createMQQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendMessage(TextMessage message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendMessage(ObjectMessage message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

}
