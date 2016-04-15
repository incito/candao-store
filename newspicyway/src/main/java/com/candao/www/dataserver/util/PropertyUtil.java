/**
 * Copyright (c) 2010 CEPRI,Inc.All rights reserved.
 * Created by 2010-7-16 
 */
package com.candao.www.dataserver.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @title :属性文件解析、参数获取工具类
 * @author: zhaiguangtao
 * @date: 2011-8-16
 */
public class PropertyUtil {

	/**
	 * @description : 获取指定属性文件中对应的属性值
	 * @param fileName : 不带扩展名的文件名称：如wsConfFile
	 * @param proName  : 需要查询属性文件中的某个键值
	 * @return
	 */
    public static String getProInfo(String fileName,String proName){
    	try{
    		if(fileName==null || proName==null || "".equals(fileName.trim()) ||"".equals(proName.trim())){
    			return null;
    		}else{
    			ResourceBundle resourceBundle = ResourceBundle.getBundle(fileName, Locale.getDefault());
    			return resourceBundle.getString(proName);
    		}
    	}catch(Exception e){
    		return null;
    	}
    }
	
    /**
     * 获取指定属性文件数据集,用于一次操作需要取多个属性
     * **/
    public static Hashtable<String,String> getProInfoMap(String fileName){
    	Hashtable<String,String> map = null;
		if(fileName==null || "".equals(fileName.trim())){
			return null;
		}else{
			try{
				ResourceBundle resourceBundle = ResourceBundle.getBundle(fileName, Locale.getDefault());
		    	Enumeration<String> enumeration = resourceBundle.getKeys();
		    	String key = null;
		    	map = new Hashtable<String,String>();
		    	while(enumeration.hasMoreElements()){
					key = enumeration.nextElement();
					String value = resourceBundle.getString(key);
					map.put(key, value == null ? "" : value);
				}
			}catch(Exception e){
	    		return null;
	    	}
		}
    	return map;
    }
}
