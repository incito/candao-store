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
        if (printer.getLastState() == nowState) {
            return false;
        }
        /**
         * 要处理的状态列表
         */
        short state;
        switch (nowState) {
            case PrintControl.STATUS_OK:
                state = 1;
                break;
            case PrintControl.STATUS_PAPEREND:
                state = 2;
                break;
            case PrintControl.STATUS_COVEROPEN:
                state = 3;
                break;
            case PrintControl.STATUS_OFFLINE:
                state = 4;
                break;
            default:
                return false;
        }
        //修改打印机最后状态
        printer.setLastState(nowState);
        executor.submit(new StatusRunable(printer, state));
        return true;
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
            com.candao.print.entity.TbPrinter tbprinter = new com.candao.print.entity.TbPrinter();
            tbprinter.setPrinterid(printer.getId());
            tbprinter.setWorkStatus(state);
            service.update(tbprinter);
        }
    }
}
