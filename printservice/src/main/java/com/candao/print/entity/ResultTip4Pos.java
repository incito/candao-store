package com.candao.print.entity;

import java.io.Serializable;
import java.util.List;

public class ResultTip4Pos implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3175108487245523968L;
	private String result;
    private TimeInfo time;
    private String msg;
    private List<TipItem> data;
    private String branchname;
    private String datetime;
    private String total;

    public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
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

    public TimeInfo getTime() {
        return time;
    }

    public void setTime(TimeInfo time) {
        this.time = time;
    }

    public List<TipItem> getData() {
        return data;
    }

    public void setData(List<TipItem> data) {
        this.data = data;
    }
}
