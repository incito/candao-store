package com.candao.www.data.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class TbPictures {
	private java.lang.String picid; //图片主键
	private java.lang.String picname; //图片名称
	private java.lang.String orderNo; //图片排序
	
	private java.lang.Integer status; //0 已删除，1 可显示
	private java.lang.String picpath;//图片路径
	private java.lang.String detail;//图片简介
	
	public java.lang.String getPicid() {
		return picid;
	}

	public void setPicid(java.lang.String picid) {
		this.picid = picid;
	}

	public java.lang.String getPicname() {
		return picname;
	}

	public void setPicname(java.lang.String picname) {
		this.picname = picname;
	}


	public java.lang.String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}

	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	public java.lang.String getPicpath() {
		return picpath;
	}

	public void setPicpath(java.lang.String picpath) {
		this.picpath = picpath;
	}

	public java.lang.String getDetail() {
		return detail;
	}

	public void setDetail(java.lang.String detail) {
		this.detail = detail;
	}

	public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("picid");
        sb.append("{picname=").append(picname);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", status=").append(status);
        sb.append(", picpath=").append(picpath);
        sb.append(", detail=").append(detail);
       
		sb.append('}');
        return sb.toString();
    }
	
}
