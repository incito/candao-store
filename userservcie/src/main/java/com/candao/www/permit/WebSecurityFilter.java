package com.candao.www.permit;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.Constant;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.utils.SessionUtils;


/**
 * @author zhao 系统资源权限过滤器
 */
public class WebSecurityFilter extends SecurityFilter {

  /**
   * 权限过滤
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String path = req.getServletPath();

    // 1，白名单判断
    if (!path.matches(super.anonUrls)) {
      // 2，登录判断
      Object so = req.getSession().getAttribute(Constant.CURRENT_USER);
      // 跳转到首页
      if (so == null || so.equals("")) {
        forword(req, res, req.getContextPath() + "/login/login");
        return;
      } else {
        // 通过组件uri 来做组件权限判断，不做参数级的权限过滤
        // 3，角色权限判断
        // 弹出没有权限的提示消息。
        //HashSet<String> userPrivilege = (HashSet<String>) SessionUtils.get(Constant.ALLOW_ACCESS_BUTTONS);
    	HttpServletRequest  http_request=(HttpServletRequest)request;
    	HashSet<String> userPrivilege = (HashSet<String>) http_request.getSession().getAttribute(Constant.ALLOW_ACCESS_BUTTONS);
          
    	String[] paths = path.split("\\?");
    	//此处判断逻辑暂且修改成遍历循环，性能问题待定
    	boolean isOk = false;
    	if(userPrivilege!=null){
    		for(String userUrl : userPrivilege){
    			if(userUrl!=null&&!"".equals(userUrl)){
    				if(paths[0].startsWith(userUrl)||paths[0].startsWith("/"+userUrl)){
            			isOk = true;
            			break;
            		}
    			}
        	}
    	}
//        if (userPrivilege == null || !userPrivilege.contains(paths[0])) {// 如果当前用户没有访问此资源的权限
//          alertSecurityError(req, res,path);
//          return;
//        }
    	if(!isOk) {// 如果当前用户没有访问此资源的权限
          alertSecurityError(req, res,path);
          return;
        }
      }
    }
    chain.doFilter(request, response);
  }

  /**
   * 跳转到没有权限提示页面，在当前页面跳转。
   * @param req
   * @param res
   * @throws IOException
   */
  private void alertSecurityError(HttpServletRequest req, HttpServletResponse res,String url) throws IOException{
    String requestType = req.getHeader("X-Requested-With");
    String contentType = req.getHeader("Accept");
    //判断是否为ajax请求，如果为ajax请求并且请求格式为json，直接在当前请求返回错误数据；否则跳转到错误页面
    if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest") && contentType !=null && contentType.contains("application/json")) {
      res.setCharacterEncoding("UTF-8");
      PrintWriter out = res.getWriter();
      Map model = new HashMap();
      model.put("isSuccess", false);
      model.put("errorMsg", "您没有权限操作"+url+"页面，请联系管理员。");
      out.print(JacksonJsonMapper.objectToJson(model));
      return;
    } else {
      res.sendRedirect(req.getContextPath() + "/login/securityError?url="+url);
    }
  }

}
