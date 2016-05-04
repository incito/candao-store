package com.candao.print.entity;

import java.io.Serializable;

public class PrintObjException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3363265272927545678L;

	private PrintObj printObj;

	private String listenerId;

	public PrintObjException(PrintObj printObj, String listenerId) {
		super();
		this.printObj = printObj;
		this.listenerId = listenerId;
	}

	public String getListenerId() {
		return listenerId;
	}

	public PrintObj getPrintObj() {
		return printObj;
	}

}
