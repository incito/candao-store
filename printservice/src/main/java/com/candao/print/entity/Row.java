package com.candao.print.entity;

import com.candao.common.utils.StringUtils;

public class Row implements Cloneable{
	// 对齐方式
	private String align;
	// 字体
	private String font;
	// 位置
	private Integer[] locations;
	// 数据
	private String[] datas;
	// 是否换行
	private boolean lineFeed;

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public Integer[] getLocations() {
		return locations;
	}

	public void setLocations(Integer[] locations) {
		this.locations = locations;
	}

	public String[] getDatas() {
		return datas;
	}

	public void setDatas(String[] datas) {
		this.datas = datas;
	}

	public boolean isLineFeed() {
		return lineFeed;
	}

	public void setLineFeed(boolean lineFeed) {
		this.lineFeed = lineFeed;
	}

	public String[] getContent() throws Exception {
		String[] res;
//		if (lineFeed) {
			res = StringUtils.getLineFeedText(datas, locations);
//		} else {
//			
//		}
		return res;
	}
	
	@Override
	public Row clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Row)super.clone();
	}

}
