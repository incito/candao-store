package com.candao.www.printer.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;

import com.candao.common.utils.Constant.ListenerType;
import com.candao.print.listener.template.ListenerTemplate;
import com.candao.www.spring.SpringContext;

public class ListenerTemplateFactory {
	
	private static Log log = LogFactory.getLog(ListenerTemplateFactory.class.getName());
	
	private static Map<String, ListenerTemplate> cacheTemplatePool = new ConcurrentHashMap<String, ListenerTemplate>();
	
	public static final String SIMPLESUFFIX = "TemplateImpl";
	
//	private Class<?> tClass;

	public synchronized static ListenerTemplate getTemplate(ListenerType listenerType , Integer type){
		String templateKey = listenerType.toString().trim().concat(SIMPLESUFFIX);
		ListenerTemplate template = null;
		if (!cacheTemplatePool.containsKey(templateKey)){
//			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//			this.tClass = classLoader.loadClass(templateKey);
			try {
				template = (ListenerTemplate) SpringContext.getBean(templateKey);				
			} catch (BeansException e) {
				e.printStackTrace();
				log.error("找不到实现类", e);
				return null;
			}
			cacheTemplatePool.put(templateKey, template);
		}
		template = cacheTemplatePool.get(templateKey);
		template.setType(type);
		return template;
	}

//	private Object directGetContextClass( ) {
//		if (this.tClass != null) {
//			try {
//				return this.tClass.newInstance();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//				log.error("初始化失败", e);
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//				log.error("加载失败", e);
//			}			
//		}
//		return null;
//	}	
}
