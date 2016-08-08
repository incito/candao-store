package com.candao.www.security.controller;

import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

/**
 * 控制层基类
 * 
 * @author  zt
 * 
 */
public class BaseController {
  
  @Autowired
  protected HttpServletRequest request;
  /**
   * 获取请求参数 返回HashMap 格式
   * 
   * @return
   */
  protected HashMap<String, Object> getReqParamMap() {
 
    // 使用map接收页面表单参数信息
    HashMap<String, Object> params = new HashMap<String, Object>(request.getParameterMap());

    // 由于接收的map值 Object 内容是String[]格式，在此需要格式转换
    Set<String> keys = params.keySet();
    for (String key : keys) {
      String value = "";
      Object valueObj = params.get(key);
      if (null == valueObj) {
        value = "";
      } else if (valueObj instanceof String[]) {
        String[] values = (String[]) valueObj;
        for (int i = 0; i < values.length; i++) {
          value += values[i].trim() + ",";
        }
        value = value.substring(0, value.length() - 1);
      } else {
        value = valueObj.toString().trim();
      }
      params.put(key, value);
      
    }
    return params;
  }

  /**
   * 获取请求参数 返回HashMap 格式，主要用于包含文件上传的form表单
   * 
   * @param req request请求
   */
  protected HashMap<String, Object> getReqParamMap(HttpServletRequest req) {
    // 获取文件上传组件的request请求对象
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
    // 使用map接收页面表单参数信息
    HashMap<String, Object> params = new HashMap<String, Object>(multipartRequest.getParameterMap());
    // 由于接收的map值 Object 内容是String[]格式，在此需要格式转换
    Set<String> keys = params.keySet();
    for (String key : keys) {
      String value = "";
      Object valueObj = params.get(key);
      if (null == valueObj) {
        value = "";
      } else if (valueObj instanceof String[]) {
        String[] values = (String[]) valueObj;
        for (int i = 0; i < values.length; i++) {
          value += values[i].trim() + ",";
        }
        value = value.substring(0, value.length() - 1);
      } else {
        value = valueObj.toString().trim();
      }
      value = HtmlUtils.htmlEscape(value);
      
      params.put(key, value);
      
    }
    return params;
  }

  

}
