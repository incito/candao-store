package com.candao.www.printer.v2;

import com.candao.print.service.PrinterService;
import com.candao.print.utils.PrintControl;
import com.candao.www.spring.SpringContext;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 打印机状态管理
 * Created by liaoy on 2016/6/14.
 */
public class PrinterStatusManager {
    public static final short NORMAL = 1;
    public static final short PAPEREND = 2;
    public static final short COVEROPEN = 3;
    public static final short CUTERROR = 4;
    public static final short DISCONNECT = 5;
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private static PrinterService printerService;

    /**
     * 打印机状态监控
     *
     * @param nowState
     * @param printer
     * @return
     */
    public static boolean stateMonitor(int nowState, Printer printer) {
        /**
         * 要处理的状态列表
         */
        short state;
        switch (nowState) {
            case PrintControl.STATUS_PAPEREND:
                state = PAPEREND;
                break;
            case PrintControl.STATUS_COVEROPEN:
                state = COVEROPEN;
                break;
            case PrintControl.STATUS_DISCONNECTE:
                state = DISCONNECT;
                break;
            case PrintControl.STATUS_CUTTER_ERROR:
                state = CUTERROR;
                break;
            default:
                state = NORMAL;
        }
        if (printer.getLastState() == state) {
            return false;
        }
        //修改打印机最后状态
        printer.setLastState(state);
        executor.submit(new StatusRunable(printer, state));
        return true;
    }

    /**
     * 转换状态编码为文字描述
     *
     * @param state
     * @return
     */
    public static String convertState(short state) {
        switch (state) {
            case NORMAL:
                return "正常";
            case PAPEREND:
                return "打印会已用尽";
            case COVEROPEN:
                return "上盖打开";
            case CUTERROR:
                return "切刀异常";
            case DISCONNECT:
                return "未连接";

        }
        return "正常";
    }

    private static PrinterService getPrinterService() {
        if (null == printerService) {
            synchronized (PrinterStatusManager.class) {
                if (null == printerService) {
                    printerService = (PrinterService) SpringContext.getBean(PrinterService.class);
                }
            }
        }
        return printerService;
    }


    static class StatusRunable implements Runnable {
        private Printer printer;
        private short state;

        public StatusRunable(Printer printer, short state) {
            this.printer = printer;
            this.state = state;
        }

        @Override
        public void run() {
            PrinterService service = getPrinterService();
            service.updateWorkState(printer.getIp(), state);
        }
    }
}
