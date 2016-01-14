package com.candao.www.data.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TorderDetail {
	


	private String orderdetailid;

	
	private String orderid;

	private String dishid;
	/**
	 * 0是不称重 或者已经沉过重   1是未称重
	 */
    private String dishstatus;

    private Date begintime;

    private Date endtime;

    private String sperequire;

    private String dishnum;
    
    private String userName;
    
    private BigDecimal orderprice;
    
    private String dishunit;
    
    private String orignalprice;
    
    private String pricetype;

    //存放 火锅的要打印到一张单子上的数据
    private String relatedishid;
    
    private String printtype;
    
    private int orderseq;
    /**
     * 0单品  1组合火锅   2套餐 
     */
    private String dishtype;
    
    private List<TorderDetail> dishes;
    
    //0 单品  1 火锅 2 套餐
    private int ordertype;
    //所属父类的主键
    private  String parentkey;
    //最大的主键  套餐的key
    private String superkey;
    //是否是父
    private int ismaster;
    //主键
    private String primarykey;
    
    //0 正常下单 1 加单
    private int isadddish;
    
    //0 单品 1 火锅 2 套餐
    private int childdishtype;
    /**
     * 0非锅底 1锅底
     */
    private int ispot;
    
    private Integer  discountrate;
    
    private Integer  discountamount;
    
    private String fishcode;
    
    private String status;

    private Integer payamount;
    
    private Integer predisamount;
    
    private String couponid;
    
    private String disuserid;
    
    private String islatecooke;
    
    private String discardUserId;
    
    private String discardReason;
    
    
    
    
	public String getDiscardReason() {
		return discardReason;
	}

	public void setDiscardReason(String discardReason) {
		this.discardReason = discardReason;
	}

	public String getDiscardUserId() {
		return discardUserId;
	}

	public void setDiscardUserId(String discardUserId) {
		this.discardUserId = discardUserId;
	}

	public String getIslatecooke() {
		return islatecooke;
	}

	public void setIslatecooke(String islatecooke) {
		this.islatecooke = islatecooke;
	}

	public String getDisuserid() {
		return disuserid;
	}

	public void setDisuserid(String disuserid) {
		this.disuserid = disuserid;
	}

	public String getCouponid() {
		return couponid;
	}

	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}

	public Integer getPredisamount() {
		return predisamount;
	}

	public void setPredisamount(Integer predisamount) {
		this.predisamount = predisamount;
	}

	public Integer getPayamount() {
		return payamount;
	}

	public void setPayamount(Integer payamount) {
		this.payamount = payamount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getDiscountrate() {
		return discountrate;
	}

	public void setDiscountrate(Integer discountrate) {
		this.discountrate = discountrate;
	}

	public Integer getDiscountamount() {
		return discountamount;
	}

	public void setDiscountamount(Integer discountamount) {
		this.discountamount = discountamount;
	}

	public String getFishcode() {
		return fishcode;
	}

	public void setFishcode(String fishcode) {
		this.fishcode = fishcode;
	}

	public String getOrderdetailid() {
		return orderdetailid;
	}

	public void setOrderdetailid(String orderdetailid) {
		this.orderdetailid = orderdetailid;
	}

	public int getIspot() {
		return ispot;
	}

	public void setIspot(int ispot) {
		this.ispot = ispot;
	}

 
 

	public int getChilddishtype() {
		return childdishtype;
	}

	public void setChilddishtype(int childdishtype) {
		this.childdishtype = childdishtype;
	}

	public int getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(int ordertype) {
		this.ordertype = ordertype;
	}

	public String getParentkey() {
		return parentkey;
	}

	public void setParentkey(String parentkey) {
		this.parentkey = parentkey;
	}

	public String getSuperkey() {
		return superkey;
	}

	public void setSuperkey(String superkey) {
		this.superkey = superkey;
	}

	public int getIsmaster() {
		return ismaster;
	}

	public void setIsmaster(int ismaster) {
		this.ismaster = ismaster;
	}

	public String getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(String primarykey) {
		this.primarykey = primarykey;
	}

//	public int getOrderdetailid() {
//		return orderdetailid;
//	}
//
//	public void setOrderdetailid(int orderdetailid) {
//		this.orderdetailid = orderdetailid;
//	}

	public String getDishtype() {
		return dishtype;
	}

	public void setDishtype(String dishtype) {
		this.dishtype = dishtype;
	}

	public List<TorderDetail> getDishes() {
		return dishes;
	}

	public void setDishes(List<TorderDetail> dishes) {
		this.dishes = dishes;
	}

	public int getOrderseq() {
		return orderseq;
	}

	public void setOrderseq(int orderseq) {
		this.orderseq = orderseq;
	}

	public String getPrinttype() {
		return printtype;
	}

	public void setPrinttype(String printtype) {
		this.printtype = printtype;
	}

	public String getRelatedishid() {
		return relatedishid;
	}

	public void setRelatedishid(String relatedishid) {
		this.relatedishid = relatedishid;
	}

	public String getOrignalprice() {
		return orignalprice;
	}

	public void setOrignalprice(String orignalprice) {
		this.orignalprice = orignalprice;
	}

	public String getPricetype() {
		return pricetype;
	}

	public void setPricetype(String pricetype) {
		this.pricetype = pricetype;
	}

	public String getDishunit() {
		return dishunit;
	}

	public void setDishunit(String dishunit) {
		this.dishunit = dishunit;
	}

	public BigDecimal getOrderprice() {
		return orderprice;
	}

	public void setOrderprice(BigDecimal orderprice) {
		this.orderprice = orderprice;
	}

	public String getSperequire() {
		return sperequire;
	}

	public void setSperequire(String sperequire) {
		this.sperequire = sperequire;
	}
	
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getDishid() {
        return dishid;
    }

    public void setDishid(String dishid) {
        this.dishid = dishid;
    }

    public String getDishstatus() {
        return dishstatus;
    }

    public void setDishstatus(String dishstatus) {
        this.dishstatus = dishstatus;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

 
    public String getDishnum() {
        return dishnum;
    }

    public void setDishnum(String dishnum) {
        this.dishnum = dishnum;
    }

	public int getIsadddish() {
		return isadddish;
	}

	public void setIsadddish(int isadddish) {
		this.isadddish = isadddish;
	}
    
    
}