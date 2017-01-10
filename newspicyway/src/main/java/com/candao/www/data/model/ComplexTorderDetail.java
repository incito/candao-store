package com.candao.www.data.model;

import java.math.BigDecimal;

public class ComplexTorderDetail extends TorderDetail implements Cloneable {

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


	public BigDecimal getPreAmount() {
		return preAmount;
	}

	public void setPreAmount(BigDecimal preAmount) {
		this.preAmount = preAmount;
	}

	@Override
	public ComplexTorderDetail clone() throws CloneNotSupportedException {
		return (ComplexTorderDetail) super.clone();
	}

}
