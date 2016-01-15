package com.candao.www.webroom.service;

import java.util.List;
import java.util.Map;

/**
 * 结算方式明细表子表
 * @author weizhifang
 * @since 2015-11-21
 *
 */
public interface SettlementDetailChildService {

	/**
	 * 查询结算方式明细详情
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> querySettDetailList(Map<String,Object> params);
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public Map<String,Object> querySettList(Map<String,Object> params);
	
}
