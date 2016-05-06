package com.candao.www.webroom.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private Boolean social;//社交功能
	private String[] seatImagename;//座位图名称
	private String[]  seatImagefileurls;//座位图文件地址web端使用地段
	private Boolean vipstatus;//是否启用会员
	private String viptype;//会员类型1餐道会员；2其他会员
	private String vipcandaourl;//会员地址(直接在数据库修改)
	private String vipotherurl;//其他会员地址(直接在数据库修改)
	private Boolean clickimagedish;//点图点菜
	private Boolean onepage;//一页菜谱
	private Boolean newplayer;//新手引导
	private Boolean chinaEnglish;//中英文国际化
	private Boolean indexad;//首页广告
	private Boolean invoice;//开发票
	private Boolean hidecarttotal;//隐藏购物车总价
	private String adtimes;//异业营销时间
	private Boolean waiterreward;//服务员打赏
	private String rewardmoney;//打赏金额
	private String youmengappkey;//友盟应用钥匙
	private String youmengchinnal;//友盟渠道号
	private String bigdatainterface;//大数据接口地址
	private String seatimageurls;//数据库保存的全部图片地址
	private String seatimagenames;//数据库保存的全部图片名称
	
	//手环信息
	private String braceletgappkey;//手环应用钥匙
	private String braceletchinnal;//手环渠道号
	//微信信息
	private Integer weixintype;//1为个人微信，2公众号微信
	private String personweixinurl;//微信个人二维码地址
	//不写入数据库字段
	private List<String> seatImageurl=new ArrayList<>();//座位图地址PAD应用字段
	private String logourl;//
	private String backgroudurl;//
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
	public Boolean getSocial() {
		return social;
	}
	public void setSocial(Boolean social) {
		this.social = social;
	}
	public String[] getSeatImagename() {
		return seatImagename;
	}
	public void setSeatImagename(String[] seatImagename) {
		this.seatImagename = seatImagename;
	}
	
	
	
	public String[] getSeatImagefileurls() {
		return seatImagefileurls;
	}
	public void setSeatImagefileurls(String[] seatImagefileurls) {
		this.seatImagefileurls = seatImagefileurls;
	}
	public Boolean getVipstatus() {
		return vipstatus;
	}
	public void setVipstatus(Boolean vipstatus) {
		this.vipstatus = vipstatus;
	}
	public String getViptype() {
		return viptype;
	}
	public void setViptype(String viptype) {
		this.viptype = viptype;
	}
	public String getVipcandaourl() {
		return vipcandaourl;
	}
	public void setVipcandaourl(String vipcandaourl) {
		this.vipcandaourl = vipcandaourl;
	}
	public String getVipotherurl() {
		return vipotherurl;
	}
	public void setVipotherurl(String vipotherurl) {
		this.vipotherurl = vipotherurl;
	}
	public Boolean getClickimagedish() {
		return clickimagedish;
	}
	public void setClickimagedish(Boolean clickimagedish) {
		this.clickimagedish = clickimagedish;
	}
	public Boolean getOnepage() {
		return onepage;
	}
	public void setOnepage(Boolean onepage) {
		this.onepage = onepage;
	}
	public Boolean getNewplayer() {
		return newplayer;
	}
	public void setNewplayer(Boolean newplayer) {
		this.newplayer = newplayer;
	}
	public Boolean getChinaEnglish() {
		return chinaEnglish;
	}
	public void setChinaEnglish(Boolean chinaEnglish) {
		this.chinaEnglish = chinaEnglish;
	}
	public Boolean getIndexad() {
		return indexad;
	}
	public void setIndexad(Boolean indexad) {
		this.indexad = indexad;
	}
	public Boolean getInvoice() {
		return invoice;
	}
	public void setInvoice(Boolean invoice) {
		this.invoice = invoice;
	}
	public Boolean getHidecarttotal() {
		return hidecarttotal;
	}
	public void setHidecarttotal(Boolean hidecarttotal) {
		this.hidecarttotal = hidecarttotal;
	}
	public String getAdtimes() {
		return adtimes;
	}
	public void setAdtimes(String adtimes) {
		this.adtimes = adtimes;
	}
	public Boolean getWaiterreward() {
		return waiterreward;
	}
	public void setWaiterreward(Boolean waiterreward) {
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
	public String getBigdatainterface() {
		return bigdatainterface;
	}
	public void setBigdatainterface(String bigdatainterface) {
		this.bigdatainterface = bigdatainterface;
	}
	public String getSeatimageurls() {
		return seatimageurls;
	}
	public void setSeatimageurls(String seatimageurls) {
		this.seatimageurls = seatimageurls;
	}
	
	public List<String> getSeatImageurl() {
		return seatImageurl;
	}
	public void setSeatImageurl(List<String> seatImageurl) {
		this.seatImageurl = seatImageurl;
	}
	public String getLogourl() {
		return logourl;
	}
	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}
	public String getBackgroudurl() {
		return backgroudurl;
	}
	public void setBackgroudurl(String backgroudurl) {
		this.backgroudurl = backgroudurl;
	}
	public String getSeatimagenames() {
		return seatimagenames;
	}
	public void setSeatimagenames(String seatimagenames) {
		this.seatimagenames = seatimagenames;
	}
	public String getBraceletgappkey() {
		return braceletgappkey;
	}
	public void setBraceletgappkey(String braceletgappkey) {
		this.braceletgappkey = braceletgappkey;
	}
	public String getBraceletchinnal() {
		return braceletchinnal;
	}
	public void setBraceletchinnal(String braceletchinnal) {
		this.braceletchinnal = braceletchinnal;
	}
	
	public String getPersonweixinurl() {
		return personweixinurl;
	}
	public void setPersonweixinurl(String personweixinurl) {
		this.personweixinurl = personweixinurl;
	}
	public Integer getWeixintype() {
		return weixintype;
	}
	public void setWeixintype(Integer weixintype) {
		this.weixintype = weixintype;
	}

	
	
	
	
	}