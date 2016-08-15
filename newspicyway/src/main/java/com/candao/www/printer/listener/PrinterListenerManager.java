package com.candao.www.printer.listener;

import com.candao.common.utils.Constant.ListenerType;
import com.candao.print.dao.TbPrinterManagerDao;
import com.candao.print.listener.QueueListener;
import com.candao.print.listener.template.ListenerTemplate;
import com.candao.www.printer.listener.namespaceHandler.SimpleNamespaceHandlerResover;
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
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
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

	private List<String> backupIPPoll;
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

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 200, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(5000));

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
		if (this.callback != null)
			callback.run();
	}

	private void stopConnections() {
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
				// 备用打印机用逗号隔开
				if (ipaddress.contains(",")) {
					String[] ips = StringUtils.delimitedListToStringArray(ipaddress, ",");
					ipaddress = ips[0];
					for (int i = 1; i < ips.length; i++) {
						String backupIp = ips[i] + ":" + it.get("port");
						if (!getBackupIPPoll().contains(backupIp)) {
							getBackupIPPoll().add(ips[i] + ":" + it.get("port"));
						}
					}
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

	private List<String> getBackupIPPoll() {
		if (backupIPPoll == null) {
			synchronized (this) {
				if (backupIPPoll == null)
					backupIPPoll = new LinkedList<>();
			}
		}
		return backupIPPoll;
	}

	/**
	 * 更新mq监听队列打印机连接
	 */
	public synchronized void updateListener() {
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> printers = tbPrinterManagerDao.find(params);
		if (ipPool == null) {
			ipPool = new ArrayList<>();
		}
		ipPool.clear();
		backupIPPoll = getBackupIPPoll();
		// 备用打印机
		List<String> buffer = new ArrayList<>();
		if (printers != null && !printers.isEmpty()) {
			for (Map<String, Object> it : printers) {
				// 备用打印机有连接无队列，主打印机有链接有队列
				String ipaddress = String.valueOf(it.get("ipaddress"));
				// 备用打印机用逗号隔开
				if (ipaddress.contains(",")) {
					String[] ips = StringUtils.tokenizeToStringArray(ipaddress, ",");
					ipaddress = ips[0];
					for (int i = 1; i < ips.length; i++) {
						String backupIp = ips[i] + ":" + it.get("port");
						if (!buffer.contains(backupIp)) {
							buffer.add(backupIp);
						}
					}
				}
				String subIpAddress = ipaddress + ":" + it.get("port");
				if (!ipPool.contains(subIpAddress)) {
					ipPool.add(ipaddress + ":" + it.get("port"));
				}
				if (listeners.get(ipaddress) == null) {
					DefaultMessageListenerContainerAdapter listener = getListener(ipaddress);
					listener.trulyStart();
					listeners.put(ipaddress, listener);
				}
			}
		}
		// 需要删除的队列
		final List<String> temp = new ArrayList<>();
		final List<String> result = new ArrayList<>();
		// 删除主打印机无用的Ip监听
		if (!CollectionUtils.isEmpty(listeners)) {
			for (String ip : listeners.keySet()) {
				boolean flag = true;
				for (String it : ipPool) {
					if (it.contains(ip)) {
						flag = false;
						break;
					}
				}
				if (flag)
					temp.add(ip);
			}
			// 异步有风险
			executor.execute(new Runnable() {
				@Override
				public void run() {
					clearListeners(temp);
				}
			});
		}
		result.addAll(temp);
		// 更新被移除的
		for (String it : backupIPPoll) {
			if (!buffer.contains(it)) {
				result.add(it);
			}
		}
		// 更新备用打印机
		backupIPPoll = buffer;
		executor.execute(new Runnable() {
			@Override
			public void run() {
				createConnections(result);
			}
		});
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
			log.error("--------------------------");
			log.error("xml模板加载失败！" + templatepath, e);
			e.printStackTrace();
		}
		try {
			lock.writeLock().lock();

			this.listenerXMLTemplate.clear();

			Map<String, XMLTemplateDefinition> buffer = null;
			try {
				buffer = xmlTemplateDefinitionReader.doLoadBeanDefinitions(xmlTemplateLoader,
						new XmlReaderContext(xmlTemplateDefinitionReader, new SimpleNamespaceHandlerResover()));
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
	@SuppressWarnings("rawtypes")
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
		// 结账单
		case ClearMachineDataTemplate:
			return true;
		// 结账单
		case ItemSellDetailTemplate:
			return true;
		// 结账单
		case MemberSaleInfoTemplate:
			return true;
		// 结账单
		case BillDetailTemplate:
			return true;
		// 结账单
		case StoreCardToNewPosTemplate:
			return true;
		// 结账单
		case TipListTemplate:
			return true;
		// 结账单
		case PreSettlementTemplate:
			return true;
		// 结账单
		case InvoiceTemplate:
			return true;
		default:
			return false;
		}
	}

	public QueueListener getXmlQueueListener(ListenerType type) {
		return (QueueListener) applicationContext.getBean(XMLTEMPLATELISTENER);
	}
}
