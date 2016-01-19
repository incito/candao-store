/**
 * 
 */
package com.candao.common.utils;

/**
 * <pre>
 * 主要用于统一封装Ajax请求的数据。
 * 
 * 权限拦截可以与这里对应，前台页面可以先判断是否成功，在读取里面得数据。
 * 
 * </pre>
 * @author YHL
 *
 */
public class AjaxResponse {
	
	/**
	 * 此次请求操作是否成功
	 */
	private boolean isSuccess=false;
	
	/**
	 * 请求失败存放的错误信息
	 */
	private String errorMsg="";
	
	/**
	 * 存放要返回的数据
	 */
	private Object data=null;

	public boolean isIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
	
}
