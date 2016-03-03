package com.candao.www.data.model;

import java.io.Serializable;

/**
 * 
 * tb_data_dictionary表
 * @author mew
 *
 */
@SuppressWarnings("serial")
public class TbDataDictionary implements Serializable {
	
	private java.lang.String id; // 类别编号
	private java.lang.String itemid; // 项目编号
	private java.lang.String itemDesc; // 项目描述
	private java.lang.Integer itemSort; // 项目排序
	private java.lang.Integer status; // 状态 1：有效  0：无效
	private java.lang.String type; // 分类
	private java.lang.String typename; //分类名称 
	private String endtime; //结束时间
	private String begintime; //开始时间
	private String chargesstatus; //分类名称
	private String memberprice; //会员价格
	private String price; //单价
	private String datetype;


	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getDatetype() {
		return datetype;
	}

	public void setDatetype(String datetype) {
		this.datetype = datetype;
	}


	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMemberprice() {
		return memberprice;
	}

	public void setMemberprice(String memberprice) {
		this.memberprice = memberprice;
	}

	public String getChargesstatus() {
		return chargesstatus;
	}

	public void setChargesstatus(String chargesstatus) {
		this.chargesstatus = chargesstatus;
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getItemid() {
		return itemid;
	}

	public void setItemid(java.lang.String itemid) {
		this.itemid = itemid;
	}

	public java.lang.String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(java.lang.String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public java.lang.Integer getItemSort() {
		return itemSort;
	}

	public void setItemSort(java.lang.Integer itemSort) {
		this.itemSort = itemSort;
	}

	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	public java.lang.String getType() {
		return type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

	public java.lang.String getTypename() {
		return typename;
	}

	public void setTypename(java.lang.String typename) {
		this.typename = typename;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TbDataDictionary");
        sb.append("{id=").append(id);
        sb.append(", itemid=").append(itemid);
        sb.append(", itemDesc=").append(itemDesc);
        sb.append(", itemSort=").append(itemSort);
        sb.append(", status=").append(status);
        sb.append(", type=").append(type);
        sb.append(", typename=").append(typename);
		sb.append('}');
        return sb.toString();
    }
}