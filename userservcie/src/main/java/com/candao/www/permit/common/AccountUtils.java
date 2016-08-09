/**
 * 
 */
package com.candao.www.permit.common;

import java.util.Random;

import com.candao.common.utils.IdentifierUtils;

/**
 * @author zhao
 *
 */
public class AccountUtils {
	
	/**
	 * 默认密码 123456，存入数据库中，需要进行MD5加密。
	 */
	public static final  String  DEFAULT_PASSWORD="123456";
	
  /**
   * 返回32位uuid
   * 
   * @return
   */
  public static String getId() {
    return IdentifierUtils.getId().generate().toString().replaceAll("-", "");
  }

  /**
   * 返回6位随机数密码
   * 
   * @return
   */
  public static String getRandomPassword() {
    return getRandomPassword(6);
  }

  /**
   * 返回指定长度的随机数密码
   * 
   * @param length
   * @return
   */
  public static String getRandomPassword(int length) {
    Random rm = new Random();
    // 获得随机数
    double pross = (1 + rm.nextDouble()) * Math.pow(10, length);
    // 将获得的获得随机数转化为字符串
    String fixLenthString = String.valueOf(pross);
    // 返回固定的长度的随机数
    return fixLenthString.substring(1, length + 1);
  }

  public static void main(String[] args) {
    for (int i = 0; i < 15; i++) {
      System.out.println(getRandomPassword());
    }
  }
}
