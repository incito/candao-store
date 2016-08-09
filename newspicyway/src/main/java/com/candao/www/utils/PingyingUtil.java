package com.candao.www.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PingyingUtil {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    System.out.println(getStringPinYin("中国人民重单"));
    System.out.println(getStringPinYin("中2国@#$人5民重单a1"));
    
    System.out.println(getPinYinHeadChar("中国人民重单"));
    System.out.println(getCharacterPinYin('单'));
  } 
  private static final String separator="(^i^)";
  public PingyingUtil() {
  }

 
  /**
   * 转换一个字符串（汉字全拼）（非汉字照原样输出）
   * @param str
   * @return
   */
  public static String getStringPinYin(String str) {
    StringBuilder sb = new StringBuilder();
    String tempPinyin = null;
    for (int i = 0; i < str.length(); ++i) {
      tempPinyin = getCharacterPinYin(str.charAt(i));
      if (tempPinyin == null) {
        // 如果str.charAt(i)非汉字，则保持原样
        sb.append(str.charAt(i));
      } else {
        sb.append(tempPinyin);
      }
    }
    return sb.toString();
  }

  /**
   * 转换单个字符 （非汉字照原样输出）
   * @param c
   * @return
   */
  public static String getCharacterPinYin(char c) {
    String[] pinyin = null;
    try {
      HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
      format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
    } catch (BadHanyuPinyinOutputFormatCombination e) {
      e.printStackTrace();
    }
    // 如果c不是汉字，toHanyuPinyinStringArray会返回null
    if (pinyin == null)
      return null;
    // 只取一个发音，如果是多音字，仅取第一个发音
    return pinyin[0];
  }

  /**
   * 获得汉字的首字母（非汉字照原样输出）
   * @param str
   * @return
   */
  public static String getPinYinHeadChar(String str) {
    String convert = "";
    for (int j = 0; j < str.length(); j++) {
      char word = str.charAt(j);
      // 提取汉字的首字母
      String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
      if (pinyinArray != null) {
        convert += pinyinArray[0].charAt(0);
      } else {
        convert += word;
      }
    }
    return convert;
  }

}
