package com.candao.www.webroom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 远程操作命令入口
 */
@Controller
@RequestMapping("/contoller")
public class ControllerController {
    private Logger logger = LoggerFactory.getLogger(ControllerController.class);

    /**
     * 重启dataserver
     *
     * @return
     */
    @RequestMapping("/restartDataserver")
    @ResponseBody
    public String restartDataServer() {
        String cmd;
        try {
            File batFile = ResourceUtils.getFile("classpath:other/DataServerRestart.bat");
            cmd = batFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("获取dataserver重启bat失败", e);
            return "1";
        }
        exec(cmd);
        return "0";
    }

    private void exec(String command) {
        try {
            Process ps = Runtime.getRuntime().exec(command);
            InputStream in = ps.getInputStream();
            int c;
            while ((c = in.read()) != -1) {
            }
            in.close();
            ps.waitFor();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("child thread done");
    }

    public static void main(String[] args) {
        ControllerController c = new ControllerController();
        c.restartDataServer();
    }
}
