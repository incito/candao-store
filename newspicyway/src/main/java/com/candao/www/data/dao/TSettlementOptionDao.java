package com.candao.www.data.dao;

import com.candao.www.webroom.model.PaywayRpet;

import java.util.List;
import java.util.Map;

/**
 * 结算方式明细表
 * @author Administrator
 *
 */
public interface TSettlementOptionDao {

	String PREFIX = TSettlementOptionDao.class.getName();

	<T, K, V> List<T> settlementOptionList(Map<K, V> params);
}
