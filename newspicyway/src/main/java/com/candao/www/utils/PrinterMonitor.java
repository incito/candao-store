package com.candao.www.utils;

import com.candao.print.service.PrinterService;
import com.candao.www.constant.Constant;
import com.candao.www.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * 打印机状态监控线程
 * Created by liaoy on 2016/6/8.
 */
public class PrinterMonitor extends Thread {
    private static Logger logger = LoggerFactory.getLogger(PrinterMonitor.class);
    @Autowired
    private PrinterService printerService = (PrinterService) SpringContext.getBean(PrinterService.class);
    /**
     * Ping次数
     */
    private int pingTimes = 10;
    /**
     * 打印机列表缓存
     */
    private List<Map<String, Object>> cache;
    /**
     * 最新缓存时间 单位ms
     */
    private long lastQuery = 0;
    /**
     * 缓存有效期 单位ms
     */
    private final long cacheExpire = 60 * 1000;

    public PrinterMonitor() {
        /**
         * 设为守护线程
         */
        this.setDaemon(true);
    }

    @Override
    public void run() {
        logger.info("[打印机状态监控]-线程启动");
        for (; ; ) {
            logger.info("[打印机状态监控]-开始一轮监控");
            List<Map<String, Object>> printerList = getPrinter();
            if (null != printerList) {
                for (Map<String, Object> printer : printerList) {
                    /**
                     * 模拟Ping命令，ICMP协议访问目标服务器，如果目标服务器不只吃ICMP协议，会访问7端口。
                     */
                    InetAddress address = null;
                    try {
                        Object ipaddress = printer.get("ipaddress");
                        address = InetAddress.getByName(ipaddress.toString());
                    } catch (Exception e) {
                        logger.error("[打印机状态监控]-出现异常", e);
                    }
                    //ping通的次数
                    int reachedCount = 0;
                    if (null != address) {
                        for (int i = 0; i < pingTimes; i++) {
                            try {
                                if (address.isReachable(4000)) {
                                    reachedCount++;
                                }
                            } catch (IOException e) {
                            }
                        }
                    }
                    short status = handleStatus(reachedCount, pingTimes);
                    com.candao.print.entity.TbPrinter tbprinter = new com.candao.print.entity.TbPrinter();
                    tbprinter.setPrinterid(printer.get("printerid").toString());
                    tbprinter.setWorkStatus(status);
                    printerService.update(tbprinter);
                }
            }
            try {
                /**
                 * 避免CPU占用率过高 休眠1ms
                 */
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取打印机列表
     *
     * @return
     */
    private List<Map<String, Object>> getPrinter() {
        if (System.currentTimeMillis() - lastQuery > cacheExpire) {
            cache = printerService.find(null);
        }
        return cache;
    }

    /**
     * 处理ping结果，根据算法得到打印机状态
     *
     * @param reachedCount   ping通的次数
     * @param totalPingCount 总ping次数
     * @return 对ping结果处理后的状态
     */
    private short handleStatus(int reachedCount, int totalPingCount) {
        float rate = (float) reachedCount / totalPingCount;
        if (rate == 0.0) {
            return Constant.PRINTER_STATUS.NOT_REACHABLE;
        }
        if (rate < 0.4) {
            return Constant.PRINTER_STATUS.BAD;
        }
        return Constant.PRINTER_STATUS.GOOD;
    }

    public static void main(String[] args) throws IOException {
        InetAddress address = InetAddress.getByName("10.66.18.3");
        while (true) {
            System.out.println(address.isReachable(3000));
        }
    }
}
