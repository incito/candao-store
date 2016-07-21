package com.candao.common.enums;
/**
 * 
 * @Description: 交互信息的枚举类型
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年5月4日下午5:53:37
 * @version 1.0
 */
public enum ResultMessage {
	
	SUCCESS("0000","操作成功"),
	
	INTERNET_EXE("1001","数据传输失败,请检查网络是否正常或者稍后再试"),
	LOST_MESSAGE("1002","数据丢失,请重新尝试"),
	NO_RETURN_MESSAGE("1003","没有数据返回")
	;
	
	
	private String code;
	private String msg;
	
	private ResultMessage(String code,String msg){
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
}
