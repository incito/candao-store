package com.candao.www.dataserver.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.candao.www.dataserver.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ytq on 2016/3/17.
 */
public class MsgAnalyzeTool {
    public static MsgResponseData analyzeQueryTerminals(String terminals) {
        MsgResponseData msgResponseData = JSON.parseObject(terminals, MsgResponseData.class);
        return msgResponseData;
    }

    public static MsgForwardData analyzeMsgForward(String msg) {
        return JSON.parseObject(msg, MsgForwardData.class);
    }

    public static WatchCheckInRespData analyzeWatchCheckInResp(String msg) {
        return JSON.parseObject(msg, WatchCheckInRespData.class);
    }

    public static PadCheckInRespData analyzePadCheckInResp(String msg) {
        return JSON.parseObject(msg, PadCheckInRespData.class);
    }

    public static OfflineMsgData analyzeOffLineResp(String msg) {
        return JSON.parseObject(msg, OfflineMsgData.class);
    }

    public static List<DeviceData> analyzeToDevice(String msg) {
        return JSON.parseArray(msg, DeviceData.class);
    }

    public static WatchLoginData analyzeToWatchDevice(String msg) {
        return JSON.parseObject(msg, WatchLoginData.class);
    }

    public static PadLoginData analyzeToPadDevice(String msg) {
        return JSON.parseObject(msg, PadLoginData.class);
    }

    public static ReConnectData analyzeToReConData(String msg) {
        return JSON.parseObject(msg, ReConnectData.class);
    }

    public static List<String> analyzeMsgForwardResp(String msg) {
        List<String> list = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(msg).getJSONObject("data");
        if (jsonObject.containsKey("code") && "0".equals(jsonObject.getString("code"))) {
            if (jsonObject.containsKey("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    String groupId = jsonArray.getString(i);
                    if (groupId.contains(":")) {
                        list.add(groupId);
                    }
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        WatchLoginData watchLoginData = new WatchLoginData();
        watchLoginData.setGroup("group_watch");
        watchLoginData.setId("0FA657D4BBFF25F0553EF08A1E1EC0E7");
        MsgForwardData msgForwardData = new MsgForwardData("100001", JSON.toJSONString(watchLoginData));
        String a = "{\"code\":\"0\",\"data\":\"[{\\\"group\\\":\\\"group_watch\\\",\\\"id\\\":\\\"1\\\"},{\\\"group\\\":\\\"group_watch\\\",\\\"id\\\":\\\"2\\\"}]\"}";
        MsgResponseData msgResponseData = new MsgResponseData();
        msgResponseData.setCode("0");
        List<DeviceData> data = new ArrayList<>();
        DeviceData deviceData = new DeviceData();
        deviceData.setGroup("1");
        deviceData.setId("1");
        DeviceData deviceData1 = new DeviceData();
        deviceData1.setId("2");
        deviceData1.setGroup("2");
        data.add(deviceData);
        data.add(deviceData1);
        msgResponseData.setData(data);
        String b = JSON.toJSONString(msgResponseData);
//        String b ="{\"code\":\"0\",\"data\":[{\"group\":\"1\",\"id\":\"1\"},{\"group\":\"2\",\"id\":\"2\"}]}";
//        {"code":"0","data":[{"group":"1","id":"1"},{"group":"2","id":"2"}]}
//        {"code":"0","data":"[{\"group\":\"group_watch\",\"id\":\"1\"},{\"group\":\"group_watch\",\"id\":\"2\"}]"}
//        MsgAnalyzeTool.analyzeQueryTerminals(a.replace("\\"","\\"));
        System.out.println();
    }
}
