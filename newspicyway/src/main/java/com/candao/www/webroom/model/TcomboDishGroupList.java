package com.candao.www.webroom.model;

import java.util.List;

import com.candao.www.data.model.TcomboDishGroup;
import com.candao.www.data.model.Tdish;

public class TcomboDishGroupList {
	private Tdish dish;
	private List<TcomboDishGroup> groupList;
	public Tdish getDish() {
		return dish;
	}
	public void setDish(Tdish dish) {
		this.dish = dish;
	}
	public List<TcomboDishGroup> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<TcomboDishGroup> groupList) {
		this.groupList = groupList;
	}

}
