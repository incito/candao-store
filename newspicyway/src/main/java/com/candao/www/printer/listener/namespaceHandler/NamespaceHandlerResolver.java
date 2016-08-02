package com.candao.www.printer.listener.namespaceHandler;

public interface NamespaceHandlerResolver {

	XmlNameSpaceHandler resolve(String namespaceUri, boolean flush) throws Exception;
}
