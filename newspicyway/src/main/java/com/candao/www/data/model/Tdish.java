package com.candao.www.data.model;

import java.util.Date;

public class Tdish {
	private String dishid;

	private String columnid;

	private String userid;

	private String title;
	/**
	 * 0 充足 1 缺少
	 */
	private String label;

	private String introduction;
	/**
	 * 根节点id
	 */
	private String source;

	private String author;

	private String image;
	/**
	 * 这个字段用于插入忌口的的组合 0 是忌口    1 不是忌口(老版本)
	 * 现在这个字段存忌口的汉字（多个用，分隔）
	 */
	private String imagetitle;

	private String content;
	/**
	 * 1：在终端显示 0：在终端不显示
	 */
	private Integer isdisplay;
 
	private Integer ishead;
	/**
	 * 多计量单位标识  标识这个菜是否是多计量的   0有多计量   1没有多计量
	 */
	private Integer headsort;
	/**
	 * 1:可供模板选择 0：已选择
	 */
	private Integer isselect;

	private Date createtime;

	private Date releasetime;

	private Date modifytime;
	/**
	 * 0:无效 1:有效 
	 */
	private Integer status;
	/**
	 * 多计量单位的分隔
	 */
	private String price;
	private String vipprice;

	private String dishno;
	/**
	 * dishtype=1 level=1 代表是双拼锅 暂时只用到这个
	 */
	private String level;

	private String printer;
	/**
	 * 0 份 1 盘 2 斤 3个 4只
	 */
	private String unit;
	/**
	 * 使用 0,1,2,3 这样的数据，0 表示 特色菜 1 推荐菜 2 经理推荐 3 特别推荐
	 */

	private String abbrdesc;
	/**
	 * 0 单点 1 套餐
	 */
	private Integer dishtype;
	
	private String orderNum;
	/**
	 * 分店id
	 */
	private Integer branchid;
	/**
	 * 区域id
	 */
	private Integer areaid;
	/**
	 * 是否推荐 0不推荐 1推荐
	 */
	private Integer recommend;
	/**
	 * 是否称重
	 */
	private Integer weigh;
	
	

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public Integer getWeigh() {
		return weigh;
	}

	public void setWeigh(Integer weigh) {
		this.weigh = weigh;
	}

	public Integer getBranchid() {
		return branchid;
	}

	public void setBranchid(Integer branchid) {
		this.branchid = branchid;
	}

	public Integer getAreaid() {
		return areaid;
	}

	public void setAreaid(Integer areaid) {
		this.areaid = areaid;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getDishid() {
		return dishid;
	}

	public void setDishid(String dishid) {
		this.dishid = dishid;
	}

	public String getColumnid() {
		return columnid;
	}

	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImagetitle() {
		return imagetitle;
	}

	public void setImagetitle(String imagetitle) {
		this.imagetitle = imagetitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsdisplay() {
		return isdisplay;
	}

	public void setIsdisplay(Integer isdisplay) {
		this.isdisplay = isdisplay;
	}

	public Integer getIshead() {
		return ishead;
	}

	public void setIshead(Integer ishead) {
		this.ishead = ishead;
	}

	public Integer getHeadsort() {
		return headsort;
	}

	public void setHeadsort(Integer headsort) {
		this.headsort = headsort;
	}

	public Integer getIsselect() {
		return isselect;
	}

	public void setIsselect(Integer isselect) {
		this.isselect = isselect;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(Date releasetime) {
		this.releasetime = releasetime;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getDishno() {
		return dishno;
	}

	public void setDishno(String dishno) {
		this.dishno = dishno;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}


	public String getAbbrdesc() {
		return abbrdesc;
	}

	public void setAbbrdesc(String abbrdesc) {
		this.abbrdesc = abbrdesc;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getVipprice() {
		return vipprice;
	}

	public void setVipprice(String vipprice) {
		this.vipprice = vipprice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getDishtype() {
		return dishtype;
	}

	public void setDishtype(Integer dishtype) {
		this.dishtype = dishtype;
	}
}