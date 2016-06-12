package com.candao.www.printer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by liaoy on 2016/6/12.
 */
public class PrinterHandler extends ChannelHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(PrinterHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int code=1;
        String ipAddress = getIpAddress(ctx.channel());
        Printer printer = PrinterManager.getPrinter(ipAddress);
        if(null!=printer){
            // TODO: 2016/6/12 接收到响应，调用对应printer的doOperation方法
            printer.doOperation(code);
        }
        System.out.println(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String ipAddress = getIpAddress(ctx.channel());
        logger.info("打印机[" + ipAddress + "]断线:，准备重连。");
        Printer printer = PrinterManager.getPrinter(ipAddress);
        Channel channel;
        /**
         * 循环重连直到连上
         */
        while (true) {
            logger.info("打印机[" + ipAddress + "]尝试重连");
            channel = PrinterConnector.createConnection(printer.getIp(), printer.getPort());
            if (null != channel) {
                printer.setChannel(channel);
                //发送自动状态返回命令
                channel.writeAndFlush(PrinterCommand.AUTO_STATUS);
                break;
            }
            Thread.sleep(3000);
        }
    }

    private String getIpAddress(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        InetAddress address = socketAddress.getAddress();
        return address.getHostAddress();
    }
}
