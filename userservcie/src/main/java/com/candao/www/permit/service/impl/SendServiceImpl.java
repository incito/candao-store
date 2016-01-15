/**
 * 发送账户服务
 */
package com.candao.www.permit.service.impl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.MD5;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.UserDao;
import com.candao.www.permit.common.AccountUtils;
import com.candao.www.permit.common.BusinessException;
import com.candao.www.permit.mail.SimpleMailSender;
import com.candao.www.permit.service.SendService;
import com.candao.www.utils.HttpUtils;

/**
 * @author zhao
 *
 */
@Service
public class SendServiceImpl implements SendService {

	final static LoggerHelper logger = LoggerFactory.getLogger(SendServiceImpl.class);

  @Autowired
  private UserDao userDao;

  @Override
  public void sendAccountByMail(String recipient, String account) throws IOException, AddressException, MessagingException {
    //每次发送账户都需要随机设置用户密码，设置密码失败时
    String password = AccountUtils.getRandomPassword();
	if (userDao.updatePassword(account, MD5.md5(password)) > 0) {
		SimpleMailSender mailSender = this.getMailSender();
		if(mailSender!=null){
			String content = this.getTemplateContent("/template/accountMsg.template",true);
			content = content.replace("{account}", account);
			content = content.replace("{password}", password);
			mailSender.sendTextMail(recipient, "餐道云管理平台：账户信息", content);
		}
	} else {
		throw new BusinessException("账户发送失败，失败原因：密码初始化失败。");
	}
  }

  @Override
  public void sendAccountBySms(String telephone, String account) throws IOException {
	//每次发送账户都需要随机设置用户密码，设置密码失败时
    String password = AccountUtils.getRandomPassword();
	if (userDao.updatePassword(account, MD5.md5(password)) > 0) {
		Map<String,String> param = this.getSmsParam();
		if(param!=null){
			String content = this.getTemplateContent("/template/accountSms.template",false);
			String url = PropertiesUtils.getValue("sms.url");
			content = content.replace("{account}", account);
			content = content.replace("{password}", password);
			
			param.put("p", telephone);
			param.put("msg", content);
			
			HttpUtils.doPost(url, param);
		}
	} else {
		throw new BusinessException("账户发送失败，失败原因：密码初始化失败。");
	}
  }

  /**
   *  通过邮件发送验证码
   * @param paramMap 参数
 * @throws IOException 
 * @throws MessagingException 
 * @throws AddressException 
   */
  public void sendValicodeByMail(String email, String valicode) throws IOException, AddressException, MessagingException{
		SimpleMailSender  mailSender = this.getMailSender();
		if(mailSender!=null){
			String content = this.getTemplateContent("/template/valicodeMsg.template",true);
			content = content.replace("{valicode}", valicode);
			mailSender.sendTextMail(email, "餐道云管理平台：设置邮箱验证码", content);
		}
  }

  /**
   *  通过短信发送验证码
   * @param paramMap 参数
 * @throws IOException 
   */
  public void sendValicodeBySms(String mobile, String valicode) throws IOException {
	  Map<String,String> param = this.getSmsParam();
	  if(param!=null){
		String content = this.getTemplateContent("/template/valicodeSms.template",false);
	    content = content.replace("{valicode}", valicode);
	    
		param.put("p", mobile);
		param.put("msg", content);
		
		String url = PropertiesUtils.getValue("sms.url");
		HttpUtils.doPost(url, param);
	  }
  }
  
  /**
   *  通过邮件发送  找回密码  验证码
   * @param paramMap 参数
 * @throws IOException 
 * @throws MessagingException 
 * @throws AddressException 
   */
  public void sendRetrievePwdValicodeByMail(String email, String valicode) throws IOException, AddressException, MessagingException{
		SimpleMailSender  mailSender = this.getMailSender();
		if(mailSender!=null){
			String content = this.getTemplateContent("/template/verfiyCodeMsg.template",true);
			content = content.replace("{userName}", "用户");
			content = content.replace("{varfiyCode}", valicode);
			mailSender.sendTextMail(email, "餐道云管理平台：找回密码/修改绑定邮箱/修改绑定手机", content);
		}
  }
  /**
   *  通过短信发送  找回密码  验证码
   * @param paramMap 参数
 * @throws IOException 
   */
  public void sendRetrievePwdValicodeBySms(String mobile, String valicode) throws IOException{
	  Map<String,String> param = this.getSmsParam();
	  if(param!=null){
		String content = this.getTemplateContent("/template/verfiyCodeSms.template",false);
        content = content.replace("{userName}", "用户");
        content = content.replace("{varfiyCode}", valicode);
        
        
		param.put("p", mobile);
		param.put("msg", content);
		
		String url = PropertiesUtils.getValue("sms.url");
		HttpUtils.doPost(url, param);
	  }
  }
  
  /**
   * 获取邮件发送对象
   * @return
   */
  private SimpleMailSender getMailSender(){
	  String enabled = PropertiesUtils.getValue("mail.smtp.enabled");
	  if(enabled.equals("true")){
		  String mailHost = PropertiesUtils.getValue("mail.smtp.host");
		  String mailHostPort = PropertiesUtils.getValue("mail.smtp.port");
		  String isAuth = PropertiesUtils.getValue("mail.smtp.auth");
		  String adminAccout = PropertiesUtils.getValue("MailAdminAccount");
		  String adminPassword = PropertiesUtils.getValue("MailAdminPassword");
		  String isSSL = PropertiesUtils.getValue("mail.smtp.ssl");
		  SimpleMailSender mailSender = new SimpleMailSender(adminAccout, adminPassword, mailHost, mailHostPort, isAuth,isSSL);
		  return mailSender;
	  }else{
		  return null;
	  }
  }
  /**
   * 读取模版内容
   * tmpPath 模版文件路径
   * @return
   * @throws IOException 
   */
  private String getTemplateContent(String tmpPath,boolean isChangeLine) throws IOException{
	//读取模版
  	InputStream is = this.getClass().getResourceAsStream(tmpPath);
  	InputStreamReader isr = new InputStreamReader(is,"UTF-8"); //指定以UTF-8编码读入 
  	BufferedReader br = new BufferedReader(isr);
  	StringBuilder sb = new StringBuilder(); 
      String line = null;  
      while ((line = br.readLine()) != null) {  
          sb.append(line);   
          if(isChangeLine){
        	  sb.append("\n");
          }
      } 
      is.close();   
      br.close();
      return sb.toString();
  }
  /**
   * 获取邮件发送对象
   * @return
   */
  private Map<String,String> getSmsParam(){
	  String enabled = PropertiesUtils.getValue("sms.enabled");
	  if(enabled.equals("true")){
		  String username = PropertiesUtils.getValue("sms.username");
		  String password = PropertiesUtils.getValue("sms.password");
		  Map<String,String> param = new HashMap<String,String>();
		  param.put("username", username);
		  param.put("pwd", password);
		  param.put("charSetStr","utf");
		  return param;
	  }else{
		  return null;
	  }
  }
  
}
