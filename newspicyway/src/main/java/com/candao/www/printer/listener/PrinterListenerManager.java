package com.candao.www.printer.listener;

import com.candao.print.dao.TbPrinterManagerDao;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by caicai on 2016-6-12.
 */
public class PrinterListenerManager implements SmartLifecycle, ApplicationContextAware {

	private ApplicationContext applicationContext;
	// 启动顺序
	private int phase = 9999;
	// 启动标志
	private boolean running = false;
	// 加载时启动
	private boolean autoStartup = true;
	@Autowired
	private TbPrinterManagerDao tbPrinterManagerDao;
	// 监听队列
	private Map<String, DefaultMessageListenerContainerAdapter> listeners = new ConcurrentHashMap<>();

	private final String activeMonitor = "activeMonitor";

	// 消息监听模板
	private String messageListeners = "";

	private Runnable callback;

	public PrinterListenerManager() {
	}

	public String getMessageListeners() {
		return messageListeners;
	}

	public void setMessageListeners(String messageListeners) {
		this.messageListeners = messageListeners;
	}

	@Override
	public boolean isAutoStartup() {
		return autoStartup;
	}

	@Override
	public void stop(Runnable callback) {
		this.stop();
		this.callback = callback;
		destory();
	}

	private void destory() {
		// TODO
		if (this.callback != null)
			callback.run();
	}

	private void stopConnections() {
		// TODO
	}

	private void stopListeners() {
		if (listeners != null && !listeners.isEmpty()) {
			for (Map.Entry<String, DefaultMessageListenerContainerAdapter> it : listeners.entrySet()) {
				it.getValue().stop();
				it.getValue().destroy();
			}
			listeners.clear();
		}
	}

	@Override
	public void start() {
		synchronized (activeMonitor) {
			createListeners();
			createConnections();
			callback = null;
			running = true;
		}
	}

	private void createConnections() {
		// TODO
	}

	// 创建队列
	private void createListeners() {
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> printers = tbPrinterManagerDao.find(params);
		if (printers != null && !printers.isEmpty()) {
			for (Map<String, Object> it : printers) {
				String ipaddress = String.valueOf(it.get("ipaddress"));
				if (ipaddress.contains(",")) {
					String[] ips = ipaddress.split(",");
					ipaddress = ips[0];
				}
				if (listeners.get(ipaddress) == null) {
					Destination dst = new ActiveMQQueue(ipaddress);
					DefaultMessageListenerContainerAdapter listener = (DefaultMessageListenerContainerAdapter) applicationContext
							.getBean(messageListeners);
					listener.setDestination(dst);
					listener.trulyStart();
					listeners.put(ipaddress, listener);
				}
			}
		}
	}

	@Override
	public void stop() {
		synchronized (activeMonitor) {
			stopListeners();
			stopConnections();
			running = false;
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public int getPhase() {
		return phase;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
