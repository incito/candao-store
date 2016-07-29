package com.candao.www.printer.listener;

import com.candao.common.utils.Constant.ListenerType;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.listener.QueueListener;
import com.candao.print.listener.template.ListenerTemplate;
import com.candao.www.printer.listener.namespaceHandler.SimpleNamespaceHandlerResover;
import com.candao.www.printer.listener.namespaceHandler.XmlReaderContext;
import com.candao.www.printer.listener.template.XMLTemplateDefinition;
import com.candao.www.printer.v2.PrinterManager;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
	// 监听模板对应字体
	private Map<String, Map<String, Object>> listenerTemplate = new ConcurrentHashMap<>();
	// 标准板式
	private static final Integer STANDARD = 1;
	// 大字体
	private static final Integer LARGE = 3;
	// 小字体
	private static final Integer SMALL = 2;

	private Log log = LogFactory.getLog(PrinterListenerManager.class.getName());

	private static ReadWriteLock lock = new ReentrantReadWriteLock();

	private final static String TEMPLATEPATH = "template/printerTemplate";
	// XML模板
	private Map<String, XMLTemplateDefinition> listenerXMLTemplate = new ConcurrentHashMap<>();

	@Autowired
	private XmlTemplateLoader xmlTemplateLoader;

	@Autowired
	private XmlTemplateDefinitionReader xmlTemplateDefinitionReader;

	private static final String XMLTEMPLATELISTENER = "xmlTemplateListener";

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

	private void stopListener(DefaultMessageListenerContainer container) {
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
			createListenerTemplate();
			// 加载模板
			loadXMLTemplate(TEMPLATEPATH);
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
	public synchronized void updateListener() {
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

	private void clearListeners(List<String> ipAddresss) {
		for (String it : ipAddresss) {
			clearListener(it);
		}
	}

	private void clearListener(String ipAddress) {
		if (StringUtils.isEmpty(ipAddress)) {
			return;
		}
		if (!CollectionUtils.isEmpty(listeners) && listeners.get(ipAddress) != null) {
			DefaultMessageListenerContainerAdapter listener = listeners.remove(ipAddress);
			this.stopListener(listener);
		}
	}

	private void createListenerTemplate() {
		// TODO
		List<Map<String, Object>> printers = tbPrinterManagerDao.find(null);
		if (printers != null && !printers.isEmpty()) {
			for (Map<String, Object> it : printers) {
				listenerTemplate.put((String) it.get("printerid"), it);
			}
		}
	}

	private void loadXMLTemplate(String templatepath) {
		try {
			xmlTemplateLoader.load(templatepath);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		try {
			lock.writeLock().lock();

			this.listenerXMLTemplate.clear();

			Map<String, XMLTemplateDefinition> buffer = null;
			try {
				buffer = xmlTemplateDefinitionReader.doLoadBeanDefinitions(xmlTemplateLoader, new XmlReaderContext(xmlTemplateDefinitionReader, new SimpleNamespaceHandlerResover()));
			} catch (Exception e) {
				log.error("打印模板解析失败！尝试重新编辑", e);
				e.printStackTrace();
			}
			if (!MapUtils.isEmpty(buffer)) {
				listenerXMLTemplate.putAll(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("加载模板失败", e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public XMLTemplateDefinition getTemplateDefinition(String id) {
		try {
			lock.readLock().tryLock(3, TimeUnit.SECONDS);
			return listenerXMLTemplate.get(id);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error(id + "获取锁失败", e);
			return null;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void updateListenerTemplate() {
		try {
			lock.writeLock().lock();

			listenerTemplate.clear();
			createListenerTemplate();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新监听失败", e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 根据监听类型找到对应的模板
	 * 
	 * @param listenerType
	 * @return
	 */
	public ListenerTemplate getListenerTemplate(String printerid, ListenerType listenerType) {
		try {
			lock.readLock().lock();
			if (printerid == null || printerid.isEmpty()) {
				return null;
			}

			if (isXmlTemplate(listenerType)) {
				return getTemplateDefinition(listenerType.toString());
			}
			if (!this.listenerTemplate.containsKey(printerid)) {
				log.error("------------  找不到打印模板！" + printerid);
				return null;
			}
			// 模板大小
			Map printer = listenerTemplate.get(printerid);
			String printerNo = (String) printer.get("printerNo");
			// 默认使用默认字体 1标准 2小 3大
			Integer type = Integer.valueOf(printerNo == null || "".equals(printerNo)
					? PrinterListenerManager.STANDARD.toString() : printerNo.trim());
			return ListenerTemplateFactory.getTemplate(listenerType, type);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("------------ 获取模板失败", e);
			return null;
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean isXmlTemplate(ListenerType listenerType) {
		if (listenerType == null) {
			return false;
		}

		switch (listenerType) {
		// 结账单
		case SettlementDishListener:
			return true;
		default:
			return false;
		}
	}

	public QueueListener getXmlQueueListener(ListenerType type) {
		return (QueueListener) applicationContext.getBean(XMLTEMPLATELISTENER);
	}
}
