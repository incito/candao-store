package com.candao.www.printer.listener.namespaceHandler.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.candao.print.entity.PrintObj;
import com.candao.print.entity.Row;
import com.candao.www.printer.listener.namespaceHandler.XmlNameSpaceHandler;
import com.candao.www.printer.listener.namespaceHandler.XmlReaderContext;

public abstract class AbstractNameSpaceHandler implements XmlNameSpaceHandler {

	private String description;

	private String align;

	private String font;

	private String[] locations;

	private String[] datas;

	private boolean lineFeed;

	private Element element;

	private Field[] fields;

	public Row row;

	private ListNamespaceHandler listNamespaceHandler;

	private static final String ROW = "row";

	private final Log log = LogFactory.getLog(getClass());

	public final static String NON_PRIMITIVE_ARRAY = "[Ljava.lang.Integer;";

	/**
	 * 默认解析row元素
	 */
	public void init() throws Exception {
		// TODO lineFeed解析
		if (element == null || element.getNodeType() != Node.ELEMENT_NODE) {
			return;
		}
		row = new Row();
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
					Field field = this.getClass().getField(ROW).getType().getDeclaredField(ele.getNodeName().trim());
					if (field != null) {
						field.setAccessible(true);
						value = resolveType(field,value);
						field.set(this.row, value);
					}
				}
			}
		}
	}
	
	protected Object resolveType(Field field, Object value){
		//TODO
		if (field == null || value == null) {
			return value;
		}
		String name = field.getType().getName();
		if (NON_PRIMITIVE_ARRAY.equals(name)) {
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

	@Override
	public List<Row> parse(PrintObj obj) throws Exception {
		if (obj == null) {
			return null;
		}
		doParsePlaceHolder(obj);
		doParseConstant(obj);
		List<Row> rows = new ArrayList<>();
		rows.add(getRow());
		return rows;
	}

	protected void doParseConstant(PrintObj obj) {
		// TODO
	}

	private Row getRow() {
		if (row == null) {
			row = new Row();
			// row.setAlign(align);
			// row.setDatas(datas);
			// Integer[] buffer = new Integer[locations.length];
			// for (int i = 0; i < locations.length; i++) {
			// buffer[i] = Integer.valueOf(locations[i]);
			// }
			// row.setLocations(buffer);
			// row.setFont(font);
			// row.setLineFeed(lineFeed);
		}
		return row;
	}

	/**
	 * 解析属性中占位符
	 * 
	 * @param obj
	 * @throws Exception
	 */
	protected void doParsePlaceHolder(PrintObj obj) throws Exception {
		// TODO统一解析
		doParseDatas(obj);
	}

	/**
	 * 解析datas, 将占位符替换为obj的属性值
	 * 
	 * @param obj
	 * @throws Exception
	 */
	protected void doParseDatas(PrintObj obj) throws Exception {
		if (row == null || ArrayUtils.isEmpty(row.getDatas())) {
			return;
		}
		String[] data = row.getDatas();
		String[] buffer = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = data[i].trim();
			// 判断是否变量
			if (!data[i].startsWith(XmlReaderContext.PLACEHOLDER_PREFIX)
					&& !data[i].endsWith(XmlReaderContext.PLACEHOLDER_SUBFIX)) {
				buffer[i] = data[i];
				continue;
			}
			// 根据占位符取值
			String[] pros = StringUtils.delimitedListToStringArray(data[i], ".");
			Object temp = obj;
			for (int j = 0; j < pros.length; j++) {
				Field fields;
				fields = temp.getClass().getDeclaredField(pros[j]);
				fields.setAccessible(true);
				temp = fields.get(temp);
			}
			buffer[i] = String.valueOf(temp);
		}
		this.row.setDatas(buffer);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String[] getLocations() {
		return locations;
	}

	public void setLocations(String[] locations) {
		this.locations = locations;
	}

	public String[] getDatas() {
		return datas;
	}

	public void setDatas(String[] datas) {
		this.datas = datas;
	}

	public boolean isLineFeed() {
		return lineFeed;
	}

	public void setLineFeed(boolean lineFeed) {
		this.lineFeed = lineFeed;
	}

}
