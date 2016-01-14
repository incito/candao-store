package com.candao.www.webroom.model;

import java.math.BigDecimal;

public class PreferentialReport {
	  private String  Activitytype;
	  private String  Activitytame;
	  private String  name;
	  private String  PaywayDesc;
	  private int  Payway;
	  private int  Num;
	  private BigDecimal Amount;
	  private BigDecimal Shouldamount;
	  private BigDecimal Paidinamount;
	  private String  Statistictime;
	  private String Datetype;
	public String getActivitytype() {
		return Activitytype;
	}
	public void setActivitytype(String activitytype) {
		Activitytype = activitytype;
	}
	public String getActivitytame() {
		return Activitytame;
	}
	public void setActivitytame(String activitytame) {
		Activitytame = activitytame;
	}
	public String getPaywayDesc() {
		return PaywayDesc;
	}
	public void setPaywayDesc(String paywayDesc) {
		PaywayDesc = paywayDesc;
	}
	public int getPayway() {
		return Payway;
	}
	public void setPayway(int payway) {
		Payway = payway;
	}
	public int getNum() {
		return Num;
	}
	public void setNum(int num) {
		Num = num;
	}
	public BigDecimal getAmount() {
		return Amount;
	}
	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}
	public BigDecimal getShouldamount() {
		return Shouldamount;
	}
	public void setShouldamount(BigDecimal shouldamount) {
		Shouldamount = shouldamount;
	}
	public BigDecimal getPaidinamount() {
		return Paidinamount;
	}
	public void setPaidinamount(BigDecimal paidinamount) {
		Paidinamount = paidinamount;
	}
	public String  getStatistictime() {
		return Statistictime;
	}
	public void setStatistictime(String statistictime) {
		Statistictime = statistictime;
	}
	public String getDatetype() {
		return Datetype;
	}
	public void setDatetype(String datetype) {
		Datetype = datetype;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

