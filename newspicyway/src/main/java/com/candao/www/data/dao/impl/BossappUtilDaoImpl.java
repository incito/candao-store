package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.BossappUtilDao;
@Repository
public class BossappUtilDaoImpl implements BossappUtilDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public List<Map<String, Object>> getAllTablesInfo(String branchid) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("branchid", branchid);
		return daoSupport.find(PREFIX + ".getAllTablesInfo",paramMap);
	}

	@Override
	public Map<String, Object> getOrderInfo(Map<String, Object> paramMap) {
		return daoSupport.findOne(PREFIX + ".getOrderInfo", paramMap);
	}
	
	@Override
	public Map<String, Object> getOrderInfoTemp(Map<String, Object> paramMap) {
		return daoSupport.findOne(PREFIX + ".getOrderInfoTemp", paramMap);
	}

	@Override
	public List<Map<String, Object>> getDayFlow(Map<String, Object> paramMap) {
		return daoSupport.find(PREFIX + ".getDayFlow", paramMap);
	}
}
