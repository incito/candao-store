package com.candao.www.weixin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.weixin.dao.WeixinDao;

@Repository
public class WeixinDaoImpl implements WeixinDao{

	@Autowired
	private DaoSupport daoSupport;
	
	@Override
	public Map<String, Object> queryWeixinInfoBybranchid(String branchid) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("branchid", branchid);
		return daoSupport.findOne(PREFIX + ".queryWeixinInfoBybranchid",paramMap);
	}

	@Override
	public Page<Map<String, Object>> queryinfos(Map<String, Object> params, int current, int pagesize) {
		
		return daoSupport.page(PREFIX + ".queryinfos", params, current, pagesize);
	}

	@Override
	public List<Map<String, Object>> queryNameByBranchid(String branchid) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("branchid", branchid);
		return daoSupport.find(PREFIX + ".queryNameByBranchid",paramMap);
	}

	@Override
	public List<Map<String, Object>> queryBranchidByName(String branchname) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("branchname", branchname);
		return daoSupport.find(PREFIX + ".queryBranchidByName",paramMap);
	}

	@Override
	public Map<String, Object> queryisExsit(Map<String, Object> param) {
		
		return daoSupport.findOne(PREFIX + ".queryisExsit",param);
	}

	@Override
	public void addweixinInfo(Map<String, Object> param) {
		param.put("id", UUID.randomUUID().toString());
		daoSupport.insert(PREFIX + ".addweixinInfo",param);
	}

	@Override
	public Map<String, Object> queryweixinInfo(String id) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", id);
		return daoSupport.findOne(PREFIX + ".queryweixinInfo",paramMap);
	}

	@Override
	public void updateweixinInfo(Map<String, Object> param) {
		daoSupport.update(PREFIX + ".updateweixinInfo",param);
	}

	@Override
	public void delete(String id) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", id);
		daoSupport.delete(PREFIX + ".delete",paramMap);
	}

	@Override
	public Map<String, Object> queryActivity(String activityCode) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("activityCode", activityCode);
		return daoSupport.findOne(PREFIX + ".queryActivity",paramMap);
	}

	@Override
	public void saveTempoldOrderid(String outTradeNo, String orderno) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orderno", orderno);
		paramMap.put("outTradeNo", outTradeNo);
		paramMap.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		daoSupport.insert(PREFIX + ".saveTempoldOrderid", paramMap);
	}

	@Override
	public Map<String, Object> selectinfos(String orderno) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orderno", orderno);
		return daoSupport.findOne(PREFIX + ".selectinfos",paramMap);
	}

	@Override
	public int queryIsSave(String orderno) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orderno", orderno);
		return daoSupport.get(PREFIX + ".queryIsSave",paramMap);
	}

	@Override
	public int deletetemp(String orderno) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orderno", orderno);
		return daoSupport.delete(PREFIX + ".deletetemp",paramMap);
	}

	@Override
	public int getweixinstatus(String branchid) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("branchid", branchid);
		return daoSupport.get(PREFIX + ".getweixinstatus",paramMap);
	}

}
