package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 礼物分类
 * @author weizhifang
 * @since 2015-11-11
 *
 */
public class TGiftType implements Serializable {

	private static final long serialVersionUID = 5485947981693388829L;

	private String id;              //主键
	private String giftType;        //礼物分类
	private String giftTypeName;    //礼物分类名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGiftType() {
		return giftType;
	}
	public void setGiftType(String giftType) {
		this.giftType = giftType;
	}
	public String getGiftTypeName() {
		return giftTypeName;
	}
	public void setGiftTypeName(String giftTypeName) {
		this.giftTypeName = giftTypeName;
	}
	
	
}
