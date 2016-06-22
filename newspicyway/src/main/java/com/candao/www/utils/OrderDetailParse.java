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
			if(sperequire.split(Constant.ORDER_REMARK_SEPARATOR).length == 6){
				return splitSperequire(sperequire, 0);
			}
			//兼容以前的数据
			return sperequire;
		}
		return Constant.BLANK_VALUE;
	}

	/**
	 * @return 菜品口味
	 */
	public static String getTaste(String sperequire) {
		return splitSperequire(sperequire, 2);
	}

	/**
	 * @return 赠菜人工号
	 */
	public static String getFreeUser(String sperequire) {
		return splitSperequire(sperequire, 3);
	}

	/**
	 * @return 赠菜授权人工号
	 */
	public static String getFreeAuthorize(String sperequire) {
		return splitSperequire(sperequire, 4);
	}

	/**
	 * @return 赠菜原因
	 */
	public static String getFreeReason(String sperequire) {
		return splitSperequire(sperequire, 5);
	}

	/**
	 * @return 全单备注
	 */
	public static String getGlobalRemark(String sperequire){
		return splitSperequire(sperequire, 1);
	}
	
	/**
	 * 拆分共用字段
	 * @param sperequire 
	 * @return 指定索引位置的值
	 */
	public static String splitSperequire(String sperequire, int index) {
		String[] split = sperequire.split(Constant.ORDER_REMARK_SEPARATOR);
		if(split.length == 6){
			return split[index];
		}
		return Constant.BLANK_VALUE;
	}
}
