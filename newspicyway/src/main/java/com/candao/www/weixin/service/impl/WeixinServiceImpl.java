package com.candao.www.weixin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.www.weixin.dao.WeixinDao;
import com.candao.www.weixin.service.WeixinService;
@Service
public class WeixinServiceImpl implements WeixinService{

    @Autowired
	private WeixinDao weixinDao;
	
	@Override
	public Map<String, Object> queryWeixinInfoBybranchid(String branchid) {
		return weixinDao.queryWeixinInfoBybranchid(branchid);
	}

	@Override
	public Page<Map<String, Object>> queryinfos(Map<String, Object> params, int page, int rows) {
		return weixinDao.queryinfos(params, page, rows);
	}

	@Override
	public List<Map<String, Object>> queryNameByBranchid(String branchid) {
		return weixinDao.queryNameByBranchid(branchid);
	}

	@Override
	public List<Map<String, Object>> queryBranchidByName(String branchname) {
		return weixinDao.queryBranchidByName(branchname);
	}

	@Override
	public Map<String, Object> queryisExsit(Map<String, Object> param) {
		return weixinDao.queryisExsit(param);
	}

	@Override
	public void addweixinInfo(Map<String, Object> param) {
		weixinDao.addweixinInfo(param);
	}

	@Override
	public Map<String, Object> queryweixinInfo(String id) {
		return weixinDao.queryweixinInfo(id);
	}

	@Override
	public void updateweixinInfo(Map<String, Object> param) {
		weixinDao.updateweixinInfo(param);
	}

	@Override
	public void delete(String id) {
		weixinDao.delete(id);
	}

	@Override
	public Map<String, Object> queryActivity(String activityCode) {
		return weixinDao.queryActivity(activityCode);
	}

	@Override
	public Map<String, Object> selectinfos(String orderno) {
		return weixinDao.selectinfos(orderno);
	}

	@Override
	public int queryIsSave(String orderno) {
		return weixinDao.queryIsSave(orderno);
	}

	@Override
	public int deletetemp(String orderno) {
		return weixinDao.deletetemp(orderno);
	}

	@Override
	public int getweixinstatus(String branchid) {
		return weixinDao.getweixinstatus(branchid);
	}

}
