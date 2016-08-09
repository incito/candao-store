package com.candao.www.printer.listener.namespaceHandler.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.candao.print.entity.Row;
import com.candao.www.printer.listener.XmlReaderContext;
import com.candao.www.printer.listener.namespaceHandler.XmlNameSpaceHandler;
import com.candao.www.utils.CloneUtil;

public abstract class AbstractNameSpaceHandler implements XmlNameSpaceHandler {

	private Element element;

	private Field[] fields;

	public Row row;

	public Row data;

	public Map<String, Object> rowMap;

	private ListNamespaceHandler listNamespaceHandler;

	public static final String ROW = "row";

	public static final String DATA = "data";

	public static final String ROW_TAG_NAME = "row:row";

	private final Log log = LogFactory.getLog(getClass());

	private final static int CLONE_LEVER = -1;

	/**
	 * 默认解析row元素
	 */
	public void parseRow() throws Exception {
		// TODO lineFeed解析
		if (element == null || element.getNodeType() != Node.ELEMENT_NODE) {
			return;
		}
		// row = new Row();
		rowMap = new HashMap<>();
		listNamespaceHandler = new ListNamespaceHandler();
		NodeList epmAttr = element.getChildNodes();
		// 得到element 的子节点
		for (int i = 0; i < epmAttr.getLength(); i++) {
			Node ele = epmAttr.item(i);
			// 得到第i个子节点
			if (ele.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childList = ele.getChildNodes();
				for (int j = 0; j < childList.getLength(); j++) {
					Node info = childList.item(j);
					Object value;
					if (info.getNodeType() == Node.TEXT_NODE) {
						Text text = (Text) info;
						String message = text.getWholeText();
						if (StringUtils.isEmpty(message) || message.contains("\t")) {
							continue;
						}
						value = message;
					} else if (info.getNodeType() == Node.ELEMENT_NODE) {
						listNamespaceHandler.init();
						listNamespaceHandler.handler((Element) info);
						value = listNamespaceHandler.getValue();
					} else {
						continue;
					}

					this.rowMap.put(ele.getNodeName().trim(), value);

					// Field field =
					// this.getClass().getField(ROW).getType().getDeclaredField(ele.getNodeName().trim());
					// if (field != null) {
					// field.setAccessible(true);
					// value = resolveType(field, value);
					// field.set(this.row, value);
					// }
				}
			}
		}
	}

	public void setRowDefine(Map<String, Object> row) {
		this.rowMap = row;
	}

	protected Object resolveType(Field field, Object value) {
		// TODO
		if (field == null || value == null) {
			return value;
		}
		String name = field.getType().getName();
		if (XmlReaderContext.NON_PRIMITIVE_INTEGER_ARRAY.equals(name)) {
			if (value.getClass().isArray()) {
				List<Integer> temp = new ArrayList<>();
				String[] val = (String[]) value;
				for (int i = 0; i < val.length; i++) {
					if (val[i] != null) {
						temp.add(Integer.valueOf(val[i]));
					}
				}
				return temp.toArray(new Integer[temp.size()]);
			}
		}
		return value;
	}

	public List<Row> parseRow(Map<String, Object> obj) throws Exception {
		if (MapUtils.isEmpty(obj)) {
			return null;
		}
		doParsePlaceHolder(obj);
		doParseConstant(obj);
		List<Row> rows = new ArrayList<>();
		rows.add(getData());
		return rows;
	}

	protected void doParseConstant(Object obj) {
		// TODO
	}

	private Row getRowDefine() {
		if (row == null) {
			row = new Row();
		}
		return row;
	}

	private Row getData() throws Exception {
		if (data == null) {
			data = (Row) CloneUtil.clone(getRowDefine(), CLONE_LEVER);
		}
		return data;
	}

	/**
	 * 解析属性中占位符
	 * 
	 * @param obj
	 * @throws Exception
	 */
	protected void doParsePlaceHolder(Map<String, Object> obj) throws Exception {
		// TODO统一解析
		doParseDatas(obj);
	}

//	/**
//	 * 解析datas, 将占位符替换为obj的属性值
//	 * 
//	 * @param obj
//	 * @throws Exception
//	 */
//	protected void doParseDatas(Map<String, Object> obj) throws Exception {
//		if (row == null || ArrayUtils.isEmpty(row.getDatas())) {
//			return;
//		}
//		data = (Row) CloneUtil.clone(getRowDefine(), CLONE_LEVER);
//		String[] data = this.data.getDatas();
//		String[] buffer = new String[0];
//		int newLength = 0;
//		for (int i = 0; i < data.length; i++) {
//			if (!StringUtils.isEmpty(data[i])) {
//				data[i] = data[i].trim();
//				buffer = Arrays.copyOf(buffer, ++newLength);
//				// 判断是否变量
//				if (!data[i].startsWith(XmlReaderContext.PLACEHOLDER_PREFIX)
//						&& !data[i].endsWith(XmlReaderContext.PLACEHOLDER_SUBFIX)) {
//					buffer[newLength - 1] = data[i];
//					continue;
//				}
//				data[i] = StringUtils.delete(StringUtils.delete(data[i], XmlReaderContext.PLACEHOLDER_PREFIX),
//						XmlReaderContext.PLACEHOLDER_SUBFIX);
//				// 根据占位符取值
//				String[] pros = StringUtils.delimitedListToStringArray(data[i], XmlReaderContext.PROPERTYSEPERATOR);
//				if (ArrayUtils.isEmpty(pros)) {
//					return;
//				}
//				Object temp = obj.get(pros[0]);
//				temp = getDesiredFieldValue(temp, pros, 1);
//				buffer[newLength - 1] = String.valueOf(temp);
//			}
//		}
//		this.data.setDatas(buffer);
//	}

