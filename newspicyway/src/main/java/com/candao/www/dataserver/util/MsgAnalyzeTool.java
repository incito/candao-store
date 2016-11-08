package com.candao.www.dataserver.util;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.model.LogCollectData;
import com.candao.www.dataserver.model.MsgForwardData;
import com.candao.www.dataserver.model.OfflineMsgData;
import com.candao.www.dataserver.model.PadCheckInRespData;
import com.candao.www.dataserver.model.PadLoginData;
import com.candao.www.dataserver.model.ReConnectData;
import com.candao.www.dataserver.model.WatchCheckInRespData;
import com.candao.www.dataserver.model.WatchLoginData;

/**
 * Created by ytq on 2016/3/17.
 */
public class MsgAnalyzeTool {

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


    public static WatchLoginData analyzeToWatchDevice(String msg) {
        return JSON.parseObject(msg, WatchLoginData.class);
    }

    public static PadLoginData analyzeToPadDevice(String msg) {
        return JSON.parseObject(msg, PadLoginData.class);
    }

    public static ReConnectData analyzeToReConData(String msg) {
        return JSON.parseObject(msg, ReConnectData.class);
    }

    public static LogCollectData analyzeToLogCoData(String msg) {
        return JSON.parseObject(msg, LogCollectData.class);
    }
}
