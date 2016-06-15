package com.candao.www.printer.v2;

import com.candao.print.entity.PrinterConstant;
import com.candao.print.utils.PrintControl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印机实例
 * Created by liaoy on 2016/6/12.
 */
public class Printer {
    private static Log logger = LogFactory.getLog(Printer.class.getName());
    /**
     * 打印机响应超时时间 单位秒
     */
    private static final Charset CHARSET = Charset.forName("GBK");
    /**
     * 打印机空闲间隔，超过该时间会发起打印机状态检查
     */
    private static long checkStateInterval = 3 * 60 * 1000;
    /**
     * 打印机数据库ID
     */
    private String id;
    /**
     * 打印机标示，默认为ip
     */
    private String key;
    private String ip;
    private int port;
    private Socket channel;
    private Lock printLock = new ReentrantLock();
    private Printer backPrinter;
    /**
     * 上次打印机状态，该状态的值见{@link PrinterStatusManager PrinterStatusManager}
     */
    private int lastState;
    /**
     * 最后活跃时间
     */
    private long lastActiveTime;

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
                lastActiveTime = System.currentTimeMillis();
                try {
                    initChannel();
                    /*打印机是否连接成功*/
                    if (null != channel && channel.isConnected()) {
                        OutputStream outputStream = channel.getOutputStream();
                        InputStream inputStream = channel.getInputStream();
                        //检查打印机状态
                        logger.info("[" + ip + "]检查打印机状态");
                        int state = PrintControl.printerIsReady(3000, outputStream, inputStream);
                        PrinterStatusManager.stateMonitor(state, this);
                        logger.info("[" + ip + "]打印机状态:" + state);
                        if (state != PrintControl.STATUS_OK) {
                            logger.info("[" + ip + "]打印机不可用:" + state);
                            boolean needCallBackPrinter = needCallBackPrinter(state);
                            if (needCallBackPrinter) {
                                logger.info("[" + ip + "]尝试调用备用打印机[" + backPrinter.getIp() + "]");
                                //调用备用打印机
                                PrintResult printResult = backPrinter.tryPrint(msg, 2000);
                                //备用打印机正常打印，返回打印结果。
                                if (printResult.getCode() == PrintControl.STATUS_OK) {
                                    logger.info("[" + ip + "]尝试调用备用打印机[" + backPrinter.getIp() + "]打印成功");
                                    return printResult;
                                } else {
                                    logger.info("[" + ip + "]尝试调用备用打印机[" + backPrinter.getIp() + "]打印失败");
                                }
                            }
                            sleep(200);
                            //主打印机可恢复异常|备用打印机未正常打印，重试。
                            logger.info("[" + ip + "]重试");
                            continue;
                        }
                        /*开始打印*/
                        logger.info("[" + ip + "]开始打印");
                        doPrint(msg, outputStream);
                        /*检查打印结果*/
                        logger.info("[" + ip + "]打印结束，检查打印结果");
                        state = PrintControl.CheckJob(3000, inputStream);
                        PrinterStatusManager.stateMonitor(state, this);
                        logger.info("[" + ip + "]打印结果:" + state);
                        //打印完成则返回
                        if (state == PrintControl.STATUS_PRINT_DONE) {
                            logger.info("[" + ip + "]打印完成");
                            result.setCode(state);
                            break;
                        }
                    } else {
                        PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                        //尝试重连
                        logger.info("[" + ip + "]连接不可用，尝试重连");
                        channel = PrinterConnector.createConnection(ip, port, 2000);
                        if (null != channel) {
                            logger.info("[" + ip + "]重连成功");
                            continue;
                        }
                        logger.info("[" + ip + "]尝试重连失败");
                        //重连失败，调用备用打印机
                        if (null != backPrinter) {
                            logger.info("[" + ip + "]尝试调用备用打印机:[" + backPrinter.getIp() + "]");
                            PrintResult printResult = backPrinter.tryPrint(msg, 2);
                            //备用打印机正常打印，返回打印结果。
                            if (printResult.getCode() == PrintControl.STATUS_OK) {
                                logger.info("[" + ip + "]尝试调用备用打印机[" + backPrinter.getIp() + "]打印成功");
                                return printResult;
                            } else {
                                logger.info("[" + ip + "]尝试调用备用打印机[" + backPrinter.getIp() + "]打印失败，重试");
                            }

                        }
                    }
                } catch (IOException e) {
                    PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                    logger.info("[" + ip + "]打印发生异常，尝试重连", e);
                    channel = PrinterConnector.createConnection(ip, port, 2000);
                    if (null == channel) {
                        logger.info("[" + ip + "]尝试重连失败");
                    } else {
                        logger.info("[" + ip + "]重连成功");
                    }
                }
            }
        } finally {
            printLock.unlock();
        }
        return result;
    }

    private void doPrint(Object[] msg, OutputStream outputStream) throws IOException {
        outputStream.write(PrinterConstant.AUTO_STATUS);
        outputStream.write(new byte[]{27, 27});
        for (Object o : msg) {
            if (null == o) {
                o = "";
            }
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
    }

    /**
     * 尝试打印，阻塞式，打印完成或等待超时后返回
     *
     * @param msg
     * @param time 超时时间 单位ms
     * @return
     */
    public PrintResult tryPrint(Object[] msg, long time) {
        if (time < 1000) {
            time = 1000;
        }
        if (null == msg) {
            msg = new Object[0];
        }
        long endTime = System.currentTimeMillis() + time;
        PrintResult result = new PrintResult();
        result.setCode(PrintControl.STATUS_ABNORMAL);
        try {
            logger.info("[" + ip + "]尝试获取打印机锁");
            boolean tryLock = printLock.tryLock(time, TimeUnit.MILLISECONDS);
            if (tryLock) {
                initChannel();
                if (null == channel || channel.isClosed()) {
                    PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                    logger.info("[" + ip + "]打印机连接关闭，尝试重连");
                    channel = PrinterConnector.createConnection(ip, port, 1000);
                    if (null == channel) {
                        logger.info("[" + ip + "]尝试重连失败");
                        return result;
                    }
                }
                OutputStream outputStream = channel.getOutputStream();
                InputStream inputStream = channel.getInputStream();
                logger.info("[" + ip + "]检查打印机状态");
                int state = PrintControl.printerIsReady(2000, outputStream, inputStream);
                PrinterStatusManager.stateMonitor(state, this);
                if (state != PrintControl.STATUS_OK) {
                    logger.info("[" + ip + "]打印机状态不正常:" + state);
                    result.setCode(state);
                    return result;
                }
                doPrint(msg, outputStream);
                logger.info("[" + ip + "]检查打印结果");
                state = PrintControl.CheckJob(3000, inputStream);
                result.setCode(state);
                PrinterStatusManager.stateMonitor(state, this);
                //打印完成则返回
                if (state == PrintControl.STATUS_PRINT_DONE) {
                    logger.info("[" + ip + "]打印完成");
                } else {
                    logger.info("[" + ip + "]打印不成功:" + state);
                }
                return result;
            }
            logger.info("[" + ip + "]尝试获取打印机锁失败");
        } catch (InterruptedException e) {
            logger.error("[" + ip + "]打印线程被中断", e);
        } catch (IOException e) {
            PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
            logger.info("[" + ip + "]打印机连接异常", e);
            //重置连接
            channel = null;
            if (System.currentTimeMillis() > endTime) {
                result.setCode(PrintControl.STATUS_DEVTIMEOUT);
                return result;
            }
            logger.info("[" + ip + "]尝试重连");
            channel = PrinterConnector.createConnection(ip, port, 1000);
            if (null == channel) {
                logger.info("[" + ip + "]尝试重连失败");
                return result;
            }
            PrinterStatusManager.stateMonitor(PrintControl.STATUS_OK, this);
            logger.info("[" + ip + "]尝试重连失败");
        } finally {
            printLock.unlock();
        }
        return result;
    }

    /**
     * 状态检查
     */
    public void checkState() {
        try {
            //超过检测周期
            if (System.currentTimeMillis() - lastActiveTime < checkStateInterval) {
                return;
            }
            logger.error("[" + ip + "]尝试发起状态检查");
            boolean tryLock = printLock.tryLock(4000, TimeUnit.MILLISECONDS);
            if (tryLock) {
                logger.info("[" + ip + "]开始状态检查");
                if (null == channel || channel.isClosed()) {
                    logger.info("[" + ip + "]打印机连接已断开，尝试重连");
                    channel = PrinterConnector.createConnection(ip, port, 2000);
                }
                if (null == channel || channel.isClosed()) {
                    logger.info("[" + ip + "]尝试重连失败");
                    PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                    return;
                }
                try {
                    int state = PrintControl.printerIsReady(3000, channel.getOutputStream(), channel.getInputStream());
                    if (state == PrintControl.STATUS_OFFLINE) {
                        state = PrintControl.STATUS_OK;
                    }
                    PrinterStatusManager.stateMonitor(state, this);
                } catch (IOException e) {
                    PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                }
            } else {
                logger.info("[" + ip + "]尝试发起状态检查失败");
            }
        } catch (InterruptedException e) {
            logger.error("[" + ip + "]尝试发起状态检查失败", e);
        } finally {
            printLock.unlock();
        }
    }

    private void initChannel() {
        if (null == channel) {
            synchronized (this) {
                if (null == channel) {
                    channel = PrinterConnector.createConnection(ip, port, 2000);
                }
            }
        }
    }

    /**
     * 是否需要调用备用打印机
     *
     * @param state
     * @return
     */
    private boolean needCallBackPrinter(int state) {
        if (null == backPrinter) {
            return false;
        }
        switch (state) {
            case PrintControl.STATUS_OK:
            case PrintControl.STATUS_PAPEREND:
            case PrintControl.STATUS_STOPPRINT:
            case PrintControl.STATUS_CLEAR_STOPPRINT_END:
            case PrintControl.STATUS_PRINT_UNDONE:
            case PrintControl.STATUS_OFFLINE:
            case PrintControl.STATUS_PRINTING:
            case PrintControl.STATUS_COVEROPEN:
            case PrintControl.STATUS_PAPERNEAREND:
            case PrintControl.STATUS_PRINT_DONE:
            case PrintControl.STATUS_ABNORMAL:
            case PrintControl.STATUS_DEVTIMEOUT:
                return false;
            default:
                return true;
        }
    }

    private void sleep(long time) {
        logger.debug("[" + ip + "]休眠" + time + "ms");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public Printer getBackPrinter() {
        return backPrinter;
    }

    public void setBackPrinter(Printer backPrinter) {
        this.backPrinter = backPrinter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLastState() {
        return lastState;
    }

    public void setLastState(int lastState) {
        this.lastState = lastState;
    }
}
