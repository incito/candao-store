package com.candao.print.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TopicConsumer implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("TopicConsumer������������ߵ���Ϣ��");
		System.out.println(message.getClass().getName());
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			try {
				System.out.println("TopicConsumer���յ�����Ϣ�ǣ�" + textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
