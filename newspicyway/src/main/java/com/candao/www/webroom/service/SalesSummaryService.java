package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.candao.common.page.Page;
import com.candao.www.data.model.Tdish;

public interface SalesSummaryService {
	/**
	 * 分页查询数据
	 * @param params
	 * @param current
	 * @param pagesize
	 * @return
	 */
	 public Page<Map<String,Object>> grid(Map<String, Object> params, int current, int pagesize);
	 public void exportxlsD(Map<String, Object> params,HttpServletRequest req, HttpServletResponse resp) throws Exception ;
	 public List<Map<String,Object>> dates(Map<String, Object> params);
}
