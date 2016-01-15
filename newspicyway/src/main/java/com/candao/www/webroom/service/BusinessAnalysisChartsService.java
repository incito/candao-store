package com.candao.www.webroom.service;

import com.candao.www.webroom.model.BusinessReport;

import java.util.List;
import java.util.Map;

/**
 * 营业分析图表
 * @author Administrator
 *
 */
public interface BusinessAnalysisChartsService {


	public List<BusinessReport> isfindBusinessReport(Map<String, Object> params);


}
