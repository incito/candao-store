package com.candao.www.listener;

import com.candao.www.printer.v2.PrinterManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 初始化Listener，需要放到Spring初始化listener之后
 * Created by liaoy on 2016/6/12.
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PrinterManager.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
