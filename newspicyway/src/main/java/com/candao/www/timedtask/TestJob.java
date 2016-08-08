package com.candao.www.timedtask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.candao.common.page.Page;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.constant.Constant;
import com.candao.www.spring.SpringContext;
//具体执行任务的类
public class TestJob implements Job {
//	TemplateService templateService=(TemplateService) SpringContext.getApplicationContext().getBean(TemplateService.class);
//	static String  url=Constant.url;
//	int pagesize=10;
//	public TestJob(){
//	}
//	@Override
//	public void execute(JobExecutionContext context) throws JobExecutionException {
//		System.out.println("触发定时器...."+new Date());
//		Map<String, Object> params=new HashMap<String,Object>();
//		params.put("status", "3");//状态是3表示审核通过
//		params.put("releasetime", new Date()); //发布时间小于现在的时间
//		//分页查询
//		Page<Map<String, Object>> pageMap = templateService.grid(params, 1, pagesize);
//		String jsonString=JacksonJsonMapper.objectToJson(pageMap);
//		System.out.println("原始的："+jsonString);
//		jsonString=jxjson(jsonString);
//		System.out.println("编辑后："+jsonString);
//		String filepath=url+File.separatorChar+DateUtils.toString(new Date(),"yyyyMMdd");
//		filepath.replace('/', File.separatorChar);
//		System.out.println(filepath);
//		delFolder(filepath);
//	    System.out.println("deleted");
//		//文件路径
//		zipfile(jsonString,1,filepath);
//		for(int i=2;i<=pageMap.getPageCount();i++){
//			pageMap=templateService.grid(params, i, pagesize);
//			jsonString=JacksonJsonMapper.objectToJson(pageMap);
//			jsonString=jxjson(jsonString);
//			zipfile(jsonString,i,filepath);
//		}
//      }
	public  static void zipfile(String jsonString,int zipNum,String filepath){
		String dateStr=DateUtils.toString(new Date(),"yyyyMMdd");
		//文件名
//		String filname="1_10.txt";
		int first=(zipNum-1)*10+1;
		int end=zipNum*10;
		String filename=dateStr+"_"+first+"_"+end+".txt";
		ReadFile.writeTextFile(jsonString,filepath,filename);
		File[] files = new File[] { new File(filepath+File.separatorChar+filename) };//包含的文件
		//压缩文件的名字
		String zipfilepath=filepath+File.separatorChar+dateStr+"_"+first+"_"+end+".zip";
		File zip = new File(zipfilepath);//压缩的文件名和路径
		try {
			ZipUtil.ZipFiles(zip,"", files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//压缩
		
	}
	/**
     * 解析json数据
     * @param args
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @SuppressWarnings( { "unchecked", "rawtypes" })
    public static String jxjson(String content) {
    	System.out.println("整个文件的json数据："+content);
 	   Map<String, Object> objMap = JacksonJsonMapper.jsonToObject(content, Map.class);
 	   ArrayList<Object> arra= (ArrayList<Object>) objMap.get("rows");
 	   ArrayList<Object> arra3= new ArrayList<Object>();
 	   if(arra!=null&&arra.size()>0){
 		   for(Object obj:arra){
 			   //System.out.println("一条板块的json："+JacksonJsonMapper.objectToJson(obj));
 			   Map<String, Object> columMap = JacksonJsonMapper.jsonToObject(JacksonJsonMapper.objectToJson(obj), Map.class);
 			   String datajsonstr=columMap.get("data").toString();
 			  //System.out.println("板块内容的json："+datajsonstr);
 			  Map<String, Object> dataMap = JacksonJsonMapper.jsonToObject(datajsonstr, Map.class);
 			 for (Map.Entry<String, Object> entry : dataMap.entrySet()) {    
 				 //System.out.println("板块内部各个部分的json："+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
 				LinkedHashMap<String,Object> arra2= (LinkedHashMap<String,Object>) dataMap.get(entry.getKey());
 				 Map<Object, Object> dataMap2=new HashMap<Object, Object>();
 				  for (Iterator it =  arra2.keySet().iterator();it.hasNext();)
 				   {
 					  Object key = it.next();
// 					  //System.out.println( key+"="+ arra2.get(key));
 					  dataMap2.put(key, arra2.get(key));
 				   }
 				  //System.out.println(JacksonJsonMapper.objectToJson(dataMap2));
 				 dataMap.put(entry.getKey(), JacksonJsonMapper.objectToJson(dataMap2));
 			  }
 			 //System.out.println("<<<<<<<<<<<"+JacksonJsonMapper.objectToJson(dataMap));
 			columMap.put("data", JacksonJsonMapper.objectToJson(dataMap));
 			//System.out.println(">>>>>>>>>"+columMap.get("data"));
 			obj=JacksonJsonMapper.jsonToObject(JacksonJsonMapper.objectToJson(columMap), Object.class);
 			//System.out.println("@@@@@@@@@@@@@"+obj);
 			arra3.add(obj);
 		   }
 	   }
 	  objMap.put("rows", arra3);
 	 //System.out.println(objMap.get("rows"));
 	  System.out.println("整个文件的json数据："+JacksonJsonMapper.objectToJson(objMap));
 	  return JacksonJsonMapper.objectToJson(objMap);
    }
  //删除文件夹
  //param folderPath 文件夹完整绝对路径

       public static void delFolder(String folderPath) {
       try {
          delAllFile(folderPath); //删除完里面所有内容
          String filePath = folderPath;
          filePath = filePath.toString();
          java.io.File myFilePath = new java.io.File(filePath);
          if (!myFilePath.exists()) {
        	  myFilePath.delete(); //删除空文件夹
            }
       } catch (Exception e) {
         e.printStackTrace(); 
       }
  }

  //删除指定文件夹下所有文件
  //param path 文件夹完整绝对路径
     public static boolean delAllFile(String path) {
         boolean flag = false;
         File file = new File(path);
         if (!file.exists()) {
           return flag;
         }
         if (!file.isDirectory()) {
           return flag;
         }
         String[] tempList = file.list();
         File temp = null;
         for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
               temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
               temp.delete();
            }
            if (temp.isDirectory()) {
               delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
               delFolder(path + "/" + tempList[i]);//再删除空文件夹
               flag = true;
            }
         }
         return flag;
       }
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}
}