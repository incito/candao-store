package com.candao.www.webroom.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.candao.www.security.controller.BaseController;
/**
 * 门店提供上传PAD日志的接口
 * @author zgt
 *
 */
@Controller
@RequestMapping("/padlog")
public class PadLogController extends BaseController{
	private static final String LOG_HOME = "/home/cloud-user/candao/padlog/";// 与logback的目录相同
	private Logger logger = LoggerFactory.getLogger(PadLogController.class);
	/**
	 * pad日志上传
	 * @param 日志文件
	 * @return
	 */
	@RequestMapping("upload")
	@ResponseBody
	public Map<String, Object> upload(HttpServletRequest request) {
	  Map<String, Object> map = new HashMap<String, Object>();
	  
	  try {
	    // 支持一次上传多个
  	  List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
  	  for (int i = 0; i < files.size(); i++) {
  	    MultipartFile file = files.get(i);
  	    if (!file.isEmpty()) {
          String fileName = file.getOriginalFilename();
          String dataDir = new Date().toLocaleString().replaceAll(" .*", "");// 按天生成目录
          File logfile = new File(LOG_HOME + dataDir + "/" + fileName);//文件名与pad日志文件名一致
          File fileDir = logfile.getParentFile();
          if(!fileDir.exists()){// 如果目录不存在 就创建目录
            fileDir.mkdirs();// 创建目录
          }
          file.transferTo(logfile);
        }
      }
  	  map.put("code","0");// 0成功 其它失败
  	  map.put("msg","上传成功");
  	  Map<String, Object> map2 = new HashMap<String, Object>();
      map2.put("result", "success");
      map.put("data", map2);
	  }catch (Exception e) {
	    map.put("code","1");// 0成功 其它失败
	    map.put("msg","上传失败");
	    Map<String, Object> map2 = new HashMap<String, Object>();
	    map2.put("result", "fail");
	    map.put("data", map2);
	    logger.error("-->",e);
	    e.printStackTrace();
	  }
	  
	  return map;
	}
	
}
