package com.candao.www.data.model;

public class TPrinterDeviceprinter {
    private String id;

    private String devicecode;

    private String devicename;

    private String printerid;

    private String printername;

    private String printerip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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

    public String getPrinterid() {
        return printerid;
    }

    public void setPrinterid(String printerid) {
        this.printerid = printerid == null ? null : printerid.trim();
    }

    public String getPrintername() {
        return printername;
    }

    public void setPrintername(String printername) {
        this.printername = printername == null ? null : printername.trim();
    }

    public String getPrinterip() {
        return printerip;
    }

    public void setPrinterip(String printerip) {
        this.printerip = printerip;
    }
}