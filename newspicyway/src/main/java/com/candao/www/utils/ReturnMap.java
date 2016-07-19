package com.candao.www.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by donglin on 2015/3/11.
 */
public class ReturnMap {

    public static Map<String,Object> getReturnMap(int flag,String code,String msg){
    	Map<String,Object> returninfo = new HashMap<String,Object>();
    	returninfo.put("flag",flag);
		returninfo.put("code", code);
		returninfo.put("desc", msg);
		returninfo.put("data", new ArrayList<Object>());
    	return returninfo;
    }
    
    public static Map<String,Object> getReturnMap(int flag,String code,String msg,List<Map<String,String>> datalist){
    	Map<String,Object> returninfo = new HashMap<String,Object>();
    	returninfo.put("flag",flag);
		returninfo.put("code", code);
		returninfo.put("desc", msg);
		returninfo.put("data", datalist);
    	return returninfo;
    }
    
    public static Map<String, Object> getSuccessMap(String msg, Object object){
    	Map<String,Object> returninfo = new HashMap<String,Object>();
		returninfo.put("code", "0");
		returninfo.put("msg", msg);
		returninfo.put("data", object);
    	return returninfo;
    }
    
    public static Map<String, Object> getFailureMap(String msg, Object object){
    	Map<String,Object> returninfo = new HashMap<String,Object>();
		returninfo.put("code", "1");
		returninfo.put("msg", msg);
		returninfo.put("data", object);
    	return returninfo;
    }
}
