package com.candao.www.printer.listener;

import com.candao.www.printer.listener.namespaceHandler.NamespaceHandlerResolver;

public class XmlReaderContext {

	private final XmlTemplateDefinitionReader reader;
	
	private final NamespaceHandlerResolver namespaceHandlerResover;

	public static final String ROWS_NAMESPACE_URI = "http://120.24.190.55/schema/rows";
	
	public static final String ROWS_DEFAULT_ATTR = "description";

	public static final String ROWS_DEFAULT_PATH = "class";
	
	public static final String ALIGN_CENTER = "ALIGN_CENTER";
	
	public static final String ALIGN_LEFT = "ALIGN_LEFT";
	
	public static final String ALIGN_RIGHT = "ALIGN_RIGHT";
	
	public static final String FONT_NORMAL = "NORMAL";
	
	public static final String FONT_BIG = "BIG";
	
	public static final String FONT_SMALL = "SMALL";
	
	public static final String PLACEHOLDER_PREFIX = "#{";
	
	public static final String PLACEHOLDER_SUBFIX = "}";
	
	public static final String DEFAULTVARIABLESNAME = "obj";
	
	public static final String PROPERTYSEPERATOR = ".";
	
	public static final String NON_PRIMITIVE_INTEGER_ARRAY = "[Ljava.lang.Integer;";
	
	public static final String NON_PRIMITIVE_STRING_ARRAY = "[Ljava.lang.String;";

	public static final String PRIMITIVE_BOOLEAN = "boolean";

	public static final String PLACEHOLDER_METHOD_PREFIX = "(";

	public static final String PLACEHOLDER_METHOD_SUBFIX = ")";

	public static final String PLACEHOLDER_PARAM_SEPERATOR = ",";

	public static final String PLACEHOLDER_TRUE = "TRUE";

	public static final String PLACEHOLDER_FALSE = "FALSE";
	
	public XmlReaderContext(XmlTemplateDefinitionReader reader ,NamespaceHandlerResolver resolver ){
		this.reader = reader;
		this.namespaceHandlerResover = resolver;
	}

	public NamespaceHandlerResolver getNamespaceHandlerResolver() {
		return namespaceHandlerResover;
	}

	public XmlTemplateDefinitionReader getReader() {
		return reader;
	}
	
	
}
