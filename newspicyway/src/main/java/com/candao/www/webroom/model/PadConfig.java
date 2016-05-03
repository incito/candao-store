package com.candao.www.webroom.model;

import java.io.Serializable;

/**
 * pad可配置服务
 * @author snail
 *
 */
public class PadConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;//主键
	private String padloginpass;//pad登录密码
	private String tablewareisfree;//餐具是否免费
	private String tablewareprice;//餐具收费价格
	private String tablewarevipprice;//餐具收费vip价格
	private String social;//社交功能
	private String seatImage;//座位图
	private String vip;//是否启用会员
	private String viptype;//会员类型1餐道会员；2雅座会员
	private String clickimagedish;//点图点菜
	private String onepage;//一页菜谱
	private String newplayer;//新手引导
	private String chinaEnglish;//中英文国际化
	private String indexad;//首页广告
	private String invoice;//开发票
	private String hidecarttotal;//隐藏购物车总价
	private String adtimes;//异业营销时间
	private String waiterreward;//服务员打赏
	private String rewardmoney;//打赏金额
	private String youmengappkey;//友盟应用钥匙
	private String youmengchinnal;//友盟渠道号
	private String youmenginterface;//大数据接口地址
	private String logoimageurl;//logo图片地址
	private String padbackgroudurl;//pad启动背景图片
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPadloginpass() {
		return padloginpass;
	}
	public void setPadloginpass(String padloginpass) {
		this.padloginpass = padloginpass;
	}
	public String getTablewareisfree() {
		return tablewareisfree;
	}
	public void setTablewareisfree(String tablewareisfree) {
		this.tablewareisfree = tablewareisfree;
	}
	public String getTablewareprice() {
		return tablewareprice;
	}
	public void setTablewareprice(String tablewareprice) {
		this.tablewareprice = tablewareprice;
	}
	public String getTablewarevipprice() {
		return tablewarevipprice;
	}
	public void setTablewarevipprice(String tablewarevipprice) {
		this.tablewarevipprice = tablewarevipprice;
	}
	public String getSocial() {
		return social;
	}
	public void setSocial(String social) {
		this.social = social;
	}
	public String getVip() {
		return vip;
	}
	public void setVip(String vip) {
		this.vip = vip;
	}
	public String getViptype() {
		return viptype;
	}
	public void setViptype(String viptype) {
		this.viptype = viptype;
	}
	public String getClickimagedish() {
		return clickimagedish;
	}
	public void setClickimagedish(String clickimagedish) {
		this.clickimagedish = clickimagedish;
	}
	public String getOnepage() {
		return onepage;
	}
	public void setOnepage(String onepage) {
		this.onepage = onepage;
	}
	public String getNewplayer() {
		return newplayer;
	}
	public void setNewplayer(String newplayer) {
		this.newplayer = newplayer;
	}
	public String getChinaEnglish() {
		return chinaEnglish;
	}
	public void setChinaEnglish(String chinaEnglish) {
		this.chinaEnglish = chinaEnglish;
	}
	public String getIndexad() {
		return indexad;
	}
	public void setIndexad(String indexad) {
		this.indexad = indexad;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getHidecarttotal() {
		return hidecarttotal;
	}
	public void setHidecarttotal(String hidecarttotal) {
		this.hidecarttotal = hidecarttotal;
	}
	public String getAdtimes() {
		return adtimes;
	}
	public void setAdtimes(String adtimes) {
		this.adtimes = adtimes;
	}
	public String getWaiterreward() {
		return waiterreward;
	}
	public void setWaiterreward(String waiterreward) {
		this.waiterreward = waiterreward;
	}
	public String getRewardmoney() {
		return rewardmoney;
	}
	public void setRewardmoney(String rewardmoney) {
		this.rewardmoney = rewardmoney;
	}
	public String getYoumengappkey() {
		return youmengappkey;
	}
	public void setYoumengappkey(String youmengappkey) {
		this.youmengappkey = youmengappkey;
	}
	public String getYoumengchinnal() {
		return youmengchinnal;
	}
	public void setYoumengchinnal(String youmengchinnal) {
		this.youmengchinnal = youmengchinnal;
	}
	public String getYoumenginterface() {
		return youmenginterface;
	}
	public void setYoumenginterface(String youmenginterface) {
		this.youmenginterface = youmenginterface;
	}
	public String getLogoimageurl() {
		return logoimageurl;
	}
	public void setLogoimageurl(String logoimageurl) {
		this.logoimageurl = logoimageurl;
	}
	public String getPadbackgroudurl() {
		return padbackgroudurl;
	}
	public void setPadbackgroudurl(String padbackgroudurl) {
		this.padbackgroudurl = padbackgroudurl;
	}
	public String getSeatImage() {
		return seatImage;
	}
	public void setSeatImage(String seatImage) {
		this.seatImage = seatImage;
	}

}
