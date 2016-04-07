package com.candao.www.dataserver.util;

/**
 * Created by lenovo on 2016/4/5.
 */
public class StringUtil {
    public static boolean isEmpty(Object obj) {
        return null == obj || obj.toString().isEmpty();
    }
    public static String clean(String str){
        if(null==str){
            return null;
        }
        return str.trim();
    }
}
