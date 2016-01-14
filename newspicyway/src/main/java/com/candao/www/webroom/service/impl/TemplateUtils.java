//package com.candao.www.webroom.service.impl;
//
//import java.io.StringWriter;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//import org.apache.velocity.Template;
//import org.apache.velocity.VelocityContext;
//import org.apache.velocity.app.VelocityEngine;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import com.candao.print.entity.CommonObject;
//import com.candao.print.entity.PrinterConstant;
//
//@Service
//public class TemplateUtils {
//
//	@Autowired
//	@Qualifier("templateconfig")
//	 private Properties properties;
//	
//	public String getTemplateMergeString(CommonObject  object){
//	
//		try {
//			 VelocityEngine engine = new VelocityEngine();
//	         engine.init(properties);
//			 
//		    Template template = engine.getTemplate(PrinterConstant.getTemplateName(object.getType()),PrinterConstant.ENCODESTR);
//			 VelocityContext context = new VelocityContext();
//			 getMapValue(context, object);
//		 
//			StringWriter sw = new StringWriter();
//			template.merge(context, sw);
//
//			sw.flush();
//			return sw.toString();
//		} catch (Exception e) {
//		    e.printStackTrace();	 
//	     }
//		
//		return "";
//	}
//	
//
//	
//	@SuppressWarnings("rawtypes")
//	public static  VelocityContext getMapValue(VelocityContext context,CommonObject object ){
//		
//		if(object.getMap() != null){
//			Map  map = object.getMap();
//			@SuppressWarnings("unchecked")
//			Iterator<String> iterable = map.keySet().iterator();
//		    for(;iterable.hasNext();){
//		    	String key = iterable.next();
//		    	Object objects = map.get(key);
//		    	if(objects   instanceof List) {
//		    		context.put(PrinterConstant.LISTLABEL, objects);
//				}else{
//					context.put(key, objects);
//				}
//		    }
//		}
//		 return context;
//	}
//}
