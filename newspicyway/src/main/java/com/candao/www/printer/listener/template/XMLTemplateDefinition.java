package com.candao.www.printer.listener.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.candao.print.entity.PrintObj;
import com.candao.print.entity.Row;
import com.candao.print.listener.template.impl.SimpleNormalDishTemplateImpl;
import com.candao.www.printer.listener.XmlReaderContext;
import com.candao.www.printer.listener.namespaceHandler.XmlNameSpaceHandler;

public class XMLTemplateDefinition extends SimpleNormalDishTemplateImpl implements TemplateDefinition {

	private String id;

	private String classpath;

	private List<XmlNameSpaceHandler> handlers;
	
	private List<Row> rows;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public void registry(XmlNameSpaceHandler handler) {
		if (handler != null) {
			getRegistrys().add(handler);
		}
	}

	public List<XmlNameSpaceHandler> getRegistrys() {
		if (CollectionUtils.isEmpty(handlers)) {
			handlers = new LinkedList<>();
		}
		return handlers;
	}

	@Override
	public List<Row> parse(PrintObj obj) {
		getRow().clear();
		for (XmlNameSpaceHandler it : handlers) {
			try {
				Map<String, Object> params = new HashMap<>();
				params.put(XmlReaderContext.DEFAULTVARIABLESNAME, obj);
				List<Row> rows = it.parse(params);
				getRow().addAll(rows);
			} catch (Exception e) {
				logger.error("解析数据出错", e);
				e.printStackTrace();
				return null;
			}
		}
		return getRow();
	}
	
	public List<Row> getRow(){
		if (rows == null) {
			rows = new ArrayList<>();
		}
		return rows;
	}

	public byte[] getAlign(String id) {
		switch (id) {
		case XmlReaderContext.ALIGN_CENTER:
			return super.setAlignCenter();
		case XmlReaderContext.ALIGN_LEFT:
			return super.setAlignLeft();
		// case XmlReaderContext.ALIGN_RIGHT:
		// return super.setAlignCenter();
		default:
			return super.setAlignLeft();
		}
	}

	public byte[] getFont(String id) {
		switch (id) {
		case XmlReaderContext.FONT_BIG:
			return super.getBIGFONT();
		case XmlReaderContext.FONT_NORMAL:
			return super.getNORMALFONT();
		case XmlReaderContext.FONT_SMALL:
			return super.getSMALLFONT();
		default:
			return super.getNORMALFONT();
		}
	}
}
