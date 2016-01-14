package com.candao.www.data.model;

import java.util.Date;

public class TbMessage {
	
	private String id;

	private String msgType;

	private Date inserttime;

	private String status;

	private Date replyTime;

	private String sentTime;

	private String tableno;

	private String userid;

	private String orderid;

	private String deviceType;

	private String deviceNo;

	private Date startTime;

	private Date timeoutTime;

	private int timeoutDuration;
	
	private String callStatus;
	
	private int callNums;
	
	private int urgeNums;
	
	private int responseNums;
	
	private Date callOverTime;

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getMsgType() {
		return msgType;
	}


	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}


	public Date getInserttime() {
		return inserttime;
	}


	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getReplyTime() {
		return replyTime;
	}


	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}


	public String getSentTime() {
		return sentTime;
	}


	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}


	public String getTableno() {
		return tableno;
	}


	public void setTableno(String tableno) {
		this.tableno = tableno;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public String getOrderid() {
		return orderid;
	}


	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}


	public String getDeviceType() {
		return deviceType;
	}


	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}


	public String getDeviceNo() {
		return deviceNo;
	}


	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}


	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getTimeoutTime() {
		return timeoutTime;
	}


	public void setTimeoutTime(Date timeoutTime) {
		this.timeoutTime = timeoutTime;
	}


	public int getTimeoutDuration() {
		return timeoutDuration;
	}


	public void setTimeoutDuration(int timeoutDuration) {
		this.timeoutDuration = timeoutDuration;
	}


	public String getCallStatus() {
		return callStatus;
	}


	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}


	public int getCallNums() {
		return callNums;
	}


	public void setCallNums(int callNums) {
		this.callNums = callNums;
	}


	public int getUrgeNums() {
		return urgeNums;
	}


	public void setUrgeNums(int urgeNums) {
		this.urgeNums = urgeNums;
	}


	public int getResponseNums() {
		return responseNums;
	}


	public void setResponseNums(int responseNums) {
		this.responseNums = responseNums;
	}


	public Date getCallOverTime() {
		return callOverTime;
	}


	public void setCallOverTime(Date callOverTime) {
		this.callOverTime = callOverTime;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TbMessage [id=");
		builder.append(id);
		builder.append(", msgType=");
		builder.append(msgType);
		builder.append(", inserttime=");
		builder.append(inserttime);
		builder.append(", status=");
		builder.append(status);
		builder.append(", replyTime=");
		builder.append(replyTime);
		builder.append(", sentTime=");
		builder.append(sentTime);
		builder.append(", tableno=");
		builder.append(tableno);
		builder.append(", userid=");
		builder.append(userid);
		builder.append(", orderid=");
		builder.append(orderid);
		builder.append(", deviceType=");
		builder.append(deviceType);
		builder.append(", deviceNo=");
		builder.append(deviceNo);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", timeoutTime=");
		builder.append(timeoutTime);
		builder.append(", timeoutDuration=");
		builder.append(timeoutDuration);
		builder.append(", callStatus=");
		builder.append(callStatus);
		builder.append(", callNums=");
		builder.append(callNums);
		builder.append(", urgeNums=");
		builder.append(urgeNums);
		builder.append(", responseNums=");
		builder.append(responseNums);
		builder.append(", callOverTime=");
		builder.append(callOverTime);
		builder.append("]");
		return builder.toString();
	}
}