package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;


/**
 * 详细数据统计表
 * @author Administrator
 *
 */
public interface DataDetailService {
	public List<Map<String,Object>>  insertDataStatistics(Map<String,Object> params);
}
