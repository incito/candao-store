package com.candao.www.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: 张吉超 johnson
 * Date: 7/17/15
 * Time: 5:44 下午
 */
public  class ToolsUtil {

	/**
	 * 将数据格式成两位小数
	 *
	 * @param data
	 * @return
	 */
	public static String formatTwoDecimal(String data) {
		if (StringUtils.isBlank(data)||StringUtils.equals(data, "null")) {
			return "";
		}
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(Double.parseDouble(data));
	}
}
