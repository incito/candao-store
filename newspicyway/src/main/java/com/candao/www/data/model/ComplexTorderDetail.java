package com.candao.www.data.model;

import java.math.BigDecimal;

public class ComplexTorderDetail extends TorderDetail {

	/**菜品类型**/
	private String level;
	/**菜品名称**/
	private String title;
	/**菜品价格**/
	private BigDecimal  debitamount;

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

}
