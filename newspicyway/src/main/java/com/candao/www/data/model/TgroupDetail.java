package com.candao.www.data.model;

import java.util.Date;
import java.util.List;

public class TgroupDetail {
    private String id;
    /**
     * 组合id
     */
    private String groupid;
    /**
     * 组合中菜的id
     */
    private String contactdishid;
    private String contactdishname;

    private Date inserttime;

    private String insertuserid;
    private String price;
    private String columnid;
    /**
     * 排序
     */
    private Integer ordernum;

    private Integer status;
    /**
     * 单位  汉字
     */
    private String dishunitid;
    /**
     * 多计量单位标示    标识这个菜是否是多计量的   0有多计量   1没有多计量
     */
    private String unitflag;
    /**
     * 份数
     */
    private String dishnum;
    /**
     * 套餐id
     */
    private String dishid;
    /**
     * 0单品 1鱼锅 2套餐
     */
    private int dishtype;
    
    private String vipprice;
    
    /**
	 * 是否称重
	 */
	private Integer weigh;
	
    public Integer getWeigh() {
		return weigh;
	}

	public void setWeigh(Integer weigh) {
		this.weigh = weigh;
	}

	public String getVipprice() {
		return vipprice;
	}

	public void setVipprice(String vipprice) {
		this.vipprice = vipprice;
	}

	public String getColumnid() {
		return columnid;
	}

	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	private int ispot;
    
    
    
    public int getIspot() {
		return ispot;
	}

	public void setIspot(int ispot) {
		this.ispot = ispot;
	}

	public String getUnitflag() {
		return unitflag;
	}

	public void setUnitflag(String unitflag) {
		this.unitflag = unitflag;
	}

	public String getContactdishname() {
		return contactdishname;
	}

	public void setContactdishname(String contactdishname) {
		this.contactdishname = contactdishname;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	private List<TgroupDetail> list;


	public List<TgroupDetail> getList() {
		return list;
	}

	public void setList(List<TgroupDetail> list) {
		this.list = list;
	}

	public int getDishtype() {
		return dishtype;
	}

	public void setDishtype(int dishtype) {
		this.dishtype = dishtype;
	}

	public String getDishid() {
		return dishid;
	}

	public void setDishid(String dishid) {
		this.dishid = dishid;
	}


    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getContactdishid() {
        return contactdishid;
    }

    public void setContactdishid(String contactdishid) {
        this.contactdishid = contactdishid;
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

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDishunitid() {
        return dishunitid;
    }

    public void setDishunitid(String dishunitid) {
        this.dishunitid = dishunitid;
    }

    public String getDishnum() {
        return dishnum;
    }

    public void setDishnum(String dishnum) {
        this.dishnum = dishnum;
    }
}