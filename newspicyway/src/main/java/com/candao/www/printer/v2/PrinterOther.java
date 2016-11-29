package com.candao.www.printer.v2;

import com.candao.print.entity.PrinterConstant;
import com.candao.print.utils.PrintControl;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * 除新北洋以外的其他打印机，不支持一票一控
 * Created by liaoy on 2016/6/21.
 */
public class PrinterOther extends Printer {
    @Override
    public PrintResult print(Object[] msg, String backPrinterIp) {
        if (null == msg) {
            msg = new Object[]{};
        }
        PrintResult result = new PrintResult();
        printLock.lock();
        Socket channel = null;
        try {
            while (true) {
                if (isDisabled()) {
                    throw new RuntimeException("准备移除打印机[" + getIp() + "]，停止打印");
                }
                lastActiveTime = System.currentTimeMillis();
                try {
                    /*打印机是否连接成功*/
                    logger.info("[" + getIp() + "]开启连接");
                    channel = PrinterConnector.createConnection(getIp(), getPort(), 2000);
                    if (null != channel && channel.isConnected()) {
                        PrinterStatusManager.stateMonitor(PrintControl.STATUS_OK, this);
                        OutputStream outputStream = channel.getOutputStream();
                        doCommand(outputStream);
                        /*开始打印*/
                        logger.info("[" + getIp() + "]开始打印");
                        doPrint(msg, outputStream);
                        logger.info("[" + getIp() + "]打印结束");
                        result.setCode(PrintControl.STATUS_PRINT_DONE);
                        doCommand(outputStream);
                        return result;
                    } else {
                        PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                        //调用备用打印机
                        if (StringUtils.isEmpty(backPrinterIp)) {
                            continue;
                        }
                        Printer backPrinter = PrinterManager.getPrinter(backPrinterIp);
                        if (null != backPrinter) {
                            logger.info("[" + getIp() + "]尝试调用备用打印机:[" + backPrinter.getIp() + "]");
                            PrintResult printResult = backPrinter.tryPrint(msg, 2000);
                            //备用打印机正常打印，返回打印结果。
                            if (printResult.getCode() == PrintControl.STATUS_PRINT_DONE) {
                                logger.info("[" + getIp() + "]尝试调用备用打印机[" + backPrinter.getIp() + "]打印成功");
                                return printResult;
                            } else {
                                logger.info("[" + getIp() + "]尝试调用备用打印机[" + backPrinter.getIp() + "]打印失败，重试");
                            }
                        }
                    }
                } catch (IOException e) {
                    PrinterConnector.closeConnection(channel);
                    PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                }
            }
        } finally {
            PrinterConnector.closeConnection(channel);
            printLock.unlock();
        }
    }

    @Override
    protected int doPrint(Object[] msg, OutputStream outputStream) throws IOException {
        //added by caicai
        //省纸
        /*outputStream.write(new byte[]{27, 27});*/
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "GBK");
        int rowCount=0;
        int pauseCount=0;
        for (Object o : msg) {
            if (null == o) {
                o = "";
            }
            if (o instanceof Byte) {
                outputStream.write(new byte[]{(byte) o});
            } else if (o instanceof byte[]) {
                outputStream.write((byte[]) o);
            } else {
                writer.write(o.toString());
                rowCount++;
            }
            outputStream.flush();
            writer.flush();
            //打印一页停顿3s
			if (rowCount > 0 && pauseCount != rowCount && rowCount % pageNum == 0) {
                pauseCount=rowCount;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                	logger.error(e);
                }
            }
        }
        //启动了分批打印
        if(pauseCount > 0)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        	logger.error(e);
        }
        logger.info("=================>内容完毕");
        outputStream.write(PrinterConstant.getLineN((byte) 3));
        outputStream.write(new byte[]{10});
        outputStream.flush();
        outputStream.write(PrinterConstant.CUT);
        outputStream.flush();
        logger.info("=================>切纸指令完毕");
        return rowCount;
    }
    
    @Override
    public PrintResult tryPrint(Object[] msg, long time) {
        {
            PrintResult result = new PrintResult();
            if (isDisabled()) {
                result.setCode(PrintControl.STATUS_STOPPRINT);
                return result;
            }
            lastActiveTime = System.currentTimeMillis();
            if (time < 1000) {
                time = 1000;
            }
            if (null == msg) {
                msg = new Object[0];
            }
            result.setCode(PrintControl.STATUS_ABNORMAL);
            logger.info("[" + getIp() + "]尝试获取打印机锁");
            boolean tryLock = false;
            try {
                tryLock = printLock.tryLock(time, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.info("[" + getIp() + "]打印线程被中断");
            }
            Socket channel = null;
            if (tryLock) {
                try {
                    channel = PrinterConnector.createConnection(getIp(), getPort(), 2000);
                    if (null == channel) {
                        PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                        logger.info("[" + getIp() + "]尝试连接失败");
                        return result;
                    }
                    PrinterStatusManager.stateMonitor(PrintControl.STATUS_OK, this);
                    OutputStream outputStream = channel.getOutputStream();
                    doCommand(outputStream);
                    logger.info("[" + getIp() + "]开始打印");
                    doPrint(msg, outputStream);
                    result.setCode(PrintControl.STATUS_PRINT_DONE);
                    doCommand(outputStream);
                    return result;

                } catch (IOException e) {
                    PrinterConnector.closeConnection(channel);
                    logger.info("[" + getIp() + "]打印机连接异常:" + e.getMessage());
                    PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                } finally {
                    PrinterConnector.closeConnection(channel);
                    printLock.unlock();
                }
            }
            logger.info("[" + getIp() + "]尝试获取打印机锁失败");
            return result;
        }
    }

    @Override
    public void checkState() {
        if (isDisabled()) {
            return;
        }
        //超过检测周期
        if (System.currentTimeMillis() - lastActiveTime < checkStateInterval) {
            return;
        }
        logger.error("[" + getIp() + "]尝试发起状态检查");
        boolean tryLock = printLock.tryLock();
        if (tryLock) {
            logger.info("[" + getIp() + "]开始状态检查");
            Socket channel = null;
            try {
                channel = PrinterConnector.createConnection(getIp(), getPort(), 2000);
                if (null == channel || channel.isClosed()) {
                    logger.info("[" + getIp() + "]尝试连接失败");
                    PrinterStatusManager.stateMonitor(PrintControl.STATUS_DISCONNECTE, this);
                    return;
                }
                PrinterStatusManager.stateMonitor(PrintControl.STATUS_OK, this);
            } finally {
                PrinterConnector.closeConnection(channel);
                printLock.unlock();
            }
        } else {
            logger.info("[" + getIp() + "]尝试发起状态检查失败");
        }
    }

    @Override
    public void openCash() {
        logger.info("开钱箱[" + getIp() + "]");
        synchronized (cmd) {
            cmd.setCommand(PrinterConstant.OPEN_CASH);
            try {
                if (printLock.tryLock(2, TimeUnit.SECONDS)) {
                    Socket channel = null;
                    try {
                        channel = PrinterConnector.createConnection(getIp(), getPort(), 2000);
                        if (null != channel && channel.isConnected()) {
                            try {
                                doCommand(channel.getOutputStream());
                            } catch (IOException e) {
                                logger.error("doCommand failed!", e);
                            }
                        }
                    } finally {
                        PrinterConnector.closeConnection(channel);
                        printLock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("10.66.18.11", 9100);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(PrinterConstant.OPEN_CASH);
        outputStream.flush();
    }
}
