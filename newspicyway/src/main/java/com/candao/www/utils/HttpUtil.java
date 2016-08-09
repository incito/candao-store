/**
 * Copyright (c) 2010 S9,Inc.All rights reserved. Created by 2010-9-9
 */
package com.candao.www.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;


/**
 * @title :http相关的功能处理工具类
 * @author: zhaiguangtao
 * @date: 2012-11-27
 */
public class HttpUtil {

	// 日志对象
	private static int connectTimeout = 3000;// 连接超时值 ,单位:毫秒

	/**
	 * @title 以GET的方式请求一个http资源,并将结果以字符串的方式返回
	 * @param url
	 *            url链接字符串
	 * @param encoding
	 *            请求编码设置 ，UTF-8 或GBK或其他
	 * @return 结果字符串
	 * @throws IOException
	 */
	public static String sendGet(String url, String charset) throws IOException {
		StringBuilder result = new StringBuilder();

		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		conn.connect();
		conn.setConnectTimeout(connectTimeout);// 设置链接超时时间
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result.append(line);
		}
		in.close();
		return result.toString();
	}

	/**
	 * @title 以POST的方式请求一个http资源,并将结果以字符串的方式返回
	 * @param url
	 *            url链接字符串
	 * @param param
	 *            请求参数
	 * @param charset
	 *            请求编码设置 ，UTF-8 或GBK或其他
	 * @param timeout
	 *            连接超时值
	 * @return 结果字符串
	 * @throws IOException
	 */
	public static String sendPost(String url, String param, String contentType, final String charset, int timeout)
			throws IOException {
		StringBuilder result = new StringBuilder();
		URL httpurl = new URL(url);
		HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
		httpConn.setDoOutput(true);
		// HttpUtil.class.getd
		httpConn.setDoInput(true);
		httpConn.setRequestProperty("Accept-Encoding", "gzip");// 设置gzip请求支持
		httpConn.addRequestProperty("Content-Type", contentType);
		// httpConn.setRequestProperty("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");//设置gzip请求支持
		// httpConn.setRequestProperty("Accept-Language",
		// "zh-CN,zh;q=0.8");//设置gzip请求支持
		// httpConn.setRequestProperty("Pragma", "no-cache");// 不缓存
		// httpConn.setRequestProperty("Cache-Control", "no-cache");// 不缓存
		httpConn.setConnectTimeout(timeout);
		PrintWriter out = new PrintWriter(httpConn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		BufferedReader in = null;
		String str = httpConn.getContentEncoding();

		if (!(str == null || str.indexOf("gzip") == -1)) {
			GZIPInputStream gin = new GZIPInputStream(httpConn.getInputStream());
			in = new BufferedReader(new InputStreamReader(gin, charset));
		} else {
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), charset));
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
	 * 调用Restful请求
	 * 
	 * @param urlPath
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @return 返回请求响应的HTML
	 */
	public static String doRestfulByHttpConnection(String urlPath, String jsonStr) {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection httpConnection = null;
		StringBuffer output = new StringBuffer();
		URL url;
		try {
			url = new URL(urlPath);
			// 设置超时时间，这两句好像有用
			// System.setProperty("sun.net.client.defaultConnectTimeout",
			// "300000");
			// System.setProperty("sun.net.client.defaultReadTimeout",
			// "300000");
			httpConnection = (HttpURLConnection) url.openConnection();
			// 设置超时时间，这两句好像不起作用
			// httpConnection.setConnectTimeout(300000);
			// httpConnection.setReadTimeout(300000);
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");// 头文件必须这样写，否则无效

			printWriter = new PrintWriter(new OutputStreamWriter(httpConnection.getOutputStream(), "UTF-8"));
			printWriter.write(jsonStr);
			// flush输出流的缓冲
			printWriter.flush();
			// OutputStream out = httpConnection.getOutputStream();
			// out.write(jsonStr.getBytes("utf-8"));
			// out.flush();
			// OutputStreamWriter out = new
			// OutputStreamWriter(httpConnection.getOutputStream());
			// out.write(jsonStr);
			// out.flush();
			// BufferedWriter writer = new BufferedWriter(new
			// OutputStreamWriter(httpConnection.getOutputStream(),"UTF-8"));
			// writer.write(jsonStr);
			// writer.flush();

			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}

			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream(), "utf-8"));
			// 读取返回结果
			char[] b = new char[1024];
			for (int n; (n = responseBuffer.read(b)) != -1;) {
				output.append(new String(b, 0, n));
			}
			httpConnection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		return output.toString();
	}

	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @return 返回请求响应的HTML
	 */
	public static String doPostByHttpConnection(String url, Map<String, Object> requestParamsMap) {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		StringBuffer responseResult = new StringBuffer();
		StringBuffer params = new StringBuffer();
		HttpURLConnection httpURLConnection = null;
		// 组织请求参数
		Iterator it = requestParamsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry element = (Map.Entry) it.next();
			params.append(element.getKey());
			params.append("=");
			params.append(element.getValue());
			params.append("&");
		}
		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
		}

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(params.length()));
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("contentType", "application/json");
			httpURLConnection.setRequestProperty("Charset", "utf-8");
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(params.toString());
			// flush输出流的缓冲
			printWriter.flush();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			// if (responseCode != 200) {
			// log.error(" Error===" + responseCode);
			// } else {
			// log.info("Post Success!");
			// }
			// 定义BufferedReader输入流来读取URL的ResponseData
			bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
			// char[] b = new char[1024];
			// for (int n; (n = bufferedReader.read(b)) != -1;) {
			// responseResult.append(new String(b));
			// }
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				responseResult.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		return responseResult.toString();
	}
}
