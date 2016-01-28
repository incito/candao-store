package com.candao.www.activemq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * 异步启动总店mq监听,门店监听不做处理
 * 
 * @author
 *
 */
public class MqAsynchronousInitListener {

	@Autowired
	@Qualifier("branchTopicListenerAdapterContainer")
	private DefaultMessageListenerContainer branchTopicListenerAdapterContainer;
	
	private Log log = LogFactory.getLog(MqAsynchronousInitListener.class.getName());

	/**
	 * 异步启动mq监听
	 */
	public void start() {
		if (branchTopicListenerAdapterContainer == null) {
			log.error("-------监听总店mq启动失败----注入失败");
			return;
		}
		// 异步启动mq,不阻塞tomcat启动
		new Thread(new Runnable() {
			public void run() {
				log.info("------启动总店mq监听-------");
				branchTopicListenerAdapterContainer.start();
			}
		}).start();
	}

	public DefaultMessageListenerContainer getBranchTopicListenerAdapterContainer() {
		return branchTopicListenerAdapterContainer;
	}

	public void setBranchTopicListenerAdapterContainer(DefaultMessageListenerContainer branchTopicListenerAdapterContainer) {
		this.branchTopicListenerAdapterContainer = branchTopicListenerAdapterContainer;
	}
}
