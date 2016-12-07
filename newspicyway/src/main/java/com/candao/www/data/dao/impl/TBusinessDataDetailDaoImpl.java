package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TBusinessDataDetailDao;

import java.util.List;
import java.util.Map;

/**
 * 营业数据明细表
 * @author Administrator
 *
 */
@Repository
public class TBusinessDataDetailDaoImpl implements TBusinessDataDetailDao {

	@Autowired
	private DaoSupport daoSupport;


	public List<Map<String, Object>> isgetBusinessDetail(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".isgetBusinessDetail", params); 
	}
	
	public List<Map<String, Object>> getOrderInfo(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".getOrderInfo", params); 
	}

	@Override
	public List<Map<String, Object>> getSettlementOptions(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".getSettlementOptions", params);
	}

	@Override
	public List<Map<String, Object>> getOrderActualSettlements(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".getOrderActualSettlements", params);
	}

	@Override
	public Map<String, Object> getOrderInflate(Map<String, Object> params) {
		return daoSupport.get(PREFIX + ".getOrderInflate", params);
	}

	@Override
	public Map<String, Object> getthActualAmount(Map<String, Object> params) {
		return daoSupport.get(PREFIX + ".getthActualAmount", params);
	}
}
