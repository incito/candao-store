package com.candao.www.data.dao.impl;

import com.candao.www.webroom.model.PaywayRpet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TSettlementOptionDao;

import java.util.List;
import java.util.Map;

/**
 * 结算方式明细表
 * @author Administrator
 *
 */
@Repository
public class TSettlementOptionDaoImpl implements TSettlementOptionDao {

	@Autowired
	private DaoSupport daoSupport;

	@Override
	public <T, K,  V> List<T> settlementOptionList(Map<K, V> params) {
		return daoSupport.find(PREFIX + ".settlementOptionList", params);	}
}
