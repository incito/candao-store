package com.candao.www.data.model;

import java.math.BigDecimal;
import java.util.Date;

public class Tsettlement {
    private String settledid;

    private BigDecimal debitamount;

    private BigDecimal creditamount;

    private String orderid;

    private Date inserttime;

    private BigDecimal indiscount;

    private BigDecimal outdiscount;

    private String userid;

    private String settleddetailid;
    
    private BigDecimal cashamount;
    
    private BigDecimal bankamount;
    
    private BigDecimal memeberamount;
    
    private Integer status;
    
    private int incomeType;
    
    private Date opendate;
    
    
   
	public int getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(int incomeType) {
		this.incomeType = incomeType;
	}

 

	public Date getOpendate() {
		return opendate;
	}

	public void setOpendate(Date opendate) {
		this.opendate = opendate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getCashamount() {
		return cashamount;
	}

	public void setCashamount(BigDecimal cashamount) {
		this.cashamount = cashamount;
	}

	public BigDecimal getBankamount() {
		return bankamount;
	}

	public void setBankamount(BigDecimal bankamount) {
		this.bankamount = bankamount;
	}

	public BigDecimal getMemeberamount() {
		return memeberamount;
	}

	public void setMemeberamount(BigDecimal memeberamount) {
		this.memeberamount = memeberamount;
	}

	public String getSettledid() {
        return settledid;
    }

    public void setSettledid(String settledid) {
        this.settledid = settledid;
    }

    public BigDecimal getDebitamount() {
        return debitamount;
    }

    public void setDebitamount(BigDecimal debitamount) {
        this.debitamount = debitamount;
    }

    public BigDecimal getCreditamount() {
        return creditamount;
    }

    public void setCreditamount(BigDecimal creditamount) {
        this.creditamount = creditamount;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public BigDecimal getIndiscount() {
        return indiscount;
    }

    public void setIndiscount(BigDecimal indiscount) {
        this.indiscount = indiscount;
    }

    public BigDecimal getOutdiscount() {
        return outdiscount;
    }

    public void setOutdiscount(BigDecimal outdiscount) {
        this.outdiscount = outdiscount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSettleddetailid() {
        return settleddetailid;
    }

    public void setSettleddetailid(String settleddetailid) {
        this.settleddetailid = settleddetailid;
    }
}