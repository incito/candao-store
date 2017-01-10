package com.candao.www.printer.listener.namespaceHandler.impl;

import com.candao.print.entity.Row;
import com.candao.www.printer.listener.XmlReaderContext;
import com.candao.www.printer.listener.XmlTemplateDefinitionReader;
import com.candao.www.printer.listener.namespaceHandler.XmlNameSpaceHandler;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-30.
 * 判断标签
 */
public class IfNameSpaceHandler extends AbstractNameSpaceHandler {

    public static final String IF = "if:if";

    private final String Test = "test";

    private String value;

    private List<XmlNameSpaceHandler> handlers;

    private List<Row> rows;

    private final String EMPTY = "Empty";

    private final String EQUAL = "==";

    private final String NOT = "!";

    private final String POSITIONDELIMITER = ".";

    @Override
    public Element handler(Element element, XmlTemplateDefinitionReader reader) throws Exception {
        if (element == null || element.getNodeType() != Node.ELEMENT_NODE
                || !IF.equals(element.getTagName().trim())) {
            return null;
        }
        this.reader = reader;
        this.value = element.getAttribute(Test);
        if (StringUtils.isEmpty(value) || !value.startsWith(XmlReaderContext.PLACEHOLDER_PREFIX) || !value.endsWith(XmlReaderContext.PLACEHOLDER_SUBFIX))
            throw new RuntimeException("test标签格式错误");
        value = value.replace(XmlReaderContext.PLACEHOLDER_PREFIX, "").replace(XmlReaderContext.PLACEHOLDER_SUBFIX, "");
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                XmlNameSpaceHandler handler = reader.getUnassembledHandle((Element) node);
                handler.handler((Element) node, this.reader);
                getHandlers().add(handler);
            }
        }

        return null;
    }

    @Override
    public void init() throws Exception {
        rows = new ArrayList<>();
    }

    @Override
    public List<Row> parse(Map<String, Object> obj) throws Exception {
        rows.clear();

        boolean flag = determineWhetherToShow(value, obj);
        if (flag) {
            for (XmlNameSpaceHandler handler : handlers) {
                List<Row> temp = handler.parse(obj);
                if (temp != null)
                    rows.addAll(temp);
            }
        }
        return rows;
    }

    /**
     * 根据obj解析test值
     * 考虑增加逻辑运算符和结合顺序，使用递归计算
     * 比如 ! Empty 223
     * Empty 1
     * 1 == 1
     * !1 == 1
     *
     * @param obj   解析对象
     * @param value test表达式
     * @return
     */
    private boolean determineWhetherToShow(String value, Map<String, Object> obj) throws Exception {
        if (StringUtils.isEmpty(value))
            return false;
        boolean flag = false;
//        StringUtils.
        Binding binding = new Binding();
//        GroovyShell

//        if (value.contains(NOT)) {
//            flag = parseNotExpression(value, obj);
//        } else if (value.contains(EQUAL)) {
//            flag = parseEqualExpression(value, obj);
//        } else if (value.contains(EMPTY)) {
//            flag = parseEmptyExpression(value, obj);
//        } else {
//            return flag;
//        }
        return flag;
    }

    private boolean parseNotExpression(String value, Map<String, Object> obj) throws Exception {
        value = value.substring(value.indexOf(NOT) + NOT.length()).trim();
        return !determineWhetherToShow(value, obj);
    }

    private boolean parseEqualExpression(String value, Map<String, Object> obj) throws Exception {
        String before = value.substring(0, value.indexOf(EQUAL)).trim();
        String after = value.substring(value.indexOf(EQUAL) + EQUAL.length(), value.length()).trim();
        Object beforeObj = super.getDesiredFieldValue(obj, StringUtils.delimitedListToStringArray(before, POSITIONDELIMITER), 0);
        beforeObj = StringUtils.isEmpty(beforeObj) ? before : beforeObj;
        Object afterObj = super.getDesiredFieldValue(obj, StringUtils.delimitedListToStringArray(after, POSITIONDELIMITER), 0);
        afterObj = StringUtils.isEmpty(afterObj) ? after : afterObj;
        return afterObj == beforeObj || afterObj.toString().equals(beforeObj.toString()) || afterObj.equals(beforeObj);
    }

    private boolean parseEmptyExpression(String value, Map<String, Object> obj) throws Exception {
        value = value.substring(value.indexOf(EMPTY) + EMPTY.length()).trim();
        return StringUtils.isEmpty(super.getDesiredFieldValue(obj, StringUtils.delimitedListToStringArray(value, POSITIONDELIMITER), 0));
    }


    private List<XmlNameSpaceHandler> getHandlers() {
        if (this.handlers == null) {
            synchronized (this) {
                if (this.handlers == null)
                    handlers = new ArrayList<>();
            }
        }
        return handlers;
    }
}
