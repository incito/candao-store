package com.candao.www.dataserver.service.msghandler;


import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.log.BaseLogService;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by ytq on 2016/3/21.
 */
public interface MsgHandler extends BaseLogService {
    Map<String, Lock> mapLocks = new Hashtable<>();
    Map<String, Condition> mapConditions = new Hashtable<>();
    Map<String, String> mapResults = new Hashtable<>();

    //消息处理
    void handler(DeviceObject deviceObject, String serialNumber, String msg);
}
