package com.candao.www.printer.listener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;

@Component
public class XmlTemplateDefinitionReader {
	
	private XmlTemplateLoader loader;
	

	public Map<String, Object> doLoadBeanDefinitions(XmlTemplateLoader loader) {
		Map<String, Object> res = new ConcurrentHashMap<>();
		this.loader = loader;
		//
		List<Document> documents = loader.getDocuments();
		if (CollectionUtils.isEmpty(documents)) {
			return res;
		}
		
		
		return null;
	}
	
}
