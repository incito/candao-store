package com.candao.www.dataserver.util;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;

/**
 * Created by lenovo on 2016/4/5.
 */
public class StringUtil {
	public static final Charset GBK = Charset.forName("GBK");
	public static final Charset UTF8 = Charset.forName("UTF-8");

	public static boolean isEmpty(Object obj) {
		return null == obj || obj.toString().isEmpty();
	}

	public static String clean(String str) {
		if (null == str) {
			return null;
		}
		return str.trim();
	}

	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}
		Pattern p = Pattern.compile("^\\d+$");
		return p.matcher(str).matches();
	}

	public static boolean isMobile(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return isNumber(str) && str.length() == 11;
	}

	public static boolean isAmount(String str) {
		if (isEmpty(str)) {
			return false;
		}
		Pattern p = Pattern.compile("^\\d*[.]?\\d{0,2}$");
		return p.matcher(str).matches();
	}

	public static String string2Unicode(String string) {

//		StringBuffer unicode = new StringBuffer();
//
//		for (int i = 0; i < string.length(); i++) {
//
//			// 取出每一个字符
//			char c = string.charAt(i);
//
//			// 转换为unicode
//			unicode.append("\\u" + Integer.toHexString(c));
//		}
//
//		return unicode.toString();
		return string;
	}

	public static String unicodeTOUtf8(String src) {
		StringBuilder res = new StringBuilder();
		if (src.contains("\\u")) {
			String[] bb = StringUtils.delimitedListToStringArray(src, "\\u");
			bb = (String[]) ArrayUtils.remove(bb, 0);
			for (int i = 0; i < bb.length; i++) {
				char a = (char) Integer.parseInt(bb[i], 16);
				res.append(new Character(a).toString());
			}
		}
		// ...
		String temp = res.toString();
		temp = StringUtils.replace(temp, "|", "\"");
		temp = StringUtils.replace(temp, "\"{", "{");
		temp = StringUtils.replace(temp, "}\"", "}");
		return StringUtils.replace(temp, "&quot", "\"");
	}

	/**
	 * 字符串转整数，转换失败返回默认值
	 *
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static int str2Int(String str, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int str2Int(Object obj, int defaultValue) {
		try {
			return Integer.parseInt(toString(obj));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String toString(Object obj) {
		return null == obj ? "" : obj.toString();
	}

	/**
	 * 字符串转浮点数，转换失败返回默认值
	 *
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static float str2Float(String str, float defaultValue) {
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static float str2Float(Object obj, float defaultValue) {
		try {
			return Float.parseFloat(toString(obj));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static void main(String[] args) {
		String requestURI = "//11/2/1/////";
		String[] uris = requestURI.split("/", -1);
		System.out.println(Arrays.toString(uris));
		// int i = 0;
		// int len = uris.length;
		// if (uris[0].isEmpty()) {
		// i++;
		// }
		// if(uris[uris.length-1].isEmpty()){
		// len--;
		// }
		// StringBuilder newUri = new StringBuilder();
		// for (; i < len; i++) {
		// String uri = uris[i];
		// if (uri.isEmpty()) {
		// newUri.append("/ ");
		// } else {
		// newUri.append("/").append(uri);
		// }
		// }
		// if(len<uris.length){
		// newUri.append("/");
		// }
		// System.out.println(newUri.toString());
	}
}
