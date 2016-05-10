package com.candao.common.enums;

  /**
 * @Date 2015年10月12日下午4:09:14
 * @Author yucx
 * @Copyright 
 * @Description 错误码信息
 */
public enum ErrorMessage {
	
	
	UNCOMPRESS("10001","解压失败,数据出现错误,请确认数据是否正确"),
	DATA_UNCOMPLETE("10002","获取的数据不完整,请重新尝试"),
	SYNDATA_FAIL("10003","同步数据失败,请重新操作"),
	NO_BRANCH_ID("10004","门店未配置正确的branch_id,请联系管理员"),
	NO_EXIST_SYN_GZIP("10005","门店同步的压缩包不存在"),
	GINZIP_READ("10006","解压数据读取失败,请重新尝试"),
	IO_CLOSE("10007","数据流管道关闭失败,请重新尝试"),
	GINZIP_WRITE("10008","压缩数据失败,请重新尝试"),
	HTTP_TRANS_ERROR("10009","数据传输失败,请重新上传"),
	HTTP_RESPONSE_ERROR("10010","对方服务器响应超时")
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
