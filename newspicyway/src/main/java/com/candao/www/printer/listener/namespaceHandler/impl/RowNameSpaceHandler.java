package com.candao.www.printer.listener.namespaceHandler.impl;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.candao.print.entity.Row;

public class RowNameSpaceHandler extends AbstractNameSpaceHandler{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handler(Element element) throws Exception {
		defaultElement(element);
		super.parseRow();
	}

	@Override
	public List<Row> parse(Map<String, Object> obj) throws Exception {
		// TODO Auto-generated method stub
		return super.parseRow(obj);
	}

}
