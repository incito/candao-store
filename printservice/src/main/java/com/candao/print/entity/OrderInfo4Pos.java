package com.candao.print.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OrderInfo4Pos implements Serializable,Cloneable{

    /**
     *
     */
    private static final long serialVersionUID = -4969358105127908050L;
    //会员消费类型
    private String type;

    private String inflated;
    private String business;
    private String businessname;
    private String cardno;
    private String coupons;
    private String couponsbalance;
    private String netvalue;
    private String operatetype;
    private String ordertime;
    private String orderday;
    private String psexpansivity;
    private String score;
    private String scorebalance;
    private String serial;
    private String stored;
    private String storedbalance;
    private String terminal;
    private String valid;
    private String batchno;

    private String totalMoney;
    private String accountsReceivableTotal;
    private String authorizer;
    private String classNo;
    private String drinks;
    private String drinksSmokeNoodle;
    private String id;
    private String includedMoneyTotal;
    private String ipaddress;
    private String itemMoney;
    private String lastNonDeposit;
    private String lastNonTable;
    private String lowConsComp;
    private String noIncludedMoneyTotal;
    private String operatorID;
    private String operatorName;
    private String posID;
    private String preferenceMoney;
    private String prettyCash;
    private String priterTime;
    private String ratedPreferenceMoney;
    private String roomMoney;
    private String serviceMoney;
    private String tBeginPeople;
    private String tClosingPeople;
    private String tBeginTableTotal;
    private String tClosingTable;
    private String tNonClosingDeposit;
    private String tNonClosingMoney;
    private String tNonClosingTable;
    private String tPresentedMoney;
    private String tableware;
    private String todayTurnover;
    private String vIn;
    private String vOut;
    
    private String tRFoodMoney;
    
    private String tipTotalAmount;

    private String ageperiod;

    private String areaNo;

    private String areaname;

    private String befprintcount;

    private String begintime;

    private String branchid;

    private String childNum;

    private String closeshiftid;

    private String couponname;

    private String couponname3;

    private String currenttableid;

    private String custnum;

    private String discountamount;

    private String disuserid;

    private String dueamount;

    private String endtime;

    private String freeamount;

    private String fullName;

    private String fulldiscountrate;

    private String gift_status;

    private String gzamount;

    private String gzcode;

    private String gzname;

    private String gztele;

    private String gzuser;

    private String invoice_id;

    private String mannum;

    private String meid;

    private String memberno;

    private String orderid;

    private String orderseq;

    private String orderstatus;

    private String ordertype;

    private String partnername;

    private String payamount;

    private String payamount2;

    private String payway;

    private String pnum;

    private String printcount;

    private String relateorderid;

    private String shiftid;

    private String specialrequied;

    private String ssamount;

    private String tableName;

    private String tableids;

    private String tipAmount;

    private String tipPaid;

    private String userid;

    private String wipeamount;

    private String womanNum;
    
    private String removeMoney;
    
    private String workdate;

    private String ymamount;

    private String zdAmount;
    
    private String accountsReceivableSubtotal;

    public List<Map<String, Object>> getPrefers() {
        return prefers;
    }

    public void setPrefers(List<Map<String, Object>> prefers) {
        this.prefers = prefers;
    }

    private List<Map<String,Object>> prefers;

    public List<String> getPreferenceDetail() {
        return preferenceDetail;
    }

    public void setPreferenceDetail(List<String> preferenceDetail) {
        this.preferenceDetail = preferenceDetail;
    }

    private List<String> preferenceDetail;

    public String getAgeperiod() {
        return ageperiod;
    }

    public void setAgeperiod(String ageperiod) {
        this.ageperiod = ageperiod;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getBefprintcount() {
        return befprintcount;
    }

    public void setBefprintcount(String befprintcount) {
        this.befprintcount = befprintcount;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getChildNum() {
        return childNum;
    }

    public void setChildNum(String childNum) {
        this.childNum = childNum;
    }

    public String getCloseshiftid() {
        return closeshiftid;
    }

    public void setCloseshiftid(String closeshiftid) {
        this.closeshiftid = closeshiftid;
    }

    public String getCouponname() {
        return couponname;
    }

    public void setCouponname(String couponname) {
        this.couponname = couponname;
    }

    public String getCouponname3() {
        return couponname3;
    }

    public void setCouponname3(String couponname3) {
        this.couponname3 = couponname3;
    }

    public String getCurrenttableid() {
        return currenttableid;
    }

    public void setCurrenttableid(String currenttableid) {
        this.currenttableid = currenttableid;
    }

    public String getCustnum() {
        return custnum;
    }

    public void setCustnum(String custnum) {
        this.custnum = custnum;
    }

    public String getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(String discountamount) {
        this.discountamount = discountamount;
    }

    public String getDisuserid() {
        return disuserid;
    }

    public void setDisuserid(String disuserid) {
        this.disuserid = disuserid;
    }

    public String getDueamount() {
        return dueamount;
    }

    public void setDueamount(String dueamount) {
        this.dueamount = dueamount;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getFreeamount() {
        return freeamount;
    }

    public void setFreeamount(String freeamount) {
        this.freeamount = freeamount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFulldiscountrate() {
        return fulldiscountrate;
    }

    public void setFulldiscountrate(String fulldiscountrate) {
        this.fulldiscountrate = fulldiscountrate;
    }

    public String getGift_status() {
        return gift_status;
    }

    public void setGift_status(String gift_status) {
        this.gift_status = gift_status;
    }

    public String getGzamount() {
        return gzamount;
    }

    public void setGzamount(String gzamount) {
        this.gzamount = gzamount;
    }

    public String getGzcode() {
        return gzcode;
    }

    public void setGzcode(String gzcode) {
        this.gzcode = gzcode;
    }

    public String getGzname() {
        return gzname;
    }

    public void setGzname(String gzname) {
        this.gzname = gzname;
    }

    public String getGztele() {
        return gztele;
    }

    public void setGztele(String gztele) {
        this.gztele = gztele;
    }

    public String getGzuser() {
        return gzuser;
    }

    public void setGzuser(String gzuser) {
        this.gzuser = gzuser;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getMannum() {
        return mannum;
    }

    public void setMannum(String mannum) {
        this.mannum = mannum;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getMemberno() {
        return memberno;
    }

    public void setMemberno(String memberno) {
        this.memberno = memberno;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderseq() {
        return orderseq;
    }

    public void setOrderseq(String orderseq) {
        this.orderseq = orderseq;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getPartnername() {
        return partnername;
    }

    public void setPartnername(String partnername) {
        this.partnername = partnername;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getPayamount2() {
        return payamount2;
    }

    public void setPayamount2(String payamount2) {
        this.payamount2 = payamount2;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getPrintcount() {
        return printcount;
    }

    public void setPrintcount(String printcount) {
        this.printcount = printcount;
    }

    public String getRelateorderid() {
        return relateorderid;
    }

    public void setRelateorderid(String relateorderid) {
        this.relateorderid = relateorderid;
    }

    public String getShiftid() {
        return shiftid;
    }

    public void setShiftid(String shiftid) {
        this.shiftid = shiftid;
    }

    public String getSpecialrequied() {
        return specialrequied;
    }

    public void setSpecialrequied(String specialrequied) {
        this.specialrequied = specialrequied;
    }

    public String getSsamount() {
        return ssamount;
    }

    public void setSsamount(String ssamount) {
        this.ssamount = ssamount;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableids() {
        return tableids;
    }

    public void setTableids(String tableids) {
        this.tableids = tableids;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getTipPaid() {
        return tipPaid;
    }

    public void setTipPaid(String tipPaid) {
        this.tipPaid = tipPaid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getWipeamount() {
        return wipeamount;
    }

    public void setWipeamount(String wipeamount) {
        this.wipeamount = wipeamount;
    }

    public String getWomanNum() {
        return womanNum;
    }

    public void setWomanNum(String womanNum) {
        this.womanNum = womanNum;
    }

    public String getWorkdate() {
        return workdate;
    }

    public void setWorkdate(String workdate) {
        this.workdate = workdate;
    }

    public String getYmamount() {
        return ymamount;
    }

    public void setYmamount(String ymamount) {
        this.ymamount = ymamount;
    }

    public String getZdAmount() {
        return zdAmount;
    }

    public void setZdAmount(String zdAmount) {
        this.zdAmount = zdAmount;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getAccountsReceivableTotal() {
        return accountsReceivableTotal;
    }

    public void setAccountsReceivableTotal(String accountsReceivableTotal) {
        this.accountsReceivableTotal = accountsReceivableTotal;
    }

    public String getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(String authorizer) {
        this.authorizer = authorizer;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getDrinks() {
        return drinks;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }

    public String getDrinksSmokeNoodle() {
        return drinksSmokeNoodle;
    }

    public void setDrinksSmokeNoodle(String drinksSmokeNoodle) {
        this.drinksSmokeNoodle = drinksSmokeNoodle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncludedMoneyTotal() {
        return includedMoneyTotal;
    }

    public void setIncludedMoneyTotal(String includedMoneyTotal) {
        this.includedMoneyTotal = includedMoneyTotal;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getItemMoney() {
        return itemMoney;
    }

    public void setItemMoney(String itemMoney) {
        this.itemMoney = itemMoney;
    }

    public String getLastNonDeposit() {
        return lastNonDeposit;
    }

    public void setLastNonDeposit(String lastNonDeposit) {
        this.lastNonDeposit = lastNonDeposit;
    }

    public String getLastNonTable() {
        return lastNonTable;
    }

    public void setLastNonTable(String lastNonTable) {
        this.lastNonTable = lastNonTable;
    }

    public String getLowConsComp() {
        return lowConsComp;
    }

    public void setLowConsComp(String lowConsComp) {
        this.lowConsComp = lowConsComp;
    }

    public String getNoIncludedMoneyTotal() {
        return noIncludedMoneyTotal;
    }

    public void setNoIncludedMoneyTotal(String noIncludedMoneyTotal) {
        this.noIncludedMoneyTotal = noIncludedMoneyTotal;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getPosID() {
        return posID;
    }

    public void setPosID(String posID) {
        this.posID = posID;
    }

    public String getPreferenceMoney() {
        return preferenceMoney;
    }

    public void setPreferenceMoney(String preferenceMoney) {
        this.preferenceMoney = preferenceMoney;
    }

    public String getPrettyCash() {
        return prettyCash;
    }

    public void setPrettyCash(String prettyCash) {
        this.prettyCash = prettyCash;
    }

    public String getPriterTime() {
        return priterTime;
    }

    public void setPriterTime(String priterTime) {
        this.priterTime = priterTime;
    }

    public String getRatedPreferenceMoney() {
        return ratedPreferenceMoney;
    }

    public void setRatedPreferenceMoney(String ratedPreferenceMoney) {
        this.ratedPreferenceMoney = ratedPreferenceMoney;
    }

    public String getRoomMoney() {
        return roomMoney;
    }

    public void setRoomMoney(String roomMoney) {
        this.roomMoney = roomMoney;
    }

    public String getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(String serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public String getTBeginPeople() {
        return tBeginPeople;
    }

    public void setTBeginPeople(String tBeginPeople) {
        this.tBeginPeople = tBeginPeople;
    }

    public String getTClosingPeople() {
        return tClosingPeople;
    }

    public void setTClosingPeople(String tClosingPeople) {
        this.tClosingPeople = tClosingPeople;
    }

    public String getTBeginTableTotal() {
        return tBeginTableTotal;
    }

    public void setTBeginTableTotal(String tBeginTableTotal) {
        this.tBeginTableTotal = tBeginTableTotal;
    }

    public String getTClosingTable() {
        return tClosingTable;
    }

    public void setTClosingTable(String tClosingTable) {
        this.tClosingTable = tClosingTable;
    }

    public String getTNonClosingDeposit() {
        return tNonClosingDeposit;
    }

    public void setTNonClosingDeposit(String tNonClosingDeposit) {
        this.tNonClosingDeposit = tNonClosingDeposit;
    }

    public String getTNonClosingMoney() {
        return tNonClosingMoney;
    }

    public void setTNonClosingMoney(String tNonClosingMoney) {
        this.tNonClosingMoney = tNonClosingMoney;
    }

    public String getTNonClosingTable() {
        return tNonClosingTable;
    }

    public void setTNonClosingTable(String tNonClosingTable) {
        this.tNonClosingTable = tNonClosingTable;
    }

    public String getTPresentedMoney() {
        return tPresentedMoney;
    }

    public void setTPresentedMoney(String tPresentedMoney) {
        this.tPresentedMoney = tPresentedMoney;
    }

    public String getTableware() {
        return tableware;
    }

    public void setTableware(String tableware) {
        this.tableware = tableware;
    }

    public String getTodayTurnover() {
        return todayTurnover;
    }

    public void setTodayTurnover(String todayTurnover) {
        this.todayTurnover = todayTurnover;
    }

    public String getVIn() {
        return vIn;
    }

    public void setVIn(String vIn) {
        this.vIn = vIn;
    }

    public String getVOut() {
        return vOut;
    }

    public void setVOut(String vOut) {
        this.vOut = vOut;
    }

    public String getInflated() {
        return inflated;
    }

    public void setInflated(String inflated) {
        this.inflated = inflated;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }

    public String getCouponsbalance() {
        return couponsbalance;
    }

    public void setCouponsbalance(String couponsbalance) {
        this.couponsbalance = couponsbalance;
    }

    public String getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(String netvalue) {
        this.netvalue = netvalue;
    }

    public String getOperatetype() {
        return operatetype;
    }

    public void setOperatetype(String operatetype) {
        this.operatetype = operatetype;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getPsexpansivity() {
        return psexpansivity;
    }

    public void setPsexpansivity(String psexpansivity) {
        this.psexpansivity = psexpansivity;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScorebalance() {
        return scorebalance;
    }

    public void setScorebalance(String scorebalance) {
        this.scorebalance = scorebalance;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getStored() {
        return stored;
    }

    public void setStored(String stored) {
        this.stored = stored;
    }

    public String getStoredbalance() {
        return storedbalance;
    }

    public void setStoredbalance(String storedbalance) {
        this.storedbalance = storedbalance;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTRFoodMoney() {
		return tRFoodMoney;
	}

	public void setTRFoodMoney(String tRFoodMoney) {
		this.tRFoodMoney = tRFoodMoney;
	}

	public String getTipTotalAmount() {
		return tipTotalAmount;
	}

	public void setTipTotalAmount(String tipTotalAmount) {
		this.tipTotalAmount = tipTotalAmount;
	}

	public String getAccountsReceivableSubtotal() {
		return accountsReceivableSubtotal;
	}

	public void setAccountsReceivableSubtotal(String accountsReceivableSubtotal) {
		this.accountsReceivableSubtotal = accountsReceivableSubtotal;
	}

	public String getRemoveMoney() {
		return removeMoney;
	}

	public void setRemoveMoney(String removeMoney) {
		this.removeMoney = removeMoney;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getOrderday() {
		return orderday;
	}

	public void setOrderday(String orderday) {
		this.orderday = orderday;
	}

}
