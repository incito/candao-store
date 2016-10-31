package com.candao.www.data.model;

import java.util.Date;
import java.util.List;

public class TPrinterDevice {
    private String deviceid;

    private String devicecode;

    private String devicename;

    private String devicearea;

    private Integer devicestatus;

    private Integer devicetype;

    private String deviceip;

    private Date inserttime;

    private Date modifiedtime;

    private String deviceowner;

    private String devicegroup;

    private List<TPrinterDeviceprinter> printers;

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid == null ? null : deviceid.trim();
    }

    public String getDevicecode() {
        return devicecode;
    }

    public void setDevicecode(String devicecode) {
        this.devicecode = devicecode == null ? null : devicecode.trim();
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename == null ? null : devicename.trim();
    }

    public String getDevicearea() {
        return devicearea;
    }

    public void setDevicearea(String devicearea) {
        this.devicearea = devicearea == null ? null : devicearea.trim();
    }

    public Integer getDevicestatus() {
        return devicestatus;
    }

    public void setDevicestatus(Integer devicestatus) {
        this.devicestatus = devicestatus;
    }

    public Integer getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(Integer devicetype) {
        this.devicetype = devicetype;
    }

    public String getDeviceip() {
        return deviceip;
    }

    public void setDeviceip(String deviceip) {
        this.deviceip = deviceip == null ? null : deviceip.trim();
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public Date getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(Date modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getDeviceowner() {
        return deviceowner;
    }

    public void setDeviceowner(String deviceowner) {
        this.deviceowner = deviceowner == null ? null : deviceowner.trim();
    }

    public String getDevicegroup() {
        return devicegroup;
    }

    public void setDevicegroup(String devicegroup) {
        this.devicegroup = devicegroup == null ? null : devicegroup.trim();
    }

    public List<TPrinterDeviceprinter> getPrinters() {
        return printers;
    }

    public void setPrinters(List<TPrinterDeviceprinter> deviceprinters) {
        this.printers = deviceprinters;
    }
}