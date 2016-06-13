package com.candao.www.printer;

import io.netty.channel.Channel;
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

    public static Socket createConnection(String host, int port) {
        Socket socket = new Socket();

        try {
            socket.connect(new InetSocketAddress("10.66.18.3", 9100), 5000);//建立连接5秒超时
            return socket;
        } catch (IOException e) {
            logger.error("创建连接失败[" + host + ":" + port + "]", e);
        }
        return null;
    }

}
