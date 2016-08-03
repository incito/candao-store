package com.candao.print.entity;

import java.io.Serializable;

/**
 * Created by liaoy on 2016/8/3.
 */
public class TipItem implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3694744087281332296L;
	private String waiterName;
    private String serviceCount;
    private String tipMoney;
    private String index;

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(String serviceCount) {
        this.serviceCount = serviceCount;
    }

    public String getTipMoney() {
        return tipMoney;
    }

    public void setTipMoney(String tipMoney) {
        this.tipMoney = tipMoney;
    }

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
