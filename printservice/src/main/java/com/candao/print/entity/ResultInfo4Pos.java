package com.candao.print.entity;

import java.io.Serializable;
import java.util.List;

public class ResultInfo4Pos implements Serializable {
    private String resut;
    private TimeInfo time;
    private String mag;
    private List<DishItem> data;
    private String branchname;
    private String datetime;
    private String total;

    public String getBranname() {
		return branchname;
	}

	public void setBranname(String branname) {
		this.branchname = branname;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

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
