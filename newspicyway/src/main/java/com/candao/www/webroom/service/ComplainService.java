package com.candao.www.webroom.service;

import net.sf.json.JSONObject;

public interface ComplainService {
	/**
	 * 
	 * 保存投诉信息
	 * 
	 * 
	 *主键ID 生成  IdentifierUtils.getId().generate()
	 * @return
	 */
	public int saveComplain(JSONObject data);
}
