package com.candao.www.data.model;

public class ComplexTorderDetail extends TorderDetail {

	/**菜品类型**/
	private String level;
	/**菜品名称**/
	private String title;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
