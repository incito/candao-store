package com.candao.www.printer.listener.namespaceHandler.impl;

import java.util.List;
import java.util.Map;

import com.candao.www.printer.listener.XmlTemplateDefinitionReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.candao.print.entity.Row;
import com.candao.www.printer.listener.namespaceHandler.XmlNameSpaceHandler;

public class ListNamespaceHandler implements XmlNameSpaceHandler {

	private String[] list;

	public static final String TAGNAME = "list";

    @Override
    public Element handler(Element element, XmlTemplateDefinitionReader reader) throws Exception {
        if (element == null || element.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        if (TAGNAME.equals(element.getTagName())) {
            NodeList nodes = element.getChildNodes();
            list = new String[nodes.getLength()];
            int m = 0;
            for (int j = 0; j < nodes.getLength(); j++) {
                if (Node.ELEMENT_NODE == nodes.item(j).getNodeType()) {
                    list[m] = nodes.item(j).getTextContent();
                    m++;
                }
            }
        }
        return null;
    }

	@Override
	public void init() throws Exception {
		list = null;
	}

	@Override
	public List<Row> parse(Map<String, Object> obj) {
		return null;
	}

	public String[] getValue() {
		return list;
	}

}
