package com.candao.www.utils;

import java.util.HashMap;
import java.util.Map;

public enum RoundingEnum {
	/** 0:不处理 1：4舍5入 2：抹零处理 **/
	// 0：分 1角 2元
	NOTHANDLE("0"), ROUNDTOINTEGER("1"), REMOVETAIL("2");
	private String roundingItme;

	private RoundingEnum(String roundingItme) {
		this.roundingItme = roundingItme;
	}

	public String getRoundingItme() {
		return roundingItme;
	}

	private static final Map<String, RoundingEnum> stringToeEnum = new HashMap<>();
	static {
		for (RoundingEnum roundingEnum : values()) {
			stringToeEnum.put(roundingEnum.toString(), roundingEnum);
		}
	}

	public static RoundingEnum fromString(String symbol) {
		return stringToeEnum.get(symbol);
	}

	@Override
	public String toString() {
		return this.roundingItme;
	}
}
