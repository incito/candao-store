/**
 * 
 */
package com.candao.www.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * @author zhao
 *
 */
public class ThreeDesUtils {
  private static final String Algorithm = "DESede"; // 定义 加密算法,可用DES,DESede,Blowfish

  /**
   * 
   * @param 加密密钥，长度为24字节
   * @param src 被加密的数据缓冲区（源）
   * @return
   */
  public static byte[] encryptMode(byte[] keybyte, byte[] src) {
    try {
      // 生成密钥
      SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

      // 加密
      Cipher c1 = Cipher.getInstance(Algorithm);
      c1.init(Cipher.ENCRYPT_MODE, deskey);
      return c1.doFinal(src);
    } catch (java.security.NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    } catch (javax.crypto.NoSuchPaddingException e2) {
      e2.printStackTrace();
    } catch (java.lang.Exception e3) {
      e3.printStackTrace();
    }
    return null;
  }
  
  /**
   * 
   * @param 加密密钥，长度为24字节
   * @param src 被加密的数据缓冲区（源）
   * @return
   */
  public static String encryptMode(byte[] keybyte, String src) {
    if(keybyte == null || src == null){
      return null;
    }
    try {
      byte[] temp = encryptMode(keybyte, src.getBytes("UTF-8"));
      if(temp != null){
          return new String(temp, "UTF-8");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 
   * @param keybyte 加密密钥，长度为24字节
   * @param src 加密后的缓冲区
   * @return
   */
  public static byte[] decryptMode(byte[] keybyte, byte[] src) {
    try {
      // 生成密钥
      SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

      // 解密
      Cipher c1 = Cipher.getInstance(Algorithm);
      c1.init(Cipher.DECRYPT_MODE, deskey);
      return c1.doFinal(src);
    } catch (java.security.NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    } catch (javax.crypto.NoSuchPaddingException e2) {
      e2.printStackTrace();
    } catch (java.lang.Exception e3) {
      e3.printStackTrace();
    }
    return null;
  }
  
  /**
   * 
   * @param keybyte 加密密钥，长度为24字节
   * @param src 加密后的缓冲区
   * @return
   */
  public static String decryptMode(byte[] keybyte, String src) {
    if(keybyte == null || src == null){
      return null;
    }
    try {
      byte[] temp = decryptMode(keybyte, src.getBytes("UTF-8"));
      if(temp != null){
          return new String(temp, "UTF-8");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  // 转换成十六进制字符串
  public static String byte2hex(byte[] b) {
    String hs = "";
    String stmp = "";

    for (int n = 0; n < b.length; n++) {
      stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else
        hs = hs + stmp;
      if (n < b.length - 1)
        hs = hs + ":";
    }
    return hs.toUpperCase();
  }
  
}
