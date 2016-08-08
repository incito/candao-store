package com.candao.www.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.candao.common.utils.Constant;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.model.User;


/**
 * Session 操作辅助类
 * 
 * @author lzl
 * 
 */
public class SessionUtils {

	public static HttpSession getSession(boolean arg0) {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
		return request.getSession(arg0);
	}

	/**
	 * 将对象存入session
	 */
	public static void put(String key, Object obj) {
		getSession(true).setAttribute(key, obj);
	}

	/**
	 * 从session取对象
	 */
	public static Object get(String key) {
		HttpSession session = getSession(false);
		if (session != null) {
			return session.getAttribute(key);
		} else {
			return null;
		}
	}

	/**
	 * 检查Session是否存在指定Key值的对象
	 */
	public static boolean contains(String key) {
		return (get(key) != null);
	}

	/**
	 * 清除指定session对象
	 */
	public static void remove(String key) {
		HttpSession session = getSession(false);
		if (session != null) {
			session.removeAttribute(key);
		}
	}

	/**
	 * 清除所有存放在session的对象
	 */
	public static void clear() {
		HttpSession session = getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	/**
	 * 获取当前登录人
	 */
	public static User getCurrentUser() {
		return (User) get(Constant.CURRENT_USER);
	}

	/**
	 * 检测是否有权限访问按钮
	 * @param url
	 * @return
	 */
	public static boolean checkAllowAccessButton(String url) {
		List<String> allowAccessButtons = (List<String>) get(Constant.ALLOW_ACCESS_BUTTONS);
		if (!ValidateUtils.isEmpty(allowAccessButtons)) {
			if (allowAccessButtons.contains(url)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}