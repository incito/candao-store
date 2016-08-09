package com.candao.www.webroom.model;

import java.util.List;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TdishUnit;
/**
 * 
 * @author sgy
 *
 * 2015年3月6日 下午3:15:29
 */
public class TdishGroup {
	private Tdish tdish;
	private List<TdishUnit> list;
	public Tdish getTdish() {
		return tdish;
	}
	public void setTdish(Tdish tdish) {
		this.tdish = tdish;
	}
	public List<TdishUnit> getList() {
		return list;
	}
	public void setList(List<TdishUnit> list) {
		this.list = list;
	}
}
