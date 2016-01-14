package com.candao.www.webroom.model;

import java.math.BigDecimal;
import java.util.Date;

public class ItemReport {
	  private String  Category;
	  private String  Itemid;
	  private String  Itemdesc;
	  private double  Num;
	  private BigDecimal Share;
	  private String Statistictime;
	  private String Datetype;
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getItemid() {
		return Itemid;
	}
	public void setItemid(String itemid) {
		Itemid = itemid;
	}
	public String getItemdesc() {
		return Itemdesc;
	}
	public void setItemdesc(String itemdesc) {
		Itemdesc = itemdesc;
	}
	public double getNum() {
		return Num;
	}
	public void setNum(double num) {
		Num = num;
	}
	public String getStatistictime() {
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
	public BigDecimal getShare() {
		return Share;
	}
	public void setShare(BigDecimal share) {
		Share = share;
	}  
}

