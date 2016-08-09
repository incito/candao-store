package com.candao.www.webroom.controller;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.candao.common.utils.FileUpLoad;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.file.fastdfs.service.FileService;
import com.candao.www.constant.Constant;
import com.candao.www.webroom.service.impl.ConvertVideoUtil;

@Controller
@RequestMapping("/video")
public class VideoUploadController {
	
	@Autowired
	FileService  fileService;
	
	@RequestMapping("/upload")
	@ResponseBody
	public String uploadVideo(MultipartHttpServletRequest request) throws Exception{
		
		CommonsMultipartResolver mutiparRe=new CommonsMultipartResolver();
		FileUpLoad up=new FileUpLoad();
		if(mutiparRe.isMultipart(request)){
			Iterator<String> list=null;
			for(list= request.getFileNames();list.hasNext();){
				MultipartFile file=null;
				String str=list.next();
				System.out.println(str);
				if(str!=null&&!"".equals(str)){
					file=request.getFile(str);
					up.upload(file, request.getSession().getServletContext().getRealPath("/")+File.separator+"upload/");
				}
			}
		}
		String imagePath= request.getContextPath()+"/"+up.getUrl();
	   // String imageRealPath=request.getRealPath("/")+File.separator+up.getUrl().replace("/", "\\");

	    String url=imagePath.substring(0,imagePath.lastIndexOf("."))+".mp4";
	    
	    String osType = System.getProperties().getProperty("os.name");
	    String ffmpegPath = request.getRealPath("/")+File.separator+"tools"+File.separator+"ffmpeg.exe ";
	    String imageRealPath = request.getRealPath("/")+ File.separator+up.getUrl().replace("/", "\\");
	    String outputRealPath=imageRealPath.substring(0,imageRealPath.lastIndexOf("."))+".mp4";
	    String tempRealPath=imageRealPath.substring(0,imageRealPath.lastIndexOf("."))+".avi";
	    
	    if(osType == null || !osType.startsWith("Windows")){
	    	osType = "linux";
	    	ffmpegPath = request.getRealPath("/")+File.separator+"tools"+File.separator+"ffmpeg "; 
	    	imageRealPath=request.getRealPath("/")+ up.getUrl();
		    outputRealPath=imageRealPath.substring(0,imageRealPath.lastIndexOf("."))+".mp4";
		    tempRealPath=imageRealPath.substring(0,imageRealPath.lastIndexOf("."))+".avi";
	    }
	    String mencoderPath=request.getRealPath("/")+File.separator+"tools"+File.separator+"mencoder.exe "; 
	    
	    System.out.println("imagePath==="+imagePath);
	    System.out.println("imageRealPath==="+imageRealPath);
	    System.out.println("outputRealPath==="+outputRealPath);
	    System.out.println("tempRealPath==="+tempRealPath);
	    System.out.println("url==="+url);
	    System.out.println("mencoderPath==="+mencoderPath);
	    System.out.println("ffmpegPath==="+ffmpegPath);
	    
	    String fileUrlpath = "";
	    ConvertVideoUtil converVideo = new ConvertVideoUtil(mencoderPath,ffmpegPath,imageRealPath,outputRealPath,tempRealPath);
	    converVideo.run();
	     
		fileUrlpath = fileService.uploadFile(new File(outputRealPath));
		 
	    
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", Constant.FILEURL_PREFIX + fileUrlpath);
		map.put("fileType", "video/mp4");
		map.put("state", "SUCCESS");
		map.put("original","");
		return JacksonJsonMapper.objectToJson(map);
	}
	

}
