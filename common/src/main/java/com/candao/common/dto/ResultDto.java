package com.candao.common.dto;

import com.candao.common.enums.ResultMessage;


/**
 * @Description: 数据交互的对象
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年5月4日下午5:50:17
 * @version 1.0
 */
public class ResultDto {
	
	private String code;
	private String message;
	
	public ResultDto(ResultMessage mes){
		this.code = mes.getCode();
		this.message = mes.getMsg();
	}
	
	public ResultDto(){
		super();
	}
	//设置返回的信息
	public void setInfo(ResultMessage mes){
		this.code = mes.getCode();
		this.message = mes.getMsg();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResultDto [code=" + code + ", message=" + message + "]";
	}
	
	

}
