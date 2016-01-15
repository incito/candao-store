package com.candao.common.utils;

import java.math.BigDecimal;


public class StringUtils {
	
	
public static String getStr(String inStr){
		if(inStr == null){
			return Constant.NOMSG;			
		}
		return "".equals(inStr.trim())?Constant.NOMSG : inStr.trim();
}
	

public static BigDecimal stringToBigDecimal(String bigDecimalStr){
	 
	BigDecimal bd = new BigDecimal(bigDecimalStr);   
	//设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)   
	return bd.setScale(2, BigDecimal.ROUND_HALF_UP);   
	//转化为字符串输出   
}
//
//注意汉子与数字所占空间不同
public static String bSubstring2(String s,int k) throws Exception{
	   if(s == null || s.equals("")){
		   return "";
	   }
	   if(s.length()>k){
		   s=s.substring(0,k);
	   }
	  
	   int j=s.length();
	   
	   for(int i=0;i<k-j;i++){
		   s=s+"　";
	   }
	   	byte[] bytes = s.getBytes("Unicode");
		
		
		
		String returnMsg = new String(bytes, 0, s.getBytes("Unicode").length, "Unicode");

		return returnMsg;
	
}

public static String bSubstring3(String s,int k) throws Exception{
	   if(s == null || s.equals("")){
		   return "";
	   }
	   if(s.length()>k){
		   s=s.substring(0,k);
	   }
	  
	   int j=s.length();
	   
	   for(int i=0;i<k-j;i++){
		   s=s+" ";
	   }
	   	byte[] bytes = s.getBytes("Unicode");
		
		
		
		String returnMsg = new String(bytes, 0, s.getBytes("Unicode").length, "Unicode");

		return returnMsg;
	
}


 

////中文字符判断
//function getStrLength(str) { 
// var len = str.length; 
// var reLen = 0; 
// for (var i = 0; i < len; i++) {        
//     if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) { 
//         // 全角    
//         reLen += 2; 
//     } else { 
//         reLen++; 
//     } 
// } 
// return reLen;    
//}

//
//public static String bSubstring(String s, int length) throws Exception
//	{
//
//	
//	   if(s == null || s.equals("")){
//		   return "";
//	   }
//
//		byte[] bytes = s.getBytes("Unicode");
//		
//		int n = 0; // 暗示当前的字节数
//		int i = 2; // 要进取的字节数，年夜第3个字节起头 
//		for (; i < bytes.length && n < length; i++)
//		{
//			// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
//			if (i % 2 == 1)
//			{
//				n++; // 在UCS2第二个字节时n加1
//			}
//			else
//			{
//				// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
//				if (bytes[i] != 0)
//				{
//					n++;
//				}
//			}
//		}
//		
////		int k = 0;
////        byte[] blankBytes = " ".getBytes("Unicode");
////        
////		for(;length > bytes.length && k < length; k++ ){
////			
////			byte[] byte_3 = new byte[blankBytes.length+bytes.length];  
////	        System.arraycopy(bytes, 0, byte_3, 0, bytes.length);  
//// 	        System.arraycopy(blankBytes, 0, byte_3, bytes.length, blankBytes.length);  
//// 
////		}
////		
////		if(length == bytes.length){
////			System.arraycopy(blankBytes, 0, bytes, bytes.length, blankBytes.length);
////		   return 	new String(bytes, 0, k, "Unicode");
////		   
////		}
//
//		// 如不美观i为奇数时，措置成偶数
//		if (i % 2 == 1)
//		{
//			// 该UCS2字符是汉字时，去失踪这个截一半的汉字
//			if (bytes[i - 1] != 0){
//				i = i - 1;
//			}
//			// 该UCS2字符是字母或数字，则保留该字符
//			else{
//				i = i + 1;
//			}
//		}
//		
//		String returnMsg = new String(bytes, 0, i, "Unicode");
////		char a = '\u0020';
//		 
//		while(returnMsg.getBytes().length < length){
//			
//			returnMsg = returnMsg +" ";
//		}
//		return returnMsg;
//	}

//半角转全角  
	public static String BtoQ(String input){  
		
		char c[] = input.toCharArray();  
		
	    for ( int i=0; i<c.length;i++ ) {  
			if (c[i] ==' ') {  
			c[i] = '\u3000';  
			
		} else if (c[i]<'\177') {  
				c[i]= (char) (c[i]+65248);  
		
		   }  
		}  
		    return new String(c);  
	} 

	private static String subStr(String str, int max){  
	    int sum = 0;  
	    if(str!=null && str.length()> max){  
	        StringBuilder sb = new StringBuilder(max);  
	        for (int i = 0; i < str.length(); i++) {  
	            int c = str.charAt(i);  
	            if((c & 0xff00) != 0)  
	                sum+=2;  
	            else  
	                sum+=1;  
	            if(sum<=max)  
	                sb.append((char)c);  
	            else  
	                break;  
	        }  
	        return sb.append("...").toString();  
	    }else  
	        return str!=null ? str : "";  
	} 
	
public static int getStrLength(String str) {
	int len = str.length();
	int sum = 0;  
	 for (int i = 0; i <len; i++) {        
        int c = str.charAt(i);  
        if((c & 0xff00) != 0) {
        	sum+=2;  
        }else {
        	sum+=1;  
        }
	} 
	return sum;
}
public static String bSubstring4(String s,int k) throws Exception{
	   if(s == null || s.equals("")){
		   return "";
	   }
	   if(getStrLength(s)>k){
		   s=s.substring(0,k);
	   }
	  
	   int j=getStrLength(s);
	   
	   for(int i=0;i<k-j;i++){
		   s=s+" ";
	   }
	   	byte[] bytes = s.getBytes("Unicode");
		
		
		
		String returnMsg = new String(bytes, 0, s.getBytes("Unicode").length, "Unicode");

		return returnMsg;
	
}
	
	public static void main(String[] args) throws Exception {
		int str1 = getStrLength("a啊3");
		System.out.println(str1);
		String str2 = bSubstring4("a啊3",2);
		System.out.println(str2);
	}
}
