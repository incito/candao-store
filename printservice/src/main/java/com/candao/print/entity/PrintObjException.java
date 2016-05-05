package com.candao.print.entity;

import java.io.Serializable;

/**
 * 异常队列消息体的封装类
 * @author zhangjijun
 *
 */
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
