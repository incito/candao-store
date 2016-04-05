package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.BizDao;
@Repository
public class BizDaoImpl implements BizDao {
	
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public List<Map<String, Object>> getUsers(String branchId) {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("branchId", branchId);
		return daoSupport.find(PREFIX+".getUsers", paramMap);
	}

	@Override
	public List<Map<String, Object>> getBizInfos(String beginTime,String endTime,String branchId) {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("branchId", branchId);
		return daoSupport.find(PREFIX+".getBizInfos", paramMap);
	}
	
	@Override
	public List<Map<String, Object>> getBizNodeClassInfos(String beginTime,String endTime,String branchId) {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("branchId", branchId);
		return daoSupport.find(PREFIX+".getBizNodeClassInfos", paramMap);
	}

}
