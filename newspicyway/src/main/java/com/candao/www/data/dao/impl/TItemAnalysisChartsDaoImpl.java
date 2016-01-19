package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TItemAnalysisChartsDao;

/**
 * 品项分析图表
 * @author Administrator
 *
 */
@Repository
public class TItemAnalysisChartsDaoImpl implements TItemAnalysisChartsDao {

	@Autowired
	private DaoSupport daoSupport;
	
	/**
	 * 查询品项分析图表
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemAnalysisChartsForPro(Map<String,Object> params){
		return daoSupport.find(PREFIX + ".itemAnalysisChartsForPro", params);
	}
	
	/**
	 * 查询品项分析图表(指定分类统计)
	 * @author weizhifang
	 * @since 2015-07-04
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> itemAnalysisChartsForColumnPro(Map<String,Object> params){
		return daoSupport.find(PREFIX + ".itemAnalysisChartsForColumnPro", params);
	}

	@Override
	public List<Map<String, Object>> getAllOrderInfo(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".getAllOrderInfo", params);
	}
	
	@Override
	public List<Map<String, Object>> getAllOrderCustnum(Map<String, Object> params) {
		try{
			return daoSupport.find(PREFIX + ".getAllOrderCustnum", params);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}


}
