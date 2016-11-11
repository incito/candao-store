package com.candao.www.data.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author LiangDong
 *服务小费设置
 */
public class TServiceCharge {
	private long id;//数据自增ID
	private String orderId;//订单号
	private int chargeOn;//是否开启
	private String chargeRule;//小费规则
	private BigDecimal charge;//小费金额
	private int isCustom;//是否自定义小费
	private String autho;//小费修改人
	private Date ctime;//创建时间
	private Date mtime;//修改时间

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getChargeOn() {
		return chargeOn;
	}

	public void setChargeOn(int chargeOn) {
		this.chargeOn = chargeOn;
	}

	public String getChargeRule() {
		return chargeRule;
	}

	public void setChargeRule(String chargeRule) {
		this.chargeRule = chargeRule;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}

	public int getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(int isCustom) {
		this.isCustom = isCustom;
	}

	public String getAutho() {
		return autho;
	}

	public void setAutho(String autho) {
		this.autho = autho;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getMtime() {
		return mtime;
	}

	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}
}
