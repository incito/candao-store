package com.candao.www.permit;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 安全过滤器父类
 * 
 */
public class SecurityFilter implements Filter {
  public static String anonUrls; // 白名单规则 配置多个用|隔开

  /**
   * 参数初始化
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    anonUrls = filterConfig.getInitParameter("anonUrls");
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
  }

  /**
   * 页面跳转 由于Web端 可能 使用iframe嵌套, 因此直接重定向到登录页面并不能总是完成地很完美, 比如HTTP请求来自 iframe对象的时候,
   * 只能让iframe加载到index.html, 体验不够好; 所以在这里将直接重定向改为向
   * 页面输出一段JS代码来实现使顶部window跳转到默认的登录页面.
   * httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.html");
   */
  public void forword(HttpServletRequest req, HttpServletResponse res,
      String url) throws IOException {
    /**
     * 当HTTP请求来自AJAX并且用户的Session已经超时时, 这时页面会没有任何反应, 因为向AJAX请求
     * 重定向或者输出一段JS实现跳转都是无法完成的. 因此这里实现当上述情况发生时, 向AJAX请求返 回特定的标识(添加到响应对象中),
     * 可以在定义AJAX请求完成回调方法时得到这个标识, 进而提示 用户并完成JS跳转.
     */
    String requestType = req.getHeader("X-Requested-With");
      PrintWriter out = res.getWriter();
      out.print("<!DOCTYPE html>");
      out.print("<html>");
      out.print("<script> ");
      out.print("var p=window;while(p!=p.parent){p=p.parent; } p.location.href='"
          + url + "';");
      out.print("</script>");
      out.print("</html>");
  }
  
  /**
   * 系统关闭时的资源释放操作
   */
  public void destroy() {

  }
}
