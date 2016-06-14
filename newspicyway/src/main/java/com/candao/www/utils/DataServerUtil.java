package com.candao.www.utils;

import com.candao.www.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dataserver工具类
 * Created by liaoy on 2016/6/2.
 */
public class DataServerUtil {
    private static Logger logger = LoggerFactory.getLogger(DataServerUtil.class);
    /**
     * 最后重启时间
     */
    private static long lastRestartTime = 0;
    /**
     * 重启后连接检测重试次数
     */
    private static final int RETRY_TIMES = 8;
    /**
     * 重启后连接检测重试间隔时间 单位ms
     */
    private static final int INTERVAL_TIME = 1000;
    /**
     * 最小重启间隔时间 单位ms
     */
    private static final long MIN_RESTART_INTERVAL = 10000;

    /**
     * 重启dataserver
     *
     * @return 是否重启成功
     * @throws IOException
     */
    public static synchronized boolean restart() {
        //如果在最小重启间隔以内，直接返回成功
        if (System.currentTimeMillis() - lastRestartTime < MIN_RESTART_INTERVAL) {
            return true;
        }
        try {
            File batFile = ResourceUtils.getFile("classpath:other/DataServerRestart.bat");
            String cmd = batFile.getAbsolutePath();
            Process ps = Runtime.getRuntime().exec(cmd);
            StreamGobbler stream1 = new StreamGobbler(ps.getInputStream(), "Info");
            stream1.start();
            lastRestartTime = System.currentTimeMillis();
        } catch (FileNotFoundException e) {
            logger.error("dataserver重启脚本不存在", e);
            return false;
        } catch (IOException e) {
            logger.error("dataserver重启失败", e);
            return false;
        }
        /**
         * 循环检测是否重启完成
         */
        for (int times = 0; times < RETRY_TIMES; times++) {
            try {
                Thread.sleep(INTERVAL_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (checkOnline()) {
                logger.info("dataserver重启成功");
                return true;
            }
        }
        logger.info("dataserver重启后重连失败，重启失败。");
        return false;
    }

    /**
     * 检测dataserver连接是否畅通
     *
     * @return
     */
    private static boolean checkOnline() {
        Socket socket = new Socket();
        //截取出dataserver地址
        Matcher matcher = Pattern.compile("//(.*?)/").matcher(Constant.TS_URL);
        String url = null;
        if (matcher.find()) {
            url = matcher.group(1);
        }
        if (null == url) {
            return false;
        }
        String[] split = url.split(":");
        try {
            socket.connect(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
            socket.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String url = "http://localhost:8081/datasnap/rest/TServerMethods1/broadcastmsg/userid/";
        Matcher matcher = Pattern.compile("//(.*?)/").matcher(url);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("localhost", 8081));
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
