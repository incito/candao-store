package com.candao.www.webroom.model;


public class BusinessReport1 {
	  private String Shouldamount="0";
	  private String Paidinamount="0";
	  private String Discountamount="0";
	  private String Discount="0";
	  private String Money="0";
	  private String Card="0";
	  private String ICBC="0";
	  private String otherbank="0";
	  private String weixin = "0";
	  private String zhifubao = "0";
	  private String Merbervaluenet="0";
	  private String Total="0";
	  private String MeberTicket="0";
	  private String Integralconsum="0";
	  private String Mebervalueadd="0";
	  private String Mebercousumcollet="0";
	  private String Tablenum="0";
	  private String Settlementnum="0";
	  private String Avgconsumtime="0";
	  private String Attendance="0";
	  private String Shouldaverage="0";
	  private String Paidinaverage="0";
	  private String tableconsumption="0";
	  private String Overtaiwan="0";
	  private String Bastfree="0";
	  private String Discountmoney="0";
	  private String Malingincom="0";
	  private String Statistictime="0";
	  private String Datetype="0";
    private String give = "0";

	private String shouldamountNormal="0"; //    #营业数据统计(堂吃应收）
	private String shouldamountTakeout="0";  //   外卖统计(应收）
	private String paidinamountTakeout="0";    //  #外卖统计(实收）
	private String ordercountTakeout="0";        //  // #外卖统计(订单数）
	private String avgpriceTakeout="0";       //  	#外卖统计(订单平均价格）
	private String membertotal="0";
	private String vipordercount="0";     //会员消费笔数
	private String viporderpercent="0";     //会员消费占比
	private String handerWay="";     //零头处理方式名称
	private String handervalue="";     //零头处理方式值
	
	private String closedordermums = "0";  //已结账单数
    private String closedordershouldamount = "0";  //已结账单应收
    private String closedorderpersonnums = "0";  //已结人数
    private String nobillnums = "0"; //未结账单数
    private String nobillshouldamount = "0"; //未结账单应收
    private String nopersonnums = "0"; //未结人数
    private String billnums = "0"; //全部账单数
    private String billshouldamount = "0"; //全部账单应收
    private String personnums = "0"; //全部人数
    private String zaitaishu = "0"; //在台数
    private String kaitaishu = "0"; //开台数

	public String getClosedordermums() {
		return closedordermums;
	}

	public void setClosedordermums(String closedordermums) {
		this.closedordermums = closedordermums;
	}

	public String getClosedordershouldamount() {
		return closedordershouldamount;
	}

	public void setClosedordershouldamount(String closedordershouldamount) {
		this.closedordershouldamount = closedordershouldamount;
	}

	public String getClosedorderpersonnums() {
		return closedorderpersonnums;
	}

	public void setClosedorderpersonnums(String closedorderpersonnums) {
		this.closedorderpersonnums = closedorderpersonnums;
	}

	public String getNobillnums() {
		return nobillnums;
	}

	public void setNobillnums(String nobillnums) {
		this.nobillnums = nobillnums;
	}

	public String getNobillshouldamount() {
		return nobillshouldamount;
	}

	public void setNobillshouldamount(String nobillshouldamount) {
		this.nobillshouldamount = nobillshouldamount;
	}

	public String getNopersonnums() {
		return nopersonnums;
	}

	public void setNopersonnums(String nopersonnums) {
		this.nopersonnums = nopersonnums;
	}

	public String getBillnums() {
		return billnums;
	}

	public void setBillnums(String billnums) {
		this.billnums = billnums;
	}

	public String getBillshouldamount() {
		return billshouldamount;
	}

	public void setBillshouldamount(String billshouldamount) {
		this.billshouldamount = billshouldamount;
	}

	public String getPersonnums() {
		return personnums;
	}

	public void setPersonnums(String personnums) {
		this.personnums = personnums;
	}

	public String getZaitaishu() {
		return zaitaishu;
	}

	public void setZaitaishu(String zaitaishu) {
		this.zaitaishu = zaitaishu;
	}

	public String getKaitaishu() {
		return kaitaishu;
	}

	public void setKaitaishu(String kaitaishu) {
		this.kaitaishu = kaitaishu;
	}

	public String getHanderWay() {
		return handerWay;
	}

	public void setHanderWay(String handerWay) {
		this.handerWay = handerWay;
	}

	public String getHandervalue() {
		return handervalue;
	}

	public void setHandervalue(String handervalue) {
		this.handervalue = handervalue;
	}

	public String getVipordercount() {
		return vipordercount;
	}

	public void setVipordercount(String vipordercount) {
		this.vipordercount = vipordercount;
	}

	public String getViporderpercent() {
		return viporderpercent;
	}

	public void setViporderpercent(String viporderpercent) {
		this.viporderpercent = viporderpercent;
	}

	public String getMembertotal() {
		return membertotal;
	}

	public void setMembertotal(String membertotal) {
		this.membertotal = membertotal;
	}

	public String getShouldamountNormal() {
		return shouldamountNormal;
	}

	public void setShouldamountNormal(String shouldamountNormal) {
		this.shouldamountNormal = shouldamountNormal;
	}

	public String getShouldamountTakeout() {
		return shouldamountTakeout;
	}

