package com.candao.www.data.model;

import java.math.BigDecimal;
import java.util.Date;

public class TsettlementDetail {
    private String sdetailid;

    private String orderid;

    private String dishid;

    private BigDecimal normalprice;

    private BigDecimal discountamount;

    private String discountid;

    private String couponid;

    private BigDecimal couponamount;

    private BigDecimal payamount;
    
    private String membercardno;
    
    private String bankcardno;
    
    private int incometype;
    
    private Date openDate;
    
    private String userName;
    
    private int payway;
    
    private String debitParterner;
    
    private int couponnum;
    
    private String settleid;
    
    private String coupondetailid;
    
    
    
    
    public String getCoupondetailid() {
		return coupondetailid;
	}

	public void setCoupondetailid(String coupondetailid) {
		this.coupondetailid = coupondetailid;
	}

	public String getSettleid() {
		return settleid;
	}

	public void setSettleid(String settleid) {
		this.settleid = settleid;
	}

	public int getCouponnum() {
		return couponnum;
	}

	public void setCouponnum(int couponnum) {
		this.couponnum = couponnum;
	}

	public String getDebitParterner() {
		return debitParterner;
	}

	public void setDebitParterner(String debitParterner) {
		this.debitParterner = debitParterner;
	}

	public int getIncometype() {
		return incometype;
	}

	public void setIncometype(int incometype) {
		this.incometype = incometype;
	}

	public int getPayway() {
		return payway;
	}

	public void setPayway(int payway) {
		this.payway = payway;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getMembercardno() {
		return membercardno;
	}

	public void setMembercardno(String membercardno) {
		this.membercardno = membercardno;
	}

	public String getBankcardno() {
		return bankcardno;
	}

	public void setBankcardno(String bankcardno) {
		this.bankcardno = bankcardno;
	}

	public String getSdetailid() {
        return sdetailid;
    }

    public void setSdetailid(String sdetailid) {
        this.sdetailid = sdetailid;
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

    public BigDecimal getNormalprice() {
        return normalprice;
    }

    public void setNormalprice(BigDecimal normalprice) {
        this.normalprice = normalprice;
    }

    public BigDecimal getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(BigDecimal discountamount) {
        this.discountamount = discountamount;
    }

    public String getDiscountid() {
        return discountid;
    }

    public void setDiscountid(String discountid) {
        this.discountid = discountid;
    }

    public String getCouponid() {
        return couponid;
    }

    public void setCouponid(String couponid) {
        this.couponid = couponid;
    }

    public BigDecimal getCouponamount() {
        return couponamount;
    }

    public void setCouponamount(BigDecimal couponamount) {
        this.couponamount = couponamount;
    }

    public BigDecimal getPayamount() {
        return payamount;
    }

    public void setPayamount(BigDecimal payamount) {
        this.payamount = payamount;
    }
}