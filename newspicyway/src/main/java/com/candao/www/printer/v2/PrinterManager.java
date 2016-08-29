package com.candao.www.printer.v2;

import com.candao.common.utils.PropertiesUtils;
import com.candao.print.service.PrinterService;
import com.candao.www.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaoy on 2016/6/12.
 */
public class PrinterManager {
    private static Logger logger = LoggerFactory.getLogger(PrinterManager.class);
    private static Map<String, Printer> printers = new ConcurrentHashMap<>();
    private static PrinterService printerService;
    /**
     * 是否支持一票一控
     */
    private static boolean printerControl = false;

    /**
     * 获取打印机实例
     *
     * @param ipAddr
     * @return
     */
    public static Printer getPrinter(String ipAddr) {
        if (printers.isEmpty()) {
            synchronized (PrinterManager.class) {
                if (printers.isEmpty()) {
                    logger.info("初始化打印机池");
                    initialize(true);
                }
            }
        }
        return printers.get(ipAddr);
    }

    /**
     * 重新初始化打印机
     *
     * @param printerAddress 需要移除的打印机
     */
    public synchronized static void initialize(List<String> printerAddress) {
        logger.info("重新初始化打印机");
        clearPrinter(printerAddress);
        initialize(false);
        logger.info("重新初始化打印机结束");
    }

    /**
     * 初始化打印机
     */
    public synchronized static void initialize(boolean clearStatus) {
        String printerControlStr = PropertiesUtils.getValue("PRINTER_CONTROL");
        printerControl = null != printerControlStr && "y".equals(printerControlStr.toLowerCase());

        PrinterService printerService = getPrinterService();
        if (clearStatus) {
            printerService.clearWorkStatus();
        }
        List<Map<String, Object>> printerList = printerService.find(null);
        if (null != printerList) {
            for (Map<String, Object> printer : printerList) {
                Object ipaddress = printer.get("ipaddress");
                Object port = printer.get("port");
                if (StringUtils.isEmpty(ipaddress) || StringUtils.isEmpty(port)) {
                    continue;
                }

                String ipStr = ipaddress.toString();
                int portInt = 0;
                try {
                    portInt = Integer.parseInt(port.toString());
                } catch (Exception e) {
                    logger.info("[" + ipStr + "]端口不正常:" + port);
                    continue;
                }

                createPrinter(ipStr, portInt);
            }
        }
    }

    private static void createPrinter(String ipStr, int portInt) {
        String[] ips = ipStr.split(",");
        int length = ips.length > 2 ? 2 : ips.length;
        for (int i = 0; i < length; i++) {
            String ip = ips[i];
            Printer p = hasCreated(ip);
            //如果该打印机还不存在，创建
            if (null == p) {
                if (printerControl) {
                    p = new Printer();
                } else {
                    p = new PrinterOther();
                }
                p.setKey(ip);
                p.setIp(ip);
                p.setPort(portInt);
                printers.put(p.getKey(), p);
                PrinterStatusManager.stateMonitor(PrinterStatusManager.NORMAL, p);
            }
        }
    }

    /**
     * 检查打印机对象是否已被创建
     *
     * @param key
     * @return
     */
    private static Printer hasCreated(String key) {
        return printers.get(key);
    }

    /**
     * 周期性检测打印机状态
     */
    public static void schedule() {
        for (Printer printer : printers.values()) {
            printer.checkState();
        }
    }

    private static PrinterService getPrinterService() {
        if (null == printerService) {
            printerService = (PrinterService) SpringContext.getBean(PrinterService.class);
        }
        return printerService;
    }

    private static void clearPrinter(List<String> printerAddress) {
        logger.info("清空打印机集合");
        PrinterService printerService = getPrinterService();
        if (null != printerAddress) {
            for (String printerKey : printerAddress) {
                String[] strings = StringUtils.tokenizeToStringArray(printerKey, ":");
                Printer printer = printers.get(strings[0]);
                printer.setDisabled(true);
                PrinterConnector.closeConnection(printer.getChannel());
                printers.remove(strings[0]);
                printerService.clearWorkStatus(strings[0]);
            }
        }
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("10.66.18.3,12.22.01.1:9100");
        initialize(list);
    }
}
