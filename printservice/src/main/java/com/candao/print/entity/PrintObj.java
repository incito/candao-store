package com.candao.print.entity;

import com.candao.common.utils.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintObj implements Serializable,Cloneable {

	
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = -4428122818683490199L;
	
	
	private String id;
	
	private int printType;
	
	private String messageMsg;
	
	private String orderNo;
	
	private String userName;
	
	private String tableNo;
	
	private String timeMsg;
	
	private String customerPrinterIp;
	
	private String customerPrinterPort;
	
	private List<PrintDish> list;
	
	private String dataMsg;
	
	private String tableArea;
	
	private int orderseq;
	
	
	private String billName;
	
	private String abbrbillName;
	
	private String welcomeMsg;
	
	private int   printway;
	
	private String tableid;
	/**
	 * 授权人
	 */
	private String discardUserId;
	
	private List<PrintDish> pDish;

    private Constant.ListenerType listenerType;
    //TODO 备用打印机
    private String ipAddress;
    
    private String printName;
	
	
	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	//xk add
	private Map<String, Object>  ordermap=new HashMap<>();
	
	private List<Map<String, Object>>  dishes=new ArrayList<>();

	public String getDiscardUserId() {
		return discardUserId;
	}

	public void setDiscardUserId(String discardUserId) {
		this.discardUserId = discardUserId;
	}

	public int getOrderseq() {
		return orderseq;
	}

	public void setOrderseq(int orderseq) {
		this.orderseq = orderseq;
	}

	public List<PrintDish> getpDish() {
		return pDish;
	}

	public void setpDish(List<PrintDish> pDish) {
		this.pDish = pDish;
	}

	public String getTableid() {
		return tableid;
	}

	public void setTableid(String tableid) {
		this.tableid = tableid;
	}

	 

	public int getPrintway() {
		return printway;
	}

	public void setPrintway(int printway) {
		this.printway = printway;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getAbbrbillName() {
		return abbrbillName;
	}

	public void setAbbrbillName(String abbrbillName) {
		this.abbrbillName = abbrbillName;
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}

	public void setWelcomeMsg(String welcomeMsg) {
		this.welcomeMsg = welcomeMsg;
	}



//	public int getPrintnum() {
//		return printnum;
//	}
//
//	public void setPrintnum(int printnum) {
//		this.printnum = printnum;
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataMsg() {
		return dataMsg;
	}

	public void setDataMsg(String dataMsg) {
		this.dataMsg = dataMsg;
	}

	public String getTableArea() {
		return tableArea;
	}

	public void setTableArea(String tableArea) {
		this.tableArea = tableArea;
	}

	
	
	

	public String getCustomerPrinterIp() {
		return customerPrinterIp;
	}

	public void setCustomerPrinterIp(String customerPrinterIp) {
		this.customerPrinterIp = customerPrinterIp;
	}

	public String getCustomerPrinterPort() {
		return customerPrinterPort;
	}

	public void setCustomerPrinterPort(String customerPrinterPort) {
		this.customerPrinterPort = customerPrinterPort;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getTimeMsg() {
		return timeMsg;
	}

	public void setTimeMsg(String timeMsg) {
		this.timeMsg = timeMsg;
	}

	public List<PrintDish> getList() {
		return list;
	}

	public void setList(List<PrintDish> list) {
		this.list = list;
	}

 

	public int getPrintType() {
		return printType;
	}

	public void setPrintType(int printType) {
		this.printType = printType;
	}

	public String getMessageMsg() {
		return messageMsg;
	}

	public void setMessageMsg(String messageMsg) {
		this.messageMsg = messageMsg;
	}

	public Map<String, Object> getOrdermap() {
		return ordermap;
	}

	public void setOrdermap(Map<String, Object> ordermap) {
		this.ordermap = ordermap;
	}

	public List<Map<String, Object>> getDishes() {
		return dishes;
	}

	public void setDishes(List<Map<String, Object>> dishes) {
		this.dishes = dishes;
	}


    public Constant.ListenerType getListenerType() {
        return listenerType;
    }

    public void setListenerType(Constant.ListenerType listenerType) {
        this.listenerType = listenerType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
