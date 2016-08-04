package com.candao.www.printer.listener;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Component
public class XmlTemplateLoader {

	public final static String ENCODE = "UTF-8";

	private List<InputStream> inputStreams;

	private List<Document> documents;

	public List<Document> getDocuments() {
		return documents;
	}

	private ClassLoader classLoader;

	/**
	 * 读模板
	 * 
	 * @param templatepath
	 * @throws Exception
	 */
	private void doLoadXML(String templatepath) throws Exception {
		ClassLoader loader = getClassLoader();
		// loader.
		Enumeration<URL> dirs = loader.getResources(templatepath);
		while (dirs.hasMoreElements()) {
			URL url = (URL) dirs.nextElement();
			String protocol = url.getProtocol();
			if ("file".equals(protocol)) {
				String filePath = URLDecoder.decode(url.getFile(), ENCODE);
				if (inputStreams == null) {
					inputStreams = new ArrayList<>();
				}
				File file = new File(filePath);
				if (!file.exists()) {
					return;
				}
				if (file.isDirectory()) {
					for (String it : file.list()) {
						doLoadXML(templatepath + "//" + it);
					}
				} else {
					inputStreams.add(loader.getResourceAsStream(templatepath));
				}
			}
		}
	}

	public void load(String templatepath) throws Exception {
		if (StringUtils.isEmpty(templatepath)) {
			return;
		}
		doLoadXML(templatepath);
		loadDocument();
		streamClose();
	}

	public void streamClose() throws Exception {
		if (!CollectionUtils.isEmpty(inputStreams)) {
			for (InputStream ip : inputStreams) {
				ip.close();
			}
		}
	}

	private ClassLoader getClassLoader() {
		if (classLoader != null) {
			return classLoader;
		}
		try {
			classLoader = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException e) {
			classLoader = XmlTemplateLoader.class.getClassLoader();
		}
		return classLoader;
	}

	public List<InputStream> getSource() {
		return this.inputStreams;
	}

	public void clear() {
		this.inputStreams.clear();
	}

	public void loadDocument() throws Exception {
		// TODO 校验格式
		DocumentBuilderFactory factory = createDocumentBuilderFactory(XmlValidationModeDetector.VALIDATION_NONE, true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		if (!CollectionUtils.isEmpty(inputStreams)) {
			documents = documents == null ? new ArrayList<Document>() : documents;
			documents.clear();
			for (InputStream ip : inputStreams) {
				InputSource inputSource = new InputSource(ip);
				Document document = builder.parse(inputSource);
				documents.add(document);
			}
		}
	}

	private DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(namespaceAware);
		if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
			factory.setValidating(true);
		}
		if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
			factory.setNamespaceAware(true);
		}
		return factory;
	}

	public static void main(String[] args) throws Exception {
		XmlTemplateLoader loader = new XmlTemplateLoader();
		loader.load("template/printerTemplate");
		List<Document> documents = loader.getDocuments();
		for (Document it : documents) {
			NodeList node = it.getElementsByTagName("row");
			System.out.println(node.item(0).getNodeName());
		}
	}

}
