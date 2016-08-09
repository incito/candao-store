package com.candao.www.webroom.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 投诉信息
 * 
 * @author YANGZHONGLI
 *
 */
public class Complain implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9002033166109972568L;
	
    private String id;
	
	private String orderid;
	
	private String deviceType ;
	
	private String deviceNo;
	
	private String userid;
	
	private String opinion;
	
	private String photoUrl;
	
	private Date complainTime;
	
	private Date visitDate;
	
	private String visiter;
	
	private String visitRecord;
	
	private String problemSolver;
	
	private String result;
	
	private String messageid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Date getComplainTime() {
		return complainTime;
	}

	public void setComplainTime(Date complainTime) {
		this.complainTime = complainTime;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisiter() {
		return visiter;
	}

	public void setVisiter(String visiter) {
		this.visiter = visiter;
	}

	public String getVisitRecord() {
		return visitRecord;
	}

	public void setVisitRecord(String visitRecord) {
		this.visitRecord = visitRecord;
	}

	public String getProblemSolver() {
		return problemSolver;
	}

	public void setProblemSolver(String problemSolver) {
		this.problemSolver = problemSolver;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}
}
