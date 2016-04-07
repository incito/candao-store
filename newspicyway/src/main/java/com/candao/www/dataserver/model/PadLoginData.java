package com.candao.www.dataserver.model;

import java.sql.Date;

/**
 * Created by ytq on 2016/3/18.
 */
public class PadLoginData extends BaseData {
    private String userId;
    private String username;
    private String password;
    private String macAddress;
    private String padLicenceNo;
    private Date loginDate;
    private String loginType;
    private String tableNo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getPadLicenceNo() {
        return padLicenceNo;
    }

    public void setPadLicenceNo(String padLicenceNo) {
        this.padLicenceNo = padLicenceNo;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }
}
