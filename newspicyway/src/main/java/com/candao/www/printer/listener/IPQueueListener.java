package com.candao.www.printer.listener;

import com.candao.common.utils.Constant;
import com.candao.common.utils.Constant.ListenerType;
import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintObj;
import com.candao.print.listener.QueueListener;
import com.candao.print.listener.template.ListenerTemplate;
import com.candao.www.printer.v2.Printer;
import com.candao.www.printer.v2.PrinterManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Administrator on 2016-6-13.
 */
public class IPQueueListener implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private PrinterListenerManager printerListenerManager;

    public void receiveMessage(PrintObj obj) {
        Constant.ListenerType listenerType = obj.getListenerType();
        QueueListener dst = null;
        try {
            dst = detemineListener(listenerType, printerListenerManager);
        } catch (Exception e1) {
            e1.printStackTrace();
            log.error("加载模板失败！" + obj.getOrderNo() + "，模板类型：" + listenerType, e1);
            return;
        }

        ListenerTemplate listenerTemplate = printerListenerManager.getListenerTemplate(obj.getPrinterid(),
                listenerType);
        PrintData data = null;
        try {
            data = dst.receiveMessage(obj, listenerTemplate);
        } catch (Exception e) {
            log.error("打印数据处理失败------------ ");
            log.error("处理方法类型：" + obj.getListenerType().toString());
            log.error("订单号： " + obj.getOrderNo());
            e.printStackTrace();
        }
        if (data != null) {
            try {
                print(data.convert(), obj);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("打印失败!订单号：" + obj.getOrderNo(), e);
            }
        }

    }

    private QueueListener detemineListener(ListenerType listenerType, PrinterListenerManager listenerManager)
            throws Exception {
        QueueListener dst = null;
        if (listenerManager.isXmlTemplate(listenerType)) {
            dst = printerListenerManager.getXmlQueueListener(listenerType);
            return dst;
        }
        try {
            dst = (QueueListener) applicationContext.getBean(listenerType.toString());
        } catch (BeansException e) {
            e.printStackTrace();
            log.error("找不到监听类： " + listenerType.toString(), e);
            throw new Exception("找不到监听类");
        }
        return dst;
    }

    private void print(final Object[] src, final PrintObj obj) throws Exception {
        // TODO
        // System.out.println("2333333333333333333333333333333");
        // System.out.println(JacksonJsonMapper.objectToJson(src));
        String ipAddress = obj.getCustomerPrinterIp();
        String backupAddress = "";
        if (ipAddress.contains(",")) {
            String[] ips = ipAddress.split(",");
            ipAddress = ips[0];
            backupAddress = ips.length > 1 ? ips[1] : backupAddress;
        }
        Printer printer = PrinterManager.getPrinter(ipAddress);
        if (printer == null) {
            log.error("-----------------------");
            log.error("打印失败，找不到目的打印机！订单号：" + obj.getOrderNo());
            return;
        }
        printer.print(src, backupAddress);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
