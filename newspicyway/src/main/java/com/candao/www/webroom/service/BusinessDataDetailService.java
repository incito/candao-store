package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

import com.candao.www.webroom.model.BusinessReport1;

/**
 * 营业数据明细
 * @author Administrator
 *
 */
public interface BusinessDataDetailService {
	public List<BusinessReport1> isgetBusinessDetail(Map<String, Object> params);
	public List<BusinessReport1> isgetBusinessDetail1(Map<String, Object> params);
	public List<Map<String, Object>> isgetBusinessDetailexcel(Map<String, Object> params);
	public List<Map<String, Object>> isgetBusinessDetailexcel1(Map<String, Object> params);

}
