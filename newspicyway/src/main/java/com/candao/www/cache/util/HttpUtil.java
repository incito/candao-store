/**
 * Copyright (c) 2010 S9,Inc.All rights reserved.
 * Created by 2010-9-9 
 */
package com.candao.www.cache.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;


/**
 * http相关的功能处理工具类
 * @author: zhaiguangtao
 * @date: 2012-11-27
 */
public class HttpUtil {
 
    private static int connectTimeout = 3000;// 连接超时值 ,单位:毫秒

    /**
     *  以GET的方式请求一个http资源,并将结果以字符串的方式返回
     * @param url url链接字符串
     * @param encoding 请求编码设置 ，UTF-8 或GBK或其他
     * @return 结果字符串
     * @throws IOException
     * */
    public static String sendGet(String url,String charset) throws IOException{
    	StringBuilder result = new StringBuilder();
        
        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        conn.connect();
        conn.setConnectTimeout(connectTimeout);// 设置链接超时时间

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        in.close();
        return result.toString();
    }
    
    /**
     *  以POST的方式请求一个http资源,并将结果以字符串的方式返回
     * @param url url链接字符串
     * @param param 请求参数
     * @param charset 请求编码设置 ，UTF-8 或GBK或其他
     * @param timeout 连接超时值
     * @return 结果字符串
     * @throws IOException
     * */
    public static String sendPost(String url, String param,final String charset, int timeout) throws IOException{
        StringBuilder result = new StringBuilder();
        URL httpurl = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Accept-Encoding", "gzip");//设置gzip请求支持
//        httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");//设置gzip请求支持
//        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");//设置gzip请求支持
//        httpConn.setRequestProperty("Pragma", "no-cache");// 不缓存
//        httpConn.setRequestProperty("Cache-Control", "no-cache");// 不缓存
        httpConn.setConnectTimeout(timeout);
        PrintWriter out = new PrintWriter(httpConn.getOutputStream());
        out.print(param);
        out.flush();
        out.close();
        BufferedReader in = null;
        String str = httpConn.getContentEncoding();
         
        if(!(str == null || str.indexOf("gzip") == -1)){
            GZIPInputStream gin = new GZIPInputStream(httpConn.getInputStream());
            in = new BufferedReader(new InputStreamReader(gin,charset));
        }else{
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),charset));
        }
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        in.close();
        httpConn.disconnect();
        return result.toString();
    }
    
    /**
     *模拟浏览器访问,常用于网页抓取
     * @param urlString      要抓取的url地址
     * @param charset        网页编码方式
     * @param timeout        超时时间
     * @return               抓取的网页内容
     * @throws IOException   抓取异常
     */
    public static String sendByUserAgent(String urlString, final String charset, int timeout) throws IOException {
      return sendByUserAgent(urlString, charset, timeout, null, null) ;
    }
    
    /**
     *模拟浏览器访问,常用于网页抓取
     * @param urlString      要抓取的url地址
     * @param charset        网页编码方式
     * @param timeout        超时时间
     * @return               抓取的网页内容
     * @throws IOException   抓取异常
     */
    public static String sendByUserAgent(String urlString, final String charset) throws IOException {
      return sendByUserAgent(urlString, charset, connectTimeout, null, null) ;
    }
    
    /**
     *模拟浏览器访问,常用于网页抓取
     * @param urlString      要抓取的url地址
     * @param charset        网页编码方式
     * @param timeout        超时时间
     * @return               抓取的网页内容
     * @throws IOException   抓取异常
     */
    public static String sendByUserAgent(String urlString, final String charset,HashMap<String,String> header) throws IOException {
      return sendByUserAgent(urlString, charset, connectTimeout, header, null) ;
    }
    
    /**
     *模拟浏览器访问,常用于网页抓取
     * @param urlString      要抓取的url地址
     * @param charset        网页编码方式
     * @param timeout        超时时间
     * @return               抓取的网页内容
     * @throws IOException   抓取异常
     */
    public static String sendByUserAgent(String urlString, final String charset,HashMap<String,String> header, String param) throws IOException {
      return sendByUserAgent(urlString, charset, connectTimeout, header, param) ;
    }
    
    /**
     * 模拟浏览器访问,常用于网页抓取
     * @param urlString      要抓取的url地址
     * @param charset        网页编码方式
     * @param timeout        超时时间
     * @return               抓取的网页内容
     * @throws IOException   抓取异常
     */
    public static String sendByUserAgent(String urlString, final String charset, int timeout, HashMap<String,String> header, String param) throws IOException {
        if (urlString == null || urlString.length() == 0) {
            return "";
        }
        urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString : ("http://" + urlString).intern();
//        urlString = EncodeUtil.ecode(urlString, "uft-8");
//        System.out.println("urlString"+urlString);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);   
        // 模拟浏览器访问
        conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36");  // 模拟手机系统
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");//只接受text/html类型，当然也可以接受图片,pdf,*/*任意，就是tomcat/conf/web里面定义那些
        // 设置 heade头信息
        if(!(header== null || header.isEmpty())){
          for (Iterator iterator = header.keySet().iterator(); iterator.hasNext();) {
            String key = (String)iterator.next();
            conn.setRequestProperty(key, header.get(key)); 
          }
        }
        
        // 设置请求参数
        if(!(param == null || "".equals(param))){
          PrintWriter out = new PrintWriter(conn.getOutputStream());
          out.print(param);
          out.flush();
          out.close();
        }
        
        // 设置超时时间
        conn.setConnectTimeout(timeout);
        try {
//          System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "";
            }
        } catch (Exception e) {
            try {
                System.out.println(e.getMessage());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return "";
        }
        InputStream input = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
        String line = null;
        StringBuilder sb = new StringBuilder("");
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        if (reader != null) {
            reader.close();
        }
        if (conn != null) {
            conn.disconnect();
        }
        return sb.toString();
    }
    
    /**
     * 文件下载
     * @throws Exception 
     * */
    public static boolean downLoad(String url,String path) throws IOException{
      File tmp_file = null;  
      try {
//            url = EncodeUtil.ecode(url, "utf-8");
          URL u = new URL(url);
          URLConnection conn = u.openConnection();
          conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36");  // 模拟手机系统
          conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");//只接受text/html类型，当然也可以接受图片,pdf,*/*任意，就是tomcat/conf/web里面定义那些
          conn.connect();
          int filesize = conn.getContentLength();
          InputStream is = conn.getInputStream();
         
          tmp_file = new File(path+".tmp");// 创建临时文件
          File parent = tmp_file.getParentFile();// 获取文件上级目录
          if(!parent.exists()){// 判断目录是否存在 如果不存在自动创建目录
              parent.mkdirs();// 创建连续的目录
          }
          FileOutputStream fos = new FileOutputStream(tmp_file);
          
          byte[] buffer = new byte[2048];
          int len = 0 ;
          int fileReadSize = 0;
          while ((len = is.read(buffer)) != -1) {
              fos.write(buffer, 0, len);
              fileReadSize += len;
          }
          is.close();
          fos.close();
          if(filesize == fileReadSize){
            tmp_file.renameTo(new File(path));//改名
          }else{
            tmp_file.deleteOnExit();// 删除临时文件
          }
      } catch (IOException e) {
        if(tmp_file !=null){
          tmp_file.deleteOnExit();//异常时 删除临时文件
        }
        throw e;
      }
      return true;
    }
    
    public static String[] UserAgent = {
        "Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.2",
        "Mozilla/5.0 (iPad; U; CPU OS 3_2_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B500 Safari/531.21.11",
        "Mozilla/5.0 (SymbianOS/9.4; Series60/5.0 NokiaN97-1/20.0.019; Profile/MIDP-2.1 Configuration/CLDC-1.1) AppleWebKit/525 (KHTML, like Gecko) BrowserNG/7.1.18121",
        "Nokia5700AP23.01/SymbianOS/9.1 Series60/3.0",
        "UCWEB7.0.2.37/28/998",
        "NOKIA5700/UCWEB7.0.2.37/28/977",
        "Openwave/UCWEB7.0.2.37/28/978",
        "Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/989",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2"
    };
}
