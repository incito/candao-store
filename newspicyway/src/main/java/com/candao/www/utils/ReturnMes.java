package com.candao.www.utils;

public enum ReturnMes {
     SUCCESS("0000","操作成功"),
     ERROR("0001","使用优惠失败!"),
	SPECIAL_FAIL("1001","当前特价卷没有包含此订单内的菜品!");
	
	
	private String code;
	private String msg;
	
	private ReturnMes(String code,String msg){
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
