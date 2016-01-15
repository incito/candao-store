package com.candao.www.webroom.service;

import com.candao.www.webroom.model.BusinessReport;
import com.candao.www.webroom.model.BusinessReport1;

import java.util.List;
import java.util.Map;

/**
 * 营业数据明细
 * @author Administrator
 *
 */
public interface BusinessDataDetailService {
	public List<BusinessReport1> isgetBusinessDetail(Map<String, Object> params);
	public List<Map<String, Object>> isgetBusinessDetailexcel(Map<String, Object> params);

}
