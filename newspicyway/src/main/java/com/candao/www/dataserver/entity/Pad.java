package com.candao.www.dataserver.entity;

/**
 * Created by ytq on 2016/3/17.
 */
public class Pad extends Device {
    public Pad(String deviceGroup, String deviceId, String ssId, String userId, String tableNo) {
        super(deviceGroup, deviceId, ssId, userId);
        this.tableNo = tableNo;
    }
}
