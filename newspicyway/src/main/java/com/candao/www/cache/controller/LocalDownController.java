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

import com.candao.www.cache.util.FileDownLoadThread;
import com.candao.www.constant.Constant;

/**
 * 本地缓存代理
 * 解决： 从云端拉取文件 改成从本地拉取 
 * 在需要拦截的链接中插入/cache/即可
 * 需要修改：\src\main\resources\config.properties
 * #门店的文件服务器代理地址
   fastdfs.url=http://image.candaochina.com/
   #云端文件服务器地址
   cloud_fastdfs.url=http://image.candaochina.com/
 * 举例：http://image.candaochina.com/image01/M00/00/1C/wKhlC1aqy6WAKhl8AAMkLo8yqFU253.jpg 或者.apk 或者其他
 * 本地缓存地址是：http://门店IP/newspicyway/cache/image01/M00/00/39/CkIVMVchy62AL6yFAAC15OFYwCc520.png 或者.apk 或者其他
 * 本地缓存文件地址：/tomcat/webapps/upload/image01/M00/00/39/CkIVMVchy62AL6yFAAC15OFYwCc520.png
 * @author zhaigt
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
    try {
      String path = request.getServletPath().replaceAll("^/cache", "");
      String could_url = "";// 原始目标地址
      // 判断url的合理性
      if (null == path || "".equals(path)) {
        return "路径： /cache/后面 资源路径为空";
      }
      // 文件服务器 could_url = "http://image.candaochina.com/" + path;
      could_url = Constant.CLOUD_FILEURL_PREFIX + path;

      // 拼接本地 缓存 文件地址 自动识别tomcat地址，放在tomcat/webapps/upload/
      String local_path = Constant.PROJECT_UPLOAD_PATH + path;
      File file = new File(local_path);
      // 判断本地是否有缓存文件
      if (file.exists()) {
        // 返回文件流
        download(response, local_path);
      } else {
        // 异步缓存资源到本地
        uploadPool.execute(new FileDownLoadThread(could_url, local_path));
        // 第一次本地没有缓存的情况下 跳转到原始目标地址
        return "redirect:" + could_url;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
		return null;// 此处需要返回null 否则提示output流already ...的异常
	}
	
	/**
   * 本地服务资源下载
   *
   * @return
   */
  private void download(HttpServletResponse response, String local_path) {
    FileInputStream fis = null;
    // 根据文件类型 设置输出的格式
    response.setContentType("octet-stream");
    try {
      OutputStream out = response.getOutputStream();
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
