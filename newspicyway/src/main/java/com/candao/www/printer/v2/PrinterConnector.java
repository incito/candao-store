package com.candao.www.printer.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 打印机连接管理
 * Created by liaoy on 2016/6/12.
 */
class PrinterConnector {
    private static Logger logger = LoggerFactory.getLogger(PrinterConnector.class);

    /**
     * 创建连接
     * @param host
     * @param port
     * @param timeout 超时时间 单位ms
     * @return
     */
    public static Socket createConnection(String host, int port,int timeout) {
        Socket socket = new Socket();

        try {
            socket.connect(new InetSocketAddress("10.66.18.3", 9100), timeout);//建立连接5秒超时
            return socket;
        } catch (IOException e) {
            logger.error("创建连接失败[" + host + ":" + port + "]", e);
        }
        return null;
    }

}
