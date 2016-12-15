package com.candao.www.utils;

public enum ReturnMes {
     SUCCESS("0000","操作成功"),
     ERROR("0001","使用优惠失败!"),
	SPECIAL_FAIL("1001","当前特价卷没有包含此订单内的菜品!"),
	DISCOUNT_FAIL("2001","当前订单菜品不在此优惠之中！"),
	DISCOUNT_FAIL_AMOUNT("2002","当前订单可优惠金额不足,不能使用此卷！");
	
	private String code;
	private String msg;
	
	
	public static String mes(String code){
		String mes="";
		    ReturnMes[] values = values();
		    for(ReturnMes reMes:values){
		    	if(reMes.getCode().equals(code)){
		    		mes=reMes.getMsg();
		    		break;
		    	}
		    }
		    		
		return mes;
	}
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
