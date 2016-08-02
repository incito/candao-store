package com.candao.www.printer.listener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.candao.www.printer.listener.namespaceHandler.XmlNameSpaceHandler;
import com.candao.www.printer.listener.namespaceHandler.XmlReaderContext;
import com.candao.www.printer.listener.template.XMLTemplateDefinition;

@Component
public class XmlTemplateDefinitionReader {

	private XmlTemplateLoader xmlLoader;

	private Map<String, XMLTemplateDefinition> definitions;

	private Object definitionMonitor = new Object();

	private XmlReaderContext xmlReaderContext;

	protected final Log log = LogFactory.getLog(getClass());

	public Map<String, XMLTemplateDefinition> doLoadBeanDefinitions(XmlTemplateLoader loader,
			XmlReaderContext readerContext) throws Exception {
		Map<String, XMLTemplateDefinition> res = getDefinition();
		this.xmlLoader = loader;
		this.xmlReaderContext = readerContext;
		//
		List<Document> documents = xmlLoader.getDocuments();
		if (CollectionUtils.isEmpty(documents)) {
			return res;
		}

		return registerBeanDefinitions(documents);
	}

	private Map<String, XMLTemplateDefinition> registerBeanDefinitions(List<Document> documents) throws Exception {
		if (CollectionUtils.isEmpty(documents)) {
			return null;
		}
		for (Document doc : documents) {
			// 先解析根元素
			Element root = doc.getDocumentElement();
			XMLTemplateDefinition define = doRegisterBeanDefinition(root);
			if (define != null) {
				getDefinition().put(define.getId(), define);
			}
		}
		return getDefinition();
	}

	private XMLTemplateDefinition doRegisterBeanDefinition(Element root) throws Exception {
		XMLTemplateDefinition define = new XMLTemplateDefinition();
		if (isDefaultNamespace(root)) {
			parseDefaultElement(root, define);
			NodeList nl = root.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node instanceof Element) {
					Element ele = (Element) node;
					if (isDefaultNamespace(ele)) {
						parseDefaultElement(ele, define);
					} else {
						parseCustomElement(ele, define);
					}
				}
			}
		} else {
			parseCustomElement(root, define);
		}
		return define;
	}

	private void parseRootAttr(Attr attr, XMLTemplateDefinition define) {
		if (attr == null) {
			return;
		}
		String name = attr.getName();
		if (StringUtils.isEmpty(name)) {
			return;
		}
		switch (name) {
		case XmlReaderContext.ROWS_DEFAULT_ATTR:
			define.setId(attr.getValue());
			break;
		case XmlReaderContext.ROWS_DEFAULT_PATH:
			define.setClasspath(attr.getValue());
			break;
		default:
			break;
		}
	}

	public void parseCustomElement(Element ele, XMLTemplateDefinition define) throws Exception {
		String namespaceUri = getNamespaceURI(ele);
		XmlNameSpaceHandler handler = this.xmlReaderContext.getNamespaceHandlerResolver().resolve(namespaceUri, true);
		if (handler == null) {
			log.error("找不到模板解析类！" + namespaceUri);
			return;
		}
		handler.handler(ele);
		define.registry(handler);
	}

	public void parseDefaultElement(Element element, XMLTemplateDefinition define) {
		if (element == null) {
			return;
		}
		String id = element.getAttribute(XmlReaderContext.ROWS_DEFAULT_ATTR);
		if (id != null) {
			define.setId(id);
		}
		String classpath = element.getAttribute(XmlReaderContext.ROWS_DEFAULT_PATH);
		if (classpath != null) {
			define.setClasspath(classpath);
		}
	}

	public boolean isDefaultNamespace(String namespaceUri) {
		return (!StringUtils.hasLength(namespaceUri) || XmlReaderContext.ROWS_NAMESPACE_URI.equals(namespaceUri));
	}

	public boolean isDefaultNamespace(Node node) {
		return isDefaultNamespace(getNamespaceURI(node));
	}

	public String getNamespaceURI(Node node) {
		return node.getNamespaceURI();
	}

	private Map<String, XMLTemplateDefinition> getDefinition() {
		if (this.definitions == null) {
			synchronized (definitionMonitor) {
				if (this.definitions == null) {
					definitions = new ConcurrentHashMap<>();
				}
			}
		}
		return definitions;
	}

}
