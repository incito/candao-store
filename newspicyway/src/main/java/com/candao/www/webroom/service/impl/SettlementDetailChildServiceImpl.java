package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TSettlementDetailChildDao;
import com.candao.www.webroom.service.SettlementDetailChildService;

/**
 * 结算方式明细表子表
 * @author weizhifang
 * @since 2015-11-21
 *
 */
@Service
public class SettlementDetailChildServiceImpl implements SettlementDetailChildService {

	@Autowired
	private TSettlementDetailChildDao tSettlementDetailChildDao;
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> querySettDetailList(Map<String,Object> params){
		List<Map<String,Object>> list = tSettlementDetailChildDao.querySettDetailList(params);
		return list;
	}
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public Map<String,Object> querySettList(Map<String,Object> params){
		Map<String,Object> map = tSettlementDetailChildDao.querySettList(params);
		return map;
	}
}
