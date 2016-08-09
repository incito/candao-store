package com.candao.www.dispatcher;

import com.candao.communication.client.NettyService;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

/**
 * Created by ytq on 2016/4/28.
 */
public class MyDispatcherServlet extends DispatcherServlet {
    @Override
    public void destroy() {
        destroyNettyServer();
        //为了防止netty服务tomcat stop时候异常
        System.out.println("#########关闭netty 连接#######");
        NettyService.off();
        super.destroy();
    }

    /**
     * 退出nettyserver
     */
    private void destroyNettyServer() {
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("wmic process where name='start.exe' call terminate");
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
