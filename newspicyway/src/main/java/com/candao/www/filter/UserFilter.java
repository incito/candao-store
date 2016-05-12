package com.candao.www.filter;

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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.candao.common.utils.ProjectPath;
import com.candao.www.constant.Constant;
public class UserFilter implements Filter {
	private String[] excludedArray;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		request.setCharacterEncoding("utf-8");
		/**
		 * 如果处理HTTP请求，并且需要访问诸如getHeader或getCookies等在ServletRequest中
		 * 无法得到的方法，就要把此request对象构造成HttpServletRequest
		 */
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String currentURL = request.getRequestURI(); // 取得根目录所对应的绝对路径:
		String targetURL = currentURL.substring(currentURL.indexOf("/", 1)); // 截取到当前文件名用于比较
		/*不过滤排除的URL*/
		if (null != excludedArray) {
			for (String excluded : excludedArray) {
				if (currentURL.startsWith(excluded)) {
					chain.doFilter(request, response);
					return;
				}
			}
		}
		// 若存在会话则返回该会话,否则返回NULL
		HttpSession session = request.getSession(false);

		if (currentURL.indexOf(".mp4") > 0 ||currentURL.indexOf(".jpg") > 0 || currentURL.indexOf(".bmp") > 0 || currentURL.indexOf(".gif") > 0 || currentURL.indexOf(".css") > 0 || ".js".equals(StringUtils.right(currentURL, 3)) || currentURL.indexOf("index.jsp") > 0|| currentURL.indexOf("login.jsp") > 0 || currentURL.indexOf(".png") > 0) {
			chain.doFilter(request, response);
			return;
		}

		if (!"/".equals(StringUtils.left(targetURL, 6))) {
			if (!"/login".equals(StringUtils.left(targetURL, 6)) && ! targetURL.startsWith("/padinterface")&& ! targetURL.startsWith("/bankinterface")&& ! targetURL.startsWith("/member")&& ! targetURL.startsWith("/weixin")&& ! targetURL.startsWith("/psi")
					&&!targetURL.startsWith("/t_user/retrievePwd")&&!targetURL.startsWith("/t_user/retrievePwdEmailValicode") &&!targetURL.startsWith("/tenant/") &&!targetURL.startsWith("/client")
					&&!targetURL.startsWith("/social")&&!targetURL.startsWith("/tip")&&!targetURL.startsWith("/managerWatch")&&!targetURL.startsWith("/cache")) {
				if (session == null || session.getAttribute(Constant.CURRENT_USER) == null) {
					response.reset();
					response.setContentType("text/html; charset=utf-8"); // 设置编码格式要在创建PrintWriter对象之前.不然不能生效
					PrintWriter pw = response.getWriter();
					pw.write("<script language='javascript'>window.location.href='" + ProjectPath.getAbsPath(request) + "login/login'</script>");
					pw.flush(); 
					pw.close();
					return;
				}
			}
		}
		// 加入filter链继续向下执行 */
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String excluded = filterConfig.getInitParameter("excluded");
		if (!org.springframework.util.StringUtils.isEmpty(excluded)) {
			excludedArray = excluded.split(",");
		}
	}
 
}
