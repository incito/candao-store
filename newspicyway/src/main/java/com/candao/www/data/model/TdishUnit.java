package com.candao.www.data.model;

import java.math.BigDecimal;
import java.util.Date;

public class TdishUnit {
	/**
	 * 主键id
	 */
    private String id;
    /**
     * 菜品id
     */
    private String dishid;
    /**
     * 单位id
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
     * 状态
     */
    private Integer status;
    /**
     * 已点人数
     */
    private Integer ordernum;
    /**
     * 0 正常价格 1 会员价格 
     */
    private Integer pricetype;
    
    /**
     * 价格描述 备用字段
     */
    private String pricedesc;
    /**
     * 插入时间
     */
    private Date inserttime;
    /**
     * 插入人员id
     */
    private String insertuserid;
    /**
     * 分店id
     */
    private Integer branchid;
    /**
     * 区域ID
     */
    private Integer areaid;
    
    
    public Integer getPricetype() {
		return pricetype;
	}

	public void setPricetype(Integer pricetype) {
		this.pricetype = pricetype;
	}

	public String getPricedesc() {
		return pricedesc;
	}

	public void setPricedesc(String pricedesc) {
		this.pricedesc = pricedesc;
	}

	public Date getInserttime() {
		return inserttime;
	}

	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}

	public String getInsertuserid() {
		return insertuserid;
	}

	public void setInsertuserid(String insertuserid) {
		this.insertuserid = insertuserid;
	}

	public Integer getBranchid() {
		return branchid;
	}

	public void setBranchid(Integer branchid) {
		this.branchid = branchid;
	}

	public Integer getAreaid() {
		return areaid;
	}

	public void setAreaid(Integer areaid) {
		this.areaid = areaid;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }
}