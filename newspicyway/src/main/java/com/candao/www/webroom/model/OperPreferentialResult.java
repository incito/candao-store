/**
 * 
 */
package com.candao.www.webroom.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.candao.www.data.model.TorderDetailPreferential;

/**
 * 操作 优惠的时候，返回操作结果
 * 
 * @author YHL
 *
 */
public class OperPreferentialResult {

	/**
	 * 金额优惠总金额
	 */
	private BigDecimal amount = new BigDecimal(0);
	/**
	 *菜品总价 
	 **/
	private  BigDecimal menuAmount=new BigDecimal(0);
	/**
	 * 支付金额
	 */
	private BigDecimal payamount=new BigDecimal(0);
	/***
	 * 小费金额
	 * */
	private BigDecimal tipAmount =new BigDecimal(0);
	
	/***
	 * 优免金额
	 * */
	private BigDecimal freeamount =new BigDecimal(0);
	/**
	 * 优惠账号
	 */
	private String memberno="";
	private List<TorderDetailPreferential> detailPreferentials =new ArrayList<>();

	public BigDecimal getAmount() {
		amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<TorderDetailPreferential> getDetailPreferentials() {
		return detailPreferentials;
	}

	public void setDetailPreferentials(List<TorderDetailPreferential> detailPreferentials) {
		this.detailPreferentials = detailPreferentials;
	}

	public BigDecimal getPayamount() {
		return payamount;
	}

	public void setPayamount(BigDecimal payamount) {
		this.payamount = payamount;
	}

	public BigDecimal getFreeamount() {
		return freeamount;
	}

	public void setFreeamount(BigDecimal freeamount) {
		this.freeamount = freeamount;
	}

	public BigDecimal getMenuAmount() {
		return menuAmount;
	}

	public void setMenuAmount(BigDecimal menuAmount) {
		this.menuAmount = menuAmount;
	}

	public BigDecimal getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(BigDecimal tipAmount) {
		this.tipAmount = tipAmount;
	}

	public String getMemberno() {
		return memberno;
	}

	public void setMemberno(String memberno) {
		this.memberno = memberno;
	}

}
