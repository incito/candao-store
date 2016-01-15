package com.candao.www.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilTest {

	
	public static void main(String args[]) throws Exception{
		     String url="http://127.0.0.1:8080/api/app/order/processOrder";
		     Map dataMap = new HashMap();
	        dataMap.put("data", "{\"orderMap\":[{\"dishid\":\"1bf992a5-f7ff-4484-9381-1f456d9f53ac\",\"dishnum\":\"1.0\"},{\"dishid\":\"DISHES_98\",\"dishnum\":\"4.0\"}],\"orderid\":\"H20151124152314000291\"}");
	        System.out.println(new HttpRequestor().doPost(url, dataMap));
	        
	}
}
