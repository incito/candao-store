package com.candao.www.dataserver.entity;

import java.util.Date;

/**
 * Created by ytq on 2016/3/17.
 */
public class Device {
    private Integer id;
    private String deviceGroup;
    private String deviceId;
    private String deviceType;

    private String ssId;
    private String userId;
    private Date createTime;
    private Date updateTime;
    private Integer validFlag;
    public String tableNo;

    public Device() {

    }

    public Device(String deviceGroup, String deviceId, String ssId, String userId) {
        this.deviceGroup = deviceGroup;
        this.deviceId = deviceId;
        this.ssId = ssId;
        this.userId = userId;
    }

    public Device(String deviceGroup, String deviceId, String ssId) {
        this.deviceGroup = deviceGroup;
        this.deviceId = deviceId;
        this.ssId = ssId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(String deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSsId() {
        return ssId;
    }

    public void setSsId(String ssId) {
        this.ssId = ssId;
    }

    public Integer getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Integer validFlag) {
        this.validFlag = validFlag;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
