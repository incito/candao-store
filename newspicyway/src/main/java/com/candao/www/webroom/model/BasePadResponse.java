package com.candao.www.webroom.model;

import java.io.Serializable;

/**
 * pad可配置服务
 * @author snail
 *
 */
public class BasePadResponse<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;//pad成功失败标识
	private String msg;//pad失败msg
	private T data;//数据对象
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
	
	
	}