package com.candao.www.utils;

import com.candao.www.constant.Constant;

/**
 * 订单详细表备注字段的处理
 * @author zhangjijun
 *
 */
public class OrderDetailParse {

	/**
	 * @return 菜品备注：忌口信息
	 */
	public static String getSperequire(String sperequire) {
		if(sperequire != null){
			if(getCount(sperequire) == 5){
				return findValue(sperequire, 0, 0);
			}
			//兼容以前版本的数据
			return sperequire;
		}
		return Constant.BLANK_VALUE;
	}

	/**
	 * @return 菜品口味
	 */
	public static String getTaste(String sperequire) {
		return checkAndFind(sperequire, 2);
	}

	/**
	 * @return 赠菜人工号
	 */
	public static String getFreeUser(String sperequire) {
		return checkAndFind(sperequire, 3);
	}

	/**
	 * @return 赠菜授权人工号
	 */
	public static String getFreeAuthorize(String sperequire) {
		return checkAndFind(sperequire, 4);
	}

	/**
	 * @return 赠菜原因
	 */
	public static String getFreeReason(String sperequire) {
		return checkAndFind(sperequire, 5);
	}

	/**
	 * @return 全单备注
	 */
	public static String getGlobalRemark(String sperequire){
		return checkAndFind(sperequire, 1);
	}

	/**
	 * @param sperequire
	 * @return
	 */
	private static String checkAndFind(String sperequire, int desIndex) {
		if(sperequire != null){
			if(getCount(sperequire) == 5){
				return findValue(sperequire, desIndex, 0);
			}
		}
		return Constant.BLANK_VALUE;
	}
	

	/**
	 * @param sperequire
	 * @return sperequire 包含"|"符号的数量
	 */
	private static int getCount(String sperequire) {
		char[] c = sperequire.toCharArray();
		int total = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == Constant.ORDER_REMARK_SEPARATOR) {
				total++;
			}
		}
		return total;
	}
	
	/**
	 * 拆分共用字段
	 * @param sperequire 
	 * @return 指定索引位置的值
	 */
	private static String findValue(String sperequire, int desIndex, int currentIndex){
		int indexOf = sperequire.indexOf(Constant.ORDER_REMARK_SEPARATOR);
		if(desIndex == currentIndex){
			if(indexOf == -1){
				return sperequire;
			}
			return sperequire.substring(0, indexOf);
		}
		if(indexOf != -1){
			sperequire = sperequire.substring(indexOf + 1);
			return findValue(sperequire, desIndex, currentIndex + 1);
		}
		return Constant.BLANK_VALUE;
	}
	
}
