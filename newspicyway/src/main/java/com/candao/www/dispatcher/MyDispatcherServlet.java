package com.candao.www.dispatcher;

import com.candao.communication.client.NettyService;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by ytq on 2016/4/28.
 */
public class MyDispatcherServlet extends DispatcherServlet {
    @Override
    public void destroy() {
        //为了防止netty服务tomcat stop时候异常
        System.out.println("#########关闭netty 连接#######");
        NettyService.off();
        super.destroy();
    }
}
