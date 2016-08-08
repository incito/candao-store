package com.candao.www.webroom.model;

import com.candao.www.constant.Constant;

import java.math.BigDecimal;


public class UrgeDish implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5986774713534117279L;

	private String actionType;
	
	private String userName;
	
	private String orderNo;
	
	private String dishNo;
	
	private String tableNo;
	
	private BigDecimal dishNum;
	
	private String dishunit;
	
	private String currenttableid;
	
	private String dishtype;

	private String potdishid;//火锅的id
	
	private String hotflag;//1锅底    0鱼 
	
	private int operationType;//（1：下单;2 :退菜） 
	
	private String sequence;//顺序
	
    //主键
    private String primarykey;
    
    //退菜授权人
    private  String discardUserId; 
    
    //退菜原因
    private String discardReason;
	private String source= Constant.SOURCE.POS; //退菜来源 PAD 1;POS 2
    

	public String getDiscardReason() {
		return discardReason;
	}

	public void setDiscardReason(String discardReason) {
		this.discardReason = discardReason;
	}

	public String getDiscardUserId() {
		return discardUserId;
	}

	public void setDiscardUserId(String discardUserId) {
		this.discardUserId = discardUserId;
	}

	public String getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(String primarykey) {
		this.primarykey = primarykey;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}


	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getPotdishid() {
		return potdishid;
	}

	public void setPotdishid(String potdishid) {
		this.potdishid = potdishid;
	}

	public String getHotflag() {
		return hotflag;
	}

	public void setHotflag(String hotflag) {
		this.hotflag = hotflag;
	}



	public String getDishtype() {
		return dishtype;
	}

	public void setDishtype(String dishtype) {
		this.dishtype = dishtype;
	}

	public String getCurrenttableid() {
		return currenttableid;
	}

	public void setCurrenttableid(String currenttableid) {
		this.currenttableid = currenttableid;
	}

	public String getDishunit() {
		return dishunit;
	}

	public void setDishunit(String dishunit) {
		this.dishunit = dishunit;
	}

	public BigDecimal getDishNum() {
		return dishNum;
	}

	public void setDishNum(BigDecimal dishNum) {
		this.dishNum = dishNum;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDishNo() {
		return dishNo;
	}

	public void setDishNo(String dishNo) {
		this.dishNo = dishNo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
