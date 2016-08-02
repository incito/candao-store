package com.candao.www.printer.listener.namespaceHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

public class SimpleNamespaceHandlerResover implements NamespaceHandlerResolver {

	public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "template/handlers/namespace.handlers";

	protected final Log log = LogFactory.getLog(getClass().getName());

	private String handlerMappingsLocation;

	private ClassLoader classLoader;

	private volatile Map<String, Object> handlerMappings;

	public SimpleNamespaceHandlerResover() {
		this(null, DEFAULT_HANDLER_MAPPINGS_LOCATION);
	}

	public SimpleNamespaceHandlerResover(ClassLoader classLoader, String handlerMappingsLocation) {
		this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
		this.handlerMappingsLocation = handlerMappingsLocation;
	}

	@Override
	public XmlNameSpaceHandler resolve(String namespaceUri, boolean flush) throws Exception {
		{
			Map<String, Object> handlerMappings = getHandlerMappings();
			Object handlerOrClassName = handlerMappings.get(namespaceUri);
			if (handlerOrClassName == null) {
				return null;
			} else if (handlerOrClassName instanceof XmlNameSpaceHandler) {
				return (XmlNameSpaceHandler) handlerOrClassName;
			} else {
				String className = (String) handlerOrClassName;
				try {
					Class<?> handlerClass = ClassUtils.forName(className, this.classLoader);
					if (!XmlNameSpaceHandler.class.isAssignableFrom(handlerClass)) {
						throw new Exception("配置文件" + handlerMappingsLocation + "出错，必须为XmlNameSpaceHandler子类");
					}
					XmlNameSpaceHandler namespaceHandler = (XmlNameSpaceHandler) BeanUtils
							.instantiateClass(handlerClass);
					namespaceHandler.init();
					if (!flush) {
						handlerMappings.put(namespaceUri, namespaceHandler);
					}
					return namespaceHandler;
				} catch (ClassNotFoundException ex) {
					throw new Exception("找不到" + namespaceUri);
				}
			}
		}
	}

	private Map<String, Object> getHandlerMappings() throws Exception {
		if (this.handlerMappings == null) {
			synchronized (this) {
				if (this.handlerMappings == null) {
					try {
						Properties mappings = PropertiesLoaderUtils.loadAllProperties(this.handlerMappingsLocation,
								this.classLoader);
						Map<String, Object> handlerMappings = new ConcurrentHashMap<String, Object>(mappings.size());
						CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
						this.handlerMappings = handlerMappings;
					} catch (IOException ex) {
						throw new Exception("找不到配置文件：" + handlerMappingsLocation);
					}
				}
			}
		}
		return this.handlerMappings;
	}

}
