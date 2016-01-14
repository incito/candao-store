package com.candao.common.utils;

import javax.servlet.http.HttpServletRequest;

public class ProjectPath {
	public static String getAbsPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath() + "/";
	}

	public static String getpath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/");
	}
}
