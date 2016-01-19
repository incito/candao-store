/**
 * 简单邮件发送器。
 */
package com.candao.www.permit.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author zhao
 *
 */
public class SimpleMailSender {
  // 发送邮件的服务器的IP和端口
  private String mailServerHost;
  private String mailServerPort = "25";
  // 登陆邮件发送服务器的用户名和密码
  private String userName;
  private String password;
  // 是否需要身份验证
  private boolean validate = false;

  private Properties props;
  
  //是否需要验证ssl
  private boolean validateSSL = false;

  /**
   * 邮件服务器登录验证
   */
  private MailAuthenticator authenticator;

  /**
   * 邮箱session
   */
  private Session session;

  public SimpleMailSender(String userName, String password) {
    // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
    String smtpHostName = "smtp." + userName.split("@")[1];
    init(userName, password, smtpHostName, null, null,null);
  }

  public SimpleMailSender(String userName, String password, String smtpHostName) {
    // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
    init(userName, password, smtpHostName, null, null,null);
  }

  public SimpleMailSender(String userName, String password, String smtpHostName, String smtpHostPort) {
    // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
    init(userName, password, smtpHostName, smtpHostPort, null,null);
  }
  
  public SimpleMailSender(String userName, String password, String smtpHostName, String smtpHostPort, String auth) {
    // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
    init(userName, password, smtpHostName, smtpHostPort, auth,null);
  }
  
  public SimpleMailSender(String userName, String password, String smtpHostName, String smtpHostPort, String auth,String isSSL) {
	// 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
	init(userName, password, smtpHostName, smtpHostPort, auth,isSSL);
  }

  /**
   * 初始化
   * 
   * @param username
   *          发送邮件的用户名(地址)
   * @param password
   *          密码
   * @param smtpHostName
   *          SMTP主机地址
   * @param smtpHostPort
   *          SMTP主机端口
   */
  private void init(String userName, String password, String smtpHostName, String smtpHostPort, String auth,String isSSL) {
    this.userName = userName;
    this.password = password;
    if (smtpHostName == null || smtpHostName.trim().length() <= 0) {
      this.mailServerHost = "smtp." + userName.split("@")[1];
    } else {
      this.mailServerHost = smtpHostName;
    }
    if (smtpHostPort != null && smtpHostPort.trim().length() > 0) {
      this.mailServerPort = smtpHostPort;
    }
    // 如果需要验证SSL,此三句代码必须在initProperties()前执行
    if (isSSL != null && isSSL.trim().length() > 0) {
      this.validateSSL = Boolean.parseBoolean(isSSL);
    }
    initProperties();
    // 如果需要身份认证，则创建一个密码验证器
    if (auth != null && auth.trim().length() > 0) {
      this.validate = Boolean.parseBoolean(auth);
    }
    if(this.validate){
      authenticator = new MailAuthenticator(userName, password);
    }
    
    // 创建session
    session = Session.getInstance(props, authenticator);
  }

  /**
   * 初始化props
   */
  private void initProperties() {
    props = new Properties();
    props.put("mail.smtp.auth", this.validate);
    props.put("mail.smtp.host", this.mailServerHost);
    props.put("mail.smtp.port", this.mailServerPort);
    props.put("mail.smtp.localhost", "localHostAdress"); //此处是为了解决在linux服务器上无法正常发送邮件，报501 Syntax: HELO hostname错误
    if(this.validateSSL){
    	props.put("mail.smtp.starttls.enable","true");
    	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
    	props.put("mail.smtp.socketFactory.fallback", "false");  
    	props.put("mail.smtp.socketFactory.port", mailServerPort);
    }
  }

  /**
   * 获得邮件会话属性
   * 
   * @return
   */
  public Properties getProperties() {
    if (props == null) {
      initProperties();
    }
    return props;
  }
  
  /**
   * 群发文本邮件
   * @param recipients
   * @param subject
   * @param content
   * @throws AddressException
   * @throws MessagingException
   */
  public void groupSendTextMail(List<String> recipients, String subject, String content)
      throws AddressException, MessagingException {
    Message mailMessage = createMailMessage(recipients, subject, content, false);
    // 发送
    Transport.send(mailMessage);
  }

  /**
   * 群发html邮件
   * @param recipients
   * @param subject
   * @param content
   * @throws AddressException
   * @throws MessagingException
   */
  public void groupSendHtmlMail(List<String> recipients, String subject, Object content)
      throws AddressException, MessagingException {
    Message mailMessage = createMailMessage(recipients, subject, content, true);
    // 发送
    Transport.send(mailMessage);
  }

  /**
   * 以文本格式发送邮件
   * 
   * @param mailInfo
   *          待发送的邮件的信息
   */
  public void sendTextMail(String recipient, String subject, String content)
      throws AddressException, MessagingException {
    List<String> recipients = new ArrayList<String>();
    recipients.add(recipient);
    Message mailMessage = createMailMessage(recipients, subject, content, false);
    // 发送邮件
    Transport.send(mailMessage);
  }

  /**
   * 以HTML格式发送邮件
   * 
   * @param recipient
   * @param subject
   * @param content
   * @throws AddressException
   * @throws MessagingException
   */
  public void sendHtmlMail(String recipient, String subject, Object content)
      throws AddressException, MessagingException {
    List<String> recipients = new ArrayList<String>();
    recipients.add(recipient);
    Message mailMessage = createMailMessage(recipients, subject, content, true);
    // 发送邮件
    Transport.send(mailMessage);
  }

  private Message createMailMessage(List<String> recipients, String subject, Object content,
      boolean isHtmlMail) throws MessagingException {
    if (recipients != null && recipients.size() <= 0) {
      throw new AddressException("没有收件人信息。");
    }
    // 根据session创建一个邮件消息
    Message mailMessage = new MimeMessage(this.session);
    // 创建邮件发送者地址
    Address from = new InternetAddress(this.userName);
    // 设置邮件消息的发送者
    mailMessage.setFrom(from);
    // 创建邮件的接收者地址，并设置到邮件消息中
    InternetAddress[] addresses = new InternetAddress[recipients.size()];
    for (int i = 0; i < recipients.size(); i++) {
      addresses[i] = new InternetAddress(recipients.get(i));
    }
    mailMessage.setRecipients(RecipientType.TO, addresses);
    // 设置邮件消息的主题
    mailMessage.setSubject(subject);
    // 设置邮件消息发送的时间
    mailMessage.setSentDate(new Date());

    // 设置邮件内容
    if (isHtmlMail) { // html邮件
      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
      Multipart mainPart = new MimeMultipart();
      // 创建一个包含HTML内容的MimeBodyPart
      BodyPart html = new MimeBodyPart();
      html.setContent(content, "text/html; charset=utf-8");
      mainPart.addBodyPart(html);
      // 将MiniMultipart对象设置为邮件内容
      mailMessage.setContent(mainPart);
    } else { // 文本邮件
      mailMessage.setText((String) content);
    }

    return mailMessage;
  }
  
  public static void main(String[] args){
    SimpleMailSender sender = new SimpleMailSender("zhaozheng@incito.com.cn", "Bean12425");
    try {
      sender.sendTextMail("zhaozheng@incito.com.cn", "测试", "试一试");
    } catch (MessagingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
