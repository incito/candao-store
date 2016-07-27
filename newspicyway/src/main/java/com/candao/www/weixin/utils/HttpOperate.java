package com.candao.www.weixin.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.common.enums.ErrorMessage;
import com.candao.common.enums.Module;
import com.candao.common.exception.SysException;
import com.candao.common.utils.HttpUtil;

/**
 * @Description: http请求的操作类
 * @Company:上海餐道
 * @create Author: 余城序
 * @create Date: 2016年7月24日下午9:41:46
 * @version 1.0
 */
public class HttpOperate {
	
	private static Logger logger = LoggerFactory.getLogger(HttpOperate.class);
	/**
	 * 
	 * @Description:post的请求方式
	 * @create: 余城序
	 * @Modification:
	 * @param url 请求路径
	 * @param map 请求参数
	 * @return String result 请求结果
	 * @throws SysException String
	 */
	public static String post(String url,Map<String,String> map) throws SysException{
		// 上传数据到总店
		String result;
		try {
			result = HttpUtil.doPost(url, map, null, "UTF-8");
		} catch (HttpException e) {
			logger.error("http数据传输失败",e);
			throw new SysException(ErrorMessage.HTTP_TRANS_ERROR, Module.LOCAL_SHOP);
		} catch (IOException e) {
			logger.error("服务器连接出现异常",e);
			throw new SysException(ErrorMessage.HTTP_RESPONSE_ERROR, Module.LOCAL_SHOP);
		}
		return result;
	}

}
