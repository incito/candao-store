package com.candao.common.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * Properties工具类
 * 
 * @author lzl
 * @version 2012-9-11 下午01:10:22
 */
@Component
public class PropertiesUtils {
	@Autowired
	@Qualifier("appconfig")
	private Properties properties;
	private static PropertiesUtils piu;

	private static String branchId;
	
	/**
	 * 初始化工具类默认属性
	 */
	@PostConstruct
	public void init() {
		piu =  this;
		piu.properties = this.properties;
	}

	public static void setBranchId(String branchID){
		branchId = branchID;
	}
	
	public static String getBranchId(){
		return branchId;
	}
	
	public static String getValue(String key) {
//		add by wly,配置文件去掉current_branch_id，从数据库获取门店ID，此处是为了兼容其他配置
		if(key.equals("current_branch_id")){
			return branchId;
		}
		if(key.equals("isbranch")){	//配置文件去掉isbranch,直接返回Y
			return "Y";
		}
		return piu.properties.getProperty(key, null);
	}
	
	/**
	 * 获取属性列表，根据传入参数的模糊匹配
	 * @param key,需要查找属性前缀
	 * @describe
	 * 		比如属性文件中有
	  			mail.smtp.host=smtp.incito.com.cn
				mail.smtp.port=25
				mail.smtp.auth=true
			通过该方法，便可以使用findValues("mail.smtp")将所有相关属性获取，并放入一个LinkedHashMap中
	 * @return
	 */
	public static Map<String,String> findValues(String key) {
		Map<String,String> propMap = new LinkedHashMap<String,String>();
		Iterator<Object> it = piu.properties.keySet().iterator();
		while(it.hasNext()){
			String k = String.valueOf(it.next());
			if(k.startsWith(key)){
				String v = piu.properties.getProperty(k, null);
				propMap.put(k, v);
			}
		}
		return propMap;
	}
}
