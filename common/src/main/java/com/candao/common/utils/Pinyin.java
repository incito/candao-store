/**
 * 
 */
package com.candao.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @author zhao
 *
 */
public class Pinyin {
  /**
   * 提取每个汉字的首字母(大写)
   * 
   * @param str
   * @return
   */
   public static String getPinYinHeadChar(String str) {
       if (isNull(str)) {
           return "";
       }
       String convert = "";
       for (int j = 0; j < str.length(); j++) {
           char word = str.charAt(j);
           // 提取汉字的首字母
           String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
           if (pinyinArray != null) {
               convert += pinyinArray[0].charAt(0);
           }
           else {
               convert += word;
           }
       }

       convert = string2AllTrim(convert);
       return convert.toUpperCase();
   }

   /*
   * 判断字符串是否为空
   */

   public static boolean isNull(Object strData) {
       if (strData == null || String.valueOf(strData).trim().equals("")) {
           return true;
       }
       return false;
   }

   /**
   * 去掉字符串包含的所有空格
   * 
   * @param value
   * @return
   */
   public static String string2AllTrim(String value) {
       if (isNull(value)) {
           return "";
       }
       return value.trim().replace(" ", "");
   }

   public static void main(String[] args) {
       String ss = Pinyin.getPinYinHeadChar("在项目中需要提取每个汉字的首字母200，下面是工具类的源码d：");
       System.out.print(ss);//ZG
   }
}
