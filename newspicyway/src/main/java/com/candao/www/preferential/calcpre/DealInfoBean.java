package com.candao.www.preferential.calcpre;

import java.math.BigDecimal;

/**
 * 
 * @author 梁冬 交易模型内部使用转换（业务bean）
 */
public class DealInfoBean {
	// 单位
	private String unit;
	// 支付金额
	private BigDecimal payAmount;
	// 加载内容
	private Object data;
    public DealInfoBean(String unit,BigDecimal payAmount) {
    	this.unit=unit;
    	this.payAmount=payAmount;
	}
	  
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
