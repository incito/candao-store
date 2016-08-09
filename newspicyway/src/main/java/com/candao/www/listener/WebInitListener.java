package com.candao.www.listener;


import com.candao.www.dataserver.util.NettyStreamGobbler;
import com.candao.www.timedtask.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WebInitListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(WebInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                Process proc = null;
//                try {
//                    proc = Runtime.getRuntime().exec("wmic process where name='start.exe' call terminate");
//                    proc.waitFor();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
        try {
            logger.info("kill 'start.exe'....");
            Process proc = Runtime.getRuntime().exec("wmic process where name='start.exe' call terminate");
            proc.waitFor();
        } catch (IOException e) {
            logger.error("kill netty server error!", e);
        } catch (InterruptedException e) {
            logger.error("kill netty server error!", e);
        }
        File zipFile = null;
        try {
            zipFile = ResourceUtils.getFile("classpath:nettyserver/CommunicationServer-bin.zip");
        } catch (FileNotFoundException e) {
            logger.error("netty server zip file is not exists!");
        }
        if (null == zipFile) {
            return;
        }
        String descDir = zipFile.getParent() + File.separator;
        try {
            logger.info("unzip netty server package.....");
            ZipUtil.unZipFiles(zipFile, descDir);
        } catch (IOException e) {
            logger.error("unzip failed", e);
            return;
        }
        logger.info("unzip netty server package success,begin to run....");
        String baseDir = descDir + "CommunicationServer" + File.separator;
//        String cmd = "cmd /c start startup.bat";
        String cmd = baseDir + File.separator + "start.exe";
        try {
            final Process process = Runtime.getRuntime().exec(cmd, null, new File(baseDir));
            NettyStreamGobbler stream1 = new NettyStreamGobbler(process.getInputStream(), "Info");
            stream1.start();
            NettyStreamGobbler stream2 = new NettyStreamGobbler(process.getErrorStream(), "Error");
            stream2.start();

        } catch (Exception e) {
            logger.error("netty server start failed!", e);
            return;
        }
        logger.info("netty server start successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
