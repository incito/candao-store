package com.candao.www.dataserver.controller;

import com.candao.www.constant.Constant;
import com.candao.www.utils.DataServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Pad转接DataServer接口
 */
@Controller
@RequestMapping("/datasnap/rest/TServerMethods1")
public class PadForwordController {
    private static final Logger logger = LoggerFactory.getLogger(PadForwordController.class);

    @ResponseBody
    @RequestMapping(value = "/QueryBalance3/{input}/{orderId}", produces = {"application/json;charset=UTF-8"})
    public String QueryBalance3(@PathVariable String input, @PathVariable String orderId) {
        logger.info("###REQUEST### PadForwordController QueryBalance3 input={} orderId={}", input, orderId);
        String result = getUrl("/QueryBalance3/" + input + "/" + orderId);
        logger.info("###RESPONSE### PadForwordController QueryBalance3 response={}", result);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/setMemberPrice2/{userId}/{orderId}/{ip}/", produces = {"application/json;charset=UTF-8"})
    public String setMemberPrice2(@PathVariable String userId, @PathVariable String orderId, @PathVariable String ip) {
        logger.info("###REQUEST### PadForwordController setMemberPrice2 userId={} orderId={}", userId, orderId);
        String result = getUrl("/setMemberPrice2/" + userId + "/" + orderId + "/" + ip + "/");
        logger.info("###RESPONSE### PadForwordController setMemberPrice2 response={}", result);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/broadcastok/{client}/{msgId}", produces = {"application/json;charset=UTF-8"})
    public String broadcastok(@PathVariable String client, @PathVariable String msgId) {
        logger.info("###REQUEST### PadForwordController broadcastok client={} msgId={}", client, msgId);
        String result = getUrl("/broadcastok/" + client + "/" + msgId);
        logger.info("###RESPONSE### PadForwordController broadcastok response={}", result);
        return result;
    }

    private String getUrl(String uri) {
        String tsUrl = Constant.TS_URL;
        String[] urls = tsUrl.split("TServerMethods1");
        String url = urls[0] + "TServerMethods1" + uri;
        try {
            String result = sendGet(url, "UTF-8");
            if (null != result && result.contains("Access violation")) {
                result = restartAndRetry(url);
            }
            return result;
        } catch (IOException e) {
            return restartAndRetry(url);
        }
    }

    private String restartAndRetry(String url) {
        DataServerUtil.restart();
        try {
            return sendGet(url, "UTF-8");
        } catch (IOException e1) {
            return "";
        }
    }

    private String sendGet(String url, String charset) throws IOException {
        StringBuilder result = new StringBuilder();

        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        conn.connect();
        conn.setConnectTimeout(10 * 1000);// 设置链接超时时间
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        in.close();
        return result.toString();
    }
}
