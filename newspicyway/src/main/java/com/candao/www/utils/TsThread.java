package com.candao.www.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class TsThread extends Thread {
	   private String  str ;
	   public TsThread(String  str){
		   this.str = str;
	   }
	   @Override
	   public void run(){
		   //根据动作打印不同的小票
			URL urlobj;
			try {
			urlobj = new URL(str);
			URLConnection	urlconn = urlobj.openConnection();
			urlconn.connect();
			InputStream myin = urlconn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
			String content = reader.readLine();
			JSONObject object = JSONObject.fromObject(content.trim());
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> resultList = (List<Map<String, Object>>) object.get("result");
			if("1".equals(String.valueOf(resultList.get(0).get("Data")))){
				System.out.println("推送成功");
			}else{
				System.out.println("推送失败");
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
	   public static void main(String[] args) {
		   new TsThread("http://10.66.9.248:8081/datasnap/rest/TServerMethods1/broadcastmsg/userid/sdfsdf".toString()).run();
	}
}
