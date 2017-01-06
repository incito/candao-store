package com.candao.www.data.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ComplexTorderDetail extends TorderDetail {

	/**菜品类型**/
	private String level;
	/**菜品名称**/
	private String title;
	/**菜品价格**/
	private BigDecimal  debitamount;
	/**菜品优惠金额**/
	private BigDecimal preAmount;
	/**使用优惠卷类型**/
	private int  calcType;
	/**当前菜品使用了什么优惠**/
	private ArrayList<ComplexTorderDetail> listpre=new ArrayList<>();

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getDebitamount() {
		return debitamount;
	}

	public void setDebitamount(BigDecimal debitamount) {
		this.debitamount = debitamount;
	}

	public int getCalcType() {
		return calcType;
	}

	public void setCalcType(int calcType) {
		this.calcType = calcType;
	}

	public ArrayList<ComplexTorderDetail> getListpre() {
		return listpre;
	}

	public void setListpre(ArrayList<ComplexTorderDetail> listpre) {
		this.listpre = listpre;
	}

	public BigDecimal getPreAmount() {
		return preAmount;
	}

	public void setPreAmount(BigDecimal preAmount) {
		this.preAmount = preAmount;
	}



}