	public void setShouldamountTakeout(String shouldamountTakeout) {
		this.shouldamountTakeout = shouldamountTakeout;
	}

	public String getPaidinamountTakeout() {
		return paidinamountTakeout;
	}

	public void setPaidinamountTakeout(String paidinamountTakeout) {
		this.paidinamountTakeout = paidinamountTakeout;
	}

	public String getOrdercountTakeout() {
		return ordercountTakeout;
	}

	public void setOrdercountTakeout(String ordercountTakeout) {
		this.ordercountTakeout = ordercountTakeout;
	}

	public String getAvgpriceTakeout() {
		return avgpriceTakeout;
	}

	public void setAvgpriceTakeout(String avgpriceTakeout) {
		this.avgpriceTakeout = avgpriceTakeout;
	}

	public String getShouldamount() {
		return Shouldamount;
	}
	public void setShouldamount(String shouldamount) {
		Shouldamount = shouldamount;
	}
	public String getPaidinamount() {
		return Paidinamount;
	}
	public void setPaidinamount(String paidinamount) {
		Paidinamount = paidinamount;
	}
	public String getDiscountamount() {
		return Discountamount;
	}
	public void setDiscountamount(String discountamount) {
		Discountamount = discountamount;
	}
	public String getDiscount() {
		return Discount;
	}
	public void setDiscount(String discount) {
		Discount = discount;
	}
	public String getMoney() {
		return Money;
	}
	public void setMoney(String money) {
		Money = money;
	}
	public String getCard() {
		return Card;
	}
	public void setCard(String card) {
		Card = card;
	}
	public String getICBC() {
		return ICBC;
	}
	public void setICBC(String iCBC) {
		ICBC = iCBC;
	}
	public String getOtherbank() {
		return otherbank;
	}
	public void setOtherbank(String otherbank) {
		this.otherbank = otherbank;
	}
	public String getMerbervaluenet() {
		return Merbervaluenet;
	}
	public void setMerbervaluenet(String merbervaluenet) {
		Merbervaluenet = merbervaluenet;
	}
	public String getTotal() {
		return Total;
	}
	public void setTotal(String total) {
		Total = total;
	}
	public String getMeberTicket() {
		return MeberTicket;
	}
	public void setMeberTicket(String meberTicket) {
		MeberTicket = meberTicket;
	}
	public String getIntegralconsum() {
		return Integralconsum;
	}
	public void setIntegralconsum(String integralconsum) {
		Integralconsum = integralconsum;
	}
	public String getMebervalueadd() {
		return Mebervalueadd;
	}
	public void setMebervalueadd(String mebervalueadd) {
		Mebervalueadd = mebervalueadd;
	}
	public String getMebercousumcollet() {
		return Mebercousumcollet;
	}
	public void setMebercousumcollet(String mebercousumcollet) {
		Mebercousumcollet = mebercousumcollet;
	}
	public String getTablenum() {
		return Tablenum;
	}
	public void setTablenum(String tablenum) {
		Tablenum = tablenum;
	}
	public String getSettlementnum() {
		return Settlementnum;
	}
	public void setSettlementnum(String settlementnum) {
		Settlementnum = settlementnum;
	}
	public String getAvgconsumtime() {
		return Avgconsumtime;
	}
	public void setAvgconsumtime(String avgconsumtime) {
		Avgconsumtime = avgconsumtime;
	}
	public String getAttendance() {
		return Attendance;
	}
	public void setAttendance(String attendance) {
		Attendance = attendance;
	}
	public String getShouldaverage() {
		return Shouldaverage;
	}
	public void setShouldaverage(String shouldaverage) {
		Shouldaverage = shouldaverage;
	}
	public String getPaidinaverage() {
		return Paidinaverage;
	}
	public void setPaidinaverage(String paidinaverage) {
		Paidinaverage = paidinaverage;
	}
	public String getTableconsumption() {
		return tableconsumption;
	}
	public void setTableconsumption(String tableconsumption) {
		this.tableconsumption = tableconsumption;
	}
	public String getOvertaiwan() {
		return Overtaiwan;
	}
	public void setOvertaiwan(String overtaiwan) {
		Overtaiwan = overtaiwan;
	}
	public String getBastfree() {
		return Bastfree;
	}
	public void setBastfree(String bastfree) {
		Bastfree = bastfree;
	}
	public String getDiscountmoney() {
		return Discountmoney;
	}
	public void setDiscountmoney(String discountmoney) {
		Discountmoney = discountmoney;
	}
	public String getMalingincom() {
		return Malingincom;
	}
	public void setMalingincom(String malingincom) {
		Malingincom = malingincom;
	}
	public String getDatetype() {
		return Datetype;
	}
	public void setDatetype(String datetype) {
		Datetype = datetype;
	}
	public String getStatistictime() {
		return Statistictime;
	}
	public void setStatistictime(String statistictime) {
		Statistictime = statistictime;
	}

    public String getGive() {
        return give;
    }

    public void setGive(String give) {
        this.give = give;
    }

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getZhifubao() {
		return zhifubao;
	}

	public void setZhifubao(String zhifubao) {
		this.zhifubao = zhifubao;
	}
    
}

