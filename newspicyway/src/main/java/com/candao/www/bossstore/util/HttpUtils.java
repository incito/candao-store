package com.candao.www.bossstore.util;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: 张文栋
 * Date: 15/7/23
 * Desc: http请求工具类
 */
public class HttpUtils {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static void getHttpPost(String link,JSONObject requestObject) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(link);
            StringEntity params = new StringEntity(requestObject.toString(), "UTF-8");
            request.addHeader("content-type", "application/json;charset=UTF-8");
            request.setEntity(params);
            CloseableHttpResponse response = httpClient.execute(request);
            logger.error("status:"+response.getStatusLine());
            response.close();
        } catch (Exception ex) {
            System.out.printf("网络出错 =?",link);
            logger.error("推送失败:"+ex.getMessage());
            ex.printStackTrace();
        } finally {
        	try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
