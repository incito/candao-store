package com.candao.www.data.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

/**
 * 赠送礼物记录表
 * @author weizhifang
 * @since 2015-11-11
 *
 */
public class TGiftLog implements Serializable {

	private static final long serialVersionUID = -80171382125326048L;

	private String id;                 //主键
	private String giftId;             //礼物id
	private String giftTypeId;         //礼物分类id
	private String giveTableNo;       //赠送礼物桌号
	private String receiveTableNo;    //接收礼物桌号
	private String receiveOrderId;    //接收礼物ID
	private String giftNo;             //礼物编号
	private String giftStatus;         //礼物状态
	private String orderId;            //赠送订单编号
	private Date insertTime;           //插入时间
	private String giftName;           //礼物名称
	private Integer giftNum;           //礼物数量
	private String giftUnit;           //礼物单位
	private Float giftPrice;           //客气单价
	private Float giftAmount;          //礼物金额 
	private String isAnonymous;        //是否匿名
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGiftId() {
		return giftId;
	}
	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}
	public String getGiftTypeId() {
		return giftTypeId;
	}
	public void setGiftTypeId(String giftTypeId) {
		this.giftTypeId = giftTypeId;
	}
	public String getGiveTableNo() {
		return giveTableNo;
	}
	public void setGiveTableNo(String giveTableNo) {
		this.giveTableNo = giveTableNo;
	}
	public String getReceiveTableNo() {
		return receiveTableNo;
	}
	public void setReceiveTableNo(String receiveTableNo) {
		this.receiveTableNo = receiveTableNo;
	}
	public String getGiftNo() {
		return giftNo;
	}
	public void setGiftNo(String giftNo) {
		this.giftNo = giftNo;
	}
	public String getGiftStatus() {
		return giftStatus;
	}
	public void setGiftStatus(String giftStatus) {
		this.giftStatus = giftStatus;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public String getGiftName() {
		return giftName;
	}
	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}
	public Integer getGiftNum() {
		return giftNum;
	}
	public void setGiftNum(Integer giftNum) {
		this.giftNum = giftNum;
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
	public Float getGiftAmount() {
		return giftAmount;
	}
	public void setGiftAmount(Float giftAmount) {
		this.giftAmount = giftAmount;
	}
	
	public String getIsAnonymous() {
		return isAnonymous;
	}
	public void setIsAnonymous(String isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	
	
	public String getReceiveOrderId() {
		return receiveOrderId;
	}
	public void setReceiveOrderId(String receiveOrderId) {
		this.receiveOrderId = receiveOrderId;
	}
	public static TGiftLog jsonToObject(JSONObject giftInfo){
		TGiftLog giftlog = new TGiftLog();
		try{
			giftlog.setGiftId(StringUtils.isBlank(giftInfo.getString("giftId"))?"":giftInfo.getString("giftId"));
			giftlog.setGiveTableNo(StringUtils.isBlank(giftInfo.getString("giveTableNo"))?"":giftInfo.getString("giveTableNo"));
			giftlog.setReceiveTableNo(StringUtils.isBlank(giftInfo.getString("receiveTableNo"))?"":giftInfo.getString("receiveTableNo"));
			giftlog.setGiftNo(StringUtils.isBlank(giftInfo.getString("giftNo"))?"":giftInfo.getString("giftNo"));
			giftlog.setOrderId(StringUtils.isBlank(giftInfo.getString("orderId"))?"":giftInfo.getString("orderId"));
			giftlog.setReceiveOrderId(StringUtils.isBlank(giftInfo.getString("receiveOrderId"))?"":giftInfo.getString("receiveOrderId"));
			giftlog.setInsertTime(new Date());
			giftlog.setGiftName(StringUtils.isBlank(giftInfo.getString("giftName"))?"":giftInfo.getString("giftName"));
			giftlog.setGiftNum(giftInfo.getInt("giftNum"));
			giftlog.setGiftUnit(StringUtils.isBlank(giftInfo.getString("giftUnit"))?"":giftInfo.getString("giftUnit"));
			String giftPrice = giftInfo.getString("giftPrice");
			String giftAmount = giftInfo.getString("giftAmount");
			if(giftPrice == null || giftPrice.equals("")){
				giftPrice = "0";
			}
			if(giftAmount == null || giftAmount.equals("")){
				giftAmount = "0";
			}
			giftlog.setGiftPrice(Float.valueOf(giftPrice));
			giftlog.setGiftAmount(Float.valueOf(giftAmount));
		}catch(Exception ex){
			
		}
		return giftlog;
	}
}
