package com.candao.www.preferential.model;

import java.math.BigDecimal;

/**
 * 
 * @author 梁冬 交易模型内部使用转换（业务bean）
 */
public class PreDealInfoBean {

	// 优惠总额
	private BigDecimal preAmount = new BigDecimal("0");
	// 是否是折上折
	private String distodis;
	/** 实际交易金额 **/
	private BigDecimal payAmount = new BigDecimal("0");
	/***
	 * 处理的金额(四舍五入或抹零)
	 */
	private BigDecimal moneyWipeAmount=new BigDecimal("0");
	/** 交易类型名称 **/
	private String moneyWipeName="";
	/** 交易类型 **/
	private String moneyDisType="";

	public PreDealInfoBean() {

	}

	public PreDealInfoBean(BigDecimal payAmount, String moneyWipeName, String moneyDisType) {
		this.payAmount = payAmount;
		this.moneyWipeName = moneyWipeName;
		this.moneyDisType = moneyDisType;
	}
	
	public PreDealInfoBean(BigDecimal payAmount, BigDecimal moneyWipeAmount) {
		this.payAmount = payAmount;
		this.moneyWipeAmount = moneyWipeAmount;
	}

	public String getDistodis() {
		return distodis;
	}

	public void setDistodis(String distodis) {
		this.distodis = distodis;
	}

	public BigDecimal getPreAmount() {
		return preAmount;
	}

	public void setPreAmount(BigDecimal preAmount) {
		this.preAmount = preAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getMoneyWipeAmount() {
		return moneyWipeAmount;
	}

	public void setMoneyWipeAmount(BigDecimal moneyWipeAmount) {
		this.moneyWipeAmount = moneyWipeAmount;
	}

	public String getMoneyWipeName() {
		return moneyWipeName;
	}

	public void setMoneyWipeName(String moneyWipeName) {
		this.moneyWipeName = moneyWipeName;
	}

	public String getMoneyDisType() {
		return moneyDisType;
	}

	public void setMoneyDisType(String moneyDisType) {
		this.moneyDisType = moneyDisType;
	}

	

}
