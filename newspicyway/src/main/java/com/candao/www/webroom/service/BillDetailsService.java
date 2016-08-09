package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Tdish;

public interface BillDetailsService {
	/**
	 * 分页查询数据
	 * @param params
	 * @param current
	 * @param pagesize
	 * @return
	 */
	 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);


}
