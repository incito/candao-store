package com.candao.print.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016-6-13.
 */
public class PrintData {

    private List data = new LinkedList();

    private String ipAddress;

    public void write(String obj) {
        data.add(obj);
    }

    public void write(byte[] obj) {
        data.add(obj);
    }

    public void flush() {

    }

    public void clear() {
        if (data != null && !data.isEmpty())
            data.clear();
    }

    public Object[] convert() {
        if (data != null && !data.isEmpty())
            return data.toArray(new Object[data.size()]);
        return null;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
