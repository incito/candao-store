package com.candao.www.printer;

import com.candao.print.entity.PrinterConstant;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印机实例
 * Created by liaoy on 2016/6/12.
 */
public class Printer {
    /**
     * 打印机响应超时时间 单位秒
     */
    private static final long SEND_TIMEOUT = 3;
    /**
     * 打印机标示，默认为ip
     */
    private String key;
    private String ip;
    private int port;
    private Channel channel;
    private Lock printLock = new ReentrantLock();
    private Condition printCondition = printLock.newCondition();
    private Printer[] backPrinters;

    /**
     * 打印方法，阻塞式，打印完成时返回
     *
     * @param msg
     * @return
     */
    public PrintResult print(String msg) {
        if (null == msg) {
            msg = "";
        }
        PrintResult result = new PrintResult();
        printLock.lock();
        try {
            while (true) {
                initChannel();
                if (null != channel && channel.isActive()) {
                    channel.writeAndFlush(PrinterConstant.AUTO_STATUS);
                    channel.write(new byte[]{27});
//                    try {
                        channel.writeAndFlush(new byte[]{27});
//                    channel.writeAndFlush(PrinterConstant.LINE);
//                    channel.writeAndFlush(PrinterConstant.LINE);
                        channel.writeAndFlush("第1行\r\n".getBytes(Charset.forName("GBK")));
                        channel.writeAndFlush("第2行\r\n".getBytes(Charset.forName("GBK")));
                        channel.writeAndFlush("第3行\r\n".getBytes(Charset.forName("GBK")));
                        channel.writeAndFlush("第4行\r\n".getBytes(Charset.forName("GBK"))).addListener(new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception {
                                channel.write(PrinterConstant.getLineN(4));
                                channel.writeAndFlush(new byte[]{10});
                                channel.writeAndFlush(PrinterConstant.CUT);
                            }
                        });

//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        printCondition.await();
                    break;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                } else {
                    // TODO: 2016/6/12 调用备用打印机
                }
            }
        } finally {
            printLock.unlock();
        }
        return result;
    }

    /**
     * 尝试打印，阻塞式，打印完成或等待超时后返回
     *
     * @param msg
     * @param time 超时时间 单位秒
     * @return
     */
    public PrintResult tryPrint(String msg, long time) {
        return null;
    }

    private void initChannel() {
        if (null == channel) {
            synchronized (this) {
                if (null == channel) {
                    channel = PrinterConnector.createConnection(ip, port);
                }
            }
        }
    }

    /**
     * 处理自动状态返回
     */
    public void doOperation(int code) {
        printLock.lock();
        try {
            int ret = checkCode(code);
            switch (ret) {
                case PrinterStatus.PRINTING:
                    break;
                case PrinterStatus.FINISHED:
                    printCondition.signal();
                    break;
                case PrinterStatus.FAILED:
                    // TODO: 2016/6/12 调用备用打印机
            }
        } finally {
            printLock.unlock();
        }
    }

    private int checkCode(int code) {
        return 0;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
