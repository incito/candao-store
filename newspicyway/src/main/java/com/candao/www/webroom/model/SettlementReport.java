package com.candao.www.webroom.model;

import java.math.BigDecimal;
import java.util.Date;

public class SettlementReport {
	  private int  Payway;
	  private String  PaywayDesc;
	  private int  Num;
	  private BigDecimal amount;
	  private Date Statistictime;
	  private String Datetype;
	public int getPayway() {
		return Payway;
	}
	public void setPayway(int payway) {
		Payway = payway;
	}
	public String getPaywayDesc() {
		return PaywayDesc;
	}
	public void setPaywayDesc(String paywayDesc) {
		PaywayDesc = paywayDesc;
	}
	public int getNum() {
		return Num;
	}
	public void setNum(int num) {
		Num = num;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getStatistictime() {
		return Statistictime;
	}
	public void setStatistictime(Date statistictime) {
		Statistictime = statistictime;
	}
	public String getDatetype() {
		return Datetype;
	}
	public void setDatetype(String datetype) {
		Datetype = datetype;
	}
}

