package com.candao.print.entity;

import java.io.Serializable;

/**
 * Created by liaoy on 2016/8/2.
 */
public class TimeInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8086327651351641779L;
	private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
