package com.candao.print.entity;

import java.util.List;
import java.util.Map;

/**
 * 打印合并，菜品分组的业务类
 * @author zhangjijun
 *
 */
public class GroupDishBusiness {

	private String groupid;
	
	private List<Map<String, Object>> values;

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public List<Map<String, Object>> getValues() {
		return values;
	}

	public void setValues(List<Map<String, Object>> values) {
		this.values = values;
	}
	
	
}
