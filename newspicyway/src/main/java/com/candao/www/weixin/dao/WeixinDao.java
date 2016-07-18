package com.candao.www.weixin.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;


public interface WeixinDao {

	public final static String PREFIX = WeixinDao.class.getName();
	
	Map<String, Object> queryWeixinInfoBybranchid(String branchid);

	Page<Map<String, Object>> queryinfos(Map<String, Object> params, int page, int rows);

	List<Map<String, Object>> queryNameByBranchid(String branchid);

	List<Map<String, Object>> queryBranchidByName(String branchname);

	Map<String, Object> queryisExsit(Map<String, Object> param);

	void addweixinInfo(Map<String, Object> param);

	Map<String, Object> queryweixinInfo(String id);

	void updateweixinInfo(Map<String, Object> param);

	void delete(String id);

	Map<String, Object> queryActivity(String activityCode);

	void saveTempoldOrderid(String outTradeNo, String orderno);

	Map<String, Object> selectinfos(String orderno);

	int queryIsSave(String orderno);

	int deletetemp(String orderno);

	int getweixinstatus(String branchid);

}
