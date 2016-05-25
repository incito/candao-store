package com.candao.common.enums;

/**
 * @Description: 编码格式
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年5月19日上午12:10:32
 * @version 1.0
 */
public enum EnCodeType {
	
	/**
	 * utf-8编码
	 */
	UTF_8("UTF-8"),
	/**
	 * gbk编码
	 */
	GBK("GBK");
	
	private String code;
	
	private EnCodeType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
