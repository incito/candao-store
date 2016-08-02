package com.candao.print.entity;

import java.io.Serializable;
import java.util.List;

public class ResultInfo4Pos implements Serializable {
    private String resut;
    private TimeInfo time;
    private String mag;
    private List<DishItem> data;

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

    public List<DishItem> getData() {
        return data;
    }

    public void setData(List<DishItem> data) {
        this.data = data;
    }
}
