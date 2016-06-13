package com.candao.print.listener;

import com.candao.common.utils.Constant;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintObj;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016-6-13.
 */
public class IPQueueListener implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    private Log log = LogFactory.getLog(IPQueueListener.class.getName());

    public void receiveMessage(PrintObj obj) {
        Constant.ListenerType listenerType = obj.getListenerType();
        PrinterListener dst = null;
        try {
            dst = (PrinterListener) applicationContext.getBean(listenerType.toString());
        } catch (BeansException e){
            e.printStackTrace();
            log.error("找不到监听类： "+listenerType.toString(),e);
            log.error("订单号： "+obj.getOrderNo());
            return;
        }

        PrintData data = null;
        try {
           data = dst.receiveMessage(obj);
        } catch (Exception e) {
            log.error("打印数据处理失败------------ ");
            log.error("订单号： "+obj.getOrderNo());
            e.printStackTrace();
        }
        if (data != null){
            try {
                print(data.convert());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("打印失败",e);
            }
        }

    }

    private void print(Object[] src) throws  Exception{
        //TODO
        System.out.println("2333333333333333333333333333333");
        System.out.println(JacksonJsonMapper.objectToJson(src));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
