package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TBusinessAnalysisChartsDao;

import java.util.List;
import java.util.Map;

/**
 * 营业分析图表
 * @author Administrator
 *
 */
@Repository
public class TBusinessAnalysisChartsDaoImpl implements TBusinessAnalysisChartsDao {

	@Autowired
	private DaoSupport daoSupport;

	@Override
	public List<Map<String, Object>> isfindBusinessReport(Map<String, Object> params) {
		if (params.get("Datetype").toString().equals("D")) {
			params.put("Datetype", "0");
		}
		if (params.get("Datetype").toString().equals("M")) {
			params.put("Datetype", "1");
		}

		return daoSupport.find(PREFIX + ".isfindBusinessReport", params);  //To change body of implemented methods use File | Settings | File Templates.
	}
}
