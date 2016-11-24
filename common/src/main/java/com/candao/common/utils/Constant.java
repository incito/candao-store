package com.candao.common.utils;

import java.util.Timer;

public class Constant {

	public static final String PRINTERENCODE = "GBK";
	
	public static final String NOMSG = "无";
	
	public static final String SPLITER = "#";
	
	public static final int PRINTLENTH = 48;
	
	/** 当前用户 */
	public final static String CURRENT_USER = "currentUser";
	
	/** 当前租户id **/
	
	public final static String CURRENT_TENANT_ID = "currenttenantid";
	/**
	 * 用户管理字典定义
	 */
	public final static String USER_DATADICTIONARY="USER";	
	/**
	 * 菜品分类字典管理
	 */
	public final static String DISH_DATADICTIONARY="DISH";
	
	/** 允许访问的按钮 */
	public final static String ALLOW_ACCESS_BUTTONS = "allowAccessButtons";
	
	/** 当前验证码 */
	public final static String CURRENT_USER_VALIDATE_CODE_KEY = "CURRENT_USER_VALIDATE_CODE_KEY";
	

	public static final String FAILUREMSG = "{\"result\":\"1\"}";
	
	public static final String FAILUROPEMSG = "{\"result\":\"2\"}";
	
	public static final String SUCCESSMSG = "{\"result\":\"0\"}";

	public static final String CHECKSTATE = "CHECKSTATE";

	public enum ListenerType {
		NormalListener("normalDishListener"), MultiDishListener("multiDishListener"), DishSetListener("dishSetListener"),
		CustDishListener("custDishListener"), StatementDishListener("statementDishListener"), TableChangeListener("tableChangeListener"),
		WeighDishListener("weighDishListener"),SettlementDishListener("settlementTemplate"),ClearMachineDataTemplate("clearMachineDataTemplate")
		,ItemSellDetailTemplate("itemSellDetailTemplate"),MemberSaleInfoTemplate("memberSaleInfoTemplate"),BillDetailTemplate("billDetailTemplate")
		,StoreCardToNewPosTemplate("storeCardToNewPosTemplate"),TipListTemplate("tipListTemplate"),PreSettlementTemplate("preSettlementTemplate")
		,InvoiceTemplate("invoiceTemplate");

		private String name;

		private ListenerType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
	
	/** */
	public static final String DELIMITER_SPECIAL = "|";

	public static final String DELIMITER_MULTISPECIAL = ";";

	public static final String DEFALUT_USER = "candao";

	public static final String DEFAULT_PASSWORD = "e10adc3949ba59abbe56e057f20f883e";

	public static final String URL_SYSTEM = "/system/systemSet";

	public static final String FUNCTION_SYSTEM = "0311";

	public static final long TIME_INTERVAL = 24*60*60*1000;

}
