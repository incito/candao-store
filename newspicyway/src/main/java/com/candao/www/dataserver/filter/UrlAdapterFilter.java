package com.candao.www.dataserver.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
            String[] uris = requestURI.split("/", -1);
            int i = 0;
            int len = uris.length;
            if (uris[0].isEmpty()) {
                i++;
            }
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
                    newUri.append("/").append(uri);
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
