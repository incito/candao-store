package com.candao.www.printer.listener.namespaceHandler;

public interface NamespaceHandlerResolver {

	XmlNameSpaceHandler resolve(String namespaceUri) throws Exception;
}