	protected void setValue(String filedname, Object value) throws Exception {
		if (data == null) {
			data = new Row();
		}

		Field field = this.getClass().getField(DATA).getType().getDeclaredField(filedname);
		if (field != null) {
			field.setAccessible(true);
			value = resolveType(field, value);
			// TODO
			if (value == null) {
				value = "";
			}
			field.set(this.data, value);
		}
	}

	@SuppressWarnings("unchecked")
	protected void doParseDatas(Map<String, Object> obj) throws Exception {
		if (CollectionUtils.isEmpty(rowMap)) {
			return;
		}
		data = new Row();
		Map<String, Object> map = (Map<String, Object>) CloneUtil.clone(rowMap, CLONE_LEVER);
		for (Entry<String, Object> it : map.entrySet()) {
			String name = it.getKey();
			Object tempValue = it.getValue();
			if (tempValue != null) {
				// TODO
				if (XmlReaderContext.NON_PRIMITIVE_STRING_ARRAY.equals(tempValue.getClass().getName())) {
					String[] value = (String[]) tempValue;
					String[] buffer = new String[0];
					int newLength = 0;
					for (String item : value) {
						if (item != null) {
							buffer = Arrays.copyOf(buffer, ++newLength);
							Object res = getDesiredFiledValue(item, obj);
							res = res == null ? "" : res;
							buffer[newLength - 1] = res.toString();
						}
					}
					setValue(name, buffer);
				} else if (tempValue instanceof String) {
					String value = (String) tempValue;
					Object fieldValue = getDesiredFiledValue(value, obj);
					setValue(name, fieldValue);
				} else {
					//TODO
				}
			}
		}
	}

	private Object getDesiredFiledValue(String filedName, Map<String, Object> obj) throws Exception {
		if (!StringUtils.isEmpty(filedName)) {
			filedName = filedName.trim();
			// 判断是否变量
			if (!filedName.startsWith(XmlReaderContext.PLACEHOLDER_PREFIX)
					&& !filedName.endsWith(XmlReaderContext.PLACEHOLDER_SUBFIX)) {
				return filedName;
			}
			filedName = StringUtils.delete(StringUtils.delete(filedName, XmlReaderContext.PLACEHOLDER_PREFIX),
					XmlReaderContext.PLACEHOLDER_SUBFIX);
			// 根据占位符取值
			String[] pros = StringUtils.delimitedListToStringArray(filedName, XmlReaderContext.PROPERTYSEPERATOR);
			if (ArrayUtils.isEmpty(pros)) {
				return null;
			}
			// 在模板中配置的要取值的对象
			Object temp = obj.get(pros[0]);
			temp = getDesiredFieldValue(temp, pros, 1);
			return temp;
		}
		return null;
	}

	protected Object getDesiredFieldValue(Object obj, String[] pros, int fromindex) throws Exception {
		if (obj == null || ArrayUtils.isEmpty(pros)) {
			return null;
		}
		Object temp = obj;
		for (int j = fromindex; j < pros.length; j++) {
			if (Map.class.isAssignableFrom(temp.getClass())) {
				temp = ((Map) temp).get(pros[j]);
			} else if (Collection.class.isAssignableFrom(temp.getClass())) {
				throw new Exception("不能解析集合");
			} else if (temp.getClass().isArray()) {
				throw new Exception("不能解析数组");
			} else {
				Field fields;
				fields = temp.getClass().getDeclaredField(pros[j]);
				fields.setAccessible(true);
				temp = fields.get(temp);
			}
		}
		if (temp == null) {
			temp = "";
		}
		return temp;
	}

	protected Field[] getFields() {
		if (fields == null) {
			synchronized (this) {
				if (fields == null) {
					fields = this.getClass().getDeclaredFields();
					for (Field field : fields) {
						field.setAccessible(true);
					}
				}
			}
		}
		return fields;
	}

	public void defaultElement(Element ele) {
		this.element = ele;
	}
}
