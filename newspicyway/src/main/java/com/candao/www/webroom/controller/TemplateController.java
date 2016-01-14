package com.candao.www.webroom.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.constant.Constant;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.Ttemplate;
import com.candao.www.timedtask.ReadFile;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.TemplateService;

@Controller
@RequestMapping("/template")
public class TemplateController {
	private static final Logger log = LoggerFactory.getLogger(TemplateController.class);
	@Autowired
	private TemplateService templateService;
	@Autowired
	private DishService dishService;

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(int page, int rows, @RequestParam Map<String, Object> params) {
		Page<Map<String, Object>> pageMap = templateService.grid(params, page, rows);
//		System.out.println(JacksonJsonMapper.objectToJson(pageMap));
//		List<Map<String, Object>>  data=   (List<Map<String, Object>>)pageMap.getRows();
//		if(!ValidateUtils.isEmpty(data)){
//		for(Map<String,Object> map:data){
//		  map.put("buttons",buttons(String.valueOf(map.get("status")),String.valueOf(map.get("id"))));	
//		}
//		}
		ModelAndView mav = new ModelAndView();
//		pageMap.setRows(data);
		mav.addObject("page", pageMap);
		System.out.println(JacksonJsonMapper.objectToJson(pageMap));
		return mav;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private String buttons(String status,String templateId){
		String str="";
		boolean allowAccess=false;
		List<String> allowAccessButtons = (List<String>) SessionUtils.get(Constant.ALLOW_ACCESS_BUTTONS);
		if (!ValidateUtils.isEmpty(allowAccessButtons)) {
			str+="<a href=\"###\" onclick=\"view_object('" + templateId + "')\">预览</a>&nbsp;&nbsp;";
			for(String ss:allowAccessButtons){
				if(ss.equals("/template/refer")){
					if(status.equals("1")){
					  str+="<a href=\"###\" onclick=\"refer_object('" + templateId + "')\">提交</a>&nbsp;&nbsp;";
					}
				}
				if(ss.equals("/template/goback")){
					str+="<a href=\"###\" onclick=\"goback_object('" + templateId + "')\">退回</a>&nbsp;&nbsp;";
				}
				if(ss.equals("/template/approve")){
					str+="<a href=\"###\" onclick=\"approve_object('" + templateId + "')\">核准</a>&nbsp;&nbsp;";
				}
				if(ss.equals("/template/save")){
					if(allowAccessButtons.contains("/template/goback")){//审核者
						str+="<a href=\"###\" onclick=\"modify_object('" + templateId + "')\">修改</a>&nbsp;&nbsp;" ;
					}else{
						if(!status.equals("2")&&!status.equals("3")){
							str+="<a href=\"###\" onclick=\"modify_object('" + templateId + "')\">修改</a>&nbsp;&nbsp;";
						}
					}
				}
				if(ss.equals("/template/delete")){
					if(allowAccessButtons.contains("/template/goback")){//审核者
			          	str+="<a href=\"###\" onclick=\"delete_object('" + templateId + "')\">删除</a>&nbsp;&nbsp;";
					}else{
						if(!status.equals("3")){
							str+="<a href=\"###\" onclick=\"delete_object('" + templateId + "')\">删除</a>&nbsp;&nbsp;";
						}
					}
				}
			}
		}  
		System.out.println(str);
		return str;
	}
	
	@RequestMapping("/index")
	public String index() {
		return "template/show";
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public String save(HttpServletRequest request, Ttemplate tbTemplate,String articleids,String oldarticleids) {
		boolean b = false;
		String id = tbTemplate.getId();
		Ttemplate oldtbTemplate=null;
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tbTemplate.setId(IdentifierUtils.getId().generate().toString());
				tbTemplate.setStatus(1);
				//tbTemplate.setCreateuserid(SessionUtils.getCurrentUser().getUserid());
				tbTemplate.setCreateuserid(SessionUtils.getCurrentUser().getId());
				b = templateService.save(tbTemplate,articleids);
			} else {// 修改articleService
				//tbTemplate.setStatus(1);
				oldtbTemplate=templateService.findById(id);
				b = templateService.update(tbTemplate,articleids,oldarticleids);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加成功");
			} else {
				map.put("message", "修改成功");
				if(oldtbTemplate.getStatus()==3){
					deletefile(oldtbTemplate);
					zipfile(templateService.findById(id));
				}
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加失败");
			} else {
				map.put("message", "修改失败");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}

	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		Ttemplate tbTemplate = templateService.findById(id);
		ModelAndView mav = new ModelAndView();
		//tbTemplate.setData(tbTemplate.getData().replace("\"\"","\""));
		mav.addObject("tbTemplate", tbTemplate);
		return mav;
	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		Ttemplate ttp=templateService.findById(id);
		updateArticles(ttp);
		boolean b = templateService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
//			deletefile(ttp);
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
	/**
	 * 版式被删除后，版式中的文章应该改变状态，变成可选文章
	 * 1是可选 0是不可选
	 * @param ttp
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  void updateArticles(Ttemplate ttp){
//		System.out.println(ttp.getData());
		 Map<String, Object> dataMap = JacksonJsonMapper.jsonToObject(ttp.getData(), Map.class);
		 String ids="";
		 for (Map.Entry<String, Object> entry : dataMap.entrySet()) {    
//			 System.out.println("板块内部各个部分的json："+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
			LinkedHashMap<String,Object> arra2= (LinkedHashMap<String,Object>) dataMap.get(entry.getKey());
			  for (Iterator it =  arra2.keySet().iterator();it.hasNext();)
			   {
				  Object key = it.next();
// 				  System.out.println( key+"="+ arra2.get(key));
 				  if("dishid".equals(key)){
 					  String id=(String) arra2.get(key);
 					  ids+=id+",";
 				  }
			   }
		  }
		 templateService.update(ttp, "", ids);
	}
	/**
	 * 提交
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/refer/{id}")
	@ResponseBody
	public String refer(@PathVariable(value = "id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag=true;
		Ttemplate tbTemplate = templateService.findById(id);
		Map<String, Object> dataMap = JacksonJsonMapper.jsonToObject(tbTemplate.getData(), Map.class);
		switch (tbTemplate.getType().toUpperCase()) {
		case "A":
			for(int i=1;i<=5;i++){
				if(dataMap.get("A"+i)==null||"".equals(dataMap.get("A"+i))){
					flag=false;
				}
			}
			break;
		case "B":
			for(int i=1;i<=4;i++){
				if(dataMap.get("B"+i)==null||"".equals(dataMap.get("B"+i))){
					flag=false;
				}
			}
			break;
		case "C":
			for(int i=1;i<=4;i++){
				if(dataMap.get("C"+i)==null||"".equals(dataMap.get("C"+i))){
					flag=false;
				}
			}
			break;
		case "D":
			for(int i=1;i<=3;i++){
				if(dataMap.get("D"+i)==null||"".equals(dataMap.get("D"+i))){
					flag=false;
				}
			}
			break;
		}
		if(!flag){
			map.put("message", "该版式未编辑完成，不能提交");
		}else{
			boolean b = templateService.updateStatus(id, 2);
			if (b) {
				map.put("message", "提交成功");
			} else {
				map.put("message", "提交失败");
			}
		}
		
		return JacksonJsonMapper.objectToJson(map);
	}

	/**
	 * 核准
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/approve/{id}")
	@ResponseBody
	public String approve(@PathVariable(value = "id") String id) {
		boolean b = templateService.updateStatus(id, 3);
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			map.put("message", "核准成功");
			log.debug("start_id="+id+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())  );
			Ttemplate tbt=templateService.findById(id);
			zipfile(tbt);
			log.debug("end_id="+id+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		} else {
			map.put("message", "核准失败");
		}
		return JacksonJsonMapper.objectToJson(map);
	}
	/**
	 * 将审核通过的文章创建txt文件，然后压缩成zip文件
	 */
	static String  url=Constant.url;
	public static void zipfile(Ttemplate ttp){
		String tbTemplateStr=JacksonJsonMapper.objectToJson(ttp);
		System.out.println(tbTemplateStr);
		tbTemplateStr=jxjson(tbTemplateStr);
		Date date=DateUtils.parse(ttp.getReleasetime(),"yyyy-MM-dd HH:mm:ss");
		String dateStr=DateUtils.toString(date, "yyyyMMddHHmmss");
		String filename=dateStr+".txt";
		ReadFile.writeTextFile(tbTemplateStr,url,filename);
		//审核通过文件不需要压缩 直接就是txt文件
//		File[] files = new File[] { new File(url+File.separatorChar+filename) };//包含的文件
//		//压缩文件的名字
//		String zipfilepath=url+File.separatorChar+dateStr+".zip";
//		File zip = new File(zipfilepath);//压缩的文件名和路径
//		try {
//			ZipUtil.ZipFiles(zip,"", files);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}//压缩
		
	}
	/**
     * 解析json数据  单个模板数据
     * @param args
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @SuppressWarnings( { "unchecked", "rawtypes" })
    public static String jxjson(String content) {
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
	 * 退回
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/goback/{id}")
	@ResponseBody
	public String goback(@PathVariable(value = "id") String id) {
		boolean b = templateService.updateStatus(id, 4);
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			map.put("message", "退回成功");
			Ttemplate ttp=templateService.findById(id);
			deletefile(ttp);
		} else {
			map.put("message", "退回失败");
		}
	
		return JacksonJsonMapper.objectToJson(map);
	}
	/**
	 * 删除版式对应的txt文件和zip文件
	 * @param ttp
	 */
	public static void deletefile(Ttemplate ttp){
		Date date=DateUtils.parse(ttp.getReleasetime(),"yyyy-MM-dd HH:mm:ss");
		String dateStr=DateUtils.toString(date, "yyyyMMddHHmmss");
		String textFilePath=url+File.separatorChar+dateStr+".txt";
//		String zipFilePath=url+File.separatorChar+dateStr+".zip";
		File textFile = new File(textFilePath);  
		if(textFile.exists()){
			textFile.delete();
		}
//		File zipFile = new File(zipFilePath);  
//		if(zipFile.exists()){
//			zipFile.delete();
//		}
	}
	@RequestMapping(value = "/validateTemplate")
	@ResponseBody
	public ModelAndView validateTemplate(String articleids,String tbTemplateid){
		ModelAndView mav = new ModelAndView();
		//判断是否选择了相同的文章
		boolean flag=true;
		String[] strs = articleids.split(",");
		for(int i = 0;i<strs.length;i++){
			for(int k=i+1;k<strs.length;k++){
				 if(strs[i].equals(strs[k])){
		                flag= false;
		                break;
		          }
			}
			if(!flag){
				break;
			}
           
        }
		if(flag){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("articleids", articleids.split(","));
		params.put("tbTemplateid", tbTemplateid);
		List<Ttemplate> tbTemplateList=templateService.validateTemplate(params);
		System.out.println(tbTemplateList.size()+"----------->>>>>>>>");
		if(tbTemplateList!=null&&tbTemplateList.size()>0){
			mav.addObject("message","fail");
			List<String> list = new ArrayList<String>();
			for(Ttemplate tbt : tbTemplateList){
				list=getAllSameElement(tbt.getArticleids().split(","),articleids.split(","),list);
			}
			if(list!=null&&list.size()>0){
				String alertmessage="";
				for(String str :list){
					Tdish tba=dishService.findById(str);
				alertmessage+=tba.getImagetitle()+",";
				}
				mav.addObject("alertmessage","文章："+alertmessage.substring(0, alertmessage.length()-1)+"已在别的模板中使用，请重新选择！");
			}
		}else{
			mav.addObject("message","success");
			mav.addObject("alertmessage","");
		}
		}else{
			mav.addObject("message","fail");
			mav.addObject("alertmessage","请勿在一个版式中选择相同的文章！");
		}
		return mav;
	}
	/**
	 * 找出两个数组中相同的元素
	 * @param strArr1
	 * @param strArr2
	 * @return
	 */
	public static List<String> getAllSameElement(String[] strArr1,String[] strArr2,List<String> list) {   
        if(strArr1 == null || strArr2 == null) {   
            return null;   
        }   
        Arrays.sort(strArr1);   
        Arrays.sort(strArr2);   
        int k = 0;   
        int j = 0;   
        while(k<strArr1.length && j<strArr2.length) {   
            if(strArr1[k].compareTo(strArr2[j])==0) {   
                if(strArr1[k].equals(strArr2[j]) ) { 
                	if(list.indexOf(strArr1[k])==-1){
                		list.add(strArr1[k]);  
                	}
                		k++;   
                		j++;   
                	
                }   
                continue;   
            } else  if(strArr1[k].compareTo(strArr2[j])<0){   
                k++;   
            } else {   
                j++;   
            }   
        }   
       return list;   
    }   


}
