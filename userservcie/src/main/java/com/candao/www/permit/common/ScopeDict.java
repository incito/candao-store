/**
 * 
 */
package com.candao.www.permit.common;

/**
 * 范围字典枚举
 * 对应表 t_b_scope_dict表中得数据.
 * <pre>
 * 使用方法：
 * 		例如：ScopeDict.HEAD_OFFICE.getValue()，可以获取对应数据库的 02 代码
 * </pre>
 * @author YHL
 *
 */
public enum ScopeDict {
	/**
	 * 全体
	 */
	ALL("01"),
	/**
	 * 总店
	 */
	HEAD_OFFICE("02"),
	/**
	 * 所有门店
	 */
	ALL_OF_STORE("03"),
	/**
	 * 指定门店
	 */
	APPOINTED_STORE("04");
	
	private ScopeDict(String value){
		this._value=value;
	}
	
	private String _value="";
	
	public String getValue(){
		return _value;
	}
}
