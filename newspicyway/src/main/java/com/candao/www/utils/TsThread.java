package com.candao.www.utils;

import com.candao.www.constant.Constant;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class TsThread extends Thread {
    private String str;
    private boolean restarted = false;
    private static Logger logger = LoggerFactory.getLogger(TsThread.class);

    public TsThread(String str) {
        this.str = str;
    }

    @Override
    public void run() {
        //根据动作打印不同的小票
        URL urlobj;
        try {
            urlobj = new URL(str);
            URLConnection urlconn = urlobj.openConnection();
            urlconn.connect();
            InputStream myin = urlconn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
            String content = reader.readLine();
            /**
             * 如果DataServer推送异常(特征值判断)，则重启Dataserver后重新推送
             */
            if(null!=content&&content.contains("Access violation")){
                if (str.startsWith(Constant.TS_URL)) {
                    restartAndRetry();
                }
            }
            JSONObject object = JSONObject.fromObject(content.trim());
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) object.get("result");
            if ("1".equals(String.valueOf(resultList.get(0).get("Data")))) {
                System.out.println("推送成功");
            } else {
                System.out.println("推送失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (str.startsWith(Constant.TS_URL)) {
                restartAndRetry();
            }
        }
    }

    private void restartAndRetry() {
        //已重启过则放弃
        if (restarted) {
            return;
        }
        restarted = true;
        if (DataServerUtil.restart()) {
            run();
        } else {
            logger.error("尝试重启DataServer失败");
        }
    }

    public static void main(String[] args) {
//        new TsThread("http://10.66.9.248:8081/datasnap/rest/TServerMethods1/broadcastmsg/userid/sdfsdf".toString()).run();
    }
}
