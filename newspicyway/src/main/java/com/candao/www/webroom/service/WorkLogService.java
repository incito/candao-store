package com.candao.www.webroom.service;

import com.candao.www.data.model.Tworklog;

public interface WorkLogService {
	/**
	 * 添加日志
	 * @return
	 */
	public int saveLog(Tworklog workLog);
	

}
