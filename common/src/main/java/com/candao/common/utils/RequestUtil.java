package com.candao.common.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
   public static String getPhoneType(HttpServletRequest request){
	 String userAgent= request.getHeader("user-agent");
		if(userAgent.contains("Android")||userAgent.contains("Linux")) {  
			return "Android";   
		} else if(userAgent.contains("iPhone")) {  
			return "iPhone"; 
		}  else if(userAgent.contains("iPad")) {  
			return "iPad"; 
		}  else {  
			return "other";
		}
   }
   
   public static String getPhoneType(String userAgent){
			if(userAgent.contains("Android")||userAgent.contains("Linux")) {  
				return "Android";   
			} else if(userAgent.contains("iPhone")) {  
				return "iPhone"; 
			}  else if(userAgent.contains("iPad")) {  
				return "iPad"; 
			}  else {  
				return "other";
			}
	   }
}
