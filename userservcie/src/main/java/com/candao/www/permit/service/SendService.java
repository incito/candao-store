/**
 * 发送账户服务
 */
package com.candao.www.permit.service;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * @author zhao
 *
 */
public interface SendService {
  /**
   * 通过邮件发送账户密码
   * @param recipient
   * @param account
 * @throws IOException 
 * @throws MessagingException 
 * @throws AddressException 
   */
  void sendAccountByMail(String recipient, String account) throws IOException, AddressException, MessagingException;
  /**
   * 通过短信发送账户密码
   * @param telephone
   * @param account
 * @throws IOException 
   */
  void sendAccountBySms(String telephone, String account) throws IOException;
  /**
   *  通过邮件发送验证码
   * @param paramMap 参数
 * @throws IOException 
 * @throws MessagingException 
 * @throws AddressException 
   */
  void sendValicodeByMail(String email, String valicode) throws IOException, AddressException, MessagingException;
  /**
   *  通过短信发送验证码
   * @param paramMap 参数
 * @throws IOException 
   */
  void sendValicodeBySms(String mobile, String valicode) throws IOException;
  /**
   *  通过邮件发送  找回密码  验证码
   * @param paramMap 参数
 * @throws IOException 
 * @throws MessagingException 
 * @throws AddressException 
   */
  void sendRetrievePwdValicodeByMail(String email, String valicode) throws IOException, AddressException, MessagingException;
  /**
   *  通过短信发送  找回密码  验证码
   * @param paramMap 参数
 * @throws IOException 
   */
  void sendRetrievePwdValicodeBySms(String mobile, String valicode) throws IOException;
  /**
   * 
   * @Description:通过短信发送信息
   * @create: 余城序
   * @Modification:
   * @param mobile 手机号码
   * @param msgUrl 模板路径
   * @param message 模板信息变量
   * void
   * @throws IOException 
   */
  public void sendMessageBySms(String mobile,String msgUrl,Map<String,String> message) throws IOException;
}
