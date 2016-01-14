package com.candao.common.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 判空
 * 
 * @author lzl
 * 
 */
public class ValidateUtils {

	/**
	 * 验证对象非空
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		boolean isEmpty = false;
		if (obj == null) {
			isEmpty = true;
		} else if (obj instanceof String) {
			isEmpty = ((String) obj).trim().isEmpty();
		} else if (obj instanceof Collection) {
			isEmpty = (((Collection) obj).size() == 0);
		} else if (obj instanceof Map) {
			isEmpty = ((Map) obj).size() == 0;
		} else if (obj.getClass().isArray()) {
			isEmpty = Array.getLength(obj) == 0;
		}
		return isEmpty;
	}
}
