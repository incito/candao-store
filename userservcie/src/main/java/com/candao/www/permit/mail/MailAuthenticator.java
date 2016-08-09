/**
 * 邮箱服务器登录验证
 */
package com.candao.www.permit.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author zhao
 *
 */
public class MailAuthenticator extends Authenticator {
  /**
   * 用户名（登录邮箱）
   */
  private String userName;
  /**
   * 密码
   */
  private String password;

  /**
   * 初始化邮箱和密码
   * @param userName 邮箱账号
   * @param password 密码
   */
  public MailAuthenticator(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  String getPassword() {
    return password;
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(userName, password);
  }

  String getUsername() {
    return userName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUsername(String userName) {
    this.userName = userName;
  }
}
