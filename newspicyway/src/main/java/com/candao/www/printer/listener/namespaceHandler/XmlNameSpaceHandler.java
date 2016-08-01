package com.candao.www.printer.listener.namespaceHandler;

import java.util.List;

import org.w3c.dom.Element;

import com.candao.print.entity.PrintObj;
import com.candao.print.entity.Row;

public interface XmlNameSpaceHandler {
	
	/**
	 * 解析打印模板
	 * @param element
	 * @throws Exception 
	 */
	public void handler(Element element) throws Exception;
	
	/**
	 * 初始化
	 * @throws Exception 
	 */
	public void init() throws Exception;
	
	/**
	 * 解析打印对象，生成打印数据
	 * @param obj
	 * @throws Exception 
	 */
	public List<Row> parse(PrintObj obj) throws Exception;

}
