package com.candao.www.printer.listener.namespaceHandler.impl;

import java.util.List;
import java.util.Map;

import com.candao.www.printer.listener.XmlTemplateDefinitionReader;
import org.w3c.dom.Element;

import com.candao.print.entity.Row;

public class RowNameSpaceHandler extends AbstractNameSpaceHandler{

	@Override
	public void init() {
	}

	@Override
	public Element handler(Element element, XmlTemplateDefinitionReader reader) throws Exception {
		defaultElement(element);
		super.parseRow();
		return null;
	}

	@Override
	public List<Row> parse(Map<String, Object> obj) throws Exception {
		return super.parseRow(obj);
	}

}
