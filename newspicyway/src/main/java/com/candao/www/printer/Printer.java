package com.candao.www.printer;

import com.candao.print.entity.PrinterConstant;
import com.candao.print.utils.PrintControl;
import com.candao.www.utils.ToolsUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
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
    private static final Charset CHARSET = Charset.forName("GBK");
    /**
     * 打印机标示，默认为ip
     */
    private String key;
    private String ip;
    private int port;
    private Socket channel;
    private Lock printLock = new ReentrantLock();
    private Lock reqLock = new ReentrantLock();
    private Condition reqCondition = reqLock.newCondition();
    private byte[] result;
    private Condition printCondition = printLock.newCondition();
    private Printer[] backPrinters;

    /**
     * 打印方法，阻塞式，打印完成时返回
     *
     * @param msg
     * @return
     */
    public PrintResult print(Object[] msg) {

        if (null == msg) {
            msg = new Object[]{};
        }
        PrintResult result = new PrintResult();
        printLock.lock();
        try {
            while (true) {
                initChannel();
                if (null != channel && channel.isConnected()) {
                    OutputStream outputStream = channel.getOutputStream();
                    //检查打印机状态
                    int state = PrintControl.printerIsReady();
                    //如果打印机不可用，进入下次循环
                    if(state!=PrintControl.STATUS_OK){
                        continue;
                    }
                    /*开始打印*/
                    outputStream.write(PrinterConstant.AUTO_STATUS);
                    outputStream.write(new byte[]{27, 27});
                    for (Object o : msg) {
                        byte[] line;
                        if (o instanceof Byte) {
                            line = new byte[]{(byte) o};
                        } else if (o instanceof byte[]) {
                            line = (byte[]) o;
                        } else {
                            line = o.toString().getBytes(CHARSET);
                        }
                        outputStream.write(line);
                        outputStream.flush();
                    }
                    outputStream.write(PrinterConstant.getLineN((byte) 4));
                    outputStream.write(new byte[]{10});
                    outputStream.flush();
                    outputStream.write(PrinterConstant.CUT);
                    outputStream.flush();
                    InputStream inputStream = channel.getInputStream();
                    byte[] ret=new byte[4];
                    state = PrintControl.CheckJob();
                    switch (state){
                        case PrintControl.STATUS_PRINT_DONE:
                            result.setCode(state);
                            break;
                    }

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
        } catch (IOException e) {
            e.printStackTrace();
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
            int ret = PrintControl.ReadDeviceStatus(ToolsUtil.int2byte(code));
            System.out.println(ret);
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

    public Socket getChannel() {
        return channel;
    }

    public void setChannel(Socket channel) {
        this.channel = channel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
