package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.WaiterShiftDao;
@Repository
public class WaiterShiftDaoImpl implements WaiterShiftDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public List<Map<String, Object>> getWaiterShiftInfo(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getWaiterShiftInfo", paramMap);
	}

	@Override
	public List<Map<String, Object>> getOrderPayMount(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getOrderPayMount", paramMap);
	}

	@Override
	public List<Map<String, Object>> getOrderInflated(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getOrderInflated", paramMap);
	}
	
	@Override
	public List<Map<String, Object>> getOrderInfoGroupByWaiter(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getOrderInfoGroupByWaiter", paramMap);
	}

	@Override
	public List<Map<String, Object>> getOrderSettlementInfo(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getOrderSettlementInfo", paramMap);
	}

	@Override
	public List<Map<String,Object>> getSettlementInfo(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getSettlementInfo", paramMap);
	}

	@Override
	public List<Map<String, Object>> getOrderSettlementInfoDetail(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getOrderSettlementInfoDetail", paramMap);
	}

	@Override
	public List<Map<String, Object>> getOrderInfoGroupByOrder(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getOrderInfoGroupByOrder", paramMap);
	}
}
