package com.candao.print.entity;

import java.io.Serializable;
import java.util.List;

public class ResultTip4Pos implements Serializable {
    private String resut;
    private TimeInfo time;
    private String mag;
    private List<TipItem> data;

    public String getResut() {
        return resut;
    }

    public void setResut(String resut) {
        this.resut = resut;
    }

    public TimeInfo getTime() {
        return time;
    }

    public void setTime(TimeInfo time) {
        this.time = time;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public List<TipItem> getData() {
        return data;
    }

    public void setData(List<TipItem> data) {
        this.data = data;
    }
}
