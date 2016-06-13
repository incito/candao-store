package com.candao.www.printer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 打印机连接管理
 * Created by liaoy on 2016/6/12.
 */
class PrinterConnector {
    private static Logger logger = LoggerFactory.getLogger(PrinterConnector.class);
    private static EventLoopGroup group = new NioEventLoopGroup();
    private static Bootstrap bootstrap = new Bootstrap();
    static{
        bootstrap.group(group)
                .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ByteArrayEncoder());
                ch.pipeline().addLast(new ByteArrayDecoder());
                ch.pipeline().addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
                ch.pipeline().addLast(new PrinterHandler());
            }
        });
    }
    public static Channel createConnection(String host, int port) {
        //发起异步链接操作
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            logger.error("创建连接失败[" + host + ":" + port + "]", e);
        } catch (Exception e) {
            logger.error("创建连接失败[" + host + ":" + port + "]", e);
        }
        return null == channelFuture ? null : channelFuture.channel();
    }

    public static void main(String[] args) {
        Channel connection = createConnection("10.66.18.4", 9100);
        System.out.println(connection);
    }
}
