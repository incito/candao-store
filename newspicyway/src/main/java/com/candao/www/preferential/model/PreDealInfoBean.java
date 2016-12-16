package com.candao.www.preferential.model;

import java.math.BigDecimal;

/**
 * 
 * @author 梁冬 交易模型内部使用转换（业务bean）
 */
public class PreDealInfoBean {

	//优惠总额
     private BigDecimal preAmount=new BigDecimal("0");
	//是否是折上折
	private String distodis;
	
    public PreDealInfoBean() {
  
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

}
