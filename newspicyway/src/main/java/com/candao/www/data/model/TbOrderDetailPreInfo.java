package com.candao.www.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author 梁东 优惠卷挂载的菜品
 */
public class TbOrderDetailPreInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键 **/
	private String id;
	/** 订单详情优惠 **/
	private String ordpreid;
	/** 订单号 **/
	private String orderid;
	/*** dishid对应订单号 */
	private String orderidetailid;
	/** 菜品编号 **/
	private String dishID;
	/** 菜品单位 **/
	private String unit;
	/** 菜品个数 **/
	private double dishNum=1;
	/**每一个菜品的优免金额**/
	private BigDecimal dePreAmount=new BigDecimal("0");
	/** 插入时间 **/
	private Date insettime;
	public TbOrderDetailPreInfo() {
	}
	public TbOrderDetailPreInfo(String id,String ordpreid,String orderid,String orderidetailid,Date date) {
		this.id=id;
		this.ordpreid=ordpreid;
		this.orderid=orderid;
		this.orderidetailid=orderidetailid;
		this.insettime=date;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrdpreid() {
		return ordpreid;
	}

	public void setOrdpreid(String ordpreid) {
		this.ordpreid = ordpreid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOrderidetailid() {
		return orderidetailid;
	}

	public void setOrderidetailid(String orderidetailid) {
		this.orderidetailid = orderidetailid;
	}

	public String getDishID() {
		return dishID;
	}

	public void setDishID(String dishID) {
		this.dishID = dishID;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getInsettime() {
		return insettime;
	}
	public void setInsettime(Date insettime) {
		this.insettime = insettime;
	}
	public double getDishNum() {
		return dishNum;
	}
	public void setDishNum(double dishNum) {
		this.dishNum = dishNum;
	}
	public BigDecimal getDePreAmount() {
		return dePreAmount;
	}
	public void setDePreAmount(BigDecimal dePreAmount) {
		this.dePreAmount = dePreAmount;
	}

}
