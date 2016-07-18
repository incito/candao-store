package com.candao.www.weixin.dto;

import java.io.Serializable;

public class PayDetail  implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String payWay;
		private String payAmount;
		private String memerberCardNo;
		private String bankCardNo;
		private String couponnum;
		private String couponid;
		private String coupondetailid;
		public String getPayWay() {
			return payWay;
		}
		public void setPayWay(String payWay) {
			this.payWay = payWay;
		}
		public String getPayAmount() {
			return payAmount;
		}
		public void setPayAmount(String payAmount) {
			this.payAmount = payAmount;
		}
		public String getMemerberCardNo() {
			return memerberCardNo;
		}
		public void setMemerberCardNo(String memerberCardNo) {
			this.memerberCardNo = memerberCardNo;
		}
		public String getBankCardNo() {
			return bankCardNo;
		}
		public void setBankCardNo(String bankCardNo) {
			this.bankCardNo = bankCardNo;
		}
		public String getCouponnum() {
			return couponnum;
		}
		public void setCouponnum(String couponnum) {
			this.couponnum = couponnum;
		}
		public String getCouponid() {
			return couponid;
		}
		public void setCouponid(String couponid) {
			this.couponid = couponid;
		}
		public String getCoupondetailid() {
			return coupondetailid;
		}
		public void setCoupondetailid(String coupondetailid) {
			this.coupondetailid = coupondetailid;
		}
}
