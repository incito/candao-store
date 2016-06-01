package com.candao.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



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

public static List<String> subString2(String src ,int num) throws UnsupportedEncodingException {
	List<String> res = new LinkedList<>();
	return subString3(src, num, res);
}

/**
 * 按需求宽度递归，每一行放入rse中
 * @param src
 * @param num
 * @param res
 * @return
 */
	private static List<String> subString3(String src, int num, List<String> res) {
		if (res == null)
			return null;
		String str = "";
		boolean flag = false;
		int truelength = 0;
		if (src != null && !"".equals(src)) {
			//计算真实长度
			for (int i = 0; i < src.length(); i++) {
				char c = src.charAt(i);
				if ((c & 0xff00) != 0) {
					truelength += 2;
				} else {
					truelength += 1;
				}
				if (truelength >= num) {
					//num+1说明结尾是中文字符
					if (truelength == num + 1) {
						//truelength此时标记换行点在src中的位置
						truelength = i;
						flag = true;
					} else {
						//判断是否已经遍历到src末尾
						//如果是，说明不用换行，truelength标识真实长度
						//如果否，truelength标识换行位置
						truelength = (i == src.length() - 1) ? truelength : i + 1;
					}
					break;
				}
			}
		} else {
			return res;
		}
		//1 如果不用换行，truelength代表真实长度，大于等于src长度
		//2 如果换行 truelength代表换行位置，小于src长度
		if (truelength != 0 && truelength < src.length()) {
			str = src.substring(0, truelength);
			if (flag)
				str = str.concat(" ");
			res.add(str);
			//递归处理剩余字符串
			res = subString3(src.substring(truelength), num, res);
		} else {
			str = src;
			for (int i = 0; i < num - truelength; i++) {
				str = str.concat(" ");
			}
			res.add(str);
		}

		return res;
	}

	public static String getStr(int num){
		String res = "";
		for (int i = 0; i < num; i++) {
			res +=" ";
		}
		return res;
	}
	
	public static String split2(String src , String s){
		if(src == null || src.isEmpty())
			return null;
		int i = 0;
		if((i = src.indexOf(s)) != -1){
			src = src.substring(0, i);
		}
		return src;
	}
	
	public static String split3(String src ,String s){
		if(src == null || src.isEmpty())
			return null;
		
		src = src.replace(s, "");
		return src;
	}
	
	/**
	 * 实现打印换行，默认使用unicode
	 * 
	 * @param src
	 *            需要换行的string,按照数组的顺序依次换行
	 * @param length
	 *            对应src数组中每个String要保留的大小，超过则换下一行
	 * @return
	 */
	public static String[] getLineFeedText(String[] src, Integer[] length) throws Exception {
		if (src == null || length == null || src.length > length.length) {
			return null;
		}
		// 换行
		List<List<String>> dst = new ArrayList<>();
		for (int i = 0; i < src.length; i++) {
			dst.add(StringUtils.subString2(StringUtils.bSubstring3(src[i], src[i].length()), length[i]));
		}
		// 取最大值
		int max = 0;
		for (int i = 0; i < dst.size(); i++) {
			if (dst.get(i).size() > max)
				max = dst.get(i).size();
		}
		// 结果集
		String[] res = new String[max];
		StringBuffer buffer = new StringBuffer();
		// 拼装
		for (int i = 0; i < max; i++) {
			buffer.setLength(0);
			for (int j = 0; j < dst.size(); j++) {
				List<String> strBuffer = dst.get(j);
				String text = (i + 1 > strBuffer.size() ? StringUtils.getStr(length[j].intValue()) : strBuffer.get(i));
				buffer.append(text).append(" ");
			}
			res[i] = buffer.toString();
		}
		return res;
	}
	
	/**
	 * 将src按delimiter和指定
	 * size递归分割
	 * @param src
	 * @param delimiter
	 * @param num
	 * @return
	 */
	public static List<String> split(String src, String delimiter, int size) {
		if (src == null || src.isEmpty()) {
			return null;
		}
		List<String> res = new LinkedList<>();
		List<Integer> indexes = split(src, delimiter, size, null, 0);
		if (indexes == null) {
			res.add(src);
			return res;
		}
		int fromIndex = 0;
		for (Integer integer : indexes) {
			res.add(src.substring(fromIndex, integer));
			fromIndex = integer;
		}
		return res;
	}
	
	/**
	 * 返回src中delimiter的position
	 * @param src
	 * @param delimiter分隔符
	 * @param size分页数
	 * @param res
	 * @param fromIndex偏移量
	 * @return
	 */
	public static List<Integer> split(String src, String delimiter, int size, List<Integer> res, int fromIndex) {
		if (src == null || src.isEmpty())
			return null;
		if (delimiter == null || delimiter.isEmpty() || size == 0) {
			return null;
		}
		if (res == null)
			res = new ArrayList<Integer>();
		int i = fromIndex, position = 0;
		for (int j = 0; j < size; j++) {
			position = src.indexOf(delimiter, i + 1);
			if (position == -1) {
				i = src.length() - 1;
				break;
			}
			i = position;
		}
		fromIndex = i + 1;
		res.add(fromIndex);
		if ((i + 1) < src.length()) {
			split(src, delimiter, size, res, fromIndex);
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		int str1 = getStrLength("a啊3");
		System.out.println(str1);
		String str2 = bSubstring4("a啊3",2);
		System.out.println(str2);
	}
}
