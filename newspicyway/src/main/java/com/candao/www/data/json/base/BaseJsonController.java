package com.candao.www.data.json.base;

import java.util.Map;

/**
 * 基础控制类
 * @author snail
 *
 */
public class BaseJsonController {

	private static final String  SUCCESSCODE="0";
	
	private static final String  ERRORCODE="1";
	
	private static final String  SUCCESSMESSAGE="操作成功";
	
	protected static final String JDE_CODE_YES="1";//是jde
	protected static final String JDE_CODE_NO="0";
	
	protected BaseJosnVo  getSuccessInstance(Map<String, Object> result){
		BaseJosnVo baseJosnVo=new BaseJosnVo(SUCCESSCODE, result, SUCCESSMESSAGE);
		return baseJosnVo;
	}
	
	protected BaseJosnVo  getFailInstance(Map<String, Object> result,String errormessage){
		BaseJosnVo baseJosnVo=new BaseJosnVo(ERRORCODE, result, errormessage);
		return baseJosnVo;
	}
	
	protected String getStringFromMap(Map<String, Object> result,String key){
		if(result==null){return null;}
		Object  obj=result.get(key);
		if(obj!=null){
			return String.valueOf(obj);
		}
		return null;
	}
	
	/**
	 * null then true
	 * @param str
	 * @return
	 */
	protected boolean isNull(String str){
		if(str==null || "".equals(str)){
			return true;
		}
		return false;
	}
}
