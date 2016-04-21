package com.candao.www.constant;

/**
 * Created by IntelliJ IDEA.
 *
 * 系统设置中需要的公共常量
 * User: johnson
 * Date: 5/28/15
 * Time: 11:27 上午
 */
public enum SystemConstant{


	DISHLABEL("DISHLABEL","菜品标签"),
	TABLESTATUS("TABLESTATUS","餐桌状态"),
	DISHSTATUS("DISHSTATUS","菜品状态"),
	BIZPERIODDATE("BIZPERIODDATE","营业时间"),
	PASSWORD("PASSWORD","通用密码"),
	DISHES("DISHES","餐具设置"),
	SPECIAL("SPECIAL","忌口分类"),
	RETURNDISH("RETURNDISH","退菜设置"),
    UNIT("UNIT","计量单位"),
	CALLTYPE("CALLTYPE","呼叫服务员类型"),
	ROUNDING("ROUNDING","零头处理方式"),
	ONEPAGETYPE("ONEPAGETYPE","一页菜谱"),
	ACCURACY("ACCURACY","零头精确度");
	private String type;

	private String typename;

	public String type() {
		return type;
	}

	public String typename() {
		return typename;
	}


	private SystemConstant(String type, String typename) {
		this.type = type;
		this.typename = typename;
	}
}
