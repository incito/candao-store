package com.candao.www.printer.listener;

import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.www.printer.v2.PrinterManager;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.jms.Destination;

import java.util.ArrayList;
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
	
	private List<String> ipPool;

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
				stopListener(it.getValue());
			}
			listeners.clear();
		}
	}
	
	private void stopListener(DefaultMessageListenerContainer container){
		if (container != null) {
			if (container.isRunning()) {
				container.stop();
			}
			container.destroy();
		}
	}

	@Override
	public void start() {
		synchronized (activeMonitor) {
			createListeners();
//			createConnections();
			callback = null;
			running = true;
		}
	}

	private void createConnections(List<String> ipPool2) {
		PrinterManager.initialize(ipPool2);
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
					DefaultMessageListenerContainerAdapter listener = getListener(ipaddress);
					listener.trulyStart();
					listeners.put(ipaddress, listener);
				}
			}
		}
	}
	
	private DefaultMessageListenerContainerAdapter getListener(String ipaddress) {
		Destination dst = new ActiveMQQueue(ipaddress);
		DefaultMessageListenerContainerAdapter listener = (DefaultMessageListenerContainerAdapter) applicationContext
				.getBean(messageListeners);
		listener.setDestination(dst);
		return listener;
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
	
	/**
	 * 更新监听队列
	 */
	public synchronized void updateListener(){
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> printers = tbPrinterManagerDao.find(params);
		if (ipPool == null) {
			ipPool = new ArrayList<>();
		}
		ipPool.clear();
		
		List<String> buffer = new ArrayList<>();
		if (printers != null && !printers.isEmpty()) {
			for (Map<String, Object> it : printers) {
				String ipaddress = String.valueOf(it.get("ipaddress"));
				if (ipaddress.contains(",")) {
					String[] ips = ipaddress.split(",");
					ipaddress = ips[0];
				}
				ipPool.add(ipaddress + ":" + it.get("port"));
				
				if (listeners.get(ipaddress) == null) {
					DefaultMessageListenerContainerAdapter listener = getListener(ipaddress);
					listener.trulyStart();
					listeners.put(ipaddress, listener);
				}
				buffer.add(ipaddress);
			}
		}
		
		if (!CollectionUtils.isEmpty(listeners)) {
			List<String> temp = new ArrayList<>();
			for (String ip : listeners.keySet()) {
				if (!buffer.contains(ip)) {
					temp.add(ip);
				}
			}
			this.clearListeners(temp);
		}
		
		this.createConnections(ipPool);
	}
	
	private void clearListeners(List<String> ipAddresss){
		for (String it : ipAddresss) {
			clearListener(it);
		}
	}
	
	private void clearListener(String ipAddress){
		if (StringUtils.isEmpty(ipAddress)) {
			return;
		}
		if ( !CollectionUtils.isEmpty(listeners) && listeners.get(ipAddress) != null) {
			DefaultMessageListenerContainerAdapter listener = listeners.remove(ipAddress);
			this.stopListener(listener);
		}
	}
}
