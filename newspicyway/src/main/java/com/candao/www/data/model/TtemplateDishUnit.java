package com.candao.www.data.model;

import java.math.BigDecimal;

public class TtemplateDishUnit {
	/**
	 * 主键
	 */
    private String id;
    /**
	 * 菜品id
	 */
    private String dishid;
    /**
	 * 菜谱id
	 */
    private String menuid;
    /**
	 * 该菜品在该菜谱中的名称
	 */
    private String dishname;
    /**
	 * 该菜品在该菜谱中的简介
	 */
    private String dishintroduction;
    /**
	 * 单位
	 */
    private String unit;
    /**
	 * 价格
	 */
    private BigDecimal price;
    /**
	 * 会员价
	 */
    private BigDecimal vipprice;
    /**
     * 店长推荐（0不推荐1推荐）
     */
    private int recommend;
    /**
     * 状态，估清状态（1估清0非估清）
     */
    private int status;
    /**
     * 店长推荐的顺序
     */
    private int sort;
    private int ispot;

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIspot() {
		return ispot;
	}

	public void setIspot(int ispot) {
		this.ispot = ispot;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDishid() {
        return dishid;
    }

    public void setDishid(String dishid) {
        this.dishid = dishid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
    }

    public String getDishintroduction() {
        return dishintroduction;
    }

    public void setDishintroduction(String dishintroduction) {
        this.dishintroduction = dishintroduction;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getVipprice() {
        return vipprice;
    }

    public void setVipprice(BigDecimal vipprice) {
        this.vipprice = vipprice;
    }
}