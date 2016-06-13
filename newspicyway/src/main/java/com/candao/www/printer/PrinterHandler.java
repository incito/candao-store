package com.candao.www.printer;

import com.candao.print.entity.PrinterConstant;
import com.candao.www.utils.ToolsUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by liaoy on 2016/6/12.
 */
public class PrinterHandler extends ChannelHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(PrinterHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("active:"+Thread.currentThread().getName());
//        Channel channel = ctx.channel();
//        channel.writeAndFlush(PrinterConstant.getLineN(4));
//        channel.writeAndFlush("顶顶顶顶顶顶顶顶顶顶\r\n".getBytes("GBK"));
//        channel.writeAndFlush(PrinterConstant.AUTO_STATUS);
//        channel.writeAndFlush(PrinterConstant.getLineN(4));
//        channel.writeAndFlush(PrinterConstant.CUT);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] b=(byte[])msg;
        System.out.println(Thread.currentThread().getName()+ Arrays.toString(b));
        String ipAddress = getIpAddress(ctx.channel());
        Printer printer = PrinterManager.getPrinter(ipAddress);
        if(null!=printer){
            printer.doOperation(ToolsUtil.byte2int(b));
        }

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
                channel.writeAndFlush(PrinterConstant.AUTO_STATUS);
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
