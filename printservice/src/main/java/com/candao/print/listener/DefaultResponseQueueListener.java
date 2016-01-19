package com.candao.print.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class DefaultResponseQueueListener implements MessageListener {

	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage textMessage = (ObjectMessage) message;
			try {
				System.out.println("接受返回信息" + textMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
