package com.candao.www.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.candao.www.printer.v2.PrinterManager;

/**
 * 初始化Listener，需要放到Spring初始化listener之后
 * Created by liaoy on 2016/6/12.
 */
public class InitListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(InitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PrinterManager.initialize(true);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
