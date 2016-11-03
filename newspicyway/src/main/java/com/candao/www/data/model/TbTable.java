package com.candao.www.data.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
//0 空闲
//1 就餐
//3 预定
//4 已结账
//5 已撤销
public class TbTable {
	private java.lang.String tableid; 
	private java.lang.String position; 
	private java.lang.Integer status; 
	
	private java.lang.String restaurantId; 
	private java.lang.Integer isVip;
	private java.lang.Integer iscompartment;
	private java.lang.String isavailable;
	private java.lang.String buildingNo;
	
	private BigDecimal minprice;
	
	private BigDecimal fixprice; 
	private java.lang.String tableNo;
	private java.lang.Integer personNum;
	private java.lang.String tabletype;
	private java.lang.String areaid;
	private java.lang.String tableName;
	
	private String orderid;
	private java.lang.String custPrinter;

	private String modifytime;

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	private String areaSort;

	public String getAreaSort() {
		return areaSort;
	}

	public void setAreaSort(String areaSort) {
		this.areaSort = areaSort;
	}

	private int operationType;//（1：下单;2 :退菜 3：并台  4换台） 
	
	private int sequence;//顺序

	private String amount;

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	private String areaname;

	public String getCustnum() {
		return custnum;
	}

	public void setCustnum(String custnum) {
		this.custnum = custnum;
	}

	private String custnum;

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	private String begintime;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}


	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public java.lang.String getCustPrinter() {
		return custPrinter;
	}

	public void setCustPrinter(java.lang.String custPrinter) {
		this.custPrinter = custPrinter;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public java.lang.String getTableid() {
		return tableid;
	}

	public void setTableid(java.lang.String tableid) {
		this.tableid = tableid;
	}

	public java.lang.String getPosition() {
		return position;
	}

	public void setPosition(java.lang.String position) {
		this.position = position;
	}

	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	public java.lang.String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(java.lang.String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public java.lang.Integer getIsVip() {
		return isVip;
	}

	public void setIsVip(java.lang.Integer isVip) {
		this.isVip = isVip;
	}

	public java.lang.Integer getIscompartment() {
		return iscompartment;
	}

	public void setIscompartment(java.lang.Integer iscompartment) {
		this.iscompartment = iscompartment;
	}

	public java.lang.String getIsavailable() {
		return isavailable;
	}

	public void setIsavailable(java.lang.String isavailable) {
		this.isavailable = isavailable;
	}

	public java.lang.String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(java.lang.String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public BigDecimal getMinprice() {
		return minprice;
	}

	public void setMinprice(BigDecimal minprice) {
		this.minprice = minprice;
	}

	public BigDecimal getFixprice() {
		return fixprice;
	}

	public void setFixprice(BigDecimal fixprice) {
		this.fixprice = fixprice;
	}

	public java.lang.String getTableNo() {
		return tableNo;
	}

	public void setTableNo(java.lang.String tableNo) {
		this.tableNo = tableNo;
	}

	public java.lang.Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(java.lang.Integer personNum) {
		this.personNum = personNum;
	}

	public java.lang.String getTabletype() {
		return tabletype;
	}

	public void setTabletype(java.lang.String tabletype) {
		this.tabletype = tabletype;
	}

	public java.lang.String getAreaid() {
		return areaid;
	}

	public void setAreaid(java.lang.String areaid) {
		this.areaid = areaid;
	}

	public java.lang.String getTableName() {
		return tableName;
	}

	public void setTableName(java.lang.String tableName) {
		this.tableName = tableName;
	}

	public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("tableid");
        sb.append("{position=").append(position);
        sb.append(", status=").append(status);
        sb.append(", restaurantId=").append(restaurantId);
        sb.append(", isVip=").append(isVip);
        sb.append(", iscompartment=").append(iscompartment);
        sb.append(", isavailable=").append(isavailable);
        sb.append(", buildingNo=").append(buildingNo);
        
        sb.append(", minprice=").append(minprice);
        sb.append(", fixprice=").append(fixprice);
        sb.append(", tableNo=").append(tableNo);
        sb.append(", personNum=").append(personNum);
        sb.append(", tabletype=").append(tabletype);
        sb.append(", areaid=").append(areaid);
        sb.append(", tableName=").append(tableName);
    
        sb.append(", custPrinter=").append(custPrinter);
		sb.append('}');
        return sb.toString();
    }
	
}
