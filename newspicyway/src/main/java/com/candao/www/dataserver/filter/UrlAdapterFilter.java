package com.candao.www.dataserver.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by lenovo on 2016/4/15.
 */
public class UrlAdapterFilter implements Filter {
    private String prex;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        prex = filterConfig.getInitParameter("prex");
        if (null == prex) {
            prex = "";
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        if (null != requestURI) {
            requestURI = URLDecoder.decode(requestURI, "UTF-8");
            String[] uris = requestURI.split("/", -1);
            int i = 0;
            int len = uris.length;
            if (uris[0].isEmpty()) {
                i++;
            }
            //最终新组成的URL排除掉项目前缀（工程名）
            if (uris[1].equals(prex)) {
                i++;
            }
            if (uris[uris.length - 1].isEmpty()) {
                len--;
            }
            StringBuilder newUri = new StringBuilder();
            for (; i < len; i++) {
                String uri = uris[i];
                if (uri.isEmpty()) {
                    newUri.append("/ ");
                } else {
                    //某些接口（saveOrderPreferential）接口参数带有换行字符，导致url mapping映射不上
                    newUri.append("/").append(uri.replaceAll("\r\n", ""));
                }
            }
            if (len < uris.length) {
                newUri.append("/");
            }
            request.getRequestDispatcher(newUri.toString()).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    public static void main(String[] args) {
        String str = "123/1//////";
        System.out.println(str.replaceAll("//", "/_/"));
    }
}
