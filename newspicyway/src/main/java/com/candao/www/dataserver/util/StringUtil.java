package com.candao.www.dataserver.util;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

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

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
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

    public static void main(String[] args) {
        String a = "{\"Data\":\"1\",\"Info\":\"现金金额\"}";
        System.out.println(new String(a.getBytes(), GBK));
    }
}
