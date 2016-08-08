package com.candao.print.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016-6-13.
 */
public class PrintData implements Serializable{
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 2979473667712649065L;

	private List<Object> data = new LinkedList<Object>();

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
