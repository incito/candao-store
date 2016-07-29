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
	 * 操作结果 1-成功 0-失败
	 */
	private int result = 0;

	/**
	 * 结果信息。如果失败，存放失败的信息
	 */
	private String msg = "";
	/**
	 * 金额
	 */
	private BigDecimal amount = new BigDecimal(0);
	private List<TorderDetailPreferential> detailPreferentials =new ArrayList<>();

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

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

}
