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
}
