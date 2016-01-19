package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 礼物表
 * @author weizhifang
 * @since 2015-11-11
 *
 */
public class TbGift implements Serializable {

	private static final long serialVersionUID = -6428894631379100621L;
	
	private String id;           //主键
	private String giftNo;       //礼物编号
	private String giftTypeId;   //礼物分类编号
	private String giftName;     //礼物名称 
	private String giftUnit;     //礼物单位
	private Float giftPrice;     //礼物价格
	private Float memberPrice;   //礼物会员价
	private String imageUrl;     //礼物地址
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGiftNo() {
		return giftNo;
	}
	public void setGiftNo(String giftNo) {
		this.giftNo = giftNo;
	}
	public String getGiftTypeId() {
		return giftTypeId;
	}
	public void setGiftTypeId(String giftTypeId) {
		this.giftTypeId = giftTypeId;
	}
	public String getGiftName() {
		return giftName;
	}
	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}
	public String getGiftUnit() {
		return giftUnit;
	}
	public void setGiftUnit(String giftUnit) {
		this.giftUnit = giftUnit;
	}
	public Float getGiftPrice() {
		return giftPrice;
	}
	public void setGiftPrice(Float giftPrice) {
		this.giftPrice = giftPrice;
	}
	public Float getMemberPrice() {
		return memberPrice;
	}
	public void setMemberPrice(Float memberPrice) {
		this.memberPrice = memberPrice;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
