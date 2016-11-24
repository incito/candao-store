package com.candao.www.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author LiangDong
 *服务小费设置
 */
public class TServiceCharge  implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;//数据自增ID
	private String orderid;//订单号
	private int chargeOn;//是否开启0关闭1开启
	private int chargeType;//服务费计算方式 服务费计算方式 0比例 1 固定 2 时长
	private int chargeRateRule;//服务规则规则 '0:实收 1:应收'
	private int  chargeRate;//'比例计算方式 比率'
	private String chargeTime;//时长计算方式 时长(分钟单位)'
	private BigDecimal chargeAmount=new BigDecimal("0");//'服务费金额' 
	private int isCustom;//是否自定义服务费0系统默认 1手动修改
	private String autho;//小费修改人
	private Date ctime;//创建时间
	private Date mtime;//修改时间
	public TServiceCharge() {
	}
	public TServiceCharge(String orderid,int  chargeOn,int chargeType,int chargeRateRule,int chargeRate,String chargeTime) {
		this.orderid=orderid;
		this.chargeOn=chargeOn;
		this.chargeType=chargeType;
		this.chargeRateRule=chargeRateRule;
		this.chargeRate=chargeRate;
		this.chargeTime=chargeTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getChargeOn() {
		return chargeOn;
	}
	public void setChargeOn(int chargeOn) {
		this.chargeOn = chargeOn;
	}
	public int getChargeType() {
		return chargeType;
	}
	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}
	public int getChargeRateRule() {
		return chargeRateRule;
	}
	public void setChargeRateRule(int chargeRateRule) {
		this.chargeRateRule = chargeRateRule;
	}
	public int getChargeRate() {
		return chargeRate;
	}
	public void setChargeRate(int chargeRate) {
		this.chargeRate = chargeRate;
	}
	public String getChargeTime() {
		return chargeTime;
	}
	public void setChargeTime(String chargeTime) {
		this.chargeTime = chargeTime;
	}
	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
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
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
  
	
	
}
