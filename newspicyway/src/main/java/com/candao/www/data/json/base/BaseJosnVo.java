package com.candao.www.data.json.base;

import java.util.HashMap;
import java.util.Map;

public class BaseJosnVo {
	//请求状态(0成功，1失败)
	private String error_code;
	//结果
	private Map<String, Object>  result=new HashMap<String, Object>();
	//提示信息
	private String message;
	
	
	public BaseJosnVo() {
		super();
	}
	
	public BaseJosnVo(String error_code, Map<String, Object> result, String message) {
		super();
		this.error_code = error_code;
		this.result = result;
		this.message = message;
	}
	
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public Map<String, Object> getResult() {
		return result;
	}
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
