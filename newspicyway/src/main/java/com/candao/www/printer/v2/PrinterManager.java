package com.candao.www.printer.v2;

import com.candao.print.service.PrinterService;
import com.candao.print.utils.PrintControl;
import com.candao.www.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaoy on 2016/6/12.
 */
public class PrinterManager {
    private static Logger logger = LoggerFactory.getLogger(PrinterManager.class);
    private static Map<String, Printer> printers = new ConcurrentHashMap<>();

    public static Printer getPrinter(String ipAddr) {
        if (printers.isEmpty()) {
            synchronized (PrinterManager.class) {
                if (printers.isEmpty()) {
                    logger.info("初始化打印机池");
                    initialize();
                }
            }
        }
        return printers.get(ipAddr);
    }

    private static void initialize() {
        PrinterService printerService = (PrinterService) SpringContext.getBean(PrinterService.class);
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

                String[] ips = ipStr.split(",");
                Printer p = new Printer();
                p.setKey(ips[0]);
                p.setIp(ips[0]);
                p.setPort(portInt);
                p.setId(printer.get("printerid").toString());
                //备用打印机处理
                if (ips.length > 1) {
                    Printer backPrinter = new Printer();
                    backPrinter.setKey(ips[1]);
                    backPrinter.setIp(ips[1]);
                    backPrinter.setPort(portInt);
                    p.setBackPrinter(backPrinter);
                    //初始化打印机状态
                    printerService.updateWorkState(backPrinter.getIp(), PrinterStatusManager.NORMAL);
                }
                //初始化打印机状态
                printerService.updateWorkState(p.getIp(), PrinterStatusManager.NORMAL);
                printers.put(p.getKey(), p);
            }
        }
//        // TODO: 2016/6/14 测试代码
//        Printer p = new Printer();
//        //备用打印机处理
//        p.setKey("10.66.18.3");
//        p.setIp("10.66.18.3");
//        p.setPort(9100);
//        printers.put(p.getKey(),p);
    }
}
