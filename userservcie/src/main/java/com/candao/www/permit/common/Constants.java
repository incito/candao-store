package com.candao.www.permit.common;

public class Constants {
  
  public static final String DEFAULT_PWD = "123456";
  
  public static final String ENABLE = "01"; //启用
  public static final String DISABLE = "02"; //停用
  
  public static final String BUSINESS = "01"; //企业
  public static final String HEADQUARTER_USER = "02"; //总店用户
  public static final String EMPLOYEE = "03"; //门店员工
  /**
   * 超级管理员
   */
  public static final String SUPER_ADMIN="10"; 
  
  /**
   * 系统管理员
   */
  public static final String SYSTEM_ADMIN="11";
  
  
  public static final String FIRST_BUSINESS_ACCOUNT = "100010"; //企业账户起始号
  
  //加密密钥
  public static final byte[] KEY_BYTES = {0x31, 0x22, 0x4F, 0x55, (byte)0x88, 0x10, 0x40, 0x38
      , 0x28, 0x25, 0x79, 0x51, (byte)0xCB, (byte)0xDD, 0x55, 0x66
      , 0x77, 0x29, 0x74, (byte)0x98, 0x30, 0x40, 0x36, (byte)0xE2};    //24字节的密钥;
}
