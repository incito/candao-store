package com.candao.print.entity;

import java.io.Serializable;

/**
 * Created by liaoy on 2016/8/2.
 */
public class DishItem implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2890521988785242265L;
	private String dishName;
    private String totlePrice;
    private String dishCount;
    private String index;

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getTotlePrice() {
        return totlePrice;
    }

    public void setTotlePrice(String totlePrice) {
        this.totlePrice = totlePrice;
    }

    public String getDishCount() {
        return dishCount;
    }

    public void setDishCount(String dishCount) {
        this.dishCount = dishCount;
    }

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
