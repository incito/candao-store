package com.candao.inorder.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

public class CommonUtil {
	private static char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	public static String getRandomChar() {
		String rand = RandomStringUtils.random(6, chr);
		return rand;
	}

	public static String getPrintFileName(String queueNo,String station) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		String str = dateFormat.format(new Date());
		StringBuffer buffer = new StringBuffer();
		buffer.append("PJ");
		buffer.append(str);
		buffer.append("_");
		buffer.append(queueNo);
		buffer.append("_");
		buffer.append(station);
		buffer.append("_");
		buffer.append(getRandomChar());
		return buffer.toString();
	}
	
	public static String yearMonthFomart(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		String str = dateFormat.format(new Date());
		return str;
	}
	public static String dateTimeFomart(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String str = dateFormat.format(new Date());
		return str;
	}
	
	public  static String getFomartGBK(String str) throws IOException{
		
		return new String(str.getBytes("ISO-8859-1"), "GBK");
	}

}
