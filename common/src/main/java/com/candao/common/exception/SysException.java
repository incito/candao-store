package com.candao.common.exception;

import com.candao.common.enums.ErrorMessage;
import com.candao.common.enums.Module;



  /**
 * @Date 2015年10月12日下午4:15:44
 * @Author yucx
 * @Copyright 
 * @Description 系统自定义异常
 */
public class SysException extends Exception {
	
	private static final long serialVersionUID = 1L;
	//错误码信息
	private ErrorMessage errorMsg;
	//报错的模块
	private Module module;
	//错误信息(可自定义，默认是ErrorMessage里的值)
	private String message;
	//错误码(同上)
	private String code;
	
	public SysException(ErrorMessage errorMsg,Module module){
		super();
		this.errorMsg = errorMsg;
		this.code = errorMsg.getCode();
		this.message = errorMsg.getMsg();
		this.module = module;
	}
	
	public SysException(ErrorMessage errorMsg,String message,Module module){
		super(message);
		this.errorMsg = errorMsg;
		this.code = errorMsg.getCode();
		this.message = message;
		this.module = module;
	}

	public ErrorMessage getErrorMsg() {
		return errorMsg;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return "SysException [errorMsg=" + errorMsg + ", module=" + module
				+ ", message=" + message + ", code=" + code + "]";
	}

}
