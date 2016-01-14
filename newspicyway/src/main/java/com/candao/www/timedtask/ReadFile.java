package com.candao.www.timedtask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.candao.common.utils.JacksonJsonMapper;
public class ReadFile {  
  /**
   * 读取文件
   * @return
   */
    public static String readTextFile(String filepath
    		){  
    	String content="";
        try{  
            File file = new File(filepath); 
           String line = null;  
            InputStreamReader read=new InputStreamReader(new FileInputStream(file),"UTF-8");   
            BufferedReader reader=new BufferedReader(read);  
            while( (line=reader.readLine())!=null){ 
            	content+=line;
            }   
            reader.close();  
            read.close();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        return content.trim();
    }  
    /**
     * 写文件
     */
    public static void writeTextFile(String jsonString,String filedirpath,String filename){
    	 try {   
    		 File dirs = new File(filedirpath);   
    		 if(!dirs.exists()){    
    			 dirs.mkdirs();
    		 }
    		 File f = new File(filedirpath+File.separator+filename);   
    		 if(!f.exists()){    
    			 f.createNewFile();
    		 }
    		 BufferedWriter output = new BufferedWriter(new FileWriter(f));   
    		 output.write(jsonString);   
    		 output.close();  
    		 } catch (Exception e) {   
    			 e.printStackTrace();  
    			 } 
    	 }
    /**
     * 解析json数据
     * @param args
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @SuppressWarnings( { "unchecked", "rawtypes" })
    public static String jxjson(String filepath) {
    	String content= readTextFile(filepath);  
    	System.out.println("整个文件的json数据："+content);
//    	content=content.replace("undefined", null);
 	   Map<String, Object> objMap = JacksonJsonMapper.jsonToObject(content, Map.class);
 	   ArrayList<Object> arra= (ArrayList<Object>) objMap.get("rows");
 	   ArrayList<Object> arra3= new ArrayList<Object>();
 	   if(arra!=null&&arra.size()>0){
 		   for(Object obj:arra){
 			   System.out.println("一条板块的json："+JacksonJsonMapper.objectToJson(obj));
 			   Map<String, Object> columMap = JacksonJsonMapper.jsonToObject(JacksonJsonMapper.objectToJson(obj), Map.class);
 			   String datajsonstr=columMap.get("data").toString();
 			  System.out.println("板块内容的json："+datajsonstr);
 			  Map<String, Object> dataMap = JacksonJsonMapper.jsonToObject(datajsonstr, Map.class);
 			 for (Map.Entry<String, Object> entry : dataMap.entrySet()) {    
 				 System.out.println("板块内部各个部分的json："+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
 				LinkedHashMap<String,Object> arra2= (LinkedHashMap<String,Object>) dataMap.get(entry.getKey());
 				 JSONArray jsonarray=new JSONArray();
 				 JSONObject object=new JSONObject();
 				  for (Iterator it =  arra2.keySet().iterator();it.hasNext();)
 				   {
 					  Object key = it.next();
// 					  System.out.println( key+"="+ arra2.get(key));
 					 object.put(key, arra2.get(key));
 				   }
 				  
 				 dataMap.put(entry.getKey(), jsonarray);
 			  }
 			 System.out.println("<<<<<<<<<<<"+JacksonJsonMapper.objectToJson(dataMap));
 			columMap.put("data", JacksonJsonMapper.objectToJson(dataMap));
 			System.out.println(">>>>>>>>>"+columMap.get("data"));
 			obj=JacksonJsonMapper.jsonToObject(JacksonJsonMapper.objectToJson(columMap), Object.class);
 			System.out.println("@@@@@@@@@@@@@"+obj);
 			arra3.add(obj);
 		   }
 	   }
 	  objMap.put("rows", arra3);
 	 System.out.println(objMap.get("rows"));
 	  System.out.println("整个文件的json数据："+JacksonJsonMapper.objectToJson(objMap));
 	  return JacksonJsonMapper.objectToJson(objMap);
    }
    /**
     * 没有使用jsonarray的拼接方式
     * @param content
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String jxjson2(String filepath) {
    	String content= readTextFile(filepath);  
    	System.out.println("整个文件的json数据："+content);
	Map<String, Object> objMap = JacksonJsonMapper.jsonToObject(content, Map.class);
 	   ArrayList<Object> arra= (ArrayList<Object>) objMap.get("rows");
 	   ArrayList<Object> arra3= new ArrayList<Object>();
 	   if(arra!=null&&arra.size()>0){
 		   for(Object obj:arra){
 			   //System.out.println("一条板块的json："+JacksonJsonMapper.objectToJson(obj));
 			   Map<String, Object> columMap = JacksonJsonMapper.jsonToObject(JacksonJsonMapper.objectToJson(obj), Map.class);
 			   String datajsonstr=columMap.get("data").toString();
 			  datajsonstr="{B1:{title:\"333311\",source:\"\",id:\"91647fad-4815-4752-aebd-782989477e03\",image:\"upload/1403158532449.jpg\",introduction:\"333322\"},B2:{title:\"333311\",source:\"\",id:\"573be853-23e3-4fbd-bcf9-16227177760e\",image:\"upload/1403158550856.jpg\",introduction:\"333322\"},B3:{title:\"11\",source:\"\",id:\"dc984b62-edd5-4a23-8eb7-aec3c2dda0f2\",image:\"upload/1403159023428.jpg\",introduction:\"11\"},B4:{title:\"ajaxFileUpload\",source:\"\",id:\"51aaa717-a840-4315-b9d6-b73fd14c6b5f\",image:\"upload/1403159033762.gif\",introduction:\"ajaxFileUpload\"}}";
 			  System.out.println("板块内容的json："+datajsonstr);
 			  Map<String, Object> dataMap = JacksonJsonMapper.jsonToObject(datajsonstr, Map.class);
 			 for (Map.Entry<String, Object> entry : dataMap.entrySet()) {    
 				 //System.out.println("板块内部各个部分的json："+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
 				LinkedHashMap<String,Object> arra2= (LinkedHashMap<String,Object>) dataMap.get(entry.getKey());
 				 Map<Object, Object> dataMap2=new HashMap<Object, Object>();
 				  for (Iterator it =  arra2.keySet().iterator();it.hasNext();)
 				   {
 					  Object key = it.next();
 					  System.out.println( key+"="+ arra2.get(key));
// 					  dataMap2.put(key, arra2.get(key));
 				   }
 				  //System.out.println(JacksonJsonMapper.objectToJson(dataMap2));
// 				 dataMap.put(entry.getKey(), JacksonJsonMapper.objectToJson(dataMap2));
 			  }
// 			 //System.out.println("<<<<<<<<<<<"+JacksonJsonMapper.objectToJson(dataMap));
// 			columMap.put("data", JacksonJsonMapper.objectToJson(dataMap));
// 			//System.out.println(">>>>>>>>>"+columMap.get("data"));
// 			obj=JacksonJsonMapper.jsonToObject(JacksonJsonMapper.objectToJson(columMap), Object.class);
// 			//System.out.println("@@@@@@@@@@@@@"+obj);
// 			arra3.add(obj);
 		   }
 	   }
// 	  objMap.put("rows", arra3);
// 	 //System.out.println(objMap.get("rows"));
// 	  System.out.println("整个文件的json数据："+JacksonJsonMapper.objectToJson(objMap));
// 	  return JacksonJsonMapper.objectToJson(objMap);
 	   return null;
    }
    /**
     * 解析json数据  单个模板数据
     * @param args
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @SuppressWarnings( { "unchecked", "rawtypes" })
    public static String jxjson3(String filepath) {
    	String content= readTextFile(filepath); 
    	System.out.println("整个文件的json数据："+content);
 	   Map<String, Object> objMap = JacksonJsonMapper.jsonToObject(content, Map.class);
		  String datajsonstr=objMap.get("data").toString();
//		  System.out.println("板块内容的json："+datajsonstr);
		  Map<String, Object> dataMap = JacksonJsonMapper.jsonToObject(datajsonstr, Map.class);
		 for (Map.Entry<String, Object> entry : dataMap.entrySet()) {    
//			 System.out.println("板块内部各个部分的json："+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
			LinkedHashMap<String,Object> arra2= (LinkedHashMap<String,Object>) dataMap.get(entry.getKey());
			 Map<Object, Object> dataMap2=new HashMap<Object, Object>();
			  for (Iterator it =  arra2.keySet().iterator();it.hasNext();)
			   {
				  Object key = it.next();
// 					  System.out.println( key+"="+ arra2.get(key));
				  dataMap2.put(key, arra2.get(key));
			   }
//			  System.out.println(JacksonJsonMapper.objectToJson(dataMap2));
			 dataMap.put(entry.getKey(), JacksonJsonMapper.objectToJson(dataMap2));
		  }
		 objMap.put("data", JacksonJsonMapper.objectToJson(dataMap));
		 System.out.println(JacksonJsonMapper.objectToJson(objMap));
 	  return JacksonJsonMapper.objectToJson(objMap);
    }
    /**
     * 封装json数据
     * @param args
     */
    public static void fzjson(){
    	
    }
    public static void main(String[] args) { 
//    	String s = "这是添加的内容，json数据生成txt文件"; 
    	jxjson3("E:\\zip\\20140624165112.txt");
//    	writeTextFile(s);
    }  
}  
