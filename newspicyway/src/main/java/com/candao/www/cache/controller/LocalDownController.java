package com.candao.www.cache.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.candao.www.constant.Constant;

/**
 * 本地缓存代理
 * 解决： 从云端拉取文件 改成从本地拉取 
 * 在需要拦截的链接中插入/cache/即可
 * 
 * 例如：http://image.candaochina.com/image01/M00/00/1C/wKhlC1aqy6WAKhl8AAMkLo8yqFU253.jpg
 * 本地缓存地址是：http://10.66.21.49/cache/image01/M00/00/39/CkIVMVchy62AL6yFAAC15OFYwCc520.png
 * @author zhaigt
 *
 */
@Controller
@RequestMapping("/cache")
public class LocalDownController {
  public static ExecutorService uploadPool = Executors.newFixedThreadPool(20);//  线程池 
	
	/**
	 * 拦截资源地址，进行本地化处理
	 */
	@RequestMapping("**")
	public String cache(HttpServletResponse response,HttpServletRequest request) {
		try{
//		 String path="/image01/M00/00/01/wKhlClYu6ReAKnWVAAvWLh-lGcM847.jpg";
		 String path = request.getServletPath().replaceAll("^/cache/", "");
		 String could_url = "";
		 //判断url的合理性
		 if(null == path || "".equals(path)){
		   return "/cache/后面无有效资源名称";
		 }else if(path.startsWith("image01")){// 图片服务器
		   could_url = Constant.FILEURL_PREFIX + path;
		 }
		  // 拼接本地 缓存 文件地址
		  String local_path = Constant.PROJECT_UPLOAD_PATH + path;
		  File file = new File(local_path);
		  // 判断本地是否有缓存文件
		  if(file.exists()){
		    // 返回文件流
		    download(response,local_path);
		  }else{
//		    request.getRequestDispatcher(could_url).forward(request, response);
		    // 异步缓存资源到本地
		    uploadPool.execute(new FileDownLoadThread(could_url, local_path));
		    return "redirect:"+could_url;
		  }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
   * 本地服务资源下载
   *
   * @return
   */
  protected void download(HttpServletResponse response,String local_path) {
    FileInputStream fis = null;
    //根据文件类型 设置输出的格式
    response.setContentType("octet-stream");
//    response.setContentType("image/gif");
    try {
        OutputStream out = response.getOutputStream();
//        File file = new File("D:"+File.separator+"timg.jpg");
        fis = new FileInputStream(local_path);
        byte[] b = new byte[fis.available()];
        fis.read(b);
        out.write(b);
        out.flush();
    } catch (Exception e) {
         e.printStackTrace();
    } finally {
        if (fis != null) {
            try {
               fis.close();
            } catch (IOException e) {
            e.printStackTrace();
        }   
          }
    }
  }
  
	/**
	 * 定时，或者自动拉取云端文件 缓存到本地
	 */
	 
}
