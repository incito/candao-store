package com.candao.common.enums;

  /**
 * @Date 2015年10月12日下午4:09:14
 * @Author yucx
 * @Copyright 
 * @Description 错误码信息
 */
public enum ErrorMessage {
	
	/**
	 * 1开头的表示,业务错误,2开头的表示非业务异常
	 */
	UNCOMPRESS("10001","解压失败,数据出现错误,请确认数据是否正确"),
	DATA_UNCOMPLETE("10002","获取的数据不完整,请重新尝试"),
	SYNDATA_FAIL("10003","同步数据失败,请重新操作"),
	NO_BRANCH_ID("10004","门店未配置正确的branch_id,请联系管理员"),
	NO_CLOSE_SHOP("10005","已经开业或者未就业不能同步数据"),
	NO_EXIST_SYN_GZIP("10006","门店同步的压缩包不存在"),
	
	
	GINZIP_READ("20006","解压数据读取失败,请重新尝试"),
	IO_CLOSE("20007","数据流管道关闭失败,请重新尝试"),
	GINZIP_WRITE("20008","压缩数据失败,请重新尝试"),
	HTTP_TRANS_ERROR("20009","数据传输失败,请重新上传"),
	HTTP_RESPONSE_ERROR("20010","对方服务器响应超时"),
	FORWARD_ERROR("20011","数据转换错误"),
	SQLEXE_ERROR("20012","sql执行错误"),
	SQLCONNECTION_ERROR("20013","数据库连接错误"),
	DATASERVER_OPERATE_ERROR("20014","数据库操作出现错误"),
	ENCODE_ERROR("20015","编码格式转换错误")
	;
	
	
	private String code;
	private String msg;
	
	private ErrorMessage(String code,String msg){
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
