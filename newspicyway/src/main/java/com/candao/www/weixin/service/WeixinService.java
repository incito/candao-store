package com.candao.www.weixin.service;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;


/**
 * 微信支付
 * @author snail
 *
 */
public interface WeixinService {

	/**
	 * 查询本店对应的微信信息
	 * @param branchid
	 * @return
	 */
	Map<String, Object> queryWeixinInfoBybranchid(String branchid);

	Page<Map<String, Object>> queryinfos(Map<String, Object> param, int page, int rows);

	List<Map<String, Object>> queryNameByBranchid(String branchid);

	List<Map<String, Object>> queryBranchidByName(String branchname);

	Map<String, Object> queryisExsit(Map<String, Object> param);

	void addweixinInfo(Map<String, Object> param);

	Map<String, Object> queryweixinInfo(String id);

	void updateweixinInfo(Map<String, Object> param);

	void delete(String id);

	Map<String, Object> queryActivity(String activityCode);

	Map<String, Object> selectinfos(String orderno);

	int queryIsSave(String orderno);

	int deletetemp(String orderno);

	int getweixinstatus(String branchid);

}
