package com.candao.www.webroom.model;

/**
 * Created by liaoy on 2016/11/29.
 */
public class LogData {
    private int level;
    private String info;
    private String time;

    public LogData() {
    }

    public LogData(int level, String info, String time) {
        this.level = level;
        this.info = info;
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
