package com.candao.www.printer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaoy on 2016/6/12.
 */
public class PrinterManager {
    private static Map<String, Printer> printers = new ConcurrentHashMap<>();

    public static Printer getPrinter(String ipAddr) {
        return printers.get(ipAddr);
    }
    public static void addPrinter(Printer printer){
        if(null==printer.getKey()){
            throw new NullPointerException("打印机标示不能为空");
        }
        printers.put(printer.getKey(),printer);
    }
}
