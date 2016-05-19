package com.candao.common.utils;

import java.io.IOException;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;













import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Description: http工具类
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年5月4日下午9:56:18
 * @version 1.0
 */
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static HttpConnectionManager connectionManager = null;

	/** 一个主机的最大连接数 */
	private static int defaultMaxConnPerHost = 40;
	/** 所有主机的最大连接数 */
	private static int defaultMaxTotalConn = 80;

	/** 连接超时时间 */
	private static int defaultConnectionTimeout = 8 * 1000;
	/** sokect超时时间，由bean factory设置 */
	//private static int defaultSokectTimeout = 120 * 1000;

	public static HttpConnectionManager getManager() {
		if (connectionManager == null) {
			connectionManager = new MultiThreadedHttpConnectionManager();
			connectionManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);
			connectionManager.getParams().setMaxTotalConnections(defaultMaxTotalConn);
			connectionManager.getParams().setConnectionTimeout(defaultConnectionTimeout);
			String timeOut = PropertiesUtils.getValue("DEFAULT_SOKECT_TIMEOUT");
			connectionManager.getParams().setSoTimeout(Integer.valueOf(timeOut));
			
			connectionManager.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
		}
		Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		return connectionManager;
	}
	

	/**
	 * 
	 * <post方式提交> <功能详细描述>
	 * 
	 * @param url
	 * @param form
	 *            postMethod.setRequestEntity(new StringRequestEntity(data,
	 *            "application/json", "utf-8"));
	 * @param header
	 * @param charSet
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	public static String doPost(String url, Map<String, String> form, Map<String, String> header, String charSet) throws HttpException, IOException {

		logger.info("url     : {}", url);
		logger.info("header  : {}", header);
		logger.info("form    : {}", form.toString());

		// 使用POST方法
		HttpClient httpClient = new HttpClient(getManager());
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("Connection", "close");
		postMethod.getParams().setContentCharset(charSet);//UTF-8

		// 将header的值放入postMethod中
		if (header != null) {
			for (Entry<String, String> e : header.entrySet()) {
				postMethod.setRequestHeader(e.getKey(), e.getValue());
			}
		}
		// 组装表单
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = form.keySet();
		for (String key : keySet) {
			nvps.add(new NameValuePair(key, form.get(key)));
		}
		NameValuePair[] param = new NameValuePair[nvps.size()];
		nvps.toArray(param);
		postMethod.addParameters(param);
		try {
			// 执行postMethod
			int statusCode = httpClient.executeMethod(postMethod);

			if (HttpStatus.SC_OK != statusCode) {
				throw new HttpException("http erro httpCode:" + statusCode);
			}
			// 打印服务器返回的状态
			logger.info("code    : {}", statusCode);

			String reciveStr = postMethod.getResponseBodyAsString();
			logger.info("receive : {}", reciveStr);
			return reciveStr;
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
	}

	/**
	 * 
	 * <流数据提交> <功能详细描述>
	 * 
	 * @param url
	 * @param data
	 * @param header
	 * @param charSet
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 * @throws Exception
	 */
	public static String doPost(String url, String data, Map<String, String> header, String charSet) throws HttpException, IOException {

		logger.info("url     : {}", url);
		logger.info("header  : {}", header);
		logger.info("data    : {}", data);

		// 使用POST方法
		HttpClient httpClient = new HttpClient(getManager());
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("Connection", "close");
		postMethod.getParams().setContentCharset(charSet);

		// 将header的值放入postMethod中
		if (header != null) {
			for (Entry<String, String> e : header.entrySet()) {
				postMethod.setRequestHeader(e.getKey(), e.getValue());
			}
		}
		try {
			postMethod.setRequestEntity(new StringRequestEntity(data, "application/x-www-form-urlencoded", charSet));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			// 执行postMethod
			int statusCode = httpClient.executeMethod(postMethod);

			if (HttpStatus.SC_OK != statusCode) {
				throw new HttpException("http erro httpCode:" + statusCode);
			}
			// 打印服务器返回的状态
			logger.info("code    : {}", statusCode);

			String reciveStr = postMethod.getResponseBodyAsString();
			logger.info("receive : {}", reciveStr);
			return reciveStr;
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
	}
}
