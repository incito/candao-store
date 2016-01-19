package com.candao.www.data.model;

import java.util.Date;

public class Torder {
    private String orderid;
    
    private String username;
    
    private int  shiftid;
    
    private String ageperiod;
    
    private String meid;
    
    private int orderNum;
    
    private String memberno;
    
    private String invoice_id;
    

    public String getMemberno() {
		return memberno;
	}

	public void setMemberno(String memberno) {
		this.memberno = memberno;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}

	public String getAgeperiod() {
		return ageperiod;
	}

	public void setAgeperiod(String ageperiod) {
		this.ageperiod = ageperiod;
	}

	public int getShiftid() {
		return shiftid;
	}

	public void setShiftid(int shiftid) {
		this.shiftid = shiftid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}


	private String userid;

    private Date begintime;

    private Date endtime;

    private Integer orderstatus;

    private Integer custnum;
    
    private Integer manNum;
    
    private Integer womanNum;
    
    private Integer childNum;
    
    private String specialrequied;
    
    private String orderNo;
    
    private String tableNo;
    private int branchid;
    
    

	private String tableids;

    private String currenttableid;

    public int getBranchid() {
		return branchid;
	}

	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSpecialrequied() {
		return specialrequied;
	}

	public void setSpecialrequied(String specialrequied) {
		this.specialrequied = specialrequied;
	}

	public Integer getManNum() {
		return manNum;
	}

	public void setManNum(Integer manNum) {
		this.manNum = manNum;
	}

	public Integer getWomanNum() {
		return womanNum;
	}

	public void setWomanNum(Integer womanNum) {
		this.womanNum = womanNum;
	}

	public Integer getChildNum() {
		return childNum;
	}

	public void setChildNum(Integer childNum) {
		this.childNum = childNum;
	}


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public Integer getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(Integer orderstatus) {
        this.orderstatus = orderstatus;
    }

    public Integer getCustnum() {
        return custnum;
    }

    public void setCustnum(Integer custnum) {
        this.custnum = custnum;
    }

    public String getTableids() {
        return tableids;
    }

    public void setTableids(String tableids) {
        this.tableids = tableids;
    }

	public String getCurrenttableid() {
		return currenttableid;
	}

	public void setCurrenttableid(String currenttableid) {
		this.currenttableid = currenttableid;
	}

 
}