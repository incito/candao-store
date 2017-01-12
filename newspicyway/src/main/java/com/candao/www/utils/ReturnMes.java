package com.candao.www.utils;

public enum ReturnMes {
    SUCCESS("0000","操作成功"),
    ERROR("0001","使用优惠失败!"),
    CACHE_ERROR("0002","缓存加载失败请退出重试！"),
    SERVICE_ERROR("0003","抱歉，服务器内部错误请重新进去餐台！"),
    NOT_SUFFICIENT_EORROR("0004","哎呀呀，参与当前优惠卷的菜品不满足此卷，不能使用此优惠!"),
	SPECIAL_FAIL("1001","哎呀呀，没有菜品能够满足特价优惠哟！"),
	DISCOUNT_FAIL("2001","当前订单菜品不在此优惠之中！");

	
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
