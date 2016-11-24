package com.candao.www.printer.listener.namespaceHandler.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.candao.print.entity.Row;
import com.candao.www.printer.listener.XmlReaderContext;

public class ForeachNameSpaceHandler extends AbstractNameSpaceHandler {

	public List<Row> rows;

	public static final String FOREACH = "foreach:foreach";

	public static final String VALUE = "value";

	public static final String ITEM = "item";

	private String value;

	private String item;
	
	private List<Map<String, Object>> rowDefination;

	@Override
	public void init() throws Exception {
		getRows();
	}

	@Override
	public void handler(Element element) throws Exception {
		if (element == null || element.getNodeType() != Node.ELEMENT_NODE
				|| !FOREACH.equals(element.getTagName().trim())) {
			return;
		}
		this.value = element.getAttribute(VALUE);
		this.item = element.getAttribute(ITEM);

		NodeList list = element.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node instanceof Element) {
				defaultElement((Element) node);
				super.parseRow();
//				getRows().add(row);
				getRowDefination().add(rowMap);
			}
		}
	}

	@Override
	public List<Row> parse(Map<String, Object> obj) throws Exception {
		if (CollectionUtils.isEmpty(getRowDefination())) {
			return null;
		}
		List<Row> rows = new ArrayList<>();
		for (Map<String, Object> row : getRowDefination()) {
			// 更换模板
			super.setRowDefine(row);
			if (value.contains(XmlReaderContext.PROPERTYSEPERATOR)) {
				String valueCopy = new String(value.getBytes());
				valueCopy = StringUtils.delete(StringUtils.delete(valueCopy, XmlReaderContext.PLACEHOLDER_PREFIX),
						XmlReaderContext.PLACEHOLDER_SUBFIX);
				String[] pros = StringUtils.delimitedListToStringArray(valueCopy, XmlReaderContext.PROPERTYSEPERATOR);
				Object temp = obj.get(pros[0]);
				// 取到需要遍历的值
				temp = getDesiredFieldValue(temp, pros, 1);
				if (Collection.class.isAssignableFrom(temp.getClass())) {
					Collection<?> its = (Collection<?>) temp;
					for (Iterator it = its.iterator(); it.hasNext();) {
						Object object = (Object) it.next();
						Map<String, Object> params = new HashMap<>();
						params.put(item, object);
						rows.addAll(super.parseRow(params));
					}
				} else {
					log.error("---------------------------> ForeachName模板格式错误,元素的值必须是一个集合");
					Map<String, Object> params = new HashMap<>();
					params.put(item, temp);
					rows.addAll(super.parseRow(params));
				}
			}
		}
		return rows;
	}

	public List<Row> getRows() {
		if (rows == null) {
			synchronized (this) {
				if (rows == null) {
					rows = new ArrayList<>();
				}
			}
		}
		return rows;
	}
	
	public List<Map<String, Object>> getRowDefination() {
		if (rowDefination == null) {
			synchronized (this) {
				if (rowDefination == null) {
					rowDefination = new ArrayList<>();
				}
			}
		}
		return rowDefination;
	}

}
