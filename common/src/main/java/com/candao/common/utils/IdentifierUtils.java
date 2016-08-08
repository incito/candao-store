package com.candao.common.utils;


public class IdentifierUtils {

	/**
	 * 生成唯一识别码
	 * 
	 * @return
	 */
	public static Generator getId() {
		return new UUIDGenerator();
	}

}
